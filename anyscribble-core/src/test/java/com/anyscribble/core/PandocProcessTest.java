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

import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class PandocProcessTest {
    @Test
    public void testAddParameter() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Call the method
        PandocProcess pandocProcess = new PandocProcess(processBuilder, Paths.get("."));
        pandocProcess.addParameter("test", "value");
        pandocProcess.addParameter("t", "value2");
        pandocProcess.addParameter(null, "filePath");
        pandocProcess.addParameter(null, "file Path");

        assertEquals(processBuilder.command().get(0), "--test=value");
        assertEquals(processBuilder.command().get(1), "-t");
        assertEquals(processBuilder.command().get(2), "value2");
        assertEquals(processBuilder.command().get(3), "filePath");
        assertEquals(processBuilder.command().get(4), "\"file Path\"");
    }

    @Test
    public void testAddParameter1() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Call the method
        PandocProcess pandocProcess = new PandocProcess(processBuilder, Paths.get("unit-test"));
        pandocProcess.addParameter(null, Paths.get("test.xml"));

        assertEquals(processBuilder.command().get(0), "test.xml");
    }

    @Test
    public void testAddParameters() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Call the method
        PandocProcess pandocProcess = new PandocProcess(processBuilder, Paths.get("unit-test"));

        pandocProcess.addParameters(null, null);
        assertTrue(processBuilder.command().isEmpty());

        pandocProcess.addParameters(null, Collections.singletonList(Paths.get(".")));
        assertEquals(processBuilder.command(), Collections.singletonList("."));
    }

}
