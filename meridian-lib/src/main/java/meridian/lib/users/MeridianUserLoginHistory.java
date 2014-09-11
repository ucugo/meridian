package meridian.lib.users;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import meridian.lib.ModelObject;

@Entity
@Table(name = "pollen8_user_login_history")
public class MeridianUserLoginHistory extends ModelObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4548167170004814487L;

	@ManyToOne 
	private MeridianUser meridianUser;

	private Date loginAt;
	private String ip;
	private String xForwardedFor;
	private String userAgent;
	
	public MeridianUser getMeridianUser() {
		return meridianUser;
	}
	public void setMeridianUser(MeridianUser meridianUser) {
		this.meridianUser = meridianUser;
	}
	public Date getLoginAt() {
		return loginAt;
	}
	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getxForwardedFor() {
		return xForwardedFor;
	}
	public void setxForwardedFor(String xForwardedFor) {
		this.xForwardedFor = xForwardedFor;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
