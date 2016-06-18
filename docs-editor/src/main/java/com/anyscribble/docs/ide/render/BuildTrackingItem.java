package com.anyscribble.docs.ide.render;

import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

/**
 * This class represents a tracked build in the build execution step.
 *
 * @author Thomas Biesaart
 */
public class BuildTrackingItem extends HBox {

    public BuildTrackingItem(BuildSelectionItem buildSelectionItem, ProcessCallback processCallback) {
        super(new Text(buildSelectionItem.getLabel()));

        Pane spacer = new Pane();
        spacer.setMinWidth(20);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        if (buildSelectionItem.isSelected()) {
            ProgressIndicator progressIndicator = new ProgressIndicator(-1);
            progressIndicator.setMaxSize(20, 20);
            processCallback.addOnComplete(
                    buildSelectionItem.getBuild(),
                    conf -> {
                        // Update the progress indicator on the JavaFX Thread
                        Platform.runLater(
                                () -> progressIndicator.setProgress(2)
                        );
                    }
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
}
