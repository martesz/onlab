/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.network;

import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.martin.getfreaky.utils.JWTService;

/**
 *
 * @author martin
 *
 * Filters all requests coming to the REST end points annotated with @Secured If
 * any problems happen during the token validation, a response with status 401
 * UNAUTHORIZED will be returned. Otherwise, the request will proceed to an
 * endpoint.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @EJB
    JWTService jWTService;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        String authorizationHeader = crc.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        try {
            String userId = jWTService.validate(authorizationHeader);
        } catch (SignatureException e) {
            crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }

}
