import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Data implements Serializable {
	/**
	 * 
	 */
	public enum typeD{
		SEQUENCE, SEQUENCESLIST, MESSAGE, MESSAGESLIST, ACTION
	}
	
	private static final long serialVersionUID = 5639929285580093586L;
	
	private typeD type;
	private Sequence sequence;
	private LinkedList<Sequence> sequences;
	private String message;
	private ArrayList<String> messages;
	private int action;
	
	public Data(Sequence s) {
		type = typeD.SEQUENCE;
		sequence = s;
	}
	
	public Data(LinkedList<Sequence> list) {
		type = typeD.SEQUENCESLIST;
		sequences = list;
	}
	
	public Data(String s) {
		type = typeD.MESSAGE;
		message = s;
	}
	
	public Data(ArrayList<String> list) {
		type = typeD.MESSAGESLIST;
		messages = list;
	}
	
	public Data(int a) {
	    type = typeD.ACTION;
	    action = a;
	}
	
	//Getters
	public typeD getType() {
		return type;
	}

	public Sequence getSequence() {
		return sequence;
	}


	public LinkedList<Sequence> getListSeq() {
		return sequences;
	}


	public String getMessage() {
		return message;
	}


	public ArrayList<String> getListMes() {
		return messages;
	}


	public int getAction() {
		return action;
	}
}


