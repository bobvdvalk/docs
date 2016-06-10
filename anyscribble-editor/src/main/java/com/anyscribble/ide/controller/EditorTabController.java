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

import com.anyscribble.ide.Preferences;
import com.google.inject.Inject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import me.biesaart.utils.IOUtils;
import me.biesaart.utils.Log;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * This controller is responsible for all ui operations inside the editor tab.
 * <p>
 * It is responsible for all buttons that perform operations on the text and for saving and syncing the current file.
 * It is fixed to a certain location on the file path. This means that will always know where to save/read the source
 * file but it also means it is not possible to change the file location.
 *
 * @author Thomas Biesaart
 */
public class EditorTabController implements AutoCloseable, Initializable {
    private static final Logger LOGGER = Log.get();
    private Preferences preferences;
    @FXML
    private Button toolbarUndoBtn;
    @FXML
    private Button toolbarRedoBtn;
    @FXML
    private CodeArea codeArea;
    private Path currentFile;
    private boolean closed;
    private final Timeline saveTimeline = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    e -> save()
            )
    );

    @Inject
    public EditorTabController(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Initialize this tab for a specific file.
     *
     * @param path the file to load
     * @throws IOException           if an IO Error occurs
     * @throws IllegalStateException if this tab has already been initialized
     */
    public void loadFile(Path path) throws IOException {
        if (currentFile != null) {
            throw new IllegalStateException("A file has already been loaded into this tab");
        }
        currentFile = path;
        refreshFromDisk();
    }

    private void refreshFromDisk() throws IOException {
        try (InputStream data = Files.newInputStream(currentFile)) {
            codeArea.clear();
            codeArea.appendText(IOUtils.toString(data));
            codeArea.getUndoManager().forgetHistory();
            saveTimeline.stop();
        }
    }

    @Override
    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeArea.setParagraphGraphicFactory(
                LineNumberFactory.get(codeArea)
        );
        codeArea.redoAvailableProperty().addListener((observable, oldValue, newValue) ->
                toolbarRedoBtn.setDisable(!newValue)
        );
        codeArea.undoAvailableProperty().addListener((observable, oldValue, newValue) ->
                toolbarUndoBtn.setDisable(!newValue)
        );
        codeArea.textProperty().addListener((observable, oldValue, newValue) ->
                saveTimeline.playFromStart()
        );

        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }

    /**
     * Key pressed even in the CodeArea
     * TODO: add save shortcut
     * TODO: switch tab shortcut
     * TODO: add more hotkeys
     * @param event key press event
     */
    private void onKeyPressed(KeyEvent event) {
        
    }

    private void save() {
        // Always end on newline
        if (!codeArea.getText().endsWith("\n")) {
            IndexRange range = codeArea.getSelection();
            codeArea.appendText("\n");
            codeArea.selectRange(range.getStart(), range.getEnd());
        }
        try (InputStream data = IOUtils.toInputStream(codeArea.getText())) {
            LOGGER.info("Saving {}", currentFile);
            Files.createDirectories(currentFile.getParent());
            Files.copy(data, currentFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Failed to save " + currentFile, e);
        }
    }

    public void toggleSelectionBold() {
        insertAroundSelection("**");
    }

    public void toggleSelectionItalic() {
        insertAroundSelection("*");
    }

    public void toggleSelectionStrikeThrough() {
        insertAroundSelection("~~");
    }

    public void toggleSelectionCode() {
        insertAroundSelection("```", "```", true);
    }

    private void insertAroundSelection(String text) {
        insertAroundSelection(text, text, false);
    }

    private void insertAroundSelection(String before, String after, boolean newLine) {
        // Insert selection
        IndexRange selection = codeArea.getSelection();
        String text = codeArea.getSelectedText();
        String prependText = before;
        String appendText = after;

        if (newLine) {
            prependText = "\n" + prependText + "\n";
            appendText = "\n" + appendText + "\n";
        }

        // Insert the text
        codeArea.replaceText(selection, prependText + text + appendText);

        // Move caret inside block
        int selectionPos = selection.getStart() + before.length();
        if (newLine) {
            // Correct for the first newline
            selectionPos++;
        }
        codeArea.selectRange(selectionPos, selectionPos);
        codeArea.requestFocus();
    }

    public void undo() {
        codeArea.undo();
    }

    public void redo() {
        codeArea.redo();
    }

    public Path getPath() {
        return currentFile;
    }
}
