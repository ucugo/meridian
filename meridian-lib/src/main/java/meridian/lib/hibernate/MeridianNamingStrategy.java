package meridian.lib.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class MeridianNamingStrategy extends ImprovedNamingStrategy {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String foreignKeyColumnName(String propertyName,
			String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName) + "_" + referencedColumnName;
	}

	@Override
	public String classToTableName(String className) {
		String n = super.classToTableName(className);
		if (n.endsWith("s")) {
			return n + "es";
		}
		if (n.endsWith("y")) {
			return n.substring(0, n.length() - 1) + "ies";
		}
		return n + "s";
	}
}
