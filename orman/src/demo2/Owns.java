package demo2;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Owns extends Model<Owns> {
	@OneToOne
	@Column(name = "cid")
	private Customer customer;
	
	@OneToOne
	@Column(name = "aid")
	private Account account;

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
	
	
}
