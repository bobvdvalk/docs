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
import com.anyscribble.ide.editor.EditorTabFactory;
import com.anyscribble.ide.files.FileTree;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * This class represents the controller for the highest level of the interface.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class GlobalController implements Initializable {
    private static final Logger LOGGER = Log.get();
    @FXML
    private BorderPane rootPane;
    @FXML
    private TabPane editorTabPane;
    private final InjectionFXMLLoader injectionFXMLLoader;
    private final EditorTabFactory editorTabFactory;
    private final FileTree fileTree;
    private final DirectoryChooser openProjectDirectoryChooser;

    @Inject
    GlobalController(InjectionFXMLLoader injectionFXMLLoader, EditorTabFactory editorTabFactory, FileTree fileTree) {
        this.injectionFXMLLoader = injectionFXMLLoader;
        this.editorTabFactory = editorTabFactory;
        this.fileTree = fileTree;
        openProjectDirectoryChooser = new DirectoryChooser();
        openProjectDirectoryChooser.setTitle(Resource.PROJECT_NEW_TITLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the left panel
        rootPane.setLeft(
                injectionFXMLLoader.load(getClass().getResource("/com/anyscribble/ide/left-panel.fxml"))
        );
    }

    /**
     * Open a new tab or select an already exist tab that matches the specified file.
     *
     * @param file the file that should be opened
     */
    public void openTab(Path file) {
        Tab tab = editorTabFactory.getTab(file)
                .orElseGet(() -> {
                    Tab newTab = editorTabFactory.buildTab(file);
                    editorTabPane.getTabs().add(newTab);
                    return newTab;
                });
        editorTabPane.getSelectionModel().select(tab);
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
}
