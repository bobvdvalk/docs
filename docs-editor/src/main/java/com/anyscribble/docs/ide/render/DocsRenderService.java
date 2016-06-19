package com.anyscribble.docs.ide.render;

import com.anyscribble.docs.core.Docs;
import com.anyscribble.docs.core.DocsException;
import com.anyscribble.docs.core.DocsProcess;
import com.anyscribble.docs.ide.Resource;
import com.anyscribble.docs.model.Project;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.VBox;
import me.biesaart.utils.IOUtils;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for the user flow of building a project.
 *
 * The render flow consists of three steps:
 *
 * 1. Present a project selection to the user
 * 2. Present a build selection to the user
 * 3. Perform the build and notify the user of its status
 *
 * @author Thomas Biesaart
 */
@Singleton
public class DocsRenderService {
    private static final Logger LOGGER = Log.get();
    private final Provider<Docs> docsProvider;
    private Docs docs;

    @Inject
    public DocsRenderService(Provider<Docs> docsProvider) {
        this.docsProvider = docsProvider;
    }

    public void startRenderFlow(Path currentSelection, List<Path> choices) {
        ChoiceDialog<Path> choiceDialog = new ChoiceDialog<>(currentSelection, choices);

        choiceDialog.setHeaderText(Resource.DIALOG_BUILD_PROJECT_TITLE);
        choiceDialog.setTitle(Resource.DIALOG_BUILD_PROJECT_TITLE);
        choiceDialog.setContentText(Resource.DIALOG_BUILD_PROJECT_CONTENT);

        choiceDialog.showAndWait().ifPresent(path -> {
            Path projectFile = path.resolve("docs.xml");
            launchRenderFlow(projectFile);
        });
    }

    private void launchRenderFlow(Path projectFile) {
        try {
            Project project = getDocs().loadProject(projectFile);
            launchRenderFlow(project);
        } catch (DocsException e) {
            LOGGER.error("Failed to load project", e);
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.setTitle("Project Error");
            alert.setHeaderText("Invalid Project");
            alert.show();
        }
    }

    private void launchRenderFlow(Project project) {
        // Prompt the user for a build selection
        List<BuildSelectionItem> builds = project.getBuild()
                .stream()
                .map(b -> new BuildSelectionItem(project, b))
                .collect(Collectors.toList());

        VBox dialogContent = new VBox();
        dialogContent.getChildren().setAll(builds);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Which build would you like to run?", ButtonType.NEXT, ButtonType.CANCEL);
        alert.setTitle("Build Project");
        alert.setHeaderText("Select Builds");
        alert.getDialogPane().setContent(dialogContent);
        alert.showAndWait().ifPresent(
                buttonType -> {
                    if (buttonType == ButtonType.NEXT) {
                        // Update Project
                        runBuild(project, builds);

                    }
                }
        );
    }

    private void runBuild(Project project, List<BuildSelectionItem> builds) {
        ProcessCallback processCallback = new ProcessCallback();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Building Project...", ButtonType.CLOSE);
        alert.setTitle("Build Project");
        alert.setHeaderText("Building...");
        VBox dialogContent = new VBox();
        // Create progress bars
        dialogContent.getChildren().setAll(
                builds.stream()
                        .map(conf -> new BuildTrackingItem(conf, processCallback))
                        .collect(Collectors.toList())
        );
        alert.getDialogPane().setContent(dialogContent);
        alert.show();

        // Update project
        project.setBuild(
                builds.stream()
                        .filter(BuildSelectionItem::isSelected)
                        .map(BuildSelectionItem::getBuild)
                        .collect(Collectors.toList())
        );

        DocsProcess process = getDocs().buildProcess(project, processCallback);
        runProcess(project, process, processCallback);
    }

    private void runProcess(Project project, DocsProcess process, ProcessCallback processCallback) {
        process.start();

        try {
            Files.walkFileTree(project.getSourceDir(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, process.getOutputStream());
                    IOUtils.write("\n", process.getOutputStream());
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            processCallback.onError(e);
        } finally {
            process.close();
        }
    }

    private Docs getDocs() {
        if (docs == null) {
            docs = docsProvider.get();
        }
        return docs;
    }


}
