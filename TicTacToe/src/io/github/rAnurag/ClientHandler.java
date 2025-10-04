package io.github.rAnurag;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static HashMap<String, String> activePlayers = new HashMap<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private String symbol;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            if (this.clientUsername == null) {
                System.out.println("Error: clientUsername is null");
                this.clientUsername = "Unknown";
            }

            // Assign symbols dynamically
            if (!activePlayers.containsValue("X")) {
                this.symbol = "X";
            } else if (!activePlayers.containsValue("O")) {
                this.symbol = "O";
            } else {
                this.symbol = "X"; // Default to "X" if both are present (fallback)
            }
            
            clientHandlers.add(this);
            activePlayers.put(clientUsername, this.symbol);
            broadcastMessage("SERVER: " + clientUsername + " (" + symbol + ") has joined the game!");
            sendSymbol(); // Send the assigned symbol to the client

            // Send initial turn info if this is the first client or if both clients are present
            if (clientHandlers.size() == 1 || activePlayers.size() == 2) {
                broadcastMessage("TURN X");
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    System.out.println("Received: " + messageFromClient); // Log received message
                    broadcastMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendSymbol() {
        try {
            bufferedWriter.write("SYMBOL " + symbol);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Symbol sent to " + clientUsername + ": " + symbol); // Log sent symbol
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void broadcastMessage(String messageToSend) {
        System.out.println("Broadcasting: " + messageToSend); // Log broadcast message
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        activePlayers.remove(clientUsername);
        broadcastMessage("SERVER: " + clientUsername + " has left the game!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
