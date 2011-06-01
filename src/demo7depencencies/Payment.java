package demo7depencencies;

import java.util.Date;

import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.sql.IndexType;

@Entity
public class Payment {
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	@Index(type=IndexType.BTREE)
	public float amount;
	
	public Date transactionDate;
}
