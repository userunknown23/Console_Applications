package io.github.rAnurag;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String text, int radius, ImageIcon icon) {
        super(text, icon);
        this.cornerRadius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.setColor(getBackground());
        g2.fill(round);

        g2.setColor(getForeground());
        g2.draw(round);

        g2.dispose();
        super.paintComponent(g);
    }
}


class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        cornerRadius = radius;
        setOpaque(false); // Make the panel transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set background color
        g2.setColor(getBackground());
        
        // Draw rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}


class TicTacToeOverlayPanel extends JPanel {
    public TicTacToeOverlayPanel() {
        setOpaque(false); // Make the panel transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(240, 240, 240));
        g2d.setStroke(new BasicStroke(10));

        // Draw the vertical lines
        g2d.drawLine(getWidth() / 3, 0 , getWidth() / 3, getHeight() );
        g2d.drawLine(2 * getWidth() / 3, 0 , 2 * getWidth() / 3, getHeight() );

        // Draw the horizontal lines
        g2d.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3);
        g2d.drawLine(0, 2 * getHeight() / 3, getWidth(), 2 * getHeight() / 3);
    }
}



public class TicTacToe implements ActionListener {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private JFrame frame;
    private JLabel textLabel;
    private static final String TEXT = "TIC TAC TOE";
    private static final int DELAY = 100; // milliseconds
    private JPanel title_panel;
    private RoundedPanel button_panel;
    private TicTacToeOverlayPanel overlay_panel; 
    private JLabel textfield;
    private JButton[] buttons;
    private boolean player1_turn;
    private JButton replayButton;
    
    public TicTacToe(Socket socket, String username) {
        try {
            this.username = username;
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            sendUsername(); // Send username to the server
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
  
        
        frame = new JFrame();
        title_panel = new JPanel();
        button_panel = new RoundedPanel(60);
        overlay_panel = new TicTacToeOverlayPanel(); // Use the custom overlay panel
        textfield = new JLabel();
        buttons = new JButton[9];
        
        JPanel topPanel = new JPanel(); 
        topPanel.setLayout(new FlowLayout()); 
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setPreferredSize(new Dimension(400, 50));
        textLabel = new JLabel(); 
        textLabel.setForeground(new Color(116,140,162));
        textLabel.setBackground(new Color(240, 240, 240));
        textLabel.setFont(new Font("Segoe Script", Font.BOLD, 32));
        textLabel.setOpaque(true);
        topPanel.add(textLabel);
        frame.add(topPanel, BorderLayout.NORTH);
        
        replayButton = new RoundedButton(
        	    "", 
        	    20, 
        	    new ImageIcon(getClass().getResource("/replayIcon.png"))
        	);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 580);
        frame.getContentPane().setBackground(new Color(240, 240, 240));
        frame.setLayout(new GridBagLayout());
        frame.setVisible(true);
        
        textfield.setBackground(new Color(240, 240, 240));
        textfield.setForeground(new Color(131,125,124));
        textfield.setFont(new Font("Bradley Hand ITC", Font.BOLD, 28)); //Bradley Hand ITC, Monotype Corsiva// Pristina
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("TIC TAC TOE");
        textfield.setOpaque(true);
        
        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 550, 40);
        
        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(255,205,210));
        
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 100));
            buttons[i].setFocusPainted(false); // Focus behavior on hover
            buttons[i].setBorderPainted(false); // borders visibility 
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            
            if(i%2 == 0) {
            	// Make the button transparent
            	buttons[i].setOpaque(false); 
            	buttons[i].setContentAreaFilled(false); 
            	buttons[i].setBorderPainted(false); 
            	buttons[i].setFocusPainted(false);
            }
            else buttons[i].setBackground(new Color(200, 230, 201));
            
        }
        
        replayButton.setBorderPainted(false);
//        replayButton.setContentAreaFilled(false);
//        replayButton.setOpaque(false);
        replayButton.setBackground(new Color(240, 240, 240));
        replayButton.setFocusable(false);
        replayButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        replayButton.setBounds(400/2 - 70/2, - 70, 70, 70);
        replayButton.addActionListener(e -> resetGame());
        
        title_panel.add(textfield);
        
        // Use JLayeredPane to stack the button panel and the overlay panel 
        JLayeredPane layeredPane = new JLayeredPane(); 
        layeredPane.setPreferredSize(new Dimension(400, 400)); 
        button_panel.setMixingCutoutShape(null);
        button_panel.setBounds(0, 0, 400, 400); 
        overlay_panel.setBounds(0, 0, 400, 400); 
        
        layeredPane.add(button_panel, JLayeredPane.DEFAULT_LAYER); 
        layeredPane.add(overlay_panel, JLayeredPane.PALETTE_LAYER); 
        layeredPane.add(replayButton, JLayeredPane.DRAG_LAYER);
        
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.insets = new Insets(15, 5, 0, 5); // Add some padding  - top, right , bot, left
        frame.add(title_panel, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.fill = GridBagConstraints.NONE; // Do not fill the entire cell 
        frame.add(layeredPane, gbc);
        
        frame.setVisible(true);
        
        startTypewriterEffect();
        startMusic();
        
    }
    
    public void startMusic() {
    	// Start the background music in a separate thread
        Thread musicThread = new Thread(() -> playMusic("/backgroundMusic2.wav"));
        musicThread.start();

    }
    
    private void playMusic(String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    public void startTypewriterEffect() {
    	// Create a new thread for the typewriter effect 
        new Thread(() -> {
        	int i;
        	while(true) {
        		for (i = 0; i <= TEXT.length(); i++) { 
        			final String partialText = TEXT.substring(0, i); 
        			SwingUtilities.invokeLater(() -> textLabel.setText(partialText)); 
        			try { 
        				Thread.sleep(DELAY); 
        			} catch (InterruptedException e) { 
        				Thread.currentThread().interrupt(); 
        			} 
        		}
        		i = 0;
        	}
        }).start();
    }
     
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            System.out.println("Checking button: " + i); // Log button index
            if (e.getSource() == buttons[i]) {
                System.out.println("Button source matched: " + i); // Log button source match
                if (buttons[i].getText().equals("")) {
                    System.out.println("Button text is empty: " + i); // Log button text check
                    if ((player1_turn && username.equals("X")) || (!player1_turn && username.equals("O"))) {
                        System.out.println("Turn matched for: " + username); // Log player turn check
                        // Existing code for handling the button click
                        if (player1_turn) {
                            buttons[i].setForeground(new Color(255, 0, 0));
                            buttons[i].setText("X");
                            player1_turn = false;
                            textfield.setText("O turn");
                            sendMove(i, "X");
                        } else {
                            buttons[i].setForeground(new Color(0, 0, 255));
                            buttons[i].setText("O");
                            player1_turn = true;
                            textfield.setText("X turn");
                            sendMove(i, "O");
                        }
                        check();
                    } else {
                        System.out.println("Turn did not match for: " + username); // Log player turn mismatch
                    }
                } else {
                    System.out.println("Button text is not empty: " + buttons[i].getText()); // Log button text mismatch
                }
            }
        }
    }

   
    public void sendUsername() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Username sent: " + username); // Log sent username
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    
    public void resetGame() { 
    	for (int i = 0; i < 9; i++) {
            if(i%2 == 0) {
            	// Make the button transparent
            	buttons[i].setOpaque(false); 
            	buttons[i].setContentAreaFilled(false); 
            	buttons[i].setBorderPainted(false); 
            	buttons[i].setFocusPainted(false);
            }
            else buttons[i].setBackground(new Color(200, 230, 201));
            
            buttons[i].setEnabled(true);
            buttons[i].setText("");
        
            textfield.setText((username.equals("X")) ? "Your turn" : "Their turn");
            replayButton.setBounds(400/2 - 70/2, -70, 70, 70);
    		player1_turn = true; // Reset turn 
    	}
    }
    
    public void check() {
        // Check X win conditions
        if (
            buttons[0].getText().equals("X") &&
            buttons[1].getText().equals("X") &&
            buttons[2].getText().equals("X")
        ) {
            xWins(0, 1, 2);
        } else if (
            buttons[3].getText().equals("X") &&
            buttons[4].getText().equals("X") &&
            buttons[5].getText().equals("X")
        ) {
            xWins(3, 4, 5);
        } else if (
            buttons[6].getText().equals("X") &&
            buttons[7].getText().equals("X") &&
            buttons[8].getText().equals("X")
        ) {
            xWins(6, 7, 8);
        } else if (
            buttons[0].getText().equals("X") &&
            buttons[3].getText().equals("X") &&
            buttons[6].getText().equals("X")
        ) {
            xWins(0, 3, 6);
        } else if (
            buttons[1].getText().equals("X") &&
            buttons[4].getText().equals("X") &&
            buttons[7].getText().equals("X")
        ) {
            xWins(1, 4, 7);
        } else if (
            buttons[2].getText().equals("X") &&
            buttons[5].getText().equals("X") &&
            buttons[8].getText().equals("X")
        ) {
            xWins(2, 5, 8);
        } else if (
            buttons[0].getText().equals("X") &&
            buttons[4].getText().equals("X") &&
            buttons[8].getText().equals("X")
        ) {
            xWins(0, 4, 8);
        } else if (
            buttons[2].getText().equals("X") &&
            buttons[4].getText().equals("X") &&
            buttons[6].getText().equals("X")
        ) {
            xWins(2, 4, 6);
        }
        
        // Check O win conditions
        if (
            buttons[0].getText().equals("O") &&
            buttons[1].getText().equals("O") &&
            buttons[2].getText().equals("O")
        ) {
            oWins(0, 1, 2);
        } else if (
            buttons[3].getText().equals("O") &&
            buttons[4].getText().equals("O") &&
            buttons[5].getText().equals("O")
        ) {
            oWins(3, 4, 5);
        } else if (
            buttons[6].getText().equals("O") &&
            buttons[7].getText().equals("O") &&
            buttons[8].getText().equals("O")
        ) {
            oWins(6, 7, 8);
        } else if (
            buttons[0].getText().equals("O") &&
            buttons[3].getText().equals("O") &&
            buttons[6].getText().equals("O")
        ) {
            oWins(0, 3, 6);
        } else if (
            buttons[1].getText().equals("O") &&
            buttons[4].getText().equals("O") &&
            buttons[7].getText().equals("O")
        ) {
            oWins(1, 4, 7);
        } else if (
            buttons[2].getText().equals("O") &&
            buttons[5].getText().equals("O") &&
            buttons[8].getText().equals("O")
        ) {
            oWins(2, 5, 8);
        } else if (
            buttons[0].getText().equals("O") &&
            buttons[4].getText().equals("O") &&
            buttons[8].getText().equals("O")
        ) {
            oWins(0, 4, 8);
        } else if (
            buttons[2].getText().equals("O") &&
            buttons[4].getText().equals("O") &&
            buttons[6].getText().equals("O")
        ) {
            oWins(2, 4, 6);
        }
        
        // Check tie condition
        noTieAssumed: {
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().equals("")) break noTieAssumed;
            }
            tie();
        }
    }

	
	public void xWins(int a, int b, int c) {
		int[] winningCombination = {a, b, c};
		for(int x: winningCombination) {
			buttons[x].setOpaque(true);
			buttons[x].setContentAreaFilled(true);
			buttons[x].setBackground((username.equals("X")) ? Color.GREEN : Color.RED);
		}
		
		for(int i=0; i<9; i++) {
			buttons[i].setEnabled(false);
		}
		
		replayButton.setBounds(400/2 - 70/2, 400/2 - 70/2, 70, 70);
		textfield.setText((username.equals("X")) ? "You won!" : "You Lose");
	}
	
	public void oWins(int a, int b, int c) {
		int[] winningCombination = {a, b, c};
		for(int x: winningCombination) {
			buttons[x].setOpaque(true);
			buttons[x].setContentAreaFilled(true);
			buttons[x].setBackground((username.equals("O")) ? Color.GREEN : Color.RED);
		}
		
		for(int i=0; i<9; i++) {
			buttons[i].setEnabled(false);
		}
		
//		textfield.setText("O Won");
		replayButton.setBounds(400/2 - 70/2, 400/2 - 70/2, 70, 70);
		textfield.setText((username.equals("O")) ? "You won!" : "You Lose!");
	}
	
	public void tie() {
		for(int i=0; i<9; i++) {
			buttons[i].setEnabled(false);
			textfield.setText("It`s a Tie");
		}
		replayButton.setBounds(400/2 - 70/2, 400/2 - 70/2, 70, 70);
	}
	
    
	public void sendMove(int index, String player) {
	    try {
	        String color = player.equals("X") ? "255,0,0" : "0,0,255";
	        String nextTurn = player.equals("X") ? "O" : "X";
	        String message = "MOVE " + index + " " + player + " " + color + " " + nextTurn;
	        bufferedWriter.write(message);
	        bufferedWriter.newLine();
	        bufferedWriter.flush();
	        System.out.println("Sent: " + message); // Log sent message
	    } catch (IOException e) {
	        closeEverything(socket, bufferedReader, bufferedWriter);
	    }
	}


	public void listenForMoves() {
	    new Thread(() -> {
	        String messageFromServer;
	        try {
	            while (socket.isConnected() && (messageFromServer = bufferedReader.readLine()) != null) {
	                System.out.println("Received: " + messageFromServer); // Log received message
	                if (messageFromServer.startsWith("SYMBOL")) {
	                    String[] data = messageFromServer.split(" ");
	                    if (data.length == 2) {
	                        username = data[1]; // Assign the received symbol to the username variable
	                        System.out.println("Assigned symbol: " + username); // Log assigned symbol
	                    }
	                } else if (messageFromServer.startsWith("MOVE")) {
	                    String[] moveData = messageFromServer.split(" ");
	                    int index = Integer.parseInt(moveData[1]);
	                    String player = moveData[2];
	                    String[] rgb = moveData[3].split(",");
	                    Color color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
	                    String nextTurn = moveData[4];
	                    
	                    SwingUtilities.invokeLater(() -> {
	                        buttons[index].setText(player);
//	                        buttons[index].setForeground(color);
	                        buttons[index].setForeground(new Color(159,186,207));
	                        player1_turn = nextTurn.equals("X");
	                        
//	                        textfield.setText(player1_turn ? "X turn" : "O turn"); // Player 1 get X
	                        if(player1_turn && username.equals("X")) {
	                        	textfield.setText("Your turn");
	                        }
	                        else if(!player1_turn && username.equals("O")) {
	                        	textfield.setText("Your turn");
	                        }
	                        else {
	                        	textfield.setText("Their turn");
	                        }
	                        
	                        check(); // Check for win conditions
	                    });
	                } else if (messageFromServer.startsWith("TURN")) {
	                    String[] data = messageFromServer.split(" ");
	                    if (data.length == 2) {
	                        player1_turn = data[1].equals("X");
	                        
//	                        SwingUtilities.invokeLater(() -> textfield.setText(player1_turn ? "X turn" : "O turn")); // Player 1 get X
	                        SwingUtilities.invokeLater(() -> {
	                        	if(player1_turn && username.equals("X")) {
		                        	textfield.setText("Your turn");
		                        }
	                        	else if(!player1_turn && username.equals("O")) {
		                        	textfield.setText("Your turn");
		                        }
		                        else {
		                        	textfield.setText("Their turn");
		                        }
	                        });
	                        
	                        System.out.println("First turn assigned: " + (player1_turn ? "X" : "O")); // Log first turn
	                    }
	                } else if (messageFromServer.startsWith("SERVER")) {
	                    System.out.println("Server message: " + messageFromServer);
	                } else if (messageFromServer.startsWith("Connection rejected")) {
	                    SwingUtilities.invokeLater(() -> textfield.setText("Game full. Connection rejected."));
	                    System.out.println("Connection rejected: Too many players.");
	                    closeEverything(socket, bufferedReader, bufferedWriter);
	                } else {
	                    System.out.println("Unknown message type: " + messageFromServer);
	                }
	            }
	        } catch (IOException e) {
	            if (e.getMessage().equals("Connection rejected: Too many players.")) {
	                SwingUtilities.invokeLater(() -> textfield.setText("Game full. Connection rejected."));
	                System.out.println("Connection rejected: Too many players.");
	            } else {
	                System.out.println("Error while reading from server: " + e.getMessage());
	            }
	            closeEverything(socket, bufferedReader, bufferedWriter);
	        }
	    }).start();
	}


	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
	    try {
	        System.out.println("Closing resources for " + username); // Log the closing of resources
	        if (bufferedReader != null) {
	            bufferedReader.close();
	        }
	        if (bufferedWriter != null) {
	            bufferedWriter.close();
	        }
	        if (socket != null) {
	            socket.close();
	        }
	        System.out.println("Resources closed for " + username); // Log successful closure
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Error while closing resources: " + e.getMessage()); // Log any errors
	    }
	}



	public static void main(String[] args) {
        String username = JOptionPane.showInputDialog(null, "Enter your username:", "Username", JOptionPane.QUESTION_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            String ipAddress = JOptionPane.showInputDialog(null, "Enter the IP address to connect:", "Connect to Server", JOptionPane.QUESTION_MESSAGE);
            String portStr = JOptionPane.showInputDialog(null, "Enter the port number:", "Connect to Server", JOptionPane.QUESTION_MESSAGE);

            if (ipAddress != null && portStr != null) {
                try {
                    int port = Integer.parseInt(portStr);
                    Socket socket = new Socket(ipAddress, port);
                    TicTacToe ticTacToe = new TicTacToe(socket, username);
                    ticTacToe.listenForMoves();
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to connect to the server. Please check the IP address and port number.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid port number. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Connection canceled.", "Connection", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Username is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
   