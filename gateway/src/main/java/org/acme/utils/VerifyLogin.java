package org.acme.utils;

import org.acme.dto.LoginResponseDto;
import org.acme.service.IdentityService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class VerifyLogin {
    @RestClient
    @Inject
    IdentityService identityService;

    public Uni<Boolean> isAdmin(HttpHeaders headers) {
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null) {
            return Uni.createFrom().item(() -> identityService.isAdmin(authorizationHeader))
                    .runSubscriptionOn(Infrastructure.getDefaultExecutor())
                    .onItem().transform(response -> response.getStatus() != Response.Status.UNAUTHORIZED.getStatusCode())
                    .onFailure().recoverWithItem(false);
        }
        return Uni.createFrom().item(false);
    }

    public Uni<Long> isLogin(HttpHeaders headers){
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null) {
            return Uni.createFrom().item(() -> identityService.isLogin(authorizationHeader))
                    .runSubscriptionOn(Infrastructure.getDefaultExecutor())
                    .onItem().transform(response -> {
                        if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                            return null;
                        }
                       LoginResponseDto loginResponse = response.readEntity(LoginResponseDto.class);
                        return loginResponse.getId();
                    })
                    .onFailure().recoverWithItem((long) 0);
        }
        return Uni.createFrom().item(null);
    }
}

