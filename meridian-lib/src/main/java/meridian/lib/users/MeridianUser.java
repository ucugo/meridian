package meridian.lib.users;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import meridian.lib.ModelObject;

@Entity
@Table(name = "meridian_users")
public class MeridianUser extends ModelObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -839799760599537172L;
	public static final String RequestScopeAttributeName="pollen8user";
	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private MeridianUserRole accountType;
	
	@NotNull
	private String name;
	@NotNull
	private String surname;
	private String skypeName;
	@NotNull
	private String emailAddress;
	@NotNull
	private String contactNumberCellphone;
	private String contactNumberLandline;
	@NotNull
	private String country;
	
	@OneToMany(mappedBy="pollen8User",fetch=FetchType.EAGER)
	private Set<UserSetting> userSettings;

	@NotNull
	private Date lastActivityAt = new Date();
	private boolean suspended;
	@Transient
	private boolean cloaked = false;
	public MeridianUser() {
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public MeridianUserRole getAccountType() {
		return accountType;
	}
	public void setAccountType(MeridianUserRole accountType) {
		this.accountType = accountType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSkypeName() {
		return skypeName;
	}
	public void setSkypeName(String skypeName) {
		this.skypeName = skypeName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactNumberCellphone() {
		return contactNumberCellphone;
	}
	public void setContactNumberCellphone(String contactNumberCellphone) {
		this.contactNumberCellphone = contactNumberCellphone;
	}
	public String getContactNumberLandline() {
		return contactNumberLandline;
	}
	public void setContactNumberLandline(String contactNumberLandline) {
		this.contactNumberLandline = contactNumberLandline;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Date getLastActivityAt() {
		return lastActivityAt;
	}
	public void setLastActivityAt(Date lastActivityAt) {
		this.lastActivityAt = lastActivityAt;
	}
	public boolean isCloaked(){
		return this.cloaked;
	}
	public void setCloaked(boolean cloaked) {
		this.cloaked = cloaked;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	public Set<UserSetting> getUserSettings() {
		return userSettings;
	}
	public void setUserSetting(Set<UserSetting> userSettings) {
		this.userSettings = userSettings;
	}
//========================================================
	public boolean isAdmin(){
		return MeridianUserRole.Admin.equals(this.accountType);
	}
	public boolean isEmployer(){
		return MeridianUserRole.Employer.equals(this.accountType);
	}
	public boolean isJobSeeker(){
		return MeridianUserRole.JobSeeker.equals(this.accountType);
	}
	public String getAccountTypeAsString(){
		return getAccountType().name();
	}
	public void setAccountTypeAsString(String name){
		setAccountType(MeridianUserRole.valueOf(name));
	}
	public UserSetting findUserSettingByName(String name){
		Iterator<UserSetting> iterator = this.userSettings.iterator();
		while(iterator.hasNext()){
			UserSetting setting = iterator.next();
			if(StringUtils.equals(name, setting.getSettingName())){
				return setting;
			}
		}
		return null;
	}
}
