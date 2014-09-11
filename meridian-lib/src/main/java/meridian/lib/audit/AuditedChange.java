package meridian.lib.audit;

import java.util.Date;

import javax.persistence.*;


@Entity
public class AuditedChange {
	@Id
	@GeneratedValue
	private Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectedAt = new Date();
	
	private String propertyName;
	private String operation;
	@Lob
	private String oldValue;
	@Lob
	private String newValue;
	private String adminUser;
	private String remoteAddress;
	@Lob
	private String notes;
	
	private String className;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getEffectedAt() {
		return effectedAt;
	}

	public void setEffectedAt(Date effectedAt) {
		this.effectedAt = effectedAt;
	}

	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	

	public void appendNotes(String string) {
		if (notes == null) {
			notes = "";
		}
		notes += string + "\n";
		
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
