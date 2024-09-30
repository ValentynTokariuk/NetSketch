import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class SaveWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField clientField;
	private JTextField textField;
	private static String filename;


	public SaveWindow(JPanel panel) {
		setFont(new Font("Dialog", Font.PLAIN, 17));
		setTitle("Save as");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 428, 227);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		clientField = new JTextField();
		clientField.setBounds(156, 75, 120, 31);
		clientField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(clientField);
		clientField.setColumns(10);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBackground(SystemColor.menu);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setText("Filename");
		textField.setBounds(50, 74, 106, 32);
		contentPane.add(textField);
		
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.setBounds(283, 79, 89, 23);
		btnValidate.addActionListener(new ActionListener() { // Define what to do when filename is entered
		public void actionPerformed(ActionEvent e) {
			filename = clientField.getText();
			clientField.setText("");
			Container c = panel;
			BufferedImage im = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
			c.paint(im.getGraphics());
			try {			
				ImageIO.write(im, "PNG", new File(filename + ".jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false); 
			dispose();
		}
		});
		
		this.getRootPane().setDefaultButton(btnValidate); // Make 'Validate' as default and enable the use of keyboard's return
		contentPane.add(btnValidate);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.clientField.requestFocus(); // Set the text cursor directly in the text field
	}	
}
