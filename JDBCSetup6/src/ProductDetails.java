import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
//import java.math.BigDecimal;
//import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.DBConnection;

/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/ProductDetails")
public class ProductDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductDetails() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html><body>");

			// get config properties
			InputStream in = getServletContext().getResourceAsStream("/config.properties");
			Properties props = new Properties();
			props.load(in);

			// create db connection and query statement
			DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
			Statement stmt = conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			String prodid = request.getParameter("prodid");
			
			boolean found = false;												// set flag for found to false
			ResultSet rs = stmt.executeQuery("select * from eproduct;");
			while (rs.next()) {	
				int id = rs.getInt("ID");
				if (id == Integer.parseInt(prodid.trim())) {					// convert text prodid to integer before compare to ID from db
					out.println(id + ": " + rs.getString("name") + "<br>");	    // if input id found, print it out
					found = true;												// set flag to true for id found, 
					break;														//   and stop the looping
				}
			}
			
			if (!found)															// no id found in the database
				out.println(prodid + " not found <br>");						//   print not found

			stmt.close();
			conn.closeConnection();

			out.println("<h4><a href=index.html>Return to Data Entry</a></h4>");  // provide link to return to beginning index.html
			out.println("</body></html>");
			conn.closeConnection();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}