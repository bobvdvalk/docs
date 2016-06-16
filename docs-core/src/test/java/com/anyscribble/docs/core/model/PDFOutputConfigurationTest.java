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

import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


public class PDFOutputConfigurationTest {
    private final PDFOutputConfiguration configuration = new PDFOutputConfiguration();
    private final PDFOutputConfiguration configurationWithParent = new PDFOutputConfiguration() {{
        attachParentConfiguration(configuration);
    }};

    @Test
    public void testGetSetParentValues() {
        testGetSet(
                OutputConfiguration::getOutputFile,
                OutputConfiguration::setOutputFile,
                Paths.get("."),
                Paths.get("..")
        );

        testGetSet(
                OutputConfiguration::getSmart,
                OutputConfiguration::setSmart,
                true,
                false
        );

        testGetSet(
                OutputConfiguration::getChapters,
                OutputConfiguration::setChapters,
                true,
                false
        );

        testGetSet(
                OutputConfiguration::getDefaultImageExtension,
                OutputConfiguration::setDefaultImageExtension,
                "png",
                "jpg"
        );

        testGetSet(
                OutputConfiguration::getBaseLevelHeaders,
                OutputConfiguration::setBaseLevelHeaders,
                3,
                2
        );

        testGetSet(
                OutputConfiguration::getFileScope,
                OutputConfiguration::setFileScope,
                true,
                false
        );

        testGetSet(
                OutputConfiguration::getNormalize,
                OutputConfiguration::setNormalize,
                true,
                false
        );

        testGetSet(
                OutputConfiguration::getPreserveTabs,
                OutputConfiguration::setPreserveTabs,
                true,
                false
        );
        testGetSet(
                OutputConfiguration::getTabStop,
                OutputConfiguration::setTabStop,
                3,
                4
        );
        testGetSet(
                OutputConfiguration::getStandalone,
                OutputConfiguration::setStandalone,
                true,
                false
        );
        testGetSet(
                OutputConfiguration::getTemplate,
                OutputConfiguration::setTemplate,
                Paths.get("."),
                Paths.get("..")
        );
        testGetSet(
                OutputConfiguration::getWrap,
                OutputConfiguration::setWrap,
                WrapStyle.AUTO,
                WrapStyle.NONE
        );
        testGetSet(
                OutputConfiguration::getColumns,
                OutputConfiguration::setColumns,
                2,
                4
        );
        testGetSet(
                OutputConfiguration::getToc,
                OutputConfiguration::setToc,
                true,
                false
        );
        testGetSet(
                OutputConfiguration::getTocDepth,
                OutputConfiguration::setTocDepth,
                2,
                3
        );
        testGetSet(
                OutputConfiguration::getHighlightStyle,
                OutputConfiguration::setHighlightStyle,
                "pretty",
                "not so pretty"
        );
        testGetSet(
                OutputConfiguration::getHeaders,
                OutputConfiguration::setHeaders,
                Collections.emptyList(),
                Collections.singletonList(Paths.get("."))
        );
        testGetSet(
                OutputConfiguration::getBeforeBody,
                OutputConfiguration::setBeforeBody,
                Collections.emptyList(),
                Collections.singletonList(Paths.get("."))
        );
        testGetSet(
                OutputConfiguration::getAfterBody,
                OutputConfiguration::setAfterBody,
                Collections.emptyList(),
                Collections.singletonList(Paths.get("."))
        );

    }


    private <T> void testGetSet(Function<PDFOutputConfiguration, T> getFunction, BiConsumer<PDFOutputConfiguration, T> setFunction, T value, T otherValue) {
        assertNull(getFunction.apply(configuration));
        assertNull(getFunction.apply(configurationWithParent));

        // Set on parent
        setFunction.accept(configuration, value);
        assertEquals(getFunction.apply(configuration), value);
        assertEquals(getFunction.apply(configurationWithParent), value);

        setFunction.accept(configurationWithParent, otherValue);
        assertEquals(getFunction.apply(configuration), value);
        assertEquals(getFunction.apply(configurationWithParent), otherValue);
    }
}
