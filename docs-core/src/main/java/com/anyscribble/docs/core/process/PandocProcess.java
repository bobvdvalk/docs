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
package com.anyscribble.docs.core.process;

import com.anyscribble.docs.model.BuildConfiguration;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * This class represents a pandoc process. It wraps a {@link ProcessBuilder} that is linked
 * to the pandoc executable.
 *
 * It is created using the {@link PandocProcessFactory}.
 *
 * @author Thomas Biesaart
 */
public class PandocProcess extends Thread {
    private static final Logger LOGGER = Log.get();
    private final ProcessBuilder processBuilder;
    private final PandocCallback callback;
    private final BuildConfiguration buildConfiguration;
    private final Path workingDir;
    private OutputStream outputStream;

    PandocProcess(ProcessBuilder processBuilder, PandocCallback callback, BuildConfiguration buildConfiguration) {
        setName("Pandoc-" + buildConfiguration.getOutputFile().getFileName());
        this.processBuilder = processBuilder;
        this.callback = callback;
        this.buildConfiguration = buildConfiguration;
        workingDir = processBuilder.directory().toPath();
    }

    @Override
    public void run() {
        Process process = startProcess();
        if (process == null) {
            return;
        }
        outputStream = process.getOutputStream();
        callback.onStart(buildConfiguration);

        try {

            Scanner scanner = new Scanner(process.getErrorStream());

            while (scanner.hasNextLine()) {
                callback.onError(new PandocExecutionException(scanner.nextLine()));
            }

            process.waitFor();
        } catch (InterruptedException e) {
            callback.onError(e);
            Thread.currentThread().interrupt();
        } finally {
            process.destroyForcibly();
        }

        callback.onProcessComplete(buildConfiguration);
    }

    private Process startProcess() {
        LOGGER.info("Starting Pandoc Process: {}", processBuilder.command());
        try {
            return processBuilder.start();
        } catch (IOException e) {
            callback.onError(e);
            return null;
        }
    }

    public void addMetadata(String variable, String value) {
        if (value != null) {
            addParameter("M", variable + ":" + value);
        }
    }

    public void addFlag(String name) {
        if (name.length() == 1) {
            processBuilder.command().add("-" + name);
        } else {
            processBuilder.command().add("--" + name);
        }
    }

    public void addParameter(String key, Path value) {
        addParameter(key, workingDir.relativize(value).toString());
    }

    public void addParameter(String key, String value) {
        String escapedValue = parameterValue(value);
        if (key == null) {
            processBuilder.command().add(escapedValue);
        } else if (key.length() == 1) {
            processBuilder.command().add("-" + key);
            processBuilder.command().add(escapedValue);
        } else {
            processBuilder.command().add(String.format("--%s=%s", key, escapedValue));
        }
    }

    private String parameterValue(String value) {
        if (value.contains(" ")) {
            return String.format("\"%s\"", value);
        }
        return value;
    }

    public OutputStream getOutputStream() {
        while (outputStream == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return outputStream;
    }

}
