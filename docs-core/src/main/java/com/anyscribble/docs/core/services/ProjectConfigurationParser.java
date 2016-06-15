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
package com.anyscribble.docs.core.services;

import com.anyscribble.docs.core.model.Project;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * This class is responsible for serialization and deserialization of
 * the xml project files.
 *
 * @author Thomas Biesaart
 */
@Singleton
public class ProjectConfigurationParser {
    private final Unmarshaller unmarshaller;

    @Inject
    public ProjectConfigurationParser(@Named("anyscribble") Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public Project load(InputStream configuration) throws JAXBException {
        return unmarshaller.unmarshal(new StreamSource(configuration), Project.class).getValue();
    }
}
