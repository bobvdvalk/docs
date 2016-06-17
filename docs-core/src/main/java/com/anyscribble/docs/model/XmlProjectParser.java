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
package com.anyscribble.docs.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class is responsible for parsing xml configuration files to projects.
 * It will use the supplied JAXB implementation to unmarshall an xml file to
 * a {@link Project} object.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class XmlProjectParser {
    private final Unmarshaller unmarshaller;

    private static Unmarshaller buildDefaultUnmarshaller() throws JAXBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(Project.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(Project.class.getResource("/docs-1.0.0.xsd"));
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    /**
     * Build a new {@link XmlProjectParser} using the default {@link JAXBContext}.
     *
     * @throws JAXBException when building the context fails
     */
    @Inject
    public XmlProjectParser() throws JAXBException, SAXException {
        this(buildDefaultUnmarshaller());
    }

    /**
     * Build a new {@link XmlProjectParser} using a custom {@link Unmarshaller}.
     *
     * @param unmarshaller the unmarshaller
     */
    public XmlProjectParser(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    /**
     * Build a {@link Project} from an xml stream.
     *
     * @param projectRootFolder the root folder of the project
     * @param inputStream       the project configuration
     * @return the project
     * @throws JAXBException if a model error occurred
     */
    public Project loadProject(Path projectRootFolder, InputStream inputStream) throws JAXBException {
        Objects.requireNonNull(inputStream);
        Project project = unmarshaller.unmarshal(new StreamSource(inputStream), Project.class).getValue();

        // Resolve paths
        project.setSourceDir(projectRootFolder.resolve(project.getSourceDir()));
        project.setBuildDir(projectRootFolder.resolve(project.getBuildDir()));

        // Set Output Files
        for (BuildConfiguration config : project.getBuild()) {
            if (project.getDefaults() != null) {
                config.setParent(project.getDefaults());
            }
            if (config.getTitle() == null) {
                config.setTitle(project.getName());
            }
            if (config.getOutputFile() == null) {
                config.setOutputFile(Paths.get(config.getTitle() + "." + config.defaultExtension()));
            }

            config.setOutputFile(
                    project.getBuildDir().resolve(config.getOutputFile())
            );
        }
        return project;
    }
}
