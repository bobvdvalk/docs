/**
 * AnyScribble Editor - Writing for Developers by Developers
 * Copyright © 2016 Thomas Biesaart (thomas.biesaart@gmail.com)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anyscribble.ide;


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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is responsible for persisting simple preferences.
 * It works by identifying a preference which is stored as a string.
 * For example, I want to save the last entered message in my dialog. Let's call this preference dialog.message.
 * <pre>
 * // Save my message
 * preferences.put("dialog.message", "Hello World");
 *
 * // Fetch my message and specify a default
 * preferences.get("dialog.message").orElse("This is the first time you see this dialog");
 * </pre>
 *
 * @author Thomas Biesaart
 */
public class Preferences {
    private static final Logger LOGGER = Log.get();
    private static Map<String, Preferences> cache = new HashMap<>();
    private final Properties properties;
    private final Path targetFile;
    private final Timeline saveTimeline = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    e -> save()
            )
    );

    private Preferences(Properties properties, Path targetFile) {
        this.properties = properties;
        this.targetFile = targetFile;
        sync();
    }

    /**
     * Create a {@link Preferences} that stores data in the user's home folder.
     *
     * @param vendor the vendor name
     * @return the preferences
     */
    public static Preferences getUserPreferences(String vendor) {
        if (cache.containsKey(vendor)) {
            return cache.get(vendor);
        }
        Properties properties = new Properties(System.getProperties());

        Path homeFolder = Paths.get(properties.getProperty("user.home"));
        Path propertiesFolder = homeFolder.resolve("." + vendor.replaceAll("[^\\w]+", "").toLowerCase());

        Preferences result = new Preferences(properties, propertiesFolder.resolve("properties.xml"));
        cache.put(vendor, result);
        return result;
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

    /**
     * Save a String preference.
     * Use this method to save a preference that is identified by a key.
     *
     * @param key   the unique key of the preference
     * @param value the value that should be saved
     * @throws NullPointerException if the key or value is null
     */
    public synchronized void put(String key, String value) {
        properties.put(key, value);
        saveTimeline.playFromStart();
        LOGGER.debug("Put preference {} = {}", key, value);
    }

    /**
     * Save a collection of objects. The string value of these objects will be saved to the persistence.
     * Fetch it later using {@link #getList(String)}.
     *
     * @param key   the unique key of the preference
     * @param value the collection
     */
    public synchronized void putList(String key, Iterable<?> value) {
        put(key, StringUtils.join(value, File.pathSeparator));
    }

    /**
     * Fetch a preference from the persistence.
     *
     * @param key the unique key of the preference
     * @return the preference of {@link Optional#empty()}} if it was not found
     */
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    /**
     * Fetch a collection preference from the persistence.
     *
     * @param key the unique key of the preference
     * @return the preference of {@link Optional#empty()}} if it was not found
     */
    public Optional<List<String>> getList(String key) {
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
