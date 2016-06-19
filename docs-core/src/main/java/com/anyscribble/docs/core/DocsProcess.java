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

import com.anyscribble.docs.core.process.PandocCallback;
import com.anyscribble.docs.core.process.PandocProcess;
import me.biesaart.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * This class represents a collection of {@link PandocProcess PandocProcesses}.
 *
 * @author Thomas Biesaart
 */
public class DocsProcess extends Thread implements AutoCloseable {
    private final List<PandocProcess> pandocProcess;
    private final PandocCallback pandocCallback;
    private final OutputStream outputStreamForwarder;

    public DocsProcess(List<PandocProcess> pandocProcess, PandocCallback pandocCallback) {
        setName("DocsProcess");
        this.pandocProcess = pandocProcess;
        this.pandocCallback = pandocCallback;
        outputStreamForwarder = new DataForwarder();
    }

    @Override
    public void run() {
        // Start all processes
        pandocProcess.forEach(Thread::start);

        // Wait for all processes to finish
        for (PandocProcess process : pandocProcess) {
            try {
                process.join();
            } catch (InterruptedException e) {
                pandocCallback.onError(e);
                Thread.currentThread().interrupt();
            }
        }

        pandocCallback.onBatchComplete();
    }

    public void write(InputStream inputStream) throws IOException {
        IOUtils.copy(inputStream, outputStreamForwarder);
    }

    public OutputStream getOutputStream() {
        return outputStreamForwarder;
    }

    @Override
    public void close() {
        // Close all process streams
        for (PandocProcess process : pandocProcess) {
            try {
                process.getOutputStream().close();
            } catch (IOException e) {
                pandocCallback.onError(e);
            }
        }
    }

    private class DataForwarder extends OutputStream {

        @Override
        public void write(byte[] b) throws IOException {
            for (PandocProcess process : pandocProcess) {
                process.getOutputStream().write(b);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {

            for (PandocProcess process : pandocProcess) {
                process.getOutputStream().write(b, off, len);
            }
        }

        @Override
        public void write(int b) throws IOException {
            for (PandocProcess process : pandocProcess) {
                process.getOutputStream().write(b);
            }
        }

        @Override
        public void flush() throws IOException {
            for (PandocProcess process : pandocProcess) {
                process.getOutputStream().flush();
            }
        }

        @Override
        public void close() throws IOException {
            for (PandocProcess process : pandocProcess) {
                process.getOutputStream().close();
            }
        }
    }
}
