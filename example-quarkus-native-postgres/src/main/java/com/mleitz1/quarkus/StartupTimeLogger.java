package com.mleitz1.quarkus;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

/**
 * @author leitz@mikeleitz.com
 */
@ApplicationScoped
public class StartupTimeLogger {
    private static final Logger LOG = Logger.getLogger(StartupTimeLogger.class);
    private final long startTime = System.nanoTime();

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    int httpPort;

    void onStart(@Observes StartupEvent ev) {
        long endTime = System.nanoTime();
        double startupTimeMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        LOG.infof("Application started in %.3f ms (%.0f ns)", startupTimeMs, startupTimeMs * 1_000_000);
        LOG.infof("example-quarkus-native-postgres is ready to ROCK on port %d", httpPort);
    }
}
