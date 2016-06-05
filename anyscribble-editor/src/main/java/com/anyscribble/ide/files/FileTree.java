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

import com.google.inject.Singleton;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class FileTree extends TreeView<Path> {
    private final TreeItem<Path> rootNode = new TreeItem<>(Paths.get("."));
    private static final Logger LOGGER = Log.get();
    private final Image folderIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/folder.png"));
    private final Image fileIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/file-text.png"));
    private final Image projectIcon = new Image(getClass().getResourceAsStream("/com/anyscribble/ide/icons/project.png"));

    public FileTree() {
        setId("fileTree");
        setCellFactory(Cell::new);
        setRoot(rootNode);
        rootNode.setExpanded(true);
    }

    public void addProject(Path projectRoot) {
        rootNode.getChildren().add(
                new PathTree(projectRoot)
        );
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
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(getValue())) {
                        List<TreeItem<Path>> result = new ArrayList<>();
                        for (Path child : stream) {
                            result.add(new PathTree(child));
                        }
                        getChildren().setAll(result);
                    } catch (IOException e) {
                        LOGGER.error("Could not expand!", e);
                    }
                } else {
                    // TODO: Open File
                }
            });
            setExpanded(false);
        }

        @Override
        public boolean isLeaf() {
            return Files.isRegularFile(getValue());
        }
    }

    private class Cell extends TreeCell<Path> {
        public Cell(TreeView<Path> pathTreeView) {
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
