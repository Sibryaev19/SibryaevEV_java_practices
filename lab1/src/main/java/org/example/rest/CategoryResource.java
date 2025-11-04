package org.example.resources;

import org.example.entity.Category;
import org.example.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    // GET /categories - все категории
    @GET
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // GET /categories/{id} - категория по ID
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            if (category == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Категория с ID " + id + " не найдена")
                        .build();
            }
            return Response.ok(category).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ошибка: " + e.getMessage())
                    .build();
        }
    }

    // POST /categories - создать категорию
    @POST
    public Response createCategory(Category category) {
        try {
            Category created = categoryService.createCategory(category);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка создания: " + e.getMessage())
                    .build();
        }
    }

    // PUT /categories/{id} - обновить категорию
    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category categoryDetails) {
        try {
            Category updated = categoryService.updateCategory(id, categoryDetails);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найдена")) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(e.getMessage())
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ошибка обновления: " + e.getMessage())
                    .build();
        }
    }

    // DELETE /categories/{id} - удалить категорию
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найдена")) {
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