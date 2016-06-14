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

import com.anyscribble.docs.core.model.Project;
import com.google.inject.Injector;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertTrue;


public class AnyScribbleIT {
    private Path projectPath;

    @BeforeClass
    public void deployProject() throws IOException {
        projectPath = Files.createTempDirectory("AnyScribble-IntegrationTest");

        Files.copy(getClass().getResourceAsStream("/anyscribble/it/project.json"), projectPath.resolve("project.json"));
        Files.copy(getClass().getResourceAsStream("/anyscribble/it/chapter.md"), projectPath.resolve("chapter.md"));
    }

    @AfterClass
    public void deleteProject() throws IOException {
        Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    @Test
    public void test() throws IOException, InterruptedException {
        Injector injector = AnyScribble.createInjector();

        AnyScribble anyScribble = injector.getInstance(AnyScribble.class);
        Project project = anyScribble.loadProject(projectPath.resolve("project.json"));
        try (AnyScribbleTask processes = anyScribble.buildProcesses(project, mock(BuildProcessCallback.class))) {
            processes.start();
            processes.join();
        }
        // Check if the files were built
        assertTrue(Files.exists(projectPath.resolve("target/AnyScribble Integration Test.pdf")));
    }
}
