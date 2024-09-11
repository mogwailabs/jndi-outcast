package de.mogwailabs.tomcat9jshell;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/lookup")
public class LookupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourceName = request.getParameter("resource");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (resourceName == null || resourceName.isEmpty()) {
            out.println("Error: Query parameter 'resource' is required.");
            return;
        }

        try {
            Object resource = InitialContext.doLookup(resourceName);

            // Make sure the file has been created.
            Thread.sleep(100);

            var filePath = "/usr/local/tomcat/temp/pwn.txt";
            var file = new File(filePath);

            out.println("<h3>JNDI Lookup Result</h3>");
            out.println("<p>Resource Name: " + resourceName + "</p>");
            out.println("<p>Resource Value: " + resource.toString() + "</p>");

            if (file.exists()) {
                // We don't need to set the status code to 200 - it's the default anyway.
                out.println("<p>EXPLOITATION SUCCESSFUL</p>");
                file.delete();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("<p>EXPLOITATION UNSUCCESSFUL</p>");
            }
        } catch (NamingException | InterruptedException e) {
            out.println("<h3>Error</h3>");
            out.println("<p>Failed to perform JNDI lookup: " + e.getMessage() + "</p>");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            out.println("<p><b>Stack Trace:</b><br>" + sw.toString().replaceAll("\n", "<br>") + "</p>");
        } finally {
            out.close();
        }
    }
}
