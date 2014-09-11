package meridian.lib.util;

import java.util.UUID;

public class UUIDUtil {
	public static String getNewUUID() {
		UUID uuid = UUID.randomUUID();
		String s = uuid.toString();
		s = s.toLowerCase().replace("-", "");
		return s;
	}
}
