package com.anyscribble.docs.ide.render;

import com.anyscribble.docs.core.process.PandocCallback;
import com.anyscribble.docs.model.BuildConfiguration;
import me.biesaart.utils.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents the callback for the UI elements used in the workflow.
 *
 * @author Thomas Biesaart
 */
public class ProcessCallback implements PandocCallback {
    private static final Logger LOGGER = Log.get();
    private final Map<BuildConfiguration, Consumer<BuildConfiguration>> onComplete = new HashMap<>();

    @Override
    public void onStart(BuildConfiguration configuration) {
        LOGGER.info("Starting Build: {}", configuration.getOutputFile());
        try {
            Files.createDirectories(configuration.getOutputFile().getParent());
        } catch (IOException e) {
            LOGGER.error("Could not create output directory", e);
        }
    }

    @Override
    public void onError(Throwable e) {
        LOGGER.error("An error occurred while building", e);
    }

    @Override
    public void onBatchComplete() {
        LOGGER.info("Batch Complete");
    }

    @Override
    public void onProcessComplete(BuildConfiguration configuration) {
        LOGGER.info("Finished {}", configuration.getOutputFile());
        Consumer<BuildConfiguration> complete = onComplete.get(configuration);
        if (complete != null) {
            complete.accept(configuration);
        }
    }

    public void addOnComplete(BuildConfiguration configuration, Consumer<BuildConfiguration> listener) {
        onComplete.put(configuration, listener);
    }
}
