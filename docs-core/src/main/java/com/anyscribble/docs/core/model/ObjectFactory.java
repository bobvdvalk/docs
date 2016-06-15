package com.anyscribble.docs.core.model;

import java.io.File;

/**
 * This class is responsible for creating new objects with defaults when
 * unmarshalling XML.
 *
 * @author Thomas Biesaart
 */
public class ObjectFactory {

    public Project buildProject() {
        Project project = new Project();
        setDefaults(project);
        project.setSourceDir(new File("src/main/docs"));
        project.setBuildDir(new File("target"));
        return project;
    }

    private OutputConfiguration setDefaults(OutputConfiguration configuration) {
        return configuration;
    }
}
