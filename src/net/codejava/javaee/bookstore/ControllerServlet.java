package net.codejava.javaee.bookstore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO;
	private UserDAO userDAO;

	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		userDAO = new UserDAO(jdbcURL, jdbcUsername, jdbcPassword);
		bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertBook(request, response);
				break;
			case "/delete":
				deleteBook(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateBook(request, response);
				break;
				case "/login":
					showNewUserForm(request, response);
					break;
				case "/add-user":
					insertUser(request, response);
					break;
				case "/logout":
					logout(request, response);
					break;
			default:
				listBook(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listBook(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		if(session.getAttribute("Username")!=null&&session.getAttribute("password")!=null) {
			String username = (String) session.getAttribute("Username");
			String password = (String) session.getAttribute("password");
			User user = userDAO.getUser(username, password);
			session.setAttribute("user", user);
			session.setAttribute("id", user.getId());
			List<Book> listBook = bookDAO.listAllBooks(user.getId());
			request.setAttribute("listBook", listBook);
			RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
			dispatcher.forward(request, response);
		}
		else {
			request.setAttribute("listBook", null);
			RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher;
		if(session.getAttribute("Username")==null&&session.getAttribute("password")==null) {
			dispatcher = request.getRequestDispatcher("BookList.jsp");
		}
		else {
			dispatcher = request.getRequestDispatcher("BookForm.jsp");
		}dispatcher.forward(request, response);
	}

	private void showNewUserForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Book existingBook = bookDAO.getBook(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
		request.setAttribute("book", existingBook);
		dispatcher.forward(request, response);

	}

	private void insertBook(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		float price = Float.parseFloat(request.getParameter("price"));
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("Username");
		String password = (String)session.getAttribute("password");

		User user = userDAO.getUser(username, password);
		Book newBook = new Book(title, author, price, user.getId());
//		BookDTO newBookDTO = new BookDTO();
//		newBookDTO.setId(newBookDTO.getId());
//		newBookDTO.setAuthor(newBook.getAuthor());
		bookDAO.insertBook(newBook);
		response.sendRedirect("BookList.jsp");
	}

	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("Username");
		session.removeAttribute("password");
		session.removeAttribute("user");
		session.removeAttribute("id");
		response.sendRedirect("list");
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		User user = new User(username, password);

//		register users
//		userDAO.insertUser(user);

		//login users
		User user1 = userDAO.getUser(username, password);
		session.setAttribute("user", user1);
		session.setAttribute("Username", user1.getUsername());
		session.setAttribute("password", user1.getPassword());
		System.out.println(user1);
		RequestDispatcher dispatcher;
		if (user1.id==null){
			dispatcher = request.getRequestDispatcher("BookList.jsp");
		}
		else {
			dispatcher = request.getRequestDispatcher("BookForm.jsp");
		}
		request.setAttribute("name", user1.username);
		dispatcher.forward(request, response);
	}

	private void updateBook(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		float price = Float.parseFloat(request.getParameter("price"));

		Book book = new Book(id, title, author, price);
		bookDAO.updateBook(book);
		response.sendRedirect("list");
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		bookDAO.deleteBook(id);
		response.sendRedirect("list");

	}

}
