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
import com.anyscribble.ide.editor.EditorTabFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * This class represents the controller for the highest level of the interface.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class GlobalController implements Initializable {
    @FXML
    private BorderPane rootPane;
    @FXML
    private TabPane editorTabPane;
    private final InjectionFXMLLoader injectionFXMLLoader;
    private final EditorTabFactory editorTabFactory;

    @Inject
    GlobalController(InjectionFXMLLoader injectionFXMLLoader, EditorTabFactory editorTabFactory) {
        this.injectionFXMLLoader = injectionFXMLLoader;
        this.editorTabFactory = editorTabFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the left panel
        rootPane.setLeft(
                injectionFXMLLoader.load(getClass().getResource("/com/anyscribble/ide/left-panel.fxml"))
        );
    }

    public void openTab(Path file) {
        Tab tab = editorTabFactory.getTab(file)
                .orElseGet(() -> {
                    Tab newTab = editorTabFactory.buildTab(file);
                    editorTabPane.getTabs().add(newTab);
                    return newTab;
                });
        editorTabPane.getSelectionModel().select(tab);
    }
}
