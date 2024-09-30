import java.io.ObjectInputStream;
import java.net.Socket;
import java.awt.Color; // Importing Color class

public class ThreadClient extends Thread {
	
	int numeroClient;
	Server server;
	Socket socketClient;
	Data data;
	boolean stop;

	// Constructor
	public ThreadClient(Server s, Socket so) {
	    server = s;
	    numeroClient = server.getClients().size(); // Fixing method reference
	    socketClient = so;
	}

	// Run method for the thread
	public void run() {
	    try {
	        // Open input stream for reading data from the client
	        ObjectInputStream in = new ObjectInputStream(socketClient.getInputStream());

	        // Main loop to read and process data from the client
	        while (!stop) {
	            data = (Data) in.readObject();
	            switch (data.getType()) {
	                case SEQUENCE:
	                    Sequence s = data.getSequence();
	                    server.draw(numeroClient, s);
	                    server.updateClientsDrawing();
	                    break;
	                case MESSAGE:
	                    String msg = data.getMessage();
	                    server.chat(numeroClient, msg);
	                    System.out.println("server receives " + msg);
	                    server.updateClientsChat();
	                    break;
	                case ACTION:
	                    if (data.getAction() == 0) {
	                        server.undo(numeroClient);
	                        server.updateClientsDrawing();
	                    } else if (data.getAction() == 1) {
	                        server.redo(numeroClient);
	                        server.updateClientsDrawing();
	                    } else if (data.getAction() == 2) {
	                        server.startNew();
	                        server.updateClientsDrawing();
	                    }
	                    break;
	                case BACKGROUNDCOLOR:
	                    Color newColor = data.getBackgroundColor();
	                    server.changeBackgroundColor(newColor);
	                    break;
	                default:
	                    System.err.println("Error in Client/Server communication");
	                    break;
	            }			
	            sleep(100); // Small pause to prevent excessive CPU usage
	        }
	    } catch (Exception e) {
	        System.err.println(e);
	    }
	}
}
