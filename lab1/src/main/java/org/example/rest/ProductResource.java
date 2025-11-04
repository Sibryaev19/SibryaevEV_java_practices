package org.example.resources;

import org.example.entity.Product;
import org.example.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    private ProductService productService;

    // GET /products - все продукты
    @GET
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // GET /products/{id} - продукт по ID
    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Продукт с ID " + id + " не найден")
                        .build();
            }
            return Response.ok(product).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка: " + e.getMessage())
                    .build();
        }
    }

    // GET /products/category/{categoryId} - продукты по категории
    @GET
    @Path("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathParam("categoryId") Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    // POST /products - создать продукт
    @POST
    public Response createProduct(Product product) {
        try {
            Product created = productService.createProduct(product);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка создания: " + e.getMessage())
                    .build();
        }
    }

    // PUT /products/{id} - обновить продукт
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product productDetails) {
        try {
            Product updated = productService.updateProduct(id, productDetails);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найден")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(e.getMessage())
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка обновления: " + e.getMessage())
                    .build();
        }
    }

    // DELETE /products/{id} - удалить продукт
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найден")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(e.getMessage())
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка удаления: " + e.getMessage())
                    .build();
        }
    }
}