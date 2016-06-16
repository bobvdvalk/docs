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
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static org.testng.Assert.*;


public class DocsProjectParserTest {
    @Test
    public void testLoadProject() throws Exception {
        DocsProjectParser docsProjectParser = new DocsProjectParser();

        Project project = docsProjectParser.loadProject(getClass().getResourceAsStream("/minimalProject.xml"));

        assertEquals(project.getName(), "Test Project");
        // Test defaults
        assertEquals(project.getBuildDir(), Paths.get("target"));
        assertEquals(project.getSourceDir(),  Paths.get("src/main/docs"));
    }

}
