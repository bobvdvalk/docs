package com.anyscribble.docs.ide.render;

import com.anyscribble.docs.model.BuildConfiguration;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;

/**
 * This class represents a tracked build in the build execution step.
 *
 * @author Thomas Biesaart
 */
public class BuildTrackingItem extends HBox {
    private static final Logger LOGGER = Log.get();
    private final Pane spacer;

    public BuildTrackingItem(BuildSelectionItem buildSelectionItem, ProcessCallback processCallback) {
        super(new Text(buildSelectionItem.getLabel()));

        spacer = new Pane();
        spacer.setMinWidth(50);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        if (buildSelectionItem.isSelected()) {
            ProgressIndicator progressIndicator = new ProgressIndicator(-1);
            progressIndicator.setMaxSize(30, 30);
            processCallback.addOnComplete(
                    buildSelectionItem.getBuild(),
                    this::onComplete
            );
            getChildren().add(
                    progressIndicator
            );
        } else {
            getChildren().add(
                    new Text("Skipped")
            );
        }

    }

    private void onComplete(BuildConfiguration conf) {
        // Replace the progress indicator with a view button
        Platform.runLater(
                () -> {
                    Button button = new Button("Open");
                    button.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
                    button.setOnAction(event -> open(conf));
                    getChildren().set(2, button);
                    spacer.setMinWidth(0);
                }
        );
    }

    private void open(BuildConfiguration buildConfiguration) {
        try {
            Desktop.getDesktop().open(
                    buildConfiguration.getOutputFile().toFile()
            );
        } catch (IOException e) {
            LOGGER.error("Failed to open file", e);
        }
    }
}
