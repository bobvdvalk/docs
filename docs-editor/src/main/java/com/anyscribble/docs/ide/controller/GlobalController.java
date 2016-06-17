/**
 * AnyScribble Docs Editor - Writing for Developers by Developers
 * Copyright © 2016 AnyScribble (thomas.biesaart@gmail.com)
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
package com.anyscribble.docs.ide.controller;

import com.anyscribble.docs.core.Docs;
import com.anyscribble.docs.core.DocsException;
import com.anyscribble.docs.core.DocsProcess;
import com.anyscribble.docs.core.process.PandocCallback;
import com.anyscribble.docs.ide.InjectionFXMLLoader;
import com.anyscribble.docs.ide.Resource;
import com.anyscribble.docs.ide.Setting;
import com.anyscribble.docs.ide.editor.EditorTabFactory;
import com.anyscribble.docs.ide.files.FileTree;
import com.anyscribble.docs.ide.prefs.Preferences;
import com.anyscribble.docs.model.BuildConfiguration;
import com.anyscribble.docs.model.Project;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.biesaart.utils.IOUtils;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private Menu projectMenu;
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
    private final Provider<Docs> docsProvider;

    @Inject
    GlobalController(Preferences preferences, InjectionFXMLLoader injectionFXMLLoader, EditorTabFactory editorTabFactory, FileTree fileTree, Provider<Docs> docsProvider) {
        this.preferences = preferences;
        this.injectionFXMLLoader = injectionFXMLLoader;
        this.editorTabFactory = editorTabFactory;
        this.fileTree = fileTree;
        this.docsProvider = docsProvider;
        fileTree.setOpenFileConsumer(this::openTab);
        openProjectDirectoryChooser = new DirectoryChooser();
        openProjectDirectoryChooser.setTitle(Resource.PROJECT_NEW_TITLE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the left panel
        rootPane.setLeft(
                injectionFXMLLoader.load(getClass().getResource("/com/anyscribble/docs/ide/left-panel.fxml"))
        );

        // Bind tab switching hot keys
        for (int i = 1; i <= 10; i++) {
            Setting setting = Setting.valueOf("HOTKEY_TAB" + i);
            int tabIndex = i; // Make this variable final

            preferences.bindHotKey(
                    rootPane,
                    setting,
                    "SHORTCUT+" + (i % 10), // Default hot key for tab 10 is 0 not 10
                    () -> editorTabPane.getSelectionModel().select(tabIndex)
            );
        }
        // Disable close tab menu item when no tabs are open
        editorTabPane.getTabs().addListener((ListChangeListener<Tab>) c ->
                closeTabMenuItem.setDisable(c.getList().isEmpty())
        );

        // Disable close project menu item when no node is selected
        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    closeProjectMenuItem.setDisable(newValue == null);
                }
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
        fileTree.closeCurrentProject();
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
            java.awt.Desktop.getDesktop().browse(
                    new URI("https://gitter.im/thomasbiesaart/anyscribble")
            );
        } catch (URISyntaxException | UnsupportedOperationException | IOException e) {
            LOGGER.info("Could not open contact url", e);
        }
    }

    public void buildProject() {
        List<Path> choices = fileTree.getProjects().stream()
                .map(TreeItem::getValue)
                .collect(Collectors.toList());

        TreeItem<Path> project = fileTree.getCurrentProject();
        ChoiceDialog<Path> choiceDialog = new ChoiceDialog<>(project == null ? null : project.getValue(), choices);

        choiceDialog.setHeaderText(Resource.DIALOG_BUILD_PROJECT_TITLE);
        choiceDialog.setTitle(Resource.DIALOG_BUILD_PROJECT_TITLE);
        choiceDialog.setContentText(Resource.DIALOG_BUILD_PROJECT_CONTENT);

        choiceDialog.showAndWait().ifPresent(path -> {
            Path projectFile = path.resolve("docs.xml");
            try {
                showBuildDialog(projectFile);
            } catch (DocsException e) {
                LOGGER.error("Failed to build project", e);
            }
        });
    }

    private void showBuildDialog(Path projectFile) throws DocsException {
        Docs docs = docsProvider.get();

        Project project = docs.loadProject(projectFile);
        try (DocsProcess process = docs.buildProcess(project, new CallbackImpl())) {
            process.start();

            Files.walkFileTree(project.getSourceDir(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith("md")) {
                        Files.copy(file, process.getOutputStream());
                        IOUtils.write("\n", process.getOutputStream());
                    }
                    return super.visitFile(file, attrs);
                }
            });

        } catch (IOException e) {
            LOGGER.error("IO ERROR", e);
        }
    }


    private class CallbackImpl implements PandocCallback {
        @Override
        public void onStart(BuildConfiguration buildConfiguration) {
            try {
                Files.createDirectories(buildConfiguration.getOutputFile().getParent());
            } catch (IOException e) {
                LOGGER.error("Failed to create directories", e);
            }
        }

        @Override
        public void onError(Throwable e) {
            LOGGER.error("Callback Error", e);
        }

        @Override
        public void onBatchComplete() {
            LOGGER.info("Batch Complete");
        }

        @Override
        public void onProcessComplete() {
            LOGGER.info("Process Complete");
        }
    }
}
