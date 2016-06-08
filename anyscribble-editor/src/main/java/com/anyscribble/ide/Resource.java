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


import com.anyscribble.core.AnyScribble;
import com.google.common.base.CaseFormat;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This class represents a collection of string resources that can be localized using a {@link ResourceBundle}.
 *
 * @author Thomas Biesaart
 */
public class Resource {
    private static final Logger LOGGER = Log.get();
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("com.anyscribble.ide.messages");
    public static final String VERSION = getVersion();
    public static final String APPLICATION_TITLE = getString("title", VERSION);
    public static final String COPYRIGHT_NOTICE = getString("copyright");
    public static final String MENU_FILE = getString("menu.file");
    public static final String MENU_FILE_NEW = getString("menu.file.new");
    public static final String MENU_FILE_OPEN = getString("menu.file.open");
    public static final String MENU_FILE_CLOSE_TAB = getString("menu.file.closetab");
    public static final String MENU_FILE_CLOSE_PROJECT = getString("menu.file.closeproject");
    public static final String MENU_FILE_EXIT = getString("menu.file.exit");
    public static final String MENU_FILE_PREFERENCES = getString("menu.file.preferences");
    public static final String MENU_HELP = getString("menu.help");
    public static final String MENU_HELP_ABOUT = getString("menu.help.about");
    public static final String MENU_HELP_CONTACT = getString("menu.help.contact");
    public static final MessageProvider ERROR_INVALID_PROJECT_ROOT = new MessageProvider("error.project.invalidroot");
    public static final MessageProvider CONFIRM_DELETE_PROJECT = new MessageProvider("confirm.delete.project");
    public static final String PROJECT_NEW_TITLE = getString("project.new.title");

    private Resource() {
        throw new IllegalAccessError("Do not make instances of utilities");
    }

    /**
     * Get a message from the resource bundle.
     *
     * @param identity   the key of the message
     * @param parameters the parameters that should be inserted
     * @return the parsed message or the identity if the message was not found
     */
    public static String getString(String identity, Object... parameters) {
        String key = "com.anyscribble.ide." + identity;
        if (!resourceBundle.containsKey(key)) {
            LOGGER.warn("No message found for " + key);
            return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, identity);
        }

        return MessageFormat.format(
                resourceBundle.getString(key),
                parameters
        );
    }

    private static String getVersion() {
        String version = AnyScribble.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "Development Version";
        }
        return version;
    }

    public static class MessageProvider {
        private final String key;

        private MessageProvider(String key) {
            this.key = key;
        }

        public String get(Object... params) {
            return getString(key, params);
        }
    }
}
