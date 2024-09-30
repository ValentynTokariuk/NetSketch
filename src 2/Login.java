import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField textField;
	private JTextField txtLogin;
	private static InetAddress adressIP = null;
	private static String pseudo;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Login frame = new Login();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.textField.requestFocus(); // Set the text cursor directly in the text field
		try {
			if (args.length >0) {
				// If there is at least one argument, take the address of the machine in args
				adressIP = InetAddress.getByName(args[0]);
		      	}
			else {
				// Else, connect 
		        adressIP = InetAddress.getLocalHost();
		      }
		    } 
		    catch (Exception e) {
		    }
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setFont(new Font("Dialog", Font.PLAIN, 17));
		setTitle("NetSketch connexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 428, 227);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(156, 75, 120, 31);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPanel.add(textField);
		textField.setColumns(10);
		
		
		txtLogin = new JTextField();
		txtLogin.setEditable(false);
		txtLogin.setBackground(SystemColor.menu);
		txtLogin.setHorizontalAlignment(SwingConstants.CENTER);
		txtLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtLogin.setText("Login");
		txtLogin.setBounds(50, 74, 96, 32);
		contentPanel.add(txtLogin);
		txtLogin.setColumns(10);
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.setBounds(283, 79, 89, 23);
		btnValidate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pseudo = textField.getText();
			textField.setText("");
			try {
			// Socket opening on port 8888
			Socket client = new Socket(adressIP, 8888);
			// Writing stream opening on socket
			ObjectOutputStream writing = new ObjectOutputStream(client.getOutputStream());
			// Reading stream opening on socket
			ObjectInputStream reading = new ObjectInputStream(client.getInputStream());
		    // UI instantiation
		    UInterface f = new UInterface(reading, writing,pseudo);
		    f.setVisible(true);
		    setVisible(false);
			}catch(Exception e1) {
				System.err.println(e1);
			}
		}
		});
		this.getRootPane().setDefaultButton(btnValidate); // Make 'Validate' as default button to enable using Return on keyboard
		contentPanel.add(btnValidate);
	}	
}
