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

import me.biesaart.utils.Log;
import me.biesaart.utils.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This class represents an instance of the pandoc process.
 * It is a wrapper that allows you to easily add arguments.
 *
 * @author Thomas Biesaart
 */
public class PandocProcess extends Thread implements AutoCloseable {
    private static final Logger LOGGER = Log.get();
    private final ProcessBuilder processBuilder;
    private final List<EventHandler<PandocProcess>> onStart = new ArrayList<>();
    private final BuildProcessCallback buildProcessCallback;
    private Process process;

    public PandocProcess(ProcessBuilder processBuilder, Path sourceDir, BuildProcessCallback buildProcessCallback) {
        this.processBuilder = processBuilder;
        this.buildProcessCallback = buildProcessCallback;
        processBuilder.directory(sourceDir.toFile());
    }

    /**
     * Add a parameter to the process.
     * If the key is one character  the character notation <pre>-X VALUE</pre> will be used, if the key is longer than
     * that the property notation <pre>--prop=VALUE</pre> will be used and if the key is null the value will simply be
     * added to the parameters.
     *
     * @param key   the key
     * @param value the value
     */
    public void addParameter(String key, String value) {
        if (key == null) {
            processBuilder.command().add(parseParameterValue(value));
        } else if (key.length() == 1) {
            processBuilder.command().add("-" + key);
            processBuilder.command().add(parseParameterValue(value));
        } else {
            processBuilder.command().add(String.format("--%s=%s", key, parseParameterValue(value)));
        }
    }

    private String parseParameterValue(String value) {
        if (value.contains(" ")) {
            return String.format("\"%s\"", value);
        } else {
            return value;
        }
    }

    /**
     * Add a path parameter to the process.
     * If the key is one character  the character notation <pre>-X VALUE</pre> will be used, if the key is longer than
     * that the property notation <pre>--prop=VALUE</pre> will be used and if the key is null the value will simply be
     * added to the parameters.
     *
     * @param key   the key
     * @param value the value
     */
    public void addParameter(String key, Path value) {
        addParameter(key, value.toString());
    }

    /**
     * Add list of parameters to the process.
     * This is equivalent to calling:
     * <pre>
     *    for (Path path : values) {
     *      addParameter(key, path);
     *   }
     * </pre>
     *
     * @param key    the key
     * @param values the value
     */
    public void addParameters(String key, Iterable<Path> values) {
        if (values != null) {
            for (Path path : values) {
                addParameter(key, path);
            }
        }
    }

    public void addOnStart(EventHandler<PandocProcess> listener) {
        onStart.add(listener);
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (IOException e) {
            buildProcessCallback.onError(e);
        }
    }

    private void doRun() throws IOException {
        // Notify on onStart
        fire(onStart, this);
        buildProcessCallback.onStart(this);

        process = processBuilder.start();

        Scanner scanner = new Scanner(process.getInputStream());

        while (scanner.hasNextLine()) {
            buildProcessCallback.onError(new PandocRuntimeException(scanner.nextLine()));
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            LOGGER.error("Execution was interrupted!");
            Thread.currentThread().interrupt();
        }
    }

    private <T> void fire(List<EventHandler<T>> eventHandlers, T data) {
        for (EventHandler<T> handler : eventHandlers) {
            try {
                handler.accept(this, data);
            } catch (IOException e) {
                buildProcessCallback.onError(e);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s%nIn %s",
                StringUtils.join(processBuilder.command(), " "),
                processBuilder.directory()
        );
    }

    @Override
    public void close() throws InterruptedException {
        if (process != null) {
            process.destroyForcibly().waitFor(2, TimeUnit.SECONDS);
        }
    }

    @FunctionalInterface
    public interface EventHandler<T> {
        void accept(PandocProcess pandocProcess, T data) throws IOException;
    }
}
