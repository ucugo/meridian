package meridian.lib.audit;

import org.springframework.web.context.request.ServletWebRequest;

import meridian.lib.web.CurrentRequestInterceptor;

public class WebRequestAuditChangeContext implements AuditChangeContext {

	public AuditedChange createAuditedChange() {
		AuditedChange change = new AuditedChange();
		ServletWebRequest request = (ServletWebRequest) CurrentRequestInterceptor.getCurrent();
		if(request == null){
			return null;
		}
		String user = request.getRemoteUser();
		if (user == null)
			user = "unknown";
		change.setAdminUser(user);
		String remoteAddr = request.getRequest().getRemoteAddr();
		if (remoteAddr == null)
			remoteAddr = "unknown";
		change.setRemoteAddress(remoteAddr);
		return change;
	}

}
