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
package com.anyscribble.ide.files;

import com.anyscribble.ide.Preferences;
import com.anyscribble.ide.Resource;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.biesaart.utils.Log;
import me.biesaart.utils.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


@Singleton
public class FileTree extends TreeView<Path> {
    private static final String PREFERENCE_PROJECTS = "open.projects";
    private final TreeItem<Path> rootNode = new TreeItem<>(Paths.get("."));
    private static final Logger LOGGER = Log.get();
    private final Image folderIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/folder.png"));
    private final Image fileIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/file-text.png"));
    private final Image projectIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/project.png"));
    private final Preferences preferences;
    private Consumer<Path> openFileConsumer;

    @Inject
    public FileTree(Preferences preferences) {
        this.preferences = preferences;
        setId("fileTree");
        setCellFactory(Cell::new);
        setRoot(rootNode);
        rootNode.setExpanded(true);
        VBox.setVgrow(this, Priority.ALWAYS);
        setShowRoot(false);

        // Open all projects
        openProjectsFromPreferences();
    }


    public void addProject(Path projectRoot) {
        rootNode.getChildren().add(
                new PathTree(projectRoot)
        );
        saveProjectsToPreferences();
    }

    public void closeProject() {
        TreeItem<Path> item = getSelectionModel().getSelectedItem();
        closeProject(item);
    }

    public void closeProject(TreeItem<Path> node) {
        if (node == null) {
            return;
        }

        // Find the project node
        TreeItem<Path> projectItem = node;
        while (projectItem.getParent() != null && projectItem.getParent().getParent() != null) {
            projectItem = projectItem.getParent();
        }

        Path project = projectItem.getValue();

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                Resource.CONFIRM_DELETE_PROJECT.get(project),
                ButtonType.YES,
                ButtonType.CANCEL
        );

        // Show the dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Remove the project
            rootNode.getChildren().remove(projectItem);
            getSelectionModel().clearSelection();
            saveProjectsToPreferences();
        }
    }

    private void saveProjectsToPreferences() {
        String projects = StringUtils.join(
                rootNode.getChildren().stream().map(TreeItem::getValue).toArray(),
                File.pathSeparator
        );

        preferences.put(PREFERENCE_PROJECTS, projects);
    }

    private void openProjectsFromPreferences() {
        preferences.getList(PREFERENCE_PROJECTS).ifPresent(projects -> {
            rootNode.getChildren().clear();
            for (String stringPath : projects) {
                Path path = Paths.get(stringPath);
                if (Files.isDirectory(path)) {
                    addProject(path);
                }
            }
        });
    }

    public void setOpenFileConsumer(Consumer<Path> openFileConsumer) {
        this.openFileConsumer = openFileConsumer;
    }

    private class ProjectItem extends PathTree {

        public ProjectItem(Path projectRoot) {
            super(projectRoot);
        }

    }

    private class PathTree extends TreeItem<Path> {

        public PathTree(Path projectRoot) {
            setValue(projectRoot);
            expandedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    refreshChildren();
                }
            });
            setExpanded(false);
        }

        private void refreshChildren() {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(getValue())) {
                List<TreeItem<Path>> result = new ArrayList<>();
                for (Path child : stream) {
                    if(!child.getFileName().toString().startsWith(".")) {
                        result.add(new PathTree(child));
                    }
                }
                getChildren().setAll(result);
            } catch (IOException e) {
                LOGGER.error("Could not expand!", e);
            }
        }

        @Override
        public boolean isLeaf() {
            return Files.isRegularFile(getValue());
        }
    }

    private class Cell extends TreeCell<Path> {
        public Cell(TreeView<Path> pathTreeView) {
            addEventFilter(
                    MouseEvent.MOUSE_CLICKED,
                    event -> {
                        if (!isEmpty() && event.getClickCount() == 2 && Files.isRegularFile(getItem()) && openFileConsumer != null) {
                            // Open this file
                            openFileConsumer.accept(getItem());
                        }
                    }
            );
        }

        @Override
        protected void updateItem(Path item, boolean empty) {
            super.updateItem(item, empty);
            if (getTreeItem() == rootNode) {
                setText("Projects");
                setGraphic(null);
            } else if (item == null) {
                setText(null);
                setGraphic(null);
            } else if (getTreeItem() instanceof ProjectItem) {
                setText(item.getFileName().toString());
                setGraphic(new ImageView(projectIcon));
            } else {
                setText(item.getFileName().toString());

                if (!Files.exists(item)) {
                    // Remove this element
                    getTreeItem().getParent().getChildren().remove(getTreeItem());
                } else if (Files.isDirectory(item)) {
                    setGraphic(new ImageView(folderIcon));
                } else {
                    setGraphic(new ImageView(fileIcon));
                }
            }
        }
    }
}
