package meridian.lib.users;

public enum MeridianUserRole {

	Admin, Employer, JobSeeker;

	public static String[] getNames() {
		String[] out = new String[values().length];
		for(int i = 0 ; i < values().length ; i++ ){
			out[i] = values()[i].name();
		}
		return out;
	} 
}
