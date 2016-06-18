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
package com.anyscribble.docs.model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class represents the implementation of the root project configuration.
 * This configuration can be loaded from xml using the [@link {@link XmlProjectParser}.
 *
 * @author Thomas Biesaart
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
public class Project {
    @XmlElement(required = true)
    private String name;

    @XmlElement(defaultValue = "target")
    @XmlJavaTypeAdapter(XmlPathAdapter.class)
    private Path buildDir = Paths.get("target");

    @XmlElement(defaultValue = "src/main/docs")
    @XmlJavaTypeAdapter(XmlPathAdapter.class)
    private Path sourceDir = Paths.get("src/main/docs");

    @XmlElements(
            @XmlElement(name = "pdf", type = PdfBuildConfiguration.class)
    )
    @XmlElementWrapper
    private List<BuildConfiguration> build;

    @XmlElement
    private DefaultsBuildConfiguration configuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getBuildDir() {
        return buildDir;
    }

    void setBuildDir(Path buildDir) {
        this.buildDir = buildDir;
    }

    public Path getSourceDir() {
        return sourceDir;
    }

    void setSourceDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    public List<BuildConfiguration> getBuild() {
        return build;
    }

    public void setBuild(List<BuildConfiguration> build) {
        this.build = build;
    }

    public DefaultsBuildConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(DefaultsBuildConfiguration configuration) {
        this.configuration = configuration;
    }
}
