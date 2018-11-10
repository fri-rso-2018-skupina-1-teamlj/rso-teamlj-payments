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
import java.time.Month;
import java.time.temporal.ChronoUnit;
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

    public Payment pay(Integer userId) {

        Payment p = new Payment();
        p.setDateOfPayment(Instant.now());
        p.setEndOfSubscription(Instant.now().plus(30, ChronoUnit.DAYS));
        p.setSubscription(true);
        p.setUserId(userId);

        try {
            beginTx();
            em.persist(p);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return p;
    }


    public Payment putPayment(Integer paymentId, Payment payment) {

        Payment p = em.find(Payment.class, paymentId);

        if (p == null) {
            return null;
        }

        try {
            beginTx();
            p.setId(p.getId());
            p = em.merge(payment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return p;
    }

    public Payment subscribed(Integer userId) {

        Payment p = em.createQuery("SELECT p FROM payments p WHERE p.userId = ?1 AND p.subscription = ?2", Payment.class)
                .setParameter(1, userId)
                .setParameter(2, true)
                .getSingleResult();

        if(p.getEndOfSubscription().isBefore(Instant.now()))
        {
            p.setSubscription(false);
            p = putPayment(p.getId(), p);
        }
        return p;
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
