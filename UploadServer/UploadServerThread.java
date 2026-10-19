
import java.io.*;
import java.net.*;

public class UploadServerThread extends Thread {

    private Socket socket = null;

    public UploadServerThread(Socket socket) {
        super("UploadServerThread");
        this.socket = socket;
    }

    public void run() {

        try {

            // Read the client's HTTP request
            BufferedInputStream in =
                new BufferedInputStream(socket.getInputStream());

            // Store the HTTP response before sending it
            ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

            HttpServletRequest req =
                new HttpServletRequest(in);

            HttpServletResponse res =
                new HttpServletResponse(baos);

            HttpServlet httpServlet =
                new UploadServlet();

            // Read the first HTTP request line
            String requestLine = readLine(in);

            System.out.println("Request: " + requestLine);

            // Handle GET and POST requests
            if (requestLine.startsWith("GET")) {

                httpServlet.doGet(req, res);

            } else if (requestLine.startsWith("POST")) {

                httpServlet.doPost(req, res);

            } else {

                PrintWriter writer =
                    new PrintWriter(baos, true);

                writer.println("HTTP/1.1 405 Method Not Allowed");
                writer.println("Content-Type: text/plain");
                writer.println();
                writer.println("Method not supported");
            }

            // Send the response to the client
            OutputStream out = socket.getOutputStream();

            out.write(baos.toByteArray());
            out.flush();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            // Close the socket even if an error occurs
            try {

                if (socket != null) {
                    socket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }

    // Reads one HTTP request line
    private String readLine(InputStream in) throws IOException {

        StringBuilder line = new StringBuilder();

        int c;

        while ((c = in.read()) != -1) {

            if (c == '\r') {

                int next = in.read();

                if (next == '\n') {
                    break;
                }

            } else {

                line.append((char) c);

            }
        }

        return line.toString();
    }
}
