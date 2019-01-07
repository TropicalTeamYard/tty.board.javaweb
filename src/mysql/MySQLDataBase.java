package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import tools.StringCollector;
import tools.TimeUtil;


public class MySQLDataBase {

	public static String Login(String userid, String password){
		Map<String,Object> data=new HashMap<String,Object>();
		
		System.out.println(TimeUtil.getTime());
		data=DatabaseOperate(userid, password);
		
		JSONObject jsonObj=JSONObject.fromObject(data);
		System.out.println(jsonObj.toString());
		return jsonObj.toString();
	}
	
	
	public static Map<String,Object> DatabaseOperate(String userid, String password) {
		System.out.println("-----Init-Login-----");

		Map<String,Object> data=new HashMap<String,Object>();
		boolean isExist = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("-----Connecting-----");
			Connection conn = DriverManager.getConnection(StringCollector.MySQLConnStr, StringCollector.SQLAccout,
					StringCollector.SQLPassword);

			if (!conn.isClosed()) {
				System.out.println("-----Connect Succeed-----");

				System.out.println("-----Returning Search-----");
				PreparedStatement pStatement=conn.prepareStatement("select * from user where userid=? AND password=?");
				pStatement.setString(1, userid);
				pStatement.setString(2, password);
				ResultSet rs = pStatement.executeQuery();
				String nickname,email;
				
				while (rs.next()) {
					nickname=rs.getString("nickname");email=rs.getString("email");
					System.out.println(rs.getString("userid")+" "+rs.getString("nickname")+" "+rs.getString("password")+" "+rs.getString("email"));
					data.put("code", 0);
					data.put("msg", "login succeed");
					data.put("userid",userid);
					data.put("nickname",nickname);
					data.put("priority", rs.getInt("priority"));
					data.put("email", rs.getString("email"));
					data.put("portrait", rs.getInt("portrait"));
					isExist=true;
				}
				
				
				if(!rs.isClosed()) rs.close();
				if(!pStatement.isClosed()) pStatement.close();
				if(!conn.isClosed()) conn.close();
				
				if(!isExist) {
					data.put("code", -101);
					data.put("msg", "login failed, userid and password not match");
				}
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	
}
