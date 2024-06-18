package org.acme.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;



@Path("/api/verify")
@RegisterRestClient
@ApplicationScoped
public interface IdentityService{
    @GET
    @Path("/login")
    Response isLogin(@HeaderParam("Authorization") String token);

    @GET
    @Path("/admin")
    Response isAdmin(@HeaderParam("Authorization") String token);
}