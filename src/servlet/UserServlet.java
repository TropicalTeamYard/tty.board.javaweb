package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mysql.MySQLDataBase;

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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("-----POST---UserServlet----");
		String method=request.getParameter("method");
		
		String password;String userid;String nickname;String token;
		
		if(method!=null) {
			System.out.println("method = "+method);
			switch (method) {
			case "login":
				userid=request.getParameter("userid");
				password=request.getParameter("password");
				if(userid==null||userid==""||password==null||password=="") {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or passowrd'}");return;}
				response.getWriter().write(MySQLDataBase.Login(userid, password));
				break;
				
			case "autologin":
				userid=request.getParameter("userid");
				token=request.getParameter("token");
				if(token==""||token==null||userid==""||userid==null) {response.getWriter().write("{'code':-100,'msg':'invalid request : null userid or passowrd'}");return;}
				response.getWriter().write(MySQLDataBase.Token(userid, token));
				break;
				
			case "register":
				nickname=request.getParameter("nickname");
				password=request.getParameter("password");
				if(nickname==null||nickname==""||password==null||password=="") {response.getWriter().write("{'code':-100,'msg':'invalid request : null nickname or passowrd'}");return;}
				System.out.println(nickname+" "+password);
				response.getWriter().write(MySQLDataBase.Register(nickname, password));
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
