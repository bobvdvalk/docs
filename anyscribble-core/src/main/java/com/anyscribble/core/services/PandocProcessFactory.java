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
import com.anyscribble.core.model.OutputConfiguration;
import com.anyscribble.core.model.PDFOutputConfiguration;
import com.anyscribble.core.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


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

    public List<ProcessBuilder> buildProcesses(Path projectRoot, Project project) throws IOException {
        List<ProcessBuilder> result = new ArrayList<>();
        if (project.getPdf() != null) {
            result.add(buildPdfProcess(projectRoot, project, project.getPdf()));
        }

        List<Path> files = gatherFiles(projectRoot.resolve(project.getSourceDir()));
        result.forEach(pb -> files.forEach(file -> addArgument(pb, null, file)));

        return result;
    }

    private ProcessBuilder buildPdfProcess(Path projectRoot, Project project, PDFOutputConfiguration pdfOutputConfiguration) throws IOException {
        ProcessBuilder processBuilder = buildBaseProcess(projectRoot, project, pdfOutputConfiguration);


        return processBuilder;
    }


    private ProcessBuilder buildBaseProcess(Path projectRoot, Project project, OutputConfiguration outputConfiguration) throws IOException {
        Path sourceDir = projectRoot.resolve(project.getSourceDir()).toRealPath();
        Path buildDir = projectRoot.resolve(project.getBuildDir());
        Path targetFile = outputConfiguration.getOutputFile();
        if (targetFile == null) {
            targetFile = buildDir.resolve(project.getName() + ".pdf");
        } else {
            targetFile = buildDir.resolve(outputConfiguration.getOutputFile());
        }

        Files.createDirectories(targetFile.getParent());

        ProcessBuilder processBuilder = new ProcessBuilder(configuration.getPandocExecutable().toString());
        processBuilder.inheritIO();
        processBuilder.directory(sourceDir.toFile());

        addArgument(processBuilder, "o", targetFile);

        parseList(processBuilder, "H", outputConfiguration.getHeaders(), projectRoot);
        parseList(processBuilder, "B", outputConfiguration.getBeforeBody(), projectRoot);
        parseList(processBuilder, "A", outputConfiguration.getAfterBody(), projectRoot);

        // TODO More Options

        return processBuilder;
    }

    private static void parseList(ProcessBuilder processBuilder, String argumentName, List<Path> value, Path baseDir) {
        if (value != null) {
            for (Path path : value) {
                Path fullPath = baseDir.resolve(path).toAbsolutePath();
                addArgument(processBuilder, argumentName, fullPath);
            }
        }
    }

    private static void addArgument(ProcessBuilder processBuilder, String key, Path path) {
        Path relative = processBuilder.directory().toPath().toAbsolutePath().relativize(path.toAbsolutePath());
        addArgument(processBuilder, key, relative.toString());
    }

    private static void addArgument(ProcessBuilder processBuilder, String key, String value) {
        if (key != null) {
            processBuilder.command().add("-" + key);
        }
        processBuilder.command().add(String.format("\"%s\"", value));
    }

    private List<Path> gatherFiles(Path sourceDir) throws IOException {
        List<Path> files = new ArrayList<>();

        if (Files.exists(sourceDir)) {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();
                    int index = fileName.lastIndexOf('.');
                    if (index != -1) {
                        String extension = fileName.substring(index + 1);
                        if ("md".equals(extension) || "markdown".equals(extension)) {
                            files.add(file);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            LOGGER.info("The source directory was not found for {}", sourceDir);
        }

        return files;
    }

}
