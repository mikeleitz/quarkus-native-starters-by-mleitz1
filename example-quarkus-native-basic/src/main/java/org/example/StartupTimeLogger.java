package com.mleitz1.quarkus;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

/**
 * @author leitz@mikeleitz.com
 */
@ApplicationScoped
public class StartupTimeLogger {
    private static final Logger LOG = Logger.getLogger(StartupTimeLogger.class);
    private final long startTime = System.nanoTime();

    void onStart(@Observes StartupEvent ev) {
        long endTime = System.nanoTime();
        double startupTimeMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        LOG.infof("Application started in %.3f ms (%.0f ns)", startupTimeMs, startupTimeMs * 1_000_000);
    }
}
