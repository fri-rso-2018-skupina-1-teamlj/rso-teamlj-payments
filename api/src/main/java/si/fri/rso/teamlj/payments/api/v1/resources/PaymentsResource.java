package si.fri.rso.teamlj.payments.api.v1.resources;

import si.fri.rso.teamlj.payments.entities.Payment;
import si.fri.rso.teamlj.payments.services.beans.PaymentsBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private PaymentsBean paymentsBean;

    @GET
    public Response getPayments() {

        List<Payment> payments = paymentsBean.getPayments(uriInfo);

        return Response.ok(payments).build();
    }

    @GET
    @Path("/{paymentId}")
    public Response getPayment(@PathParam("paymentId") Integer paymentId) {

        Payment payment = paymentsBean.getPayment(paymentId);

        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(payment).build();
    }

    @POST
    public Response createPayment(Payment payment) {

        payment = paymentsBean.createPayment(payment);

        if (payment.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(payment).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(payment).build();
        }
    }

    @PUT
    @Path("/{paymentId}")
    public Response putPayment(@PathParam("paymentId") Integer paymentId, Payment payment) {

        payment = paymentsBean.putPayment(paymentId, payment);

        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (payment.getId() != null)
                return Response.status(Response.Status.OK).entity(payment).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("/{paymentId}")
    public Response deletePayment(@PathParam("paymentId") Integer paymentId) {

        boolean deleted = paymentsBean.deletePayment(paymentId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
