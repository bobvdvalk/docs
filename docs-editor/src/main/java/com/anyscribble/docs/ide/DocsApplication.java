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
package com.anyscribble.docs.ide;

import com.anyscribble.docs.ide.prefs.Preferences;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * This class represents the main loader of the editor application.
 * It will configure the injector and onStart the application.
 *
 * @author Thomas Biesaart
 */
public class DocsApplication extends Application {
    private static final Logger LOGGER = Log.get();
    private static final String DOCS_FXML_PATH = "/com/anyscribble/docs/ide/anyscribble.fxml";
    private Injector injector;

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.debug("Building Injector");
        injector = Guice.createInjector(new InjectorConfig(primaryStage));

        setStageData(primaryStage);
        bindSizeToPreferences(primaryStage);
        loadScene(primaryStage);

        primaryStage.show();
    }

    private void loadScene(Stage primaryStage) {
        LOGGER.debug("Loading Scene");
        InjectionFXMLLoader fxmlLoader = injector.getInstance(InjectionFXMLLoader.class);

        Scene scene = new Scene(fxmlLoader.load(getClass().getResource(DOCS_FXML_PATH)));
        primaryStage.setScene(scene);
    }

    private void bindSizeToPreferences(Stage primaryStage) {
        LOGGER.debug("Binding ");
        Preferences preferences = injector.getInstance(Preferences.class);
        preferences.get(Setting.WINDOW_WIDTH).ifPresent(width -> primaryStage.setWidth(Double.parseDouble(width)));
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                preferences.put(Setting.WINDOW_WIDTH, Double.toString(newValue.doubleValue()))
        );

        // Persist Height
        preferences.get(Setting.WINDOW_HEIGHT).ifPresent(height -> primaryStage.setHeight(Double.parseDouble(height)));
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                preferences.put(Setting.WINDOW_HEIGHT, Double.toString(newValue.doubleValue()))
        );
    }

    private void setStageData(Stage primaryStage) {
        LOGGER.debug("Setting stage data");
        primaryStage.setTitle(Resource.APPLICATION_TITLE);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/com/anyscribble/docs/ide/icon.png"))
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
