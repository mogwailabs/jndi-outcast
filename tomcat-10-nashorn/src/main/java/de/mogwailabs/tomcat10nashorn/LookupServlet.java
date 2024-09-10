package de.mogwailabs.tomcat10nashorn;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lookup")
public class LookupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.ldap.object.trustSerialDat", "true");

        String resourceName = request.getParameter("resource");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (resourceName == null || resourceName.isEmpty()) {
            out.println("Error: Query parameter 'resource' is required.");
            return;
        }

        try {
            Object resource = InitialContext.doLookup(resourceName);

            out.println("<h3>JNDI Lookup Result</h3>");
            out.println("<p>Resource Name: " + resourceName + "</p>");
            out.println("<p>Resource Value: " + resource.toString() + "</p>");
        } catch (NamingException e) {
            out.println("<h3>Error</h3>");
            out.println("<p>Failed to perform JNDI lookup: " + e.getMessage() + "</p>");
        } finally {
            out.close();
        }
    }
}
