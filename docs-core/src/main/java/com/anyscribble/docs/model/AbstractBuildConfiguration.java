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

import com.anyscribble.docs.core.process.PandocProcess;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.nio.file.Path;


@XmlTransient
public abstract class AbstractBuildConfiguration implements BuildConfiguration {
    @XmlJavaTypeAdapter(XmlPathAdapter.class)
    private Path outputFile;
    @XmlElement(defaultValue = "true")
    private Boolean enabled;
    private String title;
    private String author;
    @XmlElement(defaultValue = "true")
    private Boolean toc;

    @Override
    public Path getOutputFile() {
        return outputFile;
    }

    @Override
    public void setOutputFile(Path file) {
        this.outputFile = file;
    }

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Boolean enableToc() {
        return toc;
    }

    @Override
    public void setEnableToc(Boolean enableToc) {
        this.toc = enableToc;
    }

    @Override
    public void applyOptionsTo(PandocProcess process) {
        process.addParameter("o", outputFile);
        process.addMetadata("title", title);
        process.addMetadata("author", author);
        if (toc) {
            process.addFlag("toc");
        }

    }

    @Override
    public void extractDefaults(DefaultsBuildConfiguration config) {
        if (outputFile == null) {
            setOutputFile(config.getOutputFile());
        }
        if (enabled == null) {
            setEnabled(config.isEnabled());
        }
        if (title == null) {
            setTitle(config.getTitle());
        }
        if (author == null) {
            setAuthor(config.getAuthor());
        }
        if (toc == null) {
            setEnableToc(config.enableToc());
        }
    }
}
