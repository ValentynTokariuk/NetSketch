import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
	private int nbClients; // Number of clients
	private List<ObjectOutputStream> clients;
	private LinkedList<Sequence> sequences; 
	private LinkedList<Sequence> undoStack; // Stack of undone sequences
	private ArrayList<String> messages;
	private boolean newDrawing; 
	private Sequence current; // Current working sequence
	ListIterator<Sequence> seqIterator;
	ListIterator<Sequence> undoIterator;
	
	//Constructor
	public Server() {
		nbClients = 0;
		clients = new ArrayList<ObjectOutputStream>();
		sequences = new LinkedList<Sequence>();
		messages = new ArrayList<String>();
		undoStack = new LinkedList<Sequence>();
		newDrawing = true;
	}
	
	//Clients
	public void addClient(ObjectOutputStream stream) {
	    clients.add(stream);
	    nbClients++;
	    sendDrawing(stream);
	}
	
	//Drawing
	public void sendDrawing(ObjectOutputStream out) {
	    try {
	      out.writeObject(new Data(sequences));
	      out.reset();   
	      out.flush();
	   }
	    catch(IOException e) {
	      e.printStackTrace();
	   }
	}

	public void draw(int client, Sequence s) {
		s.getClientNb(client);
		sequences.add(s);
		newDrawing = false;
	}
	
	public void updateClientsDrawing() {
		for (int i=0 ; i<clients.size() ; i++) {
		    sendDrawing((ObjectOutputStream)clients.get(i));
		    System.out.println("Informing client "+ i); // useful for debugging
		}    
	}
	
	public int getLastClientNumber() {
	    return nbClients;
	}
	
	//Chat
	public void sendChat(ObjectOutputStream out) {
	    try {
	      out.writeObject(new Data(messages));
	      out.reset();   // important (Java documentation) 
	      out.flush();
	      System.out.println("Sending chat"); // useful for debugging
	   }
	    catch(IOException e) {
	      e.printStackTrace();
	   }
	}
	
	public void chat(int client, String m) {
		messages.add(m);
	}
	
	public void updateClientsChat() {
		for (int i=0 ; i<clients.size() ; i++) {
		    sendChat((ObjectOutputStream)clients.get(i));
		    System.out.println("information chat du client "+ i);  // useful for debugging
		}    
	}
	
	//Options
	public void undo(int client) {
		//Sequence s = listeSequences.removeLast();
		seqIterator=sequences.listIterator(sequences.size());
		while(seqIterator.hasPrevious()){
			current = seqIterator.previous();
			if(current.clientId==client) {
				   undoStack.add(current);
				   seqIterator.remove();
				   return;
			}
		}
		System.out.println("Sequence list empty !");
	}
	
	public void redo(int client) {
		if(newDrawing) {
			int nbSequences = undoStack.size();
			for(int i=0;i<nbSequences;i++) {
				current = undoStack.pop();
				sequences.add(current);			
			}
			newDrawing=false;
		}else {	
			undoIterator=undoStack.listIterator(undoStack.size());
			while(undoIterator.hasPrevious()){
			   current = undoIterator.previous();
			   if(current.clientId==client) {
				   sequences.add(current);
				   undoIterator.remove();
				   return;
			   }
			}
		}		
	}
	
	public void startNew() {
		undoStack.clear(); // important to make a redo after a startNew
		int nbSequences = sequences.size();
		for(int i=0;i<nbSequences;i++) {
			current = sequences.pop();
			undoStack.add(current);			
		}
		newDrawing=true;
	}

	//Main
	static public void main(String [] args){
	    try {
	      // Server initialization
	      Server server = new Server();
	      ServerSocket serverSocket = new ServerSocket(8888);
	      while (true) {
	        // Wait for client
	        System.out.println("Server waiting for client...");
	        Socket s = serverSocket.accept();
	        // Stream opening
	        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
	        // Client connection
	        server.addClient(out);
	        int numClient = server.getLastClientNumber();
	        System.out.println("Connection of client " + numClient + "done");
	        // Client thread initialization
	        Thread t = new ThreadClient(server, s);
	        t.start();
	      }
	    }
    catch(Exception e) { e.printStackTrace();}
	}
}
