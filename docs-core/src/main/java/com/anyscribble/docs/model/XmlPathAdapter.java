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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used by JAXB to convert between {@link String Strings} and {@link Path paths}
 *
 * @author Thomas Biesaart
 */
class XmlPathAdapter extends XmlAdapter<String, Path> {

    @Override
    public Path unmarshal(String v) throws Exception {
        return Paths.get(v);
    }

    @Override
    public String marshal(Path v) throws Exception {
        return v.toString();
    }
}
