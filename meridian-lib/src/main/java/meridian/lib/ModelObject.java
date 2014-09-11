package meridian.lib;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import meridian.lib.audit.Audit;
import meridian.lib.audit.AuditedChange;
import meridian.lib.util.UUIDUtil;

@MappedSuperclass
public class ModelObject implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7496393088221765453L;

	public static final Comparator<ModelObject> ID_DESC_ORDER = new Comparator<ModelObject>() {
		
		@Override
		public int compare(ModelObject o1, ModelObject o2) {
			Integer id1 = o1.getId();
			Integer id2 = o2.getId();
			if (id1 == null)
				return -1;
			if (id2 == null)
				return 1;
			return id2 - id1;
		}
	};
	
	@Id
	@GeneratedValue
	private Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	@Basic(optional = false)
	@Column(updatable = false)
	private String uuid;
	
	public ModelObject() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
		this.uuid = UUIDUtil.getNewUUID();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isNew() {
		return id == null;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public ModelObject clone() {
		ModelObject c;
		try {
			c = (ModelObject) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		c.id = null;
		c.uuid = UUIDUtil.getNewUUID();
		return c;
	}
	protected <T extends ModelObject> T clone(T t) {
		t.id = this.id;
		t.uuid = this.uuid;
		t.createdAt = this.createdAt;
		t.updatedAt = this.updatedAt;
		return t;
	}
	@Override
	public String toString() {
		ToStringBuilder tss = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		addToStringBuilder(tss);
		return tss.toString();
	}
	
	protected void addToStringBuilder(ToStringBuilder tss) {
		tss.append("id", id).append("createdAt", createdAt).append("uuid", uuid);
	}
	
	protected void auditPropertyChange(String property, Object oldValue, Object newValue) {
		if (ObjectUtils.equals(oldValue, newValue)) {
			return;
		}
		AuditedChange change = createAuditedChange();
		if (change == null) {
			return;
		}
		change.setOperation("property change");
		change.setOldValue(String.valueOf(oldValue));
		change.setNewValue(String.valueOf(newValue));
		change.setPropertyName(property);
		change.setClassName(getClass().getName());
	}
	
	protected void auditDeleted() {
		AuditedChange change = createAuditedChange();
		change.setOperation("delete");
		change.setClassName(getClass().getName());
	}
	
	protected AuditedChange createAuditedChange() {
		return Audit.getAuditChangeContext().createAuditedChange();
	}
	
	protected void setUUID(String uuid) {
		this.uuid=uuid;
	}
}