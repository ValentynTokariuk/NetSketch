import java.io.Serializable;

public class Point implements Serializable {
    private static final long serialVersionUID = 1L;
    int x; // x-coordinate of the point
    int y; // y-coordinate of the point
    
    // Constructor to initialize point coordinates
    public Point(int a, int b) {
        x = a;
        y = b;
    }
}
