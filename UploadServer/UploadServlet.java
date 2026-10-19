import java.io.*;

public class UploadServlet extends HttpServlet {

   protected void doGet(HttpServletRequest request,
                        HttpServletResponse response) {

      try {

         PrintWriter out =
            new PrintWriter(response.getOutputStream(), true);

         out.println("HTTP/1.1 200 OK");
         out.println("Content-Type: text/html");
         out.println();

         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<body>");

         out.println("<h2>HTML Forms</h2>");

         out.println("<form action=\"http://localhost:8082\" method=\"post\" enctype=\"multipart/form-data\">");

         out.println("Caption: <input type=\"text\" name=\"caption\"/><br/><br/>");

         out.println("Date: <input type=\"date\" name=\"date\"/><br/><br/>");

         out.println("<input type=\"file\" name=\"fileName\"/><br/><br/>");

         out.println("<input type=\"submit\" value=\"Submit\"/>");

         out.println("</form>");

         out.println("</body>");
         out.println("</html>");

      } catch (Exception ex) {

         System.err.println(ex);

      }

   }


   protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) {

      try {

         InputStream in = request.getInputStream();

         // Read HTTP headers
         String contentType = "";
         int contentLength = 0;

         String line;

         while (!(line = readLine(in)).isEmpty()) {

            System.out.println(line);

            if (line.toLowerCase().startsWith("content-type:")) {

               contentType =
                  line.substring(13).trim();

            }

            if (line.toLowerCase().startsWith("content-length:")) {

               contentLength =
                  Integer.parseInt(
                     line.substring(15).trim()
                  );

            }

         }

         // Find multipart boundary
         String boundary =
            contentType.substring(
               contentType.indexOf("boundary=") + 9
            );

         boundary = "--" + boundary;

         System.out.println(
            "Boundary: " + boundary
         );

         System.out.println(
            "Content Length: " + contentLength
         );


         // Read the entire POST body
         byte[] body =
            new byte[contentLength];

         int totalRead = 0;

         while (totalRead < contentLength) {

            int bytesRead =
               in.read(
                  body,
                  totalRead,
                  contentLength - totalRead
               );

            if (bytesRead == -1) {
               break;
            }

            totalRead += bytesRead;

         }

         // keeps the byte values unchanged.
         String bodyText =
            new String(
               body,
               "ISO-8859-1"
            );


         // caption
         String caption =
            getFormValue(
               bodyText,
               "name=\"caption\""
            );


         // date
         String date =
            getFormValue(
               bodyText,
               "name=\"date\""
            );


         // original filename
         int fileNamePosition =
            bodyText.indexOf(
               "filename=\""
            );

         String fileName =
            "uploadedFile";


         if (fileNamePosition != -1) {

            int start =
               fileNamePosition + 10;

            int end =
               bodyText.indexOf(
                  "\"",
                  start
               );

            fileName =
               bodyText.substring(
                  start,
                  end
               );

         }


         // Find where the actual file data starts
         int fileStart =
            bodyText.indexOf(
               "\r\n\r\n",
               fileNamePosition
            );

         fileStart += 4;


         // Find where the file data ends
         int fileEnd =
            bodyText.indexOf(
               "\r\n" + boundary,
               fileStart
            );


         // Extract binary file bytes
         byte[] fileBytes =
            new byte[fileEnd - fileStart];


         System.arraycopy(
            body,
            fileStart,
            fileBytes,
            0,
            fileBytes.length
         );


         // Create images folder
         File imagesFolder =
            new File("images");


         if (!imagesFolder.exists()) {

            imagesFolder.mkdir();

         }


         // Create new filename
         String newFileName =
            caption + "_" +
            date + "_" +
            fileName;


         File outputFile =
            new File(
               imagesFolder,
               newFileName
            );


         // Save binary file
         FileOutputStream fileOutput =
            new FileOutputStream(
               outputFile
            );

         fileOutput.write(fileBytes);

         fileOutput.close();


         System.out.println(
            "Saved file: " +
            outputFile.getPath()
         );


         // Send HTML response
         PrintWriter out =
            new PrintWriter(
               response.getOutputStream(),
               true
            );


         out.println("HTTP/1.1 200 OK");
         out.println("Content-Type: text/html");
         out.println();


         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<body>");


         out.println(
            "<h2>Upload Successful</h2>"
         );


         out.println(
            "<p>File: " +
            newFileName +
            "</p>"
         );


         out.println("<h2>Images</h2>");


         // Get files from images folder
         File[] files =
            imagesFolder.listFiles();


         if (files != null) {

            // Sort files alphabetically
            java.util.Arrays.sort(
               files,
               (file1, file2) ->
                  file1.getName()
                     .compareToIgnoreCase(
                        file2.getName()
                     )
            );


            out.println("<ul>");


            for (File file : files) {

               out.println(
                  "<li>" +
                  file.getName() +
                  "</li>"
               );

            }


            out.println("</ul>");

         }


         out.println("</body>");
         out.println("</html>");


      } catch (Exception ex) {

         ex.printStackTrace();

      }

   }


   // Reads one line from the HTTP request
   private String readLine(InputStream in)
      throws IOException {

      StringBuilder line =
         new StringBuilder();

      int c;


      while ((c = in.read()) != -1) {

         if (c == '\r') {

            int next = in.read();

            if (next == '\n') {
               break;
            }

         } else {

            line.append(
               (char)c
            );

         }

      }


      return line.toString();

   }


   // Gets a value from a multipart form field
   private String getFormValue(
      String body,
      String fieldName) {

      int position =
         body.indexOf(fieldName);


      if (position == -1) {
         return "";
      }


      int start =
         body.indexOf(
            "\r\n\r\n",
            position
         );


      if (start == -1) {
         return "";
      }


      start += 4;


      int end =
         body.indexOf(
            "\r\n",
            start
         );


      if (end == -1) {
         return "";
      }


      return body.substring(
         start,
         end
      );

   }

}