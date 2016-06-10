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

import com.anyscribble.core.model.Project;
import com.anyscribble.core.services.PandocProcessFactory;
import com.anyscribble.core.services.ProjectConfigurationParser;
import com.google.inject.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class represents the main entry point to the AnyScribble core.
 * The core is mostly a wrapper around the pandoc document conversion tool with some
 * utilities around it.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class AnyScribble {
    private final PandocProcessFactory pandocProcessFactory;
    private final ProjectConfigurationParser projectConfigurationParser;

    @Inject
    AnyScribble(PandocProcessFactory pandocProcessFactory, ProjectConfigurationParser projectConfigurationParser) {
        this.pandocProcessFactory = pandocProcessFactory;
        this.projectConfigurationParser = projectConfigurationParser;
    }

    public Project loadProject(Path projectFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(projectFile)) {
            return projectConfigurationParser.load(inputStream);
        }
    }

    public List<ProcessBuilder> buildProcesses(Path projectRoot, Project project) throws IOException {
        return pandocProcessFactory.buildProcesses(projectRoot, project);
    }

    /**
     * Create a new injector that is configured to procude {@link AnyScribble AnyScribbles}.
     * The default configuration will be used.
     *
     * @return the injector
     */

    public static Injector createInjector(Module... modules) {
        return createInjector(Guice.createInjector(modules));
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

    public static void main(String[] args) throws IOException {
        Path projectPath = Paths.get("D:\\Libraries\\Code\\anyscribble-test");
        Path projectFile = projectPath.resolve("project.json");

        AnyScribble anyScribble = new AnyScribble(
                new PandocProcessFactory(new Configuration(Configuration.findPandoc())),
                new ProjectConfigurationParser()
        );

        Project project = anyScribble.loadProject(projectFile);

        List<ProcessBuilder> processBuilders = anyScribble.buildProcesses(projectPath, project);

        for (ProcessBuilder builder : processBuilders) {
            for(String arg : builder.command()) {
                System.out.print(arg);
                System.out.print(" ");
            }
            System.out.println();
            System.out.println("In " + builder.directory());
            builder.start();
        }

    }
}
