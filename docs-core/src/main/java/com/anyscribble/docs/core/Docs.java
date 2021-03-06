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

import com.anyscribble.docs.core.process.PandocCallback;
import com.anyscribble.docs.core.process.PandocProcess;
import com.anyscribble.docs.core.process.PandocProcessFactory;
import com.anyscribble.docs.model.BuildConfiguration;
import com.anyscribble.docs.model.Project;
import com.anyscribble.docs.model.XmlProjectParser;
import com.google.inject.Inject;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the main entry point into the AnyScribble Docs core.
 *
 * @author Thomas Biesaart
 */
public class Docs {
    private final XmlProjectParser xmlProjectParser;
    private final PandocProcessFactory pandocProcessFactory;

    @Inject
    public Docs(XmlProjectParser xmlProjectParser, PandocProcessFactory pandocProcessFactory) {
        this.xmlProjectParser = xmlProjectParser;
        this.pandocProcessFactory = pandocProcessFactory;
    }

    public Project loadProject(Path projectFile) throws DocsException {
        Path projectFolder = projectFile.getParent();
        return loadProject(projectFolder, projectFile);
    }

    public Project loadProject(Path projectFolder, Path projectFile) throws DocsException {
        try (InputStream inputStream = Files.newInputStream(projectFile, StandardOpenOption.READ)) {
            return xmlProjectParser.loadProject(projectFolder, inputStream);
        } catch (NoSuchFileException | AccessDeniedException e) {
            throw new ConfigurationNotFoundException("No configuration file found at " + projectFile, e);
        } catch (IOException e) {
            throw new DocsException("IO Error while loading project configuration from " + projectFile, e);
        } catch (UnmarshalException e) {
            throw new DocsException(e.getCause().getMessage(), e);
        } catch (JAXBException e) {
            throw new DocsException(e.getMessage(), e);
        }
    }

    public DocsProcess buildProcess(Project project, PandocCallback callback) {
        List<PandocProcess> processes = project.getBuild().stream()
                .filter(conf -> conf.isEnabled() != null)
                .filter(BuildConfiguration::isEnabled)
                .map(buildConfiguration -> pandocProcessFactory.buildProcess(project, buildConfiguration, callback))
                .collect(Collectors.toCollection(ArrayList::new));

        return new DocsProcess(processes, callback);
    }
}
