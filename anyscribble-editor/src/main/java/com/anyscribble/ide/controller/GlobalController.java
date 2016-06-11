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
package com.anyscribble.ide.controller;

import com.anyscribble.ide.InjectionFXMLLoader;
import com.anyscribble.ide.Resource;
import com.anyscribble.ide.Setting;
import com.anyscribble.ide.editor.EditorTabFactory;
import com.anyscribble.ide.files.FileTree;
import com.anyscribble.ide.prefs.Preferences;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.biesaart.utils.Log;
import org.slf4j.Logger;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.input.KeyEvent;
/**
 * This class represents the controller for the highest level of the interface.
 *
 * @author Thomas Biesaart
 * @author Bob van der Valk
 */
@Singleton
public class GlobalController implements Initializable {
    private static final Logger LOGGER = Log.get();
    @FXML
    private MenuItem closeProjectMenuItem;
    @FXML
    private MenuItem closeTabMenuItem;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TabPane editorTabPane;
    private final Preferences preferences;
    private final InjectionFXMLLoader injectionFXMLLoader;
    private final EditorTabFactory editorTabFactory;
    private final FileTree fileTree;
    private final DirectoryChooser openProjectDirectoryChooser;

    @Inject
    GlobalController(Preferences preferences, InjectionFXMLLoader injectionFXMLLoader, EditorTabFactory editorTabFactory, FileTree fileTree) {
        this.preferences = preferences;
        this.injectionFXMLLoader = injectionFXMLLoader;
        this.editorTabFactory = editorTabFactory;
        this.fileTree = fileTree;
        fileTree.setOpenFileConsumer(this::openTab);
        openProjectDirectoryChooser = new DirectoryChooser();
        openProjectDirectoryChooser.setTitle(Resource.PROJECT_NEW_TITLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the left panel
        rootPane.setLeft(
                injectionFXMLLoader.load(getClass().getResource("/com/anyscribble/ide/left-panel.fxml"))
        );
        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            hotKeyBinder(Setting.HOTKEY_TAB1, "CTRL+1", e -> openTabOnHotkey(0), event);
            hotKeyBinder(Setting.HOTKEY_TAB2, "CTRL+2", e -> openTabOnHotkey(1), event);
            hotKeyBinder(Setting.HOTKEY_TAB3, "CTRL+3", e -> openTabOnHotkey(2), event);
            hotKeyBinder(Setting.HOTKEY_TAB4, "CTRL+4", e -> openTabOnHotkey(3), event);
            hotKeyBinder(Setting.HOTKEY_TAB5, "CTRL+5", e -> openTabOnHotkey(4), event);
            hotKeyBinder(Setting.HOTKEY_TAB6, "CTRL+6", e -> openTabOnHotkey(5), event);
            hotKeyBinder(Setting.HOTKEY_TAB7, "CTRL+7", e -> openTabOnHotkey(6), event);
            hotKeyBinder(Setting.HOTKEY_TAB8, "CTRL+8", e -> openTabOnHotkey(7), event);
            hotKeyBinder(Setting.HOTKEY_TAB9, "CTRL+9", e -> openTabOnHotkey(8), event);
            hotKeyBinder(Setting.HOTKEY_TAB0, "CTRL+0", e -> openTabOnHotkey(9), event);
        });

        // Disable close tab menu item when no tabs are open
        editorTabPane.getTabs().addListener((ListChangeListener<Tab>) c ->
                closeTabMenuItem.setDisable(c.getList().isEmpty())
        );

        // Disable close project menu item when no node is selected
        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                closeProjectMenuItem.setDisable(newValue == null)
        );

        // Open all previously open tabs
        preferences.getList(Setting.OPEN_TABS)
                .ifPresent(
                        list -> list.stream()
                                .map(Paths::get)
                                .filter(Files::isReadable)
                                .forEach(this::openTab)
                );
    }

    /**
     * This method binds hotkeys to method and executes them.
     */
    private void hotKeyBinder(Setting setting, String combination, Consumer<KeyEvent> consumer, KeyEvent keyEvent) {
        String key = preferences.get(setting).orElse(combination);
        KeyCombination keyCombination = KeyCombination.keyCombination(key);
        if(keyCombination.match(keyEvent)) {
            consumer.accept(keyEvent);
        }
    }

    /**
     * This method opens a tab
     * @param index tab number you want to open
     */
    private void openTabOnHotkey(int index) {
        editorTabPane.getSelectionModel().select(index);
    }

    /**
     * Open a new tab or select an already exist tab that matches the specified file.
     *
     * @param file the file that should be opened
     */
    public void openTab(Path file) {
        Tab tab = editorTabFactory.getTab(file)
                .orElseGet(() -> newTab(file));
        if (tab != null) {
            editorTabPane.getSelectionModel().select(tab);
        }
    }

    private Tab newTab(Path file) {
        try {
            Tab newTab = editorTabFactory.buildTab(file);
            editorTabPane.getTabs().add(newTab);
            return newTab;
        } catch (IOException e) {
            LOGGER.error("Failed to open new tab for " + file, e);
            return null;
        }
    }

    /**
     * Open a folder selection dialog and create a new project.
     */
    public void openProject() {
        File folder = openProjectDirectoryChooser.showDialog(rootPane.getScene().getWindow());
        if (folder != null) {
            Path directory = folder.toPath();
            if (directory.getFileName() == null) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        Resource.ERROR_INVALID_PROJECT_ROOT.get(directory)
                );

                alert.setTitle(Resource.PROJECT_NEW_TITLE);

                alert.show();
            } else {
                fileTree.addProject(folder.toPath());
            }
        }
    }

    /**
     * Open a save dialog to create a new file.
     */
    public void newFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Markdown (*.markdown)", "*.markdown", "*.md")
        );
        TreeItem<Path> select = fileTree.getSelectionModel().getSelectedItem();
        if (select != null) {
            Path path = select.getValue();
            if (!Files.isDirectory(path)) {
                path = path.getParent();
            }
            fileChooser.setInitialDirectory(path.toFile());
        }
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

        if (file != null) {
            try {
                Files.createFile(file.toPath());
                openTab(file.toPath());
            } catch (IOException e) {
                LOGGER.error("Failed to create file.", e);
            }
        }

    }

    public void closeCurrentTab() {
        int tab = editorTabPane.getSelectionModel().getSelectedIndex();
        if (tab >= 0) {
            editorTabPane.getTabs().remove(tab);
        }
    }

    public void exit() {
        Platform.exit();
    }

    public void closeCurrentProject() {
        fileTree.closeProject();
    }

    public void openContact() {
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION,
                "The chat will open in your default browser.",
                ButtonType.OK, ButtonType.CANCEL
        );
        alert.setHeaderText("Join the chat");
        alert.showAndWait().ifPresent(
                type -> {
                    if (type == ButtonType.OK) {
                        browseToChat();
                    }
                }
        );
    }

    private void browseToChat() {
        try {
            Desktop.getDesktop().browse(
                    new URI("https://gitter.im/thomasbiesaart/anyscribble")
            );
        } catch (URISyntaxException | UnsupportedOperationException | IOException e) {
            LOGGER.info("Could not open contact url", e);
        }
    }
}
