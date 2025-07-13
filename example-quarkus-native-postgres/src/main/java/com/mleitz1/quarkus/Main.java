package com.mleitz1.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Main entry point for the Quarkus application.
 * This class is not strictly necessary as Quarkus can start without it,
 * but it provides a clear entry point and allows for custom initialization if needed.
 */
@QuarkusMain
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Quarkus Native PostgreSQL Application...");
        Quarkus.run(args);
    }
}
