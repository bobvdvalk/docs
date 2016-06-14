package com.anyscribble.docs.core;

import java.io.IOException;

/**
 * This exception is generally thrown when there is a mistake in the project configuration
 * file.
 *
 * @author Thomas Biesaart
 */
public class ProjectConfigurationException extends IOException {
    public ProjectConfigurationException(String message) {
        super(message);
    }
}
