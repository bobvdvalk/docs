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

import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class represents the configuration for the AnyScript core.
 *
 * @author Thomas Biesaart
 */
public class Configuration {
    private static final Logger LOGGER = Log.get();
    public static final String PANDOC_BIN_KEY = "pandoc.bin";
    private static final String[] PANDOC_EXTENSIONS = {"exe"};
    private static boolean scannedLocation;
    private static Path pandocLocation;

    private final Path pandocExecutable;

    public Configuration(Path pandocExecutable) {
        this.pandocExecutable = pandocExecutable;
    }

    /**
     * This method will locate a pandoc installation on the current system.
     * It will first check the pandoc.bin system property for overrides. If that
     * property is not present this method will attempt to find the pandoc executable in the path
     * variable.
     *
     * @return the path to the pandoc executable
     * @throws PandocNotFoundException if no pandoc installation was found
     */
    static synchronized Path findPandoc() {
        if (!scannedLocation) {
            LOGGER.debug("Locating pandoc executable");
            if (System.getProperty(PANDOC_BIN_KEY) != null) {
                LOGGER.debug("Found system property: " + PANDOC_BIN_KEY);
                // Load the system property
                pandocLocation = Paths.get(System.getProperty(PANDOC_BIN_KEY));
            } else if (System.getenv("path") != null) {
                pandocLocation = scanPathVariable();
            }
            // Set the flag so we do not scan again
            scannedLocation = true;
        }

        if (pandocLocation == null) {
            throw new PandocNotFoundException();
        }

        return pandocLocation;
    }

    private static Path scanPathVariable() {
        // Scan the path variable
        for (String path : System.getenv("path").split(File.pathSeparator)) {
            try {
                Path javaPath = Paths.get(path);

                Path exec = scanFolder(javaPath);
                if (exec != null) {
                    return exec;
                }
            } catch (InvalidPathException e) {
                LOGGER.warn("Failed to parse " + path + " as a path.\n" + e.getReason(), e);
            }
        }
        return null;
    }

    private static Path scanFolder(Path javaPath) {
        for (String extension : PANDOC_EXTENSIONS) {
            Path exec = javaPath.resolve("pandoc." + extension);
            if (Files.exists(exec)) {
                LOGGER.debug("Found pandoc instance at {}", exec);
                return exec;
            }
        }
        return null;
    }

    public Path getPandocExecutable() {
        return pandocExecutable;
    }
}
