package si.fri.rso.teamlj.payments.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "payments")
@NamedQueries(value =
        {
                @NamedQuery(name = "Payment.getAll", query = "SELECT p FROM payments p")
        })
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	private Instant dateOfPayment;
	
	private Instant endOfSubscription;
	
	private boolean subscription;
	
	private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDateOfPayment(Instant dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }
    
    public Instant getDateOfPayment() {
        return dateOfPayment;
    }

    public void setEndOfSubscription(Instant endOfSubscription) {
        this.endOfSubscription = endOfSubscription;
    }

	public Instant getEndOfSubscription() {
        return endOfSubscription;
    }
    
    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }
    
    public boolean getSubscription() {
        return subscription;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getUserId() {
        return userId;
    }
}
