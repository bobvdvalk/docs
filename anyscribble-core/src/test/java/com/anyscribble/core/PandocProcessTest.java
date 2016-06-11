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
