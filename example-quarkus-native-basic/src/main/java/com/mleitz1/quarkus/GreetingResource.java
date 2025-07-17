package com.mleitz1.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple REST endpoint using Quarkus RESTEasy Reactive.
 *
 * Note: Even though this endpoint appears to use a synchronous API (returning String directly),
 * it's running on Quarkus' reactive HTTP engine. Quarkus RESTEasy Reactive automatically handles
 * the conversion between reactive and synchronous programming models, allowing for a simpler API
 * while still benefiting from reactive execution.
 */
@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus Native!";
    }
}
