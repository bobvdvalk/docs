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

import com.anyscribble.docs.core.model.Project;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * This injection module should be used to load the AnyScript environment.
 *
 * @author Thomas Biesaart
 */
class AnyScribbleInjectionModule extends AbstractModule {
    private final Configuration configuration;

    AnyScribbleInjectionModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(configuration);
    }

    @Provides
    @Singleton
    @Named("anyscribble")
    JAXBContext jaxbContext() throws JAXBException {
        return JAXBContext.newInstance(Project.class);
    }

    @Provides
    @Named("anyscribble")
    Unmarshaller unmarshaller(@Named("anyscribble") JAXBContext context) throws JAXBException {
        return context.createUnmarshaller();
    }
}
