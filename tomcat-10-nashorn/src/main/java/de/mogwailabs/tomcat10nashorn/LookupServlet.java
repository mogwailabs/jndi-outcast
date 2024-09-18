package de.mogwailabs.tomcat10nashorn;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lookup")
public class LookupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // We set the status code to 400 by default and only change it to 200 in case of exploitation success.
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        var resourceName = request.getParameter("resource");

        response.setContentType("text/html");
        var out = response.getWriter();

        if (resourceName == null || resourceName.isEmpty()) {
            out.println("Error: Query parameter 'resource' is required.");
            return;
        }

        try {
            var resource = InitialContext.doLookup(resourceName);

            // Make sure the file has been created.
            Thread.sleep(100);

            var filePath = "/usr/local/tomcat/temp/pwn.txt";
            var file = new File(filePath);

            out.println("<h3>JNDI Lookup Result</h3>");
            out.println("<p>Resource Name: " + resourceName + "</p>");
            out.println("<p>Resource Value: " + resource.toString() + "</p>");

            if (file.exists()) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("<p>EXPLOITATION SUCCESSFUL</p>");
                file.delete();
            } else {
                out.println("<p>EXPLOITATION UNSUCCESSFUL</p>");
            }
        } catch (NamingException | InterruptedException e) {
            out.println("<h3>Error</h3>");
            out.println("<p>Failed to perform JNDI lookup: " + e.getMessage() + "</p>");

            var sw = new StringWriter();
            var pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            out.println("<p><b>Stack Trace:</b><br>" + sw.toString().replaceAll("\n", "<br>") + "</p>");
        } finally {
            out.close();
        }
    }
}