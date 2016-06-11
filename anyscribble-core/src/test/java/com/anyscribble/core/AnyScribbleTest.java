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
