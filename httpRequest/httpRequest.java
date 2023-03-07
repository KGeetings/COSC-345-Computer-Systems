package httpRequest;
/* Creates a connection to http://csapp.cs.cmu.edu/3e/students.html
 * Returns the response from the server as a string
 */

import java.io.*;
import java.net.*;

public class httpRequest {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("csapp.cs.cmu.edu", 80)) {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("GET /3e/students.html HTTP/1.1");
            writer.println("Host: csapp.cs.cmu.edu");
            writer.println("Connection: close");
            writer.println();

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}