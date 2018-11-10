package si.fri.rso.teamlj.payments.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import si.fri.rso.teamlj.payments.entities.Payment;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class PaymentsBean {

    private Logger log = Logger.getLogger(PaymentsBean.class.getName());

    @Inject
    private EntityManager em;


    public List<Payment> getPayments(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Payment.class, queryParameters);

    }

    public Payment getPayment(Integer paymentId) {

        Payment payment = em.find(Payment.class, paymentId);

        if (payment == null) {
            throw new NotFoundException();
        }

        return payment;
    }

    public Payment createPayment(Payment payment) {

        try {
            beginTx();
            em.persist(payment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return payment;
    }

    public Payment putPayment(Integer paymentId, Payment payment) {

        Payment b = em.find(Payment.class, paymentId);

        if (b == null) {
            return null;
        }

        try {
            beginTx();
            b.setId(b.getId());
            b = em.merge(payment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return b;
    }
	
    public boolean deletePayment(Integer paymentId) {

        Payment payment = em.find(Payment.class, paymentId);

        if (payment != null) {
            try {
                beginTx();
                em.remove(payment);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
