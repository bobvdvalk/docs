/**
 * AnyScribble Editor - Writing for Developers by Developers
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
package com.anyscribble.ide;

public enum Setting {
    HOTKEY_SAVE,
    HOTKEY_TAB1,
    HOTKEY_TAB2,
    HOTKEY_TAB3,
    HOTKEY_TAB4,
    HOTKEY_TAB5,
    HOTKEY_TAB6,
    HOTKEY_TAB7,
    HOTKEY_TAB8,
    HOTKEY_TAB9,
    HOTKEY_TAB10,
    HOTKEY_BOLD,
    HOTKEY_ITALIC,
    HOTKEY_CODE,
    HOTKEY_H1,
    HOTKEY_H2,
    HOTKEY_H3,
    HOTKEY_H4,
    HOTKEY_H5,
    HOTKEY_FIND,
    HOTKEY_FIND_FILE,
    OPEN_TABS,
    OPEN_PROJECTS,
    WINDOW_WIDTH,
    WINDOW_HEIGHT;

    public String getLabel() {
        return name().toLowerCase().replaceAll("_", ".");
    }
}
