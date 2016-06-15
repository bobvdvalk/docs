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
package com.anyscribble.docs.core;

import com.anyscribble.docs.core.model.Project;
import com.anyscribble.docs.core.services.PandocProcessFactory;
import com.anyscribble.docs.core.services.ProjectConfigurationParser;
import com.google.inject.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
    private final PandocProcessFactory pandocProcessFactory;
    private final ProjectConfigurationParser projectConfigurationParser;

    @Inject
    AnyScribble(PandocProcessFactory pandocProcessFactory, ProjectConfigurationParser projectConfigurationParser) {
        this.pandocProcessFactory = pandocProcessFactory;
        this.projectConfigurationParser = projectConfigurationParser;
    }

    public Project loadProject(Path projectFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(projectFile)) {
            Project project = projectConfigurationParser.load(inputStream);
            if (project.getName() == null) {
                throw new ProjectConfigurationException("The project must have a name");
            }
            Path projectDir = projectFile.getParent();
            project.setSourceDir(projectDir.resolve(project.getSourcePath()).toFile());
            project.setBuildDir(projectDir.resolve(project.getBuildPath()).toFile());

            return project;
        } catch (JAXBException e) {
            throw new IOException("Invalid project configuration.\n" + e.getMessage(), e);
        }
    }

    public AnyScribbleTask buildProcesses(Project project, BuildProcessCallback processCallback) {
        return new AnyScribbleTask(pandocProcessFactory.buildProcesses(project, processCallback), processCallback);
    }

    /**
     * Create a new injector that is configured to procude {@link AnyScribble AnyScribbles}.
     * The default configuration will be used.
     *
     * @return the injector
     */

    public static Injector createInjector(Module... modules) {
        Module[] injectorModules = new Module[modules.length + 1];
        injectorModules[0] = new AnyScribbleInjectionModule(new Configuration(Configuration.findPandoc()));
        System.arraycopy(modules, 0, injectorModules, 1, modules.length);
        return Guice.createInjector(injectorModules);
    }
}
