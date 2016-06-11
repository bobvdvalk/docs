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
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * This class represents a collection of pandoc processes.
 *
 * @author Thomas Biesaart
 */
public class AnyScribbleTask extends Thread implements AutoCloseable {
    private static final Logger LOGGER = Log.get();
    private final List<PandocProcess> pandocProcessList;

    public AnyScribbleTask(List<PandocProcess> pandocProcessList) {
        this.pandocProcessList = pandocProcessList;
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

        for (PandocProcess process : pandocProcessList) {
            process.start();
        }

        for (PandocProcess process : pandocProcessList) {
            try {
                process.join();
            } catch (InterruptedException e) {
                LOGGER.error("Execution was interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() throws InterruptedException {
        for (PandocProcess process : pandocProcessList) {
            process.close();
        }
    }
}
