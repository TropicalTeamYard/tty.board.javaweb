package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import com.mysql.jdbc.Blob;

import mysql.MySQLUser;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("-----GET---UserServlet----");
		response.getWriter().write("Helloworld\n");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("-----POST---UserServlet----");
		String method=request.getParameter("method");
		
		String password, userid, nickname, token, email, portrait;
		
		if(method!=null) {
			System.out.println("method = "+method);
			switch (method) {

			case "login":
				userid=request.getParameter("userid");
				password=request.getParameter("password");
				if(userid==null||userid.equals("")||password==null||password.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or passowrd'}");return;}
				response.getWriter().write(MySQLUser.Login(userid, password));
				break;

			case "autologin":
				userid=request.getParameter("userid");
				token=request.getParameter("token");
				if(token==null||token.equals("")||userid==null||userid.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or token'}");return;}
				response.getWriter().write(MySQLUser.Token(userid, token));
				break;

			case "register":
				nickname=request.getParameter("nickname");
				password=request.getParameter("password");
				if(nickname==null||nickname.equals("")||password==null||password.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null nickname or passowrd'}");return;}
				System.out.println(nickname+" "+password);
				response.getWriter().write(MySQLUser.Register(nickname, password));
				break;

			case "changeinfo":
				userid=request.getParameter("userid");
				token=request.getParameter("token");
				nickname=request.getParameter("nickname");
				email=request.getParameter("email");
				portrait=request.getParameter("portrait");
				if(token==null||token.equals("")||userid==null||userid.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or token'}");return;}
				response.getWriter().write(MySQLUser.ChangeInfo(userid, token, nickname, email, portrait));
				break;

			case "getuserinfo":
				userid=request.getParameter("userid");
				token=request.getParameter("token");
				if(token==null||token.equals("")||userid==null||userid.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or token'}");return;}
				response.getWriter().write(MySQLUser.GetUserInfo(userid, token));
				break;

			case "getpublicinfo":
				String user=request.getParameter("userids");
				if(user==null||user.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userids'}");return;}
				String[] userids;
				JSONArray array=JSONArray.fromObject(user);
				userids=new String[array.size()];
				if(array.size()<=0) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userids'}");return;}
				for(int i=0;i<array.size();i++) {
					userids[i]=array.optString(i);
					System.out.println(array.opt(i));
				}

				System.out.println(userids[0]);
				response.getWriter().write(MySQLUser.GetUserInfo(userids));
				break;

			case "changepassword":
				userid=request.getParameter("userid");
				password=request.getParameter("password");
				String newPassword=request.getParameter("newpassword");
				if(userid==null||userid.equals("")||password==null||password.equals("")||newPassword==null||newPassword.equals("")) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or passowrd or newpassword'}");return;}
				response.getWriter().write(MySQLUser.ChangePassword(userid, password, newPassword));
				break;

			default:
				System.out.println("invalid request");
				response.getWriter().write("{'code':-100,'msg':'invalid request'}");
				
				break;
			}
		} else {
			System.out.println("Helloworld");
		}
		//response.getWriter().append("留言板 Served at: ").append(request.getContextPath());
	}

}
