package meridian.lib.users;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import meridian.lib.ModelObject;

@Table
@Entity
public class UserSetting extends ModelObject{
	private static final long serialVersionUID = -2798228854892895724L;
	@ManyToOne(fetch=FetchType.EAGER)
	private MeridianUser pollen8User;
	private String settingName;
	@Type(type="text")
	private String settingValue;
	
	public MeridianUser getPollen8User() {
		return pollen8User;
	}
	public void setPollen8User(MeridianUser pollen8User) {
		this.pollen8User = pollen8User;
	}
	public String getSettingName() {
		return settingName;
	}
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	public static String findSettingByName(Set<UserSetting> settings, String name) {
		for(UserSetting setting : settings){
			if(StringUtils.equalsIgnoreCase(setting.getSettingName(), name)){
				return setting.getSettingValue();
			}
		}
		return null;
	}
}
