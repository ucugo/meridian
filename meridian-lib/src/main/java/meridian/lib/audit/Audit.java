package meridian.lib.audit;

public class Audit {
	
	private static AuditChangeContext auditChangeContext;
	
	public static AuditChangeContext getAuditChangeContext() {
		if (auditChangeContext == null) {
			return new WebRequestAuditChangeContext();
		} else {
			return auditChangeContext;
		}
	}
	
	public static void setAuditChangeContext(
			AuditChangeContext auditChangeContext) {
		Audit.auditChangeContext = auditChangeContext;
	}
}
