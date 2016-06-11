package com.anyscribble.core;

/**
 * This exception is generally thrown whenever the execution of a pandoc process fails.
 *
 * @author Thomas Biesaart
 */
public class PandocRuntimeException extends RuntimeException {
    public PandocRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
