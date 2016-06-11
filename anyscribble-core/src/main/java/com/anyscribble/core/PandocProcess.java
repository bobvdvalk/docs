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
package com.anyscribble.core;

import me.biesaart.utils.Log;
import me.biesaart.utils.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an instance of the pandoc process.
 * It is a wrapper that allows you to easily add arguments.
 *
 * @author Thomas Biesaart
 */
public class PandocProcess extends Thread {
    private static final Logger LOGGER = Log.get();
    private final ProcessBuilder processBuilder;
    private final Path workingDirectory;
    private final List<EventHandler> onStart = new ArrayList<>();
    private Process process;

    public PandocProcess(ProcessBuilder processBuilder, Path sourceDir) {
        this.processBuilder = processBuilder;
        this.workingDirectory = sourceDir.toAbsolutePath();
        processBuilder.directory(sourceDir.toFile());
    }

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

    public void addParameter(String key, Path value) {
        Path absolutePath = workingDirectory.resolve(value);
        Path relativePath = workingDirectory.relativize(absolutePath.toAbsolutePath());

        addParameter(key, relativePath.toString());
    }

    public void addParameters(String key, Iterable<Path> values) {
        if (values != null) {
            for (Path path : values) {
                addParameter(key, path);
            }
        }
    }

    public void addOnStart(EventHandler listener) {
        onStart.add(listener);
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (IOException e) {
            throw new PandocRuntimeException("Execution failed", e);
        }
    }

    private void doRun() throws IOException {
        // Notify on start
        fire(onStart);

        processBuilder.inheritIO();
        process = processBuilder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new PandocRuntimeException("Execution interrupted", e);
        }
    }

    private void fire(List<EventHandler> eventHandlers) throws IOException {
        for (EventHandler handler : eventHandlers) {
            handler.accept(this);
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

    public interface EventHandler {
        void accept(PandocProcess pandocProcess) throws IOException;
    }
}
