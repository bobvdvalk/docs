package com.anyscribble.docs.ide.render;

import com.anyscribble.docs.model.BuildConfiguration;
import com.anyscribble.docs.model.Project;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

/**
 * This element is used when selecting the builds that should run.
 *
 * @author Thomas Biesaart
 */
public class BuildSelectionItem extends HBox {
    private final BuildConfiguration buildConfiguration;
    private final CheckBox checkBox;
    private final String label;

    public BuildSelectionItem(Project project, BuildConfiguration buildConfiguration) {
        this.buildConfiguration = buildConfiguration;
        this.label = project.getBuildDir().relativize(buildConfiguration.getOutputFile()).toString();
        this.checkBox = new CheckBox(label);
        this.checkBox.setSelected(true);
        getChildren().setAll(checkBox);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public BuildConfiguration getBuild() {
        return buildConfiguration;
    }

    public String getLabel() {
        return label;
    }
}
