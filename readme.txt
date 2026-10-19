COMP 3940 Assignment 3
Upload Server and Client

DESCRIPTION
This project implements a custom HTTP upload server and a Java client. Users can upload files through a browser or Java client. Uploaded files are saved in the images folder with the caption and date included in the filename. After an upload, the server returns an HTML page listing files alphabetically.

REQUIREMENTS
Java JDK 11

COMPILE
From the project root directory:

javac UploadServer/*.java
javac AsyncTask/*.java
javac -cp ConsoleApp:AsyncTask ConsoleApp/*.java

START SERVER
java -cp UploadServer UploadServer

The server listens on port 8082. Keep it running while testing.

BROWSER CLIENT
Open http://localhost:8082/
Enter a caption and date, choose a file, and submit the form.

JAVA CLIENT
Open a second Terminal in the project root and run:

java -cp ConsoleApp:AsyncTask Activity

The client sends a multipart POST request to the server. Its caption, date, and file path are configured in UploadClient.java.

VERIFY UPLOADS
Run ls images to list uploaded files.
Uploaded files are stored in the images folder relative to the server's working directory.

NOTES
Start the server before running the Java client. Ensure port 8082 is available and the file path in UploadClient.java points to an existing file.
