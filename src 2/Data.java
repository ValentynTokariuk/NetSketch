import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Color; // Importing Color class

public class Data implements Serializable {
    
    // Enum for the type of data being communicated
    public enum typeD {
        SEQUENCE, SEQUENCESLIST, MESSAGE, MESSAGESLIST, ACTION, BACKGROUNDCOLOR
    }
    
    private static final long serialVersionUID = 5639929285580093586L;
    
    private typeD type;                // The type of data
    private Sequence sequence;         // Single sequence
    private LinkedList<Sequence> sequences;  // List of sequences
    private String message;            // Single message
    private ArrayList<String> messages;  // List of messages
    private int action;                // Action (undo, redo, etc.)
    private Color backgroundColor;     // Background color
    
    // Constructor for a single sequence
    public Data(Sequence s) {
        type = typeD.SEQUENCE;
        sequence = s;
    }
    
    // Constructor for a list of sequences
    public Data(LinkedList<Sequence> list) {
        type = typeD.SEQUENCESLIST;
        sequences = list;
    }
    
    // Constructor for a single message
    public Data(String s) {
        type = typeD.MESSAGE;
        message = s;
    }
    
    // Constructor for a list of messages
    public Data(ArrayList<String> list) {
        type = typeD.MESSAGESLIST;
        messages = list;
    }
    
    // Constructor for an action
    public Data(int a) {
        type = typeD.ACTION;
        action = a;
    }

    // Constructor for background color
    public Data(Color c) {
        type = typeD.BACKGROUNDCOLOR;
        backgroundColor = c;
    }
    
    // Getters for the data attributes
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

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
