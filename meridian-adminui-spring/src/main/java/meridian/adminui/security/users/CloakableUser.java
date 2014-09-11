package meridian.adminui.security.users;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class CloakableUser implements UserDetails{
	private static final long serialVersionUID = -2058588043102211629L;
	private final User user;
	private User cloakingUser = null;

	public CloakableUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public User getCloakingUser(){
		return this.cloakingUser;
	}
	public void setCloakingUser(User user){
		this.cloakingUser = user;
	}
	public void unCloakUser(){
		setCloakingUser(null);
	}
	public boolean isCloaked(){
		return this.cloakingUser != null;
	}
	private final User getVisibleUser(){
		if(isCloaked()){
			return this.cloakingUser;
		}
		return this.user;
	}
	
	public static boolean isUserInGrantedAuthorityGroup(final User user, final String role){
		Iterator<GrantedAuthority> authorities = user.getAuthorities().iterator();
		while(authorities.hasNext()){
			GrantedAuthority authority = authorities.next();
			if(role.equals(authority.getAuthority())){
				return true;
			}
		}
		return false;
	}
	public String getVisibleUsername() {
		return getVisibleUser().getUsername();
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return getVisibleUser().getAuthorities();
	}
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}
	@Override
	public String getUsername() {
		return this.user.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return getVisibleUser().isAccountNonExpired();
	}
	@Override
	public boolean isAccountNonLocked() {
		return getVisibleUser().isAccountNonLocked();
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return getVisibleUser().isCredentialsNonExpired();
	}
	@Override
	public boolean isEnabled() {
		return getVisibleUser().isEnabled();
	}
}
