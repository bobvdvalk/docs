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
package com.anyscribble.docs.ide.prefs;


import com.anyscribble.docs.ide.Setting;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class is responsible for persisting simple preferences.
 * It works by identifying a preference which is stored as a string.
 * For example, I want to save the last entered message in my dialog. Let's call this preference dialog.message.
 * <pre>
 * // Save my message
 * preferences.put("dialog.message", "Hello World");
 *
 * // Fetch my message and specify a default
 * preferences.get("dialog.message").orElse("This is the first time you see this dialog");
 * </pre>
 *
 * @author Thomas Biesaart
 */
public abstract class Preferences {
    private static Map<String, Preferences> cache = new HashMap<>();

    /**
     * Create a {@link Preferences} that stores data in the user's home folder.
     *
     * @param vendor the vendor name
     * @return the preferences
     */
    public static Preferences getUserPreferences(String vendor) {
        if (cache.containsKey(vendor)) {
            return cache.get(vendor);
        }
        Properties properties = new Properties(System.getProperties());

        Path homeFolder = Paths.get(properties.getProperty("user.home"));
        Path propertiesFolder = homeFolder.resolve("." + vendor.replaceAll("[^\\w]+", "").toLowerCase());

        Preferences result = new XMLPreferences(properties, propertiesFolder.resolve("properties.xml"));
        cache.put(vendor, result);
        return result;
    }

    /**
     * Bind a hot key from preferences to an action on a node.
     *
     * Whenever that node has focus and it meets an unconsumed key pressed event that matches the key combination
     * it will fire the action.
     *
     * @param target        the node
     * @param key           the preference
     * @param defaultHotKey the default hot key
     * @param action        the action
     */
    public void bindHotKey(Node target, Setting key, KeyCombination defaultHotKey, Callback action) {
        target.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCombination keyCombination = get(key).map(KeyCombination::keyCombination).orElse(defaultHotKey);
            if (keyCombination.match(event)) {
                action.trigger();
            }
        });
    }

    /**
     * Bind a hot key from preferences to an action on a node.
     *
     * Whenever that node has focus and it meets an unconsumed key pressed event that matches the key combination
     * it will fire the action.
     *
     * @param target        the node
     * @param key           the preference
     * @param defaultHotKey the default hot key
     * @param action        the action
     */
    public void bindHotKey(Node target, Setting key, String defaultHotKey, Callback action) {
        bindHotKey(target, key, KeyCombination.keyCombination(defaultHotKey), action);
    }

    /**
     * Fetch a preference from the persistence.
     *
     * @param key the unique key of the preference
     * @return the preference of {@link Optional#empty()}} if it was not found
     */
    public abstract Optional<String> get(Setting key);

    /**
     * Fetch a collection preference from the persistence.
     *
     * @param key the unique key of the preference
     * @return the preference of {@link Optional#empty()}} if it was not found
     */
    public abstract Optional<List<String>> getList(Setting key);

    /**
     * Save a String preference.
     * Use this method to save a preference that is identified by a key.
     *
     * @param key   the unique key of the preference
     * @param value the value that should be saved
     * @throws NullPointerException if the key or value is null
     */
    public abstract void put(Setting key, String value);

    /**
     * Save a collection of objects. The string value of these objects will be saved to the persistence.
     * Fetch it later using {@link #getList(Setting)}.
     *
     * @param key   the unique key of the preference
     * @param value the collection
     */
    public abstract void putList(Setting key, Iterable<String> value);

    @FunctionalInterface
    public interface Callback {
        void trigger();
    }
}
