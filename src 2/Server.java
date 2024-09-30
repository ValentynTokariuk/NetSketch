import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.Color; // Importing Color class

public class Server {
    private int nbClients; // Number of clients connected to the server
    private List<ObjectOutputStream> clients; // List of client output streams
    private LinkedList<Sequence> sequences; // List of drawing sequences
    private LinkedList<Sequence> undoStack; // Stack of undone sequences for redo functionality
    private ArrayList<String> messages; // List of chat messages
    private boolean newDrawing; // Flag to indicate if a new drawing session has started
    private Sequence current; // Current working sequence
    private Color backgroundColor = Color.white; // Background color
    ListIterator<Sequence> seqIterator; // Iterator for sequences
    ListIterator<Sequence> undoIterator; // Iterator for undo stack

    // Constructor to initialize the server
    public Server() {
        nbClients = 0;
        clients = new ArrayList<ObjectOutputStream>();
        sequences = new LinkedList<Sequence>();
        messages = new ArrayList<String>();
        undoStack = new LinkedList<Sequence>();
        newDrawing = true;
    }

    // Method to get the list of clients
    public List<ObjectOutputStream> getClients() {
        return clients;
    }

    // Method to add a new client to the server
    public void addClient(ObjectOutputStream stream) {
        clients.add(stream);
        nbClients++;
        sendDrawing(stream); // Send the current drawing to the new client
        sendBackgroundColor(stream); // Send the current background color to the new client
    }

    // Method to send the current drawing to a specific client
    public void sendDrawing(ObjectOutputStream out) {
        try {
            out.writeObject(new Data(sequences)); // Send the sequences as Data object
            out.reset();
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Method to send the current background color to a specific client
    public void sendBackgroundColor(ObjectOutputStream out) {
        try {
            out.writeObject(new Data(backgroundColor)); // Send the background color as Data object
            out.reset();
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update all clients with the current drawing
    public void updateClientsDrawing() {
        for (int i = 0; i < clients.size(); i++) {
            sendDrawing((ObjectOutputStream)clients.get(i));
            System.out.println("Informing client " + i); // Debugging information
        }    
    }

    // Method to update all clients with the current background color
    public void updateClientsBackgroundColor() {
        for (int i = 0; i < clients.size(); i++) {
            sendBackgroundColor((ObjectOutputStream)clients.get(i));
            System.out.println("Updating background color for client " + i); // Debugging information
        }    
    }

    // Method to update all clients with the current chat
    public void updateClientsChat() {
        for (int i = 0; i < clients.size(); i++) {
            sendChat((ObjectOutputStream)clients.get(i));
            System.out.println("Updating chat for client " + i); // Debugging information
        }    
    }

    // Method to send the current chat to a specific client
    public void sendChat(ObjectOutputStream out) {
        try {
            out.writeObject(new Data(messages)); // Send the messages as Data object
            out.reset();
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a drawing sequence from a client
    public void draw(int client, Sequence s) {
        s.getClientNb(client);
        sequences.add(s);
        newDrawing = false;
    }

    // Method to add a chat message from a client
    public void chat(int client, String m) {
        messages.add(m);
    }

    // Method to undo the last drawing action by a specific client
    public void undo(int client) {
        seqIterator = sequences.listIterator(sequences.size());
        while (seqIterator.hasPrevious()) {
            current = seqIterator.previous();
            if (current.clientId == client) {
                undoStack.add(current);
                seqIterator.remove();
                return;
            }
        }
        System.out.println("Sequence list empty!"); // Debugging information
    }

    // Method to redo the last undone action by a specific client
    public void redo(int client) {
        if (newDrawing) {
            int nbSequences = undoStack.size();
            for (int i = 0; i < nbSequences; i++) {
                current = undoStack.pop();
                sequences.add(current);
            }
            newDrawing = false;
        } else {
            undoIterator = undoStack.listIterator(undoStack.size());
            while (undoIterator.hasPrevious()) {
                current = undoIterator.previous();
                if (current.clientId == client) {
                    sequences.add(current);
                    undoIterator.remove();
                    return;
                }
            }
        }
    }

    // Method to start a new drawing session
    public void startNew() {
        undoStack.clear(); // Clear the undo stack for the new session
        int nbSequences = sequences.size();
        for (int i = 0; i < nbSequences; i++) {
            current = sequences.pop();
            undoStack.add(current);
        }
        newDrawing = true;
    }

    // Method to change the background color
    public void changeBackgroundColor(Color c) {
        backgroundColor = c;
        updateClientsBackgroundColor();
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            // Initialize the server
            Server server = new Server();
            ServerSocket serverSocket = new ServerSocket(8888); // Listen on port 8888
            while (true) {
                // Wait for client connections
                System.out.println("Server waiting for client...");
                Socket s = serverSocket.accept();
                // Open output stream for the client
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                // Add the new client to the server
                server.addClient(out);
                System.out.println("Connection of client done");
                // Initialize a new thread to handle the client
                Thread t = new ThreadClient(server, s);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
