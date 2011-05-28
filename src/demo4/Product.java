package demo4;

import java.util.Date;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Product extends Model<Product>{
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	@Column(type = "CHAR(64)")
	private String name;
	
	@Column(type = "CHAR(100)")
	private String description;
	
	private float price;
	
	private Date submitDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String val) {
		name = val;
	}
	
	public void setDescription(String desc) {
		description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float pri) {
		price = pri;
	}
	
	public Date getSubmitDate() {
		return submitDate;
	}
	
	public void setSubmitDate(Date addDate) {
		submitDate = addDate;
	}
	
}
