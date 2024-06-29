package com.distribuida;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;

@Path("/hola")

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class HolaMundo {

    @Inject
    @ConfigProperty(name = "app.books.msg", defaultValue = "no encuentra msg")
    private String message;

    @GET
    public String hola() {

        //API, scanea las fuentes y retonar el msg configuracion.
//        Config config = ConfigProvider.getConfig();

//        config.getConfigSources()
//                .forEach(t -> {
//                    System.out.printf("%d: %s\n ", t.getOrdinal(), t.getName());
//                });
        //msg de configuracion...
//        var msg = config.getValue("app.books.msg", String.class);
        System.out.println(message);

        return message + " " + LocalDateTime.now();
    }
}
