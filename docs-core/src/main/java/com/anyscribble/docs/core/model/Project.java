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
package com.anyscribble.docs.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.nio.file.Path;

/**
 * This class represents the configuration for the a project.
 *
 * It matches the structure of the json project configuration.
 *
 * @author Thomas Biesaart
 */
@XmlRootElement
public class Project extends OutputConfiguration {
    private File sourceDir;
    private File buildDir;
    private String name;

    /**
     * Get the root directory of the sources.
     * This will be used as the working directory for the build.
     *
     * @return the path
     */
    public File getSourceDir() {
        return sourceDir;
    }

    public Path getSourcePath() {
        return sourceDir.toPath();
    }

    /**
     * Get the root directory of the sources.
     *
     * @param sourceDir the path
     */
    @XmlElement
    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    /**
     * Get the build directory.
     * This is the directory where all outputs will be placed.
     *
     * @return the path
     */
    public File getBuildDir() {
        return buildDir;
    }

    public Path getBuildPath() {
        return buildDir.toPath();
    }

    /**
     * Set the build directory.
     *
     * @param buildDir this is the directory where all outputs will be placed.
     */
    @XmlElement
    public void setBuildDir(File buildDir) {
        this.buildDir = buildDir;
    }

    /**
     * Get the friendly name of the project.
     *
     * @return the name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * Set the friendly name of the project.
     *
     * @param name the name
     */
    @XmlElement(required = true)
    public void setName(String name) {
        this.name = name;
    }
}
