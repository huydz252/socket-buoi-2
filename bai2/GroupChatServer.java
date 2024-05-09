import java.io.*;
import java.net.*;
import java.util.*;

public class GroupChatServer {
    private static final Map<Socket, PrintWriter> clientWriters = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void broadcast(String message) {
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                // Prompt client for username
                output.println("Enter your username:");
                String username = input.readLine();
                System.out.println("User " + username + " joined the chat.");
                broadcast(username + " joined the chat.");

                // Add client's writer to the map
                clientWriters.put(socket, output);

                // Listen for messages from this client and broadcast them
                String message;
                while ((message = input.readLine()) != null) {
                    System.out.println(username + ": " + message);
                    broadcast(username + ": " + message);
                }

                // Remove client's writer from the map when client disconnects
                clientWriters.remove(socket);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
