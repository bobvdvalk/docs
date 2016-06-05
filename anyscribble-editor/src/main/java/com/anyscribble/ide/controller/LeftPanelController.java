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


import com.anyscribble.ide.files.FileTree;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * This class represents the controller for the left side-panel.
 *
 * @author Thomas Biesaart
 */
public class LeftPanelController implements Initializable {
    @FXML
    private VBox leftPanel;
    private final FileTree fileTree;

    @Inject
    public LeftPanelController(FileTree fileTree) {
        this.fileTree = fileTree;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(fileTree, Priority.ALWAYS);
        leftPanel.getChildren().setAll(
                fileTree
        );

        fileTree.addProject(Paths.get("docs"));
    }
}
