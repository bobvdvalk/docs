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
package com.anyscribble.core.services;

import com.anyscribble.core.Configuration;
import com.anyscribble.core.PandocProcess;
import com.anyscribble.core.model.OutputConfiguration;
import com.anyscribble.core.model.PDFOutputConfiguration;
import com.anyscribble.core.model.Project;
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

    public List<PandocProcess> buildProcesses(Path projectRoot, Project project) throws IOException {
        List<PandocProcess> result = new ArrayList<>();
        if (project.getPdf() != null) {
            result.add(buildPdfProcess(projectRoot, project, project.getPdf()));
        }

        Path sourcePath = projectRoot.resolve(project.getSourceDir()).toRealPath();
        List<Path> files = gatherFiles(sourcePath);

        result.forEach(p -> {
            p.addParameters(null, files);
            LOGGER.debug("{}", p);
        });

        return result;
    }

    private List<Path> gatherFiles(Path sourceDir) throws IOException {
        List<Path> result = new ArrayList<>();

        if (Files.exists(sourceDir)) {
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
        }
        return result;
    }

    private PandocProcess buildPdfProcess(Path projectRoot, Project project, PDFOutputConfiguration pdfOutputConfiguration) throws IOException {
        PandocProcess process = buildBaseProcess(projectRoot, project, pdfOutputConfiguration);


        return process;
    }


    private PandocProcess buildBaseProcess(Path projectRoot, Project project, OutputConfiguration outputConfiguration) throws IOException {
        Path sourceDir = projectRoot.resolve(project.getSourceDir()).toRealPath();
        Path buildDir = projectRoot.resolve(project.getBuildDir());
        Path targetFile = buildDir.resolve(
                Optional.ofNullable(outputConfiguration.getOutputFile())
                        .orElse(Paths.get(project.getName() + ".pdf"))
        );

        ProcessBuilder processBuilder = new ProcessBuilder(configuration.getPandocExecutable().toString());

        PandocProcess process = new PandocProcess(processBuilder, sourceDir);

        process.addOnStart(p -> Files.createDirectories(targetFile.getParent()));

        process.addParameter("o", targetFile);

        process.addParameters("H", outputConfiguration.getHeaders());
        process.addParameters("B", outputConfiguration.getBeforeBody());
        process.addParameters("A", outputConfiguration.getAfterBody());

        // TODO More Options

        return process;
    }

}
