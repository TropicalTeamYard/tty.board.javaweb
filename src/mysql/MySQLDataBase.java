package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Blob;

import net.sf.json.JSONObject;
import tools.CreditUtil;
import tools.StringCollector;
import tools.TimeUtil;


public class MySQLDataBase {

	public static String Login(String userid, String password){
		Map<String,Object> data=new HashMap<String,Object>();
		
		System.out.println(TimeUtil.getTime());
		data=LoginCheck(userid, password);
		
		JSONObject jsonObj=JSONObject.fromObject(data);
		System.out.println(jsonObj.toString());
		return jsonObj.toString();
	}
	
	public static String Token(String userid, String token){
		Map<String,Object> data=new HashMap<String,Object>();
		
		System.out.println(TimeUtil.getTime());
		data=TokenCheck(userid, token);
		
		JSONObject jsonObj=JSONObject.fromObject(data);
		System.out.println(jsonObj.toString());
		return jsonObj.toString();
	}
	
	public static String Register(String nickname, String password){
		Map<String,Object> data=new HashMap<String,Object>();
		
		System.out.println(TimeUtil.getTime());
		data=RegiserOperator(nickname, password);
		
		JSONObject jsonObj=JSONObject.fromObject(data);
		System.out.println(jsonObj.toString());
		return jsonObj.toString();
	}
	
	public static String ChangeInfo(String userid, String token, String nickname, String email, byte[] portrait){
		Map<String,Object> data=new HashMap<String,Object>();
		String newToken=CreditUtil.GetToken(userid, 0);
		boolean isExist = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("-----Connecting-----");
			Connection conn = DriverManager.getConnection(StringCollector.MySQLConnStr, StringCollector.SQLAccout,
					StringCollector.SQLPassword);

			if (!conn.isClosed()) {
				System.out.println("-----Connect Succeed-----");

				System.out.println("-----Returning-----");
				PreparedStatement pStatement=conn.prepareStatement("select * from user where userid=? AND token=?");
				pStatement.setString(1, userid);
				pStatement.setString(2, token);
				ResultSet rs = pStatement.executeQuery();
				
				while (rs.next()) {
					if(nickname==null) {nickname=rs.getString("nickname");}
					if(email==null) {email=rs.getString("email");}
					
					// TODO 解决Bug
					if(portrait==null&&rs.getBlob("portrait").length()>0) {
						java.sql.Blob blob = conn.createBlob();
						blob=rs.getBlob("portrait");
						portrait=blob.getBytes(1, (int) blob.length());
					}
					System.out.println(rs.getString("userid")+" "+rs.getString("nickname")+" "+rs.getString("password")+" "+rs.getString("email"));
					
					isExist=true;
				}
				
				if(isExist) {
					
					pStatement=conn.prepareStatement("UPDATE user SET nickname=?,email=?,portrait=?,token=? WHERE userid=?");
					pStatement.setString(1, nickname);
					pStatement.setString(2, email);
					
					java.sql.Blob blob = conn.createBlob();
					if(portrait!=null) {
						blob.setBytes(1, portrait);
					}
					
					pStatement.setBlob(3, blob);
					pStatement.setString(4, newToken);
					pStatement.setString(5, userid);
					pStatement.executeUpdate();
					data.put("code", 0);
					data.put("msg", "update succeed");
					data.put("nickname", nickname);
					data.put("email", email);
					data.put("portrait", portrait);
					data.put("token", newToken);
				}
				
				if(!rs.isClosed()) rs.close();
				if(!pStatement.isClosed()) pStatement.close();
				if(!conn.isClosed()) conn.close();
				
				if(!isExist) {
					data.put("code", -104);
					data.put("msg", "update failed, userid and token not match");
				}
			}
			
			
		} catch (ClassNotFoundException e) {
			data.put("code", -104);
			data.put("msg", "sever error 104");
			e.printStackTrace();
		} catch (SQLException e) {
			data.put("code", -104);
			data.put("msg", "sever error 104");
			e.printStackTrace();
		}
		
		
		JSONObject temp=JSONObject.fromObject(data);
		
		System.out.println(temp.toString());
		
		return temp.toString();
	} 
	
	public static Map<String,Object> LoginCheck(String userid, String password) {
		System.out.println("-----Init-Login-----");
		
		String newToken=CreditUtil.GetToken(userid, 0);

		Map<String,Object> data=new HashMap<String,Object>();
		boolean isExist = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("-----Connecting-----");
			Connection conn = DriverManager.getConnection(StringCollector.MySQLConnStr, StringCollector.SQLAccout,
					StringCollector.SQLPassword);

			if (!conn.isClosed()) {
				System.out.println("-----Connect Succeed-----");

				System.out.println("-----Returning-----");
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
					data.put("email", email);
					data.put("portrait", rs.getBlob("portrait"));
					// TODO token
					data.put("token", newToken);
					
					isExist=true;
				}
				
				if(isExist) {
					
					pStatement=conn.prepareStatement("UPDATE user SET token=? WHERE userid=?");
					pStatement.setString(1, newToken);
					pStatement.setString(2, userid);
					pStatement.executeUpdate();
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
			data.put("code", -101);
			data.put("msg", "sever error 101");
			e.printStackTrace();
		} catch (SQLException e) {
			data.put("code", -101);
			data.put("msg", "sever error 101");
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static Map<String,Object> TokenCheck(String userid, String token) {
		System.out.println("-----Check-Login-----");
		
		String newToken=CreditUtil.GetToken(userid, 0);

		Map<String,Object> data=new HashMap<String,Object>();
		boolean isExist = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("-----Connecting-----");
			Connection conn = DriverManager.getConnection(StringCollector.MySQLConnStr, StringCollector.SQLAccout,
					StringCollector.SQLPassword);

			if (!conn.isClosed()) {
				System.out.println("-----Connect Succeed-----");

				System.out.println("-----Returning-----");
				PreparedStatement pStatement=conn.prepareStatement("select * from user where userid=? AND token=?");
				pStatement.setString(1, userid);
				pStatement.setString(2, token);
				ResultSet rs = pStatement.executeQuery();
				
				while (rs.next()) {
					System.out.println(rs.getString("userid")+" "+rs.getString("nickname")+" "+rs.getString("password")+" "+rs.getString("email"));
					data.put("code", 0);
					data.put("msg", "check succeed");
					
					// TODO token
					data.put("token", newToken);
					isExist=true;
				}
				
				if(isExist) {
					
					pStatement=conn.prepareStatement("UPDATE user SET token=? WHERE userid=?");
					pStatement.setString(1, newToken);
					pStatement.setString(2, userid);
					pStatement.executeUpdate();
				}
				
				
				if(!rs.isClosed()) rs.close();
				if(!pStatement.isClosed()) pStatement.close();
				if(!conn.isClosed()) conn.close();
				
				if(!isExist) {
					data.put("code", -102);
					data.put("msg", "check failed, token invalid");
				}
				
			}
			
			
		} catch (ClassNotFoundException e) {
			data.put("code", -102);
			data.put("msg", "sever error 102");
			e.printStackTrace();
		} catch (SQLException e) {
			data.put("code", -102);
			data.put("msg", "sever error 102");
			e.printStackTrace();
		}
		
		return data;
	}

	public static Map<String,Object> RegiserOperator(String nickname, String password) {
		System.out.println("-----Check-Register-----");
		

		long newid = 0;
		Map<String,Object> data=new HashMap<String,Object>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("-----Connecting-----");
			Connection conn = DriverManager.getConnection(StringCollector.MySQLConnStr, StringCollector.SQLAccout,
					StringCollector.SQLPassword);

			if (!conn.isClosed()) {
				System.out.println("-----Connect Succeed-----");

				System.out.println("-----Returning-----");
				PreparedStatement pStatement=conn.prepareStatement("select * from user order by _id DESC limit 1");
				ResultSet rs = pStatement.executeQuery();
				
				while (rs.next()) {
					newid=Integer.parseInt(rs.getString("userid"))+1;
					
				}
				
				if(newid>10000) {
					boolean isInsertSucceed=false;
					pStatement=conn.prepareStatement("INSERT INTO user (userid,nickname,password,priority,registertime) VALUES (?,?,?,0,?)");
					pStatement.setString(1, String.valueOf(newid));
					pStatement.setString(2, nickname);
					pStatement.setString(3, password);
					pStatement.setString(4, TimeUtil.getTime());
					pStatement.execute();
					
					pStatement=conn.prepareStatement("select * from user where userid=?");
					pStatement.setString(1, String.valueOf(newid));
					rs = pStatement.executeQuery();
					
					while (rs.next()) {
						isInsertSucceed=true;
						
					}
					
					if (isInsertSucceed) {
						data.put("code", 0);
						data.put("msg", "register successful");
						data.put("userid", String.valueOf(newid));
						data.put("nickname", nickname);
					} else {
						data.put("code", -103);
						data.put("msg", "sever error 103");
					}
					
				} else {
					data.put("code", -103);
					data.put("msg", "sever error 103");
					return data;
				}
				
				
				
				
				if(!rs.isClosed()) rs.close();
				if(!pStatement.isClosed()) pStatement.close();
				if(!conn.isClosed()) conn.close();
				
				
			}
			
			
		} catch (ClassNotFoundException e) {
			data.put("code", -103);
			data.put("msg", "sever error 103");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			data.put("code", -103);
			data.put("msg", "sever error 103");
		}
		
		return data;
	}
 
	
	
}
