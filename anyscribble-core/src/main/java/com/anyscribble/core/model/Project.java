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
package com.anyscribble.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class represents the configuration for the a project.
 *
 * It matches the structure of the json project configuration.
 *
 * @author Thomas Biesaart
 */
public class Project extends OutputConfiguration {
    private Path sourceDir;
    private Path buildDir;
    private String name;
    private PDFOutputConfiguration pdf;

    /**
     * This creator is used when we deserialize a json.
     * This way we can have defaults when we come from a json but no defaults when we instantiate the
     * class ourselves.
     *
     * @return the project default
     */
    @JsonCreator
    public static Project buildProject() {
        Project project = new Project();
        project.setSourceDir(Paths.get("src/main/markdown"));
        project.setBuildDir(Paths.get("target"));
        return project;
    }

    /**
     * Get the root directory of the sources.
     * This will be used as the working directory for the build.
     *
     * @return the path
     */
    public Path getSourceDir() {
        return sourceDir;
    }

    /**
     * Get the root directory of the sources.
     *
     * @param sourceDir the path
     */
    public void setSourceDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    /**
     * Get the build directory.
     * This is the directory where all outputs will be placed.
     *
     * @return the path
     */
    public Path getBuildDir() {
        return buildDir;
    }

    /**
     * Set the build directory.
     *
     * @param buildDir this is the directory where all outputs will be placed.
     */
    public void setBuildDir(Path buildDir) {
        this.buildDir = buildDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PDFOutputConfiguration getPdf() {
        return pdf;
    }

    public void setPdf(PDFOutputConfiguration pdf) {
        this.pdf = pdf;
    }
}
