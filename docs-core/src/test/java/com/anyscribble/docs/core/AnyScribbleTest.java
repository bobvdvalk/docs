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
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.testng.Assert.assertEquals;


public class AnyScribbleTest {
    private final AnyScribble anyScribble = new AnyScribble(
            new PandocProcessFactory(new Configuration(Paths.get("."))),
            new ProjectConfigurationParser(new AnyScribbleInjectionModule(null).jaxbContext().createUnmarshaller())
    );

    public AnyScribbleTest() throws JAXBException {
    }

    @Test
    public void testLoadProject() throws Exception {
        Path projectFile = Files.createTempFile("Unit Test", ".xml");

        Files.copy(getClass().getResourceAsStream("/testProject.xml"), projectFile, StandardCopyOption.REPLACE_EXISTING);

        Project project = anyScribble.loadProject(projectFile);

        assertEquals(project.getName(), "Test Project");
    }
}
