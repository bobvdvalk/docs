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

import com.anyscribble.core.model.Project;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class ProjectConfigurationParserTest {
    private final ProjectConfigurationParser projectConfigurationParser = new ProjectConfigurationParser();

    @Test
    public void testLoad() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/testProject.json")) {
            Project project = projectConfigurationParser.load(inputStream);
            assertEquals(project.getName(), "Unit Test");
            assertEquals(project.getBuildDir(), Paths.get("target")); // Default Value
            assertEquals(project.getSourceDir(), Paths.get("src"));
        }
    }

    @Test
    public void testWrite() throws Exception {
        Project project = new Project();
        project.setName("Write Unit Test");
        project.setSourceDir(Paths.get("."));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        projectConfigurationParser.write(project, outputStream);
        String json = outputStream.toString();
        assertTrue(json.contains(project.getName()));
        assertTrue(json.contains("."));
        assertTrue(json.length() < 60); // Don't serialize nulls
    }

}
