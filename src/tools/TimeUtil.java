package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getTime(){
		String time = null;
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		time=sdf.format(date);
		
		return time; 
	}
}
