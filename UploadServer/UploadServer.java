import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadServer {

    // Thread pool with 3 worker threads
    static ExecutorService threadPool =
        Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(8082);

        } catch (IOException e) {

            System.err.println(
                "Could not listen on port: 8082."
            );

            System.exit(-1);
        }


        System.out.println(
            "UploadServer running on port 8082..."
        );


        while (true) {

            Socket socket =
                serverSocket.accept();

            // Give the connection to the thread pool
            threadPool.execute(
                new UploadServerThread(socket)
            );
        }
    }
}