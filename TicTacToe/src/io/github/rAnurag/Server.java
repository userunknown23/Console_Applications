package io.github.rAnurag;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                if (ClientHandler.clientHandlers.size() < 2) {
                    Socket socket = serverSocket.accept();
                    System.out.println("A new player has joined the game!");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                } else {
                    Socket socket = serverSocket.accept();
                    System.out.println("Rejecting connection from third client.");
                    socket.getOutputStream().write("Connection rejected: Too many players.\n".getBytes());
                    socket.close(); // Close the socket for the third client immediately
                }
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
