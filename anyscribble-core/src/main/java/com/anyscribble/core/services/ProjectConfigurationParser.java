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
package com.anyscribble.core.services;

import com.anyscribble.core.model.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public class ProjectConfigurationParser {
    private final ObjectMapper objectMapper;
    private final ObjectReader objectReader;
    private final ObjectWriter objectWriter;

    @Inject
    public ProjectConfigurationParser() {
        this(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL));
    }

    public ProjectConfigurationParser(ObjectMapper objectMapper) {
        this(
                objectMapper,
                objectMapper.readerFor(Project.class),
                objectMapper.writerFor(Project.class)
                        .withDefaultPrettyPrinter()

        );
    }

    public ProjectConfigurationParser(ObjectMapper objectMapper, ObjectReader objectReader, ObjectWriter objectWriter) {
        this.objectMapper = objectMapper;
        this.objectReader = objectReader;
        this.objectWriter = objectWriter;
    }

    public Project load(InputStream configuration) throws IOException {
        return objectReader.readValue(configuration);
    }

    public void write(Project project, OutputStream outputStream) throws IOException {
        objectWriter.writeValue(outputStream, project);
    }
}
