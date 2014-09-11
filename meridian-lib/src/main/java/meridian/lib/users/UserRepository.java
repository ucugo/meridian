package meridian.lib.users;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import meridian.lib.AbstractRepository;

public class UserRepository extends AbstractRepository {
	public MeridianUser findPollen8UserByUsername(String username) {
		List<MeridianUser> users = em.createQuery("FROM MeridianUser WHERE username = :username", MeridianUser.class).setParameter("username", username).getResultList();
		if(users.isEmpty()){
			return null;
		}
		return users.get(0);
	}
	public MeridianUser findPollen8UserById(int id) {
		return em.createQuery("FROM MeridianUser WHERE id = :id", MeridianUser.class).setParameter("id", id).getSingleResult();
	}
	public List<MeridianUser> getPollen8Users() {
		return em.createQuery("FROM MeridianUser", MeridianUser.class).getResultList();
	}
	public List<MeridianUser> findEmployerUsers() {
		return em.createQuery("FROM MeridianUser WHERE accountType = 'Employer'", MeridianUser.class).getResultList();
	}
	public MeridianUserLoginHistory findLastLoginForUser(MeridianUser meridianUser) {
		try {
			return em.createQuery("FROM MeridianUserLoginHistory WHERE meridianUser=:meridianUser ORDER BY id DESC", MeridianUserLoginHistory.class).setParameter("meridianUser", meridianUser)
					.setMaxResults(1)
					.setFirstResult(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public void updateLastActivityTime(int pollen8UserId){
		Query query = em.createNativeQuery("UPDATE pollen8_users SET last_activity_at = :now WHERE id = :id");
		query.setParameter("id",pollen8UserId);
		query.setParameter("now", new Date());
		query.executeUpdate();
	}

	/*//===================================================================
	public UserGlobal createUser(UserGlobal userGlobal) {
		this.em.persist(userGlobal);
		return userGlobal;
	}
	public UserPerSite createUser(UserPerSite userPerSite) {
		this.em.persist(userPerSite);
		return userPerSite;
	}
	public UserGlobal findUserGlobalByUUID(String userGlobalUUID) {
		try {
			return this.em.createQuery("FROM UserGlobal WHERE uuid=:uuid", UserGlobal.class).setParameter("uuid", userGlobalUUID).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public UserPerSite findUserPerSiteByUUID(String userPerSiteUUID) {
		try {
			return this.em.createQuery("FROM UserPerSite WHERE uuid=:uuid", UserPerSite.class).setParameter("uuid", userPerSiteUUID).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public void updateUser(UserPerSite userPerSite) {
		if(userPerSite.isNew()){
			createUser(userPerSite);
			return;
		}
		this.em.merge(userPerSite);
		this.em.persist(userPerSite);
	}
	public void logUserPerSiteChange(UserPerSiteChangeLog changeLog) {
		this.em.persist(changeLog);
	}
	public UserPerSite getUserPerSite(int userId) {
		return this.em.find(UserPerSite.class, userId);
	}
	public List<UserPerSite> findUsersPerSiteByRefTypeAndRefValue(int siteId, String siteReferenceType, String siteReferenceValue, int maxCount) {
		try{
			if(StringUtils.isBlank(siteReferenceType)){
				return em.createQuery("FROM UserPerSite WHERE site.id = :siteId AND siteReferenceType IS NULL AND siteReferenceValue = :siteReferenceValue", UserPerSite.class)
						.setMaxResults(maxCount)
						.setParameter("siteId", siteId)
						.setParameter("siteReferenceValue", siteReferenceValue)
						.getResultList();
			}else{
				return em.createQuery("FROM UserPerSite WHERE site.id = :siteId AND siteReferenceType = :siteReferenceType AND siteReferenceValue = :siteReferenceValue", UserPerSite.class)
						.setMaxResults(maxCount)
						.setParameter("siteId", siteId)
						.setParameter("siteReferenceType", siteReferenceType)
						.setParameter("siteReferenceValue", siteReferenceValue)
						.getResultList();				
			}
		}catch(NoResultException resultException){
			return null;
		}
	}
	public void incrementUserStats(UserPerSite userPerSite, UserPerSite incData) {
		incrementUserStats("UserPerSite", userPerSite.getId(), incData);
	}
	public void incrementUserStats(UserGlobal userGlobal, UserPerSite incData) {
		incrementUserStats("UserGlobal", userGlobal.getId(), incData);
	}
	
	private void incrementUserStats(String table, int id, UserPerSite data) {
		String seperator = "";
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" UPDATE ").append(table).append(" ");
		queryBuilder.append(" SET ");
		
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, false, "lastRequestAt", data.getLastRequestAt() != null);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, false, "lastImpressionAt", data.getLastImpressionAt() != null);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, false, "lastClickAt", data.getLastClickAt() != null);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, false, "lastAcquisitionAt", data.getLastAcquisitionAt() != null);

		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, true, "requestCount", data.getRequestCount() > 0);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, true, "impressionCount", data.getImpressionCount() > 0);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, true, "clickCount", data.getClickCount() > 0);
		seperator = incrementUserStatsBuilderHelperAddToSet(queryBuilder, seperator, true, "acquisitionCount", data.getAcquisitionCount() > 0);

		queryBuilder.append(" WHERE id = :id");
		Query query = this.em.createQuery(queryBuilder.toString());
		
		incrementUserStatsBuilderHelperAddParam(query, "id", id, true);
		
		incrementUserStatsBuilderHelperAddParam(query, "lastRequestAt", data.getLastRequestAt(), data.getLastRequestAt() != null);
		incrementUserStatsBuilderHelperAddParam(query, "lastImpressionAt", data.getLastImpressionAt(),data.getLastImpressionAt() != null);
		incrementUserStatsBuilderHelperAddParam(query, "lastClickAt", data.getLastClickAt(),data.getLastClickAt() != null);
		incrementUserStatsBuilderHelperAddParam(query, "lastAcquisitionAt", data.getLastAcquisitionAt(),data.getLastAcquisitionAt() != null);

		incrementUserStatsBuilderHelperAddParam(query, "requestCount", data.getRequestCount(),data.getRequestCount() > 0);
		incrementUserStatsBuilderHelperAddParam(query, "impressionCount", data.getImpressionCount(),data.getImpressionCount() > 0);
		incrementUserStatsBuilderHelperAddParam(query, "clickCount", data.getClickCount(),data.getClickCount() > 0);
		incrementUserStatsBuilderHelperAddParam(query, "acquisitionCount", data.getAcquisitionCount(),data.getAcquisitionCount() > 0);
		
		query.executeUpdate();
	}
	private String incrementUserStatsBuilderHelperAddToSet(StringBuilder builder, String seperator, boolean accumulate, String name, boolean add){
		if(!add){
			return seperator; 
		}
		builder.append(seperator).append(" ").append(name).append(" = ");
		if(accumulate){
			builder.append(" ").append(name).append("+ ");
		}
		builder.append(" :").append(name).append(" ");
		return ",";
	}
	private void incrementUserStatsBuilderHelperAddParam(Query query, String paramName, Object value, boolean add){
		if(!add){
			return;
		}
		query.setParameter(paramName, value);
	}
	public void deleteTrackingTargetCap(UserPerSiteFrequencyCap removeCap) {
		this.em.remove(removeCap);
	}*/
	
	
	
}
