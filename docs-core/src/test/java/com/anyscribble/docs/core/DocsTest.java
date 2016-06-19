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

import com.anyscribble.docs.model.Project;
import com.google.inject.Guice;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.testng.Assert.assertEquals;

public class DocsTest {
    private Path projectFile;

    @BeforeClass
    public void deployProjectFile() throws IOException {
        projectFile = Files.createTempFile("UnitTest", ".xml");
        Files.copy(getClass().getResourceAsStream("/minimalProject.xml"), projectFile, StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterClass
    public void cleanUp() throws IOException {
        Files.delete(projectFile);
    }

    @Test
    public void testLoadProject() throws Exception {
        Docs docs = Guice.createInjector().getInstance(Docs.class);

        Project project = docs.loadProject(projectFile);

        assertEquals(project.getName(), "Test Project");
        assertEquals(project.getBuildDir().getParent().normalize().toString(), projectFile.getParent().normalize().toString());
    }

}
