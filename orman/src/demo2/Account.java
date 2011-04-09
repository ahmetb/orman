package demo2;

import java.util.Date;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;

@Entity
public class Account extends Model<Account> {
	@Id
	@Column(type = "CHAR(8)")
	private String aid;
	
	@Column(type = "VARCHAR(50)")
	private String branch;
	
	private Float balance;
	
	private Date openDate;

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAid() {
		return aid;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getBalance() {
		return balance;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getOpenDate() {
		return openDate;
	}	
}
