/**
 * AnyScribble Core - Writing for Developers by Developers
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
package com.anyscribble.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.nio.file.Path;

/**
 * This class represents the main entry point to the AnyScribble core.
 * The core is mostly a wrapper around the pandoc document conversion tool with some
 * utilities around it.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class AnyScribble {
    private final Configuration configuration;

    @Inject
    AnyScribble(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Create a new injector that is configured to procude {@link AnyScribble AnyScribbles}.
     * The default configuration will be used.
     *
     * @return the injector
     */
    public static Injector createInjector() {
        return createInjector(Guice.createInjector());
    }

    /**
     * Create a new child injector that is configured to procude {@link AnyScribble AnyScribbles}.
     * The default configuration will be used.
     *
     * @param injector the parent injector
     * @return the injector
     */
    private static Injector createInjector(Injector injector) {
        return createInjector(injector, Configuration.findPandoc());
    }

    /**
     * Create a new injector that is configured to procude {@link AnyScribble AnyScribbles}.
     * The provided configuration will be used.
     *
     * @param injector      the parent injector
     * @param pandocBinPath the path to the pandoc installation
     * @return the injector
     */
    private static Injector createInjector(Injector injector, Path pandocBinPath) {
        return injector.createChildInjector(new AnyScribbleInjectionModule(new Configuration(pandocBinPath)));
    }
}
