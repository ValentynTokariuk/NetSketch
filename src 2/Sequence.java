import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class Sequence implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Point> points;
	public int brushSize;
	public Color brushColor;
	public int clientId = -1;

	public Sequence(int x, int y) {
		// useful for debugging
		brushSize = 0;
		brushColor = Color.BLACK;
		Point p = new Point(x,y);
		points = new ArrayList<Point>();
		points.add(p);
	}
	
	public Sequence(int t, Color c) {
		brushSize = t;
		brushColor = c;
		points = new ArrayList<Point>();
	}
	
	public Sequence() {
		// useful for debugging
		brushSize = 0;
		brushColor = Color.BLACK;
		points = new ArrayList<Point>();
	}
	
	public void addPoint(Point p) {
		points.add(p);
	}
	
	public void addPoint(int x, int y) {
		Point p = new Point(x,y);
		points.add(p);		
	}
	
	public void getClientNb(int n) {
		this.clientId = n;
	}
}
