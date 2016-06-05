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
package com.anyscribble.ide;

import com.anyscribble.core.AnyScribble;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * This class represents the main loader of the editor application.
 * It will configure the injector and start the application.
 *
 * @author Thomas Biesaart
 */
public class AnyScribbleApplication extends Application {
    private static final Logger LOGGER = Log.get();
    private static final String ANYSCRIBBLE_FXML_PATH = "/com/anyscribble/ide/anyscribble.fxml";

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Configuring Injection");
        Injector injector = AnyScribble.createInjector(
                new InjectorConfig(primaryStage)
        );
        InjectionFXMLLoader fxmlLoader = injector.getInstance(InjectionFXMLLoader.class);

        LOGGER.info("Loading Interface");
        Scene scene = new Scene(
                fxmlLoader.load(getClass().getResource(ANYSCRIBBLE_FXML_PATH))
        );

        primaryStage.setTitle(Resource.APPLICATION_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
