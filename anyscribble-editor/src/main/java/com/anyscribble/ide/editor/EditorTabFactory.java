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
package com.anyscribble.ide.editor;

import com.anyscribble.ide.InjectionFXMLLoader;
import com.anyscribble.ide.Setting;
import com.anyscribble.ide.controller.EditorTabController;
import com.anyscribble.ide.prefs.Preferences;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is responsible for building the editor tab.
 * It will also keep track of which controller matches which path.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class EditorTabFactory {
    private static final Logger LOGGER = Log.get();
    private final InjectionFXMLLoader fxmlLoader;
    private Map<Path, EditorTabData> controllerCache = new HashMap<>();
    private final Preferences preferences;

    @Inject
    EditorTabFactory(InjectionFXMLLoader fxmlLoader, Preferences preferences) {
        this.fxmlLoader = fxmlLoader;
        this.preferences = preferences;
    }

    public Tab buildTab(Path file) throws IOException {
        Path normalizedPath = normalized(file);
        Tab tab = new Tab(normalizedPath.getFileName().toString());
        FXMLLoader loader = fxmlLoader.getLoader();
        loader.setLocation(getClass().getResource(
                "/com/anyscribble/ide/editor-tab.fxml"
        ));
        try {
            tab.setContent(loader.load());
        } catch (IOException e) {
            LOGGER.error("Failed to load Tab from FXML", e);
        }
        EditorTabController controller = loader.getController();
        if (controller != null) {
            controller.loadFile(normalizedPath);
            controllerCache.put(normalizedPath, new EditorTabData(tab, controller));
        }

        tab.setOnClosed(event -> {
            controllerCache.remove(normalizedPath);
            saveOpenTabs();
        });

        saveOpenTabs();

        return tab;
    }

    private void saveOpenTabs() {
        preferences.putList(
                Setting.OPEN_TABS,
                controllerCache.values().stream()
                        .map(c -> c.controller.getPath())
                        .map(Path::toString)
                        .collect(Collectors.toList())
        );
    }

    public Optional<EditorTabController> getController(Path path) {
        return getData(path).map(d -> d.controller);
    }

    public Optional<Tab> getTab(Path path) {
        return getData(path).map(d -> d.tab);
    }

    private Optional<EditorTabData> getData(Path path) {
        Path normalizedPath = normalized(path);
        EditorTabData data = controllerCache.get(normalizedPath);

        if (data == null) {
            return Optional.empty();
        } else if (data.controller.isClosed()) {
            controllerCache.remove(normalizedPath);
            return Optional.empty();
        } else {
            return Optional.of(data);
        }
    }

    private final Path normalized(Path path) {
        try {
            return path.toRealPath();
        } catch (IOException e) {
            LOGGER.warn("Failed to normalize " + path, e);
        }
        return path.normalize().toAbsolutePath();
    }

    private class EditorTabData {
        private final Tab tab;
        private final EditorTabController controller;

        private EditorTabData(Tab tab, EditorTabController controller) {
            this.tab = tab;
            this.controller = controller;
        }
    }
}
