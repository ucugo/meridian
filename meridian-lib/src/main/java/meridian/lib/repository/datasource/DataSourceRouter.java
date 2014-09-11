package meridian.lib.repository.datasource;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource{
	private static final ThreadLocal<String> overrideDataSource = new ThreadLocal<String>();
	private Set<String> overrideDataSources = new HashSet<String>();
	
	@Override
	protected Object determineCurrentLookupKey() {
		String key = StringUtils.trimToEmpty(StringUtils.lowerCase(getOverrideDataSource()));
		if(StringUtils.isBlank(key)){
			return null;
		}
		if(this.overrideDataSources.contains(key)){
			return key;
		}
		throw new RuntimeException("Failed to route datasource [" + key + "]. Reason: Datasource doesn't exist");
	}
	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		this.overrideDataSources.clear();
		for(Entry<Object, Object> entry : targetDataSources.entrySet()){
			String key = StringUtils.trimToNull(StringUtils.lowerCase(entry.getKey().toString()));
			if(StringUtils.isNotBlank(key)){
				this.overrideDataSources.add(key);
			}
		}
	}
	
	public static void setOverrideDataSource(String override){
		DataSourceRouter.overrideDataSource.set(override);
	}
	public static String getOverrideDataSource(){
		return DataSourceRouter.overrideDataSource.get();
	}
	public static void clearOverrideDataSource(){
		DataSourceRouter.overrideDataSource.remove();
	}
}
