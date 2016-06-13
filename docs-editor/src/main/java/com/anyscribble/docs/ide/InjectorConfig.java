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
package com.anyscribble.docs.ide;

import com.anyscribble.docs.ide.prefs.Preferences;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javafx.stage.Stage;

/**
 * This class represent the configuration for the application level injector.
 *
 * @author Thomas Biesaart
 */
class InjectorConfig extends AbstractModule {
    private final Stage primaryStage;

    InjectorConfig(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    protected void configure() {
        bind(Stage.class).toInstance(primaryStage);
    }

    @Provides
    Preferences preferences() {
        return Preferences.getUserPreferences("anyscribble");
    }
}
