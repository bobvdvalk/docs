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
package com.anyscribble.docs.core.services;

import com.anyscribble.docs.core.BuildProcessCallback;
import com.anyscribble.docs.core.Configuration;
import com.anyscribble.docs.core.PandocProcess;
import com.anyscribble.docs.core.model.OutputConfiguration;
import com.anyscribble.docs.core.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class is responsible for building processes for a specific project configuration.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class PandocProcessFactory {
    private static final Logger LOGGER = me.biesaart.utils.Log.get();
    private final Configuration configuration;

    @Inject
    public PandocProcessFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<PandocProcess> buildProcesses(Project project, BuildProcessCallback processCallback) {
        List<PandocProcess> result = new ArrayList<>();

        List<Path> files = gatherFiles(project.getSourcePath(), processCallback);

        result.forEach(p -> {
            p.addParameters(null, files);
            LOGGER.debug("{}", p);
        });

        return result;
    }

    private List<Path> gatherFiles(Path sourceDir, BuildProcessCallback callback) {
        List<Path> result = new ArrayList<>();

        if (Files.exists(sourceDir)) {
            try {
                Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String fileName = file.getFileName().toString();
                        int index = fileName.lastIndexOf('.');
                        if (index != -1) {
                            String ext = fileName.substring(index + 1);
                            if ("md".equals(ext) || "markdown".equals(ext)) {
                                result.add(file);
                            }
                        }
                        return super.visitFile(file, attrs);
                    }
                });
            } catch (IOException e) {
                callback.onError(e);
            }
        }
        return result;
    }

    private PandocProcess buildBaseProcess(Project project, OutputConfiguration outputConfiguration, BuildProcessCallback processCallback) {
        Path sourceDir = project.getSourcePath();
        Path buildDir = project.getBuildPath();
        Path targetFile = buildDir.resolve(
                Optional.ofNullable(outputConfiguration.getOutputPath())
                        .orElse(Paths.get(project.getName() + ".pdf"))
        );

        ProcessBuilder processBuilder = new ProcessBuilder(configuration.getPandocExecutable().toString());

        PandocProcess process = new PandocProcess(processBuilder, sourceDir, processCallback);

        process.addOnStart((p, x) -> Files.createDirectories(targetFile.getParent()));

        process.addParameter("o", targetFile);

        // TODO More Options

        return process;
    }

}
