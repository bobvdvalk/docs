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

import com.anyscribble.docs.core.AnyScribble;
import com.anyscribble.docs.core.AnyScribbleTask;
import com.anyscribble.docs.core.BuildProcessCallback;
import com.anyscribble.docs.core.PandocNotFoundException;
import com.anyscribble.docs.core.model.Project;
import com.anyscribble.docs.ide.Resource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import me.biesaart.utils.Log;
import org.omg.CORBA.Any;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * This class is responsible for strapping the core to the editor.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class AnyScribbleRenderer {
    private static final Logger LOGGER = Log.get();
    private final Provider<AnyScribble> anyScribbleProvider;

    @Inject
    public AnyScribbleRenderer(Provider<AnyScribble> anyScribbleProvider) {
        this.anyScribbleProvider = anyScribbleProvider;
    }

    public void startBuild(Project project, BuildProcessCallback callback) {
        Optional<AnyScribble> anyScribbleOptional = getAnyScribble();

        if (!anyScribbleOptional.isPresent()) {
            // This is handled with a dialog
            return;
        }

        AnyScribble anyScribble = anyScribbleOptional.get();
        AnyScribbleTask anyScribbleTask = anyScribble.buildProcesses(project, callback);

    }

    private Optional<AnyScribble> getAnyScribble() {
        try {
            return Optional.of(anyScribbleProvider.get());
        } catch (PandocNotFoundException e) {
            LOGGER.warn("No Pandoc installation was found", e);
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    Resource.ERROR_PANDOC_NOT_FOUND,
                    ButtonType.YES,
                    ButtonType.NO
            );
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    visit("http://pandoc.org/installing.html");
                }
            });
        }
        return Optional.empty();
    }

    private void visit(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Could not open " + url, e);
        }
    }
}
