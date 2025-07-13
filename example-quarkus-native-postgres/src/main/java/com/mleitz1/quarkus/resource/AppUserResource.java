package com.mleitz1.quarkus.resource;

import java.net.URI;
import com.mleitz1.quarkus.model.AppUser;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppUserResource {

    private static final Logger LOG = Logger.getLogger(AppUserResource.class);

    @Inject
    PgPool client;

    @GET
    public Multi<AppUser> getAll() {
        return client.query("SELECT id, name, email FROM app_user ORDER BY name")
                .execute()
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .onItem().transform(this::mapToAppUser);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return client.preparedQuery("SELECT id, name, email FROM app_user WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? mapToAppUser(iterator.next()) : null)
                .onItem().transform(user -> user != null ? Response.ok(user).build() :
                                           Response.status(Status.NOT_FOUND).build());
    }

    @POST
    public Uni<Response> create(AppUser user) {
        return client.preparedQuery("INSERT INTO app_user (name, email) VALUES ($1, $2) RETURNING id")
                .execute(Tuple.of(user.getName(), user.getEmail()))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? iterator.next().getLong("id") : null)
                .onItem().transform(id -> URI.create("/users/" + id))
                .onItem().transform(uri -> Response.created(uri).build())
                .onFailure().recoverWithItem(throwable -> {
                    LOG.error("Failed to create user", throwable);
                    return Response.status(Status.BAD_REQUEST).build();
                });
    }

    private AppUser mapToAppUser(Row row) {
        AppUser user = new AppUser();
        user.setId(row.getLong("id"));
        user.setName(row.getString("name"));
        user.setEmail(row.getString("email"));
        return user;
    }
}
