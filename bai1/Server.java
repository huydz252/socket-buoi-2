import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                // Create a thread to handle each client
                new Thread(() -> {
                    try {
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        for (int i = 1; i <= 1000; i++) {
                            output.println(i);
                            Thread.sleep(1000); // Sleep 1 second
                        }
                        socket.close();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
