package com.ashwin.dropwizard.resource;

import com.ashwin.dropwizard.model.Employee;
import com.ashwin.dropwizard.service.MongodbService;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    private MongoCollection<Document> collection;
    private final MongodbService mongoService;
    private final ObjectMapper objectMapper;

    public EmployeeResource(MongoCollection<Document> collection, MongodbService mongoService) {
        this.collection = collection;
        this.mongoService = mongoService;
        this.objectMapper = new ObjectMapper();
    }

    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmployee(@NotNull @Valid final Employee employee) {
        try {
            String json = objectMapper.writeValueAsString(employee);
            mongoService.insertOne(collection, new Document(BasicDBObject.parse(json)));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Employee created successfully");
            return Response.ok(response).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Timed
    @Path("/createEmployees")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmployees(@NotNull final List<Employee> employees) {
        try {
            List<Document> employeeDocuments = new ArrayList<>();
            String json;
            for (Employee employee : employees) {
                json = objectMapper.writeValueAsString(employee);
                employeeDocuments.add(new Document(BasicDBObject.parse(json)));
            }
            mongoService.insertMany(collection, employeeDocuments);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Employees created successfully");
            return Response.ok(response).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Timed
    public Response getEmployees() {
        List<Document> documents = mongoService.find(collection);
        return Response.ok(documents).build();
    }

    @GET
    @Timed
    @Path("{name}")
    public Response getEmployee(@PathParam("name") final String name) {
        List<Document> documents = mongoService.findByKey(collection, "name", name);
        return Response.ok(documents).build();
    }

    @GET
    @Timed
    @Path("/salary/sort")
    public Response getEmployee() {
        List<Document> documents = mongoService.findByCriteria(collection, "salary", 25000, 1000, 1);
        return Response.ok(documents).build();
    }

    @PUT
    @Timed
    public Response editEmployee(@NotNull @Valid final Employee employee) {
        mongoService.updateOneEmployee(collection, "name", "department", "salary", employee);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee with Name: " + employee.getName() + " updated successfully");
        return Response.ok(response).build();
    }

    @DELETE
    @Timed
    @Path("{name}")
    public Response deleteEmployee(@PathParam("name") final String name) {
        mongoService.deleteOne(collection, "name", name);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee with Name: " + name + " deleted successfully");
        return Response.ok(response).build();
    }
}
