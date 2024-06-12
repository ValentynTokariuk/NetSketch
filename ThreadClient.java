import java.io.ObjectInputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
	
	//Attributs
	int numeroClient;
	Server serveur;
	Socket socketClient;
	Data data;
	boolean stop;
	
	//Constructeur	
	public ThreadClient(Server s, Socket so) {
	    serveur = s;
	    numeroClient = serveur.getLastClientNumber();
	    socketClient = so;
	}
	
	//M�thodes
	public void run() {
	    try {
	      // Ouvrir flux lecture des données envoyées par le joueur
	      ObjectInputStream in = new ObjectInputStream(socketClient.getInputStream());
	      // Boucle de jeu
	      while ( !stop ) {
	    	// Lecture de la derni�re s�quence entr�e par le client
	    	  
	    	  data = (Data) in.readObject();
	    	  switch(data.getType()) {
				case SEQUENCE:
			        Sequence s = data.getSequence();
			        serveur.draw(numeroClient, s);
			        serveur.updateClientsDrawing();
					break;
				case MESSAGE:
					String msg = data.getMessage();
					serveur.chat(numeroClient, msg);
					System.out.println("serveur recoit " + msg);
					serveur.updateClientsChat();
					break;
				case ACTION:
				  if(data.getAction()==0) {
				    serveur.undo(numeroClient);
				    serveur.updateClientsDrawing();
				  }else if(data.getAction()==1) {
				    serveur.redo(numeroClient);
				    serveur.updateClientsDrawing();
				  }else if(data.getAction()==2) {
					serveur.startNew();
					serveur.updateClientsDrawing();
				  }
				  break;
			default:
				System.err.println("Erreur Communication Client/Serveur");
				break;
				}			
	        // Petite pause...
	        sleep(100);
	      }
	    }
	    catch(Exception e) {System.err.println(e);}
	}
	

}
