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

/**
 * This interface represents an object that listens to a pandoc process.
 *
 * @author Thomas Biesaart
 */
public interface PandocCallback {
    void onStart(BuildConfiguration configuration);

    void onError(Throwable e);

    void onBatchComplete();

    void onProcessComplete(BuildConfiguration configuration);
}
