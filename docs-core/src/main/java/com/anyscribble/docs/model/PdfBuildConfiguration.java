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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfBuildConfiguration extends BuildConfiguration {
    @XmlElement
    private String fontFamily;

    @Override
    public String defaultExtension() {
        return "pdf";
    }

    public String getFontFamily() {
        if (fontFamily == null) {
            return "arev";
        }
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    @Override
    public void applyOptionsTo(PandocProcess process) {
        super.applyOptionsTo(process);

        process.addMetadata("fontfamily", getFontFamily());
        process.addMetadata("margin-top", "100pt");
        process.addMetadata("margin-bottom", "160pt");
        process.addMetadata("margin-left", "60pt");
        process.addMetadata("margin-right", "60pt");

    }
}
