package demo.javax.servlet.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
	
	

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("param_name");
		
		req.setAttribute("username", name);
		req.getRequestDispatcher("/a/b/c.jsp").forward(req, resp);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
