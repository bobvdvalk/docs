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
package com.anyscribble.docs.core;

import com.anyscribble.docs.model.Project;
import com.google.inject.Inject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Objects;

/**
 * This class is responsible for parsing xml configuration files to projects.
 *
 * @author Thomas Biesaart
 */
public class DocsProjectParser {
    private final Unmarshaller unmarshaller;

    @Inject
    public DocsProjectParser() throws JAXBException {
        this(JAXBContext.newInstance(Project.class).createUnmarshaller());
    }


    public DocsProjectParser(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public Project loadProject(InputStream inputStream) throws JAXBException {
        Objects.requireNonNull(inputStream);
        return unmarshaller.unmarshal(new StreamSource(inputStream), Project.class).getValue();
    }
}
