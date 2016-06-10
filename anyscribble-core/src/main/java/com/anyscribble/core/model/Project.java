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

import java.nio.file.Path;

public class Project extends OutputConfiguration {
    private Path sourceDir;
    private Path buildDir;
    private String name;
    private PDFOutputConfiguration pdf;

    public Path getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    public Path getBuildDir() {
        return buildDir;
    }

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
