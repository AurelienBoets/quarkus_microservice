package org.acme.service.grpc;


import org.acme.dto.payment.CreatePaymentDto;
import org.acme.dto.payment.PaymentResponseDto;
import org.acme.utils.VerifyLogin;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import payment.PaymentGrpc;
import payment.PaymentIntent;

@Path("api/payment")
public class PaymentGrpcService {
    @GrpcClient
    PaymentGrpc paymentGrpc;

    private final VerifyLogin verifyLogin;

    @Inject public PaymentGrpcService(VerifyLogin verifyLogin){
        this.verifyLogin=verifyLogin;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createPaymentIntent(@Context HttpHeaders httpHeaders,CreatePaymentDto dto){
        return verifyLogin.isLogin(httpHeaders)
            .onItem().transformToUni(userId -> {
                if (userId == null) {
                    return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                }
                return paymentGrpc.createPaymentIntent(PaymentIntent.newBuilder().setAmount(dto.getAmount()).build())
                    .onItem().transform(p -> Response.ok(new PaymentResponseDto(p.getKey())).build());
            });
    }
}
