/**
 * AnyScribble Docs Core - Writing for Developers by Developers
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
package com.anyscribble.docs.core.process;

import com.anyscribble.docs.model.BuildConfiguration;
import com.anyscribble.docs.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.anyscribble.docs.core.process.ProcessUtils.findExecutableOnPath;

@Singleton
public class PandocProcessFactory {
    private static final Logger LOGGER = Log.get();
    private static final Path PANDOC_EXEC;

    static {
        if (System.getProperty("pandoc.bin") != null) {
            PANDOC_EXEC = Paths.get(System.getProperty("pandoc.bin"));
        } else {
            PANDOC_EXEC = findExecutableOnPath("pandoc.exe");
        }
        if (PANDOC_EXEC != null) {
            LOGGER.info("Found Pandoc Installation at {}", PANDOC_EXEC);
        } else {
            LOGGER.warn("No Pandoc Installation found");
        }
    }

    private final Path pandocExecutable;

    @Inject
    public PandocProcessFactory() {
        this(PANDOC_EXEC);
    }

    public PandocProcessFactory(Path pandocExecutable) {
        this.pandocExecutable = pandocExecutable;
    }

    public PandocProcess buildProcess(Project project, BuildConfiguration config, PandocCallback callback) {
        ProcessBuilder processBuilder = new ProcessBuilder(pandocExecutable.toAbsolutePath().toString());
        processBuilder.directory(project.getSourceDir().toFile());

        PandocProcess process = new PandocProcess(processBuilder, callback, config);

        // Output File
        process.addParameter("f", "markdown");
        process.addParameter("o", config.getOutputFile());

        process.addMetadata("title", config.getTitle());
        process.addMetadata("author", config.getAuthor());

        return process;
    }
}
