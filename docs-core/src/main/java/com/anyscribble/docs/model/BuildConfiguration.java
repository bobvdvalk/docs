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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
public class BuildConfiguration {
    @XmlElement
    @XmlJavaTypeAdapter(XmlPathAdapter.class)
    private Path outputFile;

    @XmlElement(defaultValue = "true")
    private Boolean enabled = true;
    @XmlTransient
    private BuildConfiguration parent = this;

    @XmlElement
    private String title;

    @XmlElement
    private String author;

    public Path getOutputFile() {
        return parentOrSelf(outputFile, parent.outputFile);
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    public Boolean getEnabled() {
        return parentOrSelf(enabled, parent.enabled);
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return parentOrSelf(title, parent.title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return parentOrSelf(author, parent.author);
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setParent(BuildConfiguration parent) {
        this.parent = parent;
    }

    public String defaultExtension() {
        return "md";
    }

    private <T> T parentOrSelf(T self, T parent) {
        if (self != null) {
            return self;
        }
        return parent;
    }

}
