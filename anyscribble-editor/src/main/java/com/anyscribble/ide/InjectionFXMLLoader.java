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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * This class is responsible for loading fxml files with controller dependency injection.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class InjectionFXMLLoader {
    private static final Logger LOGGER = Log.get();
    private final FXMLLoader fxmlLoader;
    private final Injector injector;

    @Inject
    public InjectionFXMLLoader(FXMLLoader fxmlLoader, Injector injector) {
        this.fxmlLoader = fxmlLoader;
        this.injector = injector;
        fxmlLoader.setControllerFactory(this::buildController);
    }

    private Object buildController(Class<?> aClass) {
        LOGGER.debug("Initializing Controller: {}", aClass);
        return injector.getInstance(aClass);
    }

    /**
     * Load an fxml file using dependency injection.
     *
     * @param fxmlResource the fxml resource
     * @param <T>          the type of root node in the resource
     * @return the node
     */
    public synchronized <T> T load(URL fxmlResource) {
        fxmlLoader.setLocation(fxmlResource);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load fxml from " + fxmlResource, e);
        }
    }
}
