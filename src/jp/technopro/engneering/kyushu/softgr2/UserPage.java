package jp.technopro.engneering.kyushu.softgr2;

import java.io.IOException;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.technopro.engneering.kyushu.softgr2.bean.PersonBean;
import jp.technopro.engneering.kyushu.softgr2.dao.PersonDAO;

@WebServlet("/Validation")
public class UserPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String resultPage = "/WEB-INF/jsp/error.jsp";
		String name = request.getParameter("name");
		String password = request.getParameter("password");

        try {
            PersonDAO dao = new PersonDAO();
            PersonBean person = dao.getPersonByName(name);

            if (person == null) {
            	request.setAttribute("errorMessage", "そんなユーザーはいません");
            } else if (password.equals(person.getPassword())) {
            	HttpSession session = request.getSession(true);
            	session.setAttribute("loginname", name);
            	session.setAttribute("person", person);
                resultPage = "/WEB-INF/jsp/userPage.jsp";
            } else {
            	request.setAttribute("errorMessage", "パスワードが違います");
            }

        } catch (NamingException e) {
            request.setAttribute("errorMessage", e.getMessage());

        } catch (SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        
        if (request.getAttribute("errorMessage") == null) {
	        RequestDispatcher dispatcher = request.getRequestDispatcher(resultPage);
	        dispatcher.forward(request, response);
        } else {
        	
        }
	}
}
