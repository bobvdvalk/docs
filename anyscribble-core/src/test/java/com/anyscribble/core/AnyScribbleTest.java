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
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;


public class AnyScribbleTest {
    @Test
    public void testLoadProject() throws Exception {

        Path projectFile = Files.createTempFile("Unit Test", ".json");

        AnyScribble anyScribble = new AnyScribble(
                new PandocProcessFactory(new Configuration(Paths.get("."))),
                new ProjectConfigurationParser()
        );

        Files.copy(IOUtils.toInputStream("{\"name\": \"Hello World\"}"), projectFile, StandardCopyOption.REPLACE_EXISTING);

        Project project = anyScribble.loadProject(projectFile);

        assertEquals(project.getName(), "Hello World");
    }

}
