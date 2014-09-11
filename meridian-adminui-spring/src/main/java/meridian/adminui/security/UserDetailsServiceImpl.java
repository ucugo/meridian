package meridian.adminui.security;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import meridian.adminui.security.users.CloakableUser;
import meridian.lib.users.MeridianUser;
import meridian.lib.users.UserRepository;

@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_INTERNAL = "ROLE_EMPLOYER";
	public static final String ROLE_EXTERNAL = "ROLE_JOBSEEKER";

	@Autowired private UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		username = StringUtils.trim(username);
		MeridianUser userEntity = userRepository.findPollen8UserByUsername(username);
		return new CloakableUser(loadUser(userEntity));
	}
	@Transactional(readOnly = true)
	public MeridianUser loadUserByUsername(String username, String password) throws UsernameNotFoundException, DataAccessException {
		username = StringUtils.trim(username);
		password = StringUtils.trim(password);
		MeridianUser userEntity = userRepository.findPollen8UserByUsername(username);
		if(userEntity != null && !userEntity.isSuspended() && userEntity.getPassword().equals(UserDetailsServiceImpl.getSha256dPassword(password))){
			return userEntity;			
		}
		return null;
	}
	public static final String getSha256dPassword(String password){
		return new ShaPasswordEncoder(256).encodePassword(password, null);
	}
	private User loadUser(MeridianUser userEntity){
		if (userEntity == null){
			throw new UsernameNotFoundException("user not found");
		}
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(userEntity.isAdmin()){
			authorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
		}else if(userEntity.isEmployer()){
			authorities.add(new SimpleGrantedAuthority(ROLE_INTERNAL));
		}else if(userEntity.isJobSeeker()){
			authorities.add(new SimpleGrantedAuthority(ROLE_EXTERNAL));
		}
		logger.info("UserId:[{}] Username:[{}] authorities:[{}]",new Object[]{userEntity.getId(),userEntity.getUsername(),authorities});
		return new User(userEntity.getUsername(), userEntity.getPassword(), true,true, true, !userEntity.isSuspended(), authorities);
	}
	
	public boolean isUserClockedToAppearAsAnotherUser(){
		CloakableUser cloakableUser = getLogedInUser();
		return cloakableUser != null && cloakableUser.isCloaked();
	}
	public void cloakUser(long userId) throws SecurityException{
		CloakableUser cloakableUser = getLogedInUser();
		if(CloakableUser.isUserInGrantedAuthorityGroup(cloakableUser.getUser(), ROLE_ADMIN)){
			MeridianUser userEntity = userRepository.findPollen8UserById(((Long)userId).intValue());
			User user = loadUser(userEntity);
			if(!CloakableUser.isUserInGrantedAuthorityGroup(user, ROLE_ADMIN)){
				cloakableUser.setCloakingUser(user);
				return;
			}
		}
		throw new SecurityException("Access Denied");
	}
	public void unCloakUser(){
		CloakableUser cloakableUser = getLogedInUser();
		cloakableUser.unCloakUser();		
	}
	public CloakableUser getLogedInUser(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Object object = auth.getPrincipal();
		if(object == null || (object instanceof String && StringUtils.equalsIgnoreCase("anonymousUser", (String)object)) ){
			return null;
		}
		if(object instanceof CloakableUser){
			return (CloakableUser) object;
		}
		if(object instanceof User){
			return null;
		}
		throw new IllegalArgumentException("Operation cannot be performed [" + ToStringBuilder.reflectionToString(object) + "]");
	}
}