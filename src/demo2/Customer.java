package demo2;

import java.util.Date;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;

@Entity
public class Customer extends Model<Customer> {
	@Id
	@Column(type = "CHAR(12)")
	private String cid;
	
	@Column(type = "VARCHAR(50)")
	private String name;
	
	@Column(name = "bdate")
	private Date birthDate;
	
	@Column(type = "VARCHAR(50)")
	private String address;
	
	@Column(type = "VARCHAR(50)")
	private String city;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
