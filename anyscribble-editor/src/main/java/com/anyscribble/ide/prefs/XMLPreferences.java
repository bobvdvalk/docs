/**
 * AnyScribble Editor - Writing for Developers by Developers
 * Copyright © 2016 Thomas Biesaart (thomas.biesaart@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anyscribble.ide.prefs;

import com.anyscribble.ide.Resource;
import com.anyscribble.ide.Setting;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import me.biesaart.utils.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * This implementation of the Preferences class persists the settings on a one-second delay to an xml file.
 *
 * @author Thomas Biesaart
 */
class XMLPreferences extends Preferences {
    private static final Logger LOGGER = Log.get();
    private final Properties properties;
    private final Path targetFile;
    private final Timeline saveTimeline = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    e -> save()
            )
    );

    XMLPreferences(Properties properties, Path targetFile) {
        this.properties = properties;
        this.targetFile = targetFile;
        sync();
    }

    private void save() {
        try {
            LOGGER.debug("Saving properties to {}", targetFile);
            Files.createDirectories(targetFile.getParent());
            try (OutputStream outputStream = Files.newOutputStream(targetFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                properties.storeToXML(outputStream, Resource.APPLICATION_TITLE);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save " + targetFile, e);
        }

    }

    private synchronized void sync() {
        LOGGER.debug("Reading properties from {}", targetFile);
        saveTimeline.stop();
        if (Files.exists(targetFile)) {
            try (InputStream stream = Files.newInputStream(targetFile)) {
                properties.loadFromXML(stream);
            } catch (IOException e) {
                LOGGER.error("Failed to load properties from " + targetFile, e);
            }
        }
    }

    @Override
    public synchronized void put(Setting key, String value) {
        properties.put(key.getLabel(), value);
        saveTimeline.playFromStart();
        LOGGER.debug("Put preference {} = {}", key, value);
    }

    @Override
    public synchronized void putList(Setting key, Iterable<String> value) {
        put(key, StringUtils.join(value, File.pathSeparator));
    }

    @Override
    public Optional<String> get(Setting key) {
        return Optional.ofNullable(properties.getProperty(key.getLabel()));
    }

    @Override
    public Optional<List<String>> getList(Setting key) {
        String value = get(key).orElse(null);
        if (value == null) {
            return Optional.empty();
        }

        String[] parts = value.split(Pattern.quote(File.pathSeparator));

        List<String> result = new ArrayList<>(parts.length);
        for (String part : parts) {
            String cleanPart = part.trim();
            if (!cleanPart.isEmpty()) {
                result.add(part);
            }
        }
        return Optional.of(result);
    }
}
