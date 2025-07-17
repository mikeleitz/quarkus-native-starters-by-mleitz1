package com.mleitz1.quarkus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * A simple REST endpoint using Quarkus RESTEasy Reactive.
 *
 * Note: This endpoint is part of a fully reactive application. While this particular
 * endpoint doesn't explicitly use reactive types like Uni<Response> or Multi<String>,
 * it's still running on Quarkus' reactive HTTP engine. For examples of explicit reactive
 * programming, see the AppUserResource class which uses Mutiny's Uni and Multi types
 * for non-blocking database operations.
 */
@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() {
        return Response.status(200)
            .entity("Hello from Quarkus Native with PostgreSQL!")
            .build();
    }
}
