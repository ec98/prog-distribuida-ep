package com.distribuida.books.rest;

import com.distribuida.books.clients.AuthorRestClient;
import com.distribuida.books.db.Book;
import com.distribuida.books.dtos.BookDto;
import com.distribuida.books.repo.BooksRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URL;
import java.util.List;

@Path("/books") //localhost:8080/books
// genera tipo JSON
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class BookRest {

    @Inject
    BooksRepository repository;

    @Inject
    @RestClient
    AuthorRestClient authorRestClient;

    @GET
    public List<BookDto> findAll() {
        System.out.println("findAll");
        System.out.println("**************************");
        var books = repository.listAll();

        // TRY / CATCH
//        RestClientBuilder.newBuilder()
//                .baseUrl(new URL("http://localhost:9090"))
////                .readTimeout()
////                .proxyAddress()
//                .build(AuthorRestClient.class);

//        repository.streamAll()

        return books.stream()
                .map(book -> {
            //buscar el autor obj.getAuthorId();
            //conectarse al servicio 'app-authors'
            // http://localhost:9090/authors/{id}
            var author = authorRestClient.findById(book.getAuthorId());

            BookDto dto = new BookDto();

            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setIsbn(book.getIsbn());
            dto.setPrice(book.getPrice());
            dto.setAuthor(book.getAuthor());

            dto.setAuthorName(author.getFirstName());
//            dto.setAuthorName(...);
            return dto;
        }).toList();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        System.out.println("findById");

        var fd = repository.findByIdOptional(id);

        if (fd.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build(); //404
        }
        return Response.ok(fd.get()).build(); //code 200
    }

    @POST
    public Response create(Book book) {
        book.setId(null);
        repository.persist(book);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Book book) {
        var obj = repository.findById(id);

        obj.setIsbn(book.getIsbn());
        obj.setAuthor(book.getAuthor());
        obj.setTitle(book.getTitle());
        obj.setPrice(book.getPrice());

        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        repository.deleteById(id);
        return Response.ok().build();
    }
}
