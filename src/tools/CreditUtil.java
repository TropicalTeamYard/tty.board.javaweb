package tools;

import java.util.UUID;

public class CreditUtil {

	public static String GetToken(String userid, long validtime) {
		
		return GetGUID();
	}
	
	public static String GetGUID()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
