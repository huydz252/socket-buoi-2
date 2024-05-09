import java.io.*;
import java.net.*;

public class GroupChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Prompt client to enter username
            System.out.println("Enter your username:");
            String username = consoleInput.readLine();
            output.println(username);

            // Create a thread to listen for messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Listen for messages from console and send them to the server
            String message;
            while ((message = consoleInput.readLine()) != null) {
                output.println(message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
