package com.distribuida.books;


import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@ApplicationScoped
public class BooksLifeCycle {

    @ConfigProperty(name = "consul.host", defaultValue = "localhost")
    private String consultHost;

    //necesario para iniciar el port.
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    private int consultPort;

    @ConfigProperty(name = "quarkus.http.port")
    private int port;

    //id de la instancia y sea unica
    private String serviceId;

    //Observes llama el metodo
    public void init(@Observes StartupEvent evt, Vertx vertx) throws UnknownHostException {
        System.out.println("********BooksLifeCycle init********");

        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()
                        .setHost(consultHost)
                        .setPort(consultPort)
        );

        //genera un random extenso
        serviceId = UUID.randomUUID().toString();
        //tambien puedes poner otros atributos for example name addres
        var ipAddress = InetAddress.getLocalHost();
        String httpCheckUrl = String.format("http://%s:%d/books", ipAddress.getHostAddress(), port);

        client.registerServiceAndAwait(
                new ServiceOptions()
                        .setName("app-books")
                        .setId(serviceId)
                        .setAddress(ipAddress.getHostAddress())
                        .setPort(port)
                        .setCheckOptions(
                                new CheckOptions()
                                        .setHttp(httpCheckUrl)
                                        .setInterval("10s")
                                        .setDeregisterAfter("20s")
                        )
        );
    }

    //no es necesario porque el consul lo detiene
    public void stop(@Observes ShutdownEvent stevt, Vertx vertx) {
        System.out.println("********BooksLifeCycle stop********");

        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()
                        .setHost(consultHost)
                        .setPort(consultPort)
        );

        client.deregisterServiceAndAwait(serviceId);
    }
}
