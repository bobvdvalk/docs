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

public class PDFOutputConfiguration extends OutputConfiguration {
    private Boolean chapters;
    private Boolean numberSections;
    private Integer numberOffset;
    private Boolean noTexLigatures;
    private Boolean listings;

    public Boolean getChapters() {
        return chapters;
    }

    public void setChapters(Boolean chapters) {
        this.chapters = chapters;
    }

    public Boolean getNumberSections() {
        return numberSections;
    }

    public void setNumberSections(Boolean numberSections) {
        this.numberSections = numberSections;
    }

    public Integer getNumberOffset() {
        return numberOffset;
    }

    public void setNumberOffset(Integer numberOffset) {
        this.numberOffset = numberOffset;
    }

    public Boolean getNoTexLigatures() {
        return noTexLigatures;
    }

    public void setNoTexLigatures(Boolean noTexLigatures) {
        this.noTexLigatures = noTexLigatures;
    }

    public Boolean getListings() {
        return listings;
    }

    public void setListings(Boolean listings) {
        this.listings = listings;
    }
}
