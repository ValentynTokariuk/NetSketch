
import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import java.awt.Font;
import java.awt.Graphics;




public class UInterface extends JFrame implements Runnable  {

	private static final long serialVersionUID = 1L;
	
	// Client/Server attributes
	private LinkedList<Sequence> sequences;
	private Data data;
	private String msg;
	private ArrayList<String> messages;
	private String clientName;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	// UI attributes
	private DrawingPanel drawingPanel = new DrawingPanel();
	private JPanel contentPane;	
	private final JToolBar toolBar = new JToolBar();
	private final JButton btnColor = new JButton("Color");
	private final JButton btnBackground = new JButton("Fond");
	private final JButton btnNew = new JButton("New");
	private final JButton btnSend;
	private final JButton btnSizePlus = new JButton(" + ");
	private final JButton btnSizeMinus = new JButton(" - ");
	private final JTextField textSize = new JTextField("Size : ");
	private final JTextField modifySize = new JTextField(""+drawingPanel.getPointerSize());
	private final JTextArea textArea = new JTextArea();
	private final JButton btnUndo = new JButton("Undo");
	private final JButton btnRedo = new JButton("Redo");
	private final JButton btnSave = new JButton("Save");
	
	public UInterface(ObjectInputStream inp, ObjectOutputStream outp, String pseudo) {
		
	    // Reading and writing streams
	    this.in = inp;
	    this.out = outp;
	    
	    // Client name
	    this.clientName = pseudo;
	    
	    // Thread launching
	    Thread update = new Thread(this);
	    update.start();
	    
	    // Graphical initialization
	    setTitle("NetSketch - " + clientName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1031, 779);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Drawing panel
		drawingPanel.setBounds(9, 37, 725, 692);
		contentPane.add(drawingPanel);
		
		// Chat panel
		JPanel panelChat = new JPanel();
		panelChat.setBounds(744, 11, 261, 718);
		contentPane.add(panelChat);
		panelChat.setLayout(null);
		
		// Text typing area
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 593, 241, 125);
		textArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					btnSend.doClick();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					textArea.setText("");
				}				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		panelChat.add(textArea);
	
		// Chat area
		textArea.setEditable(false);
		textArea.setBounds(1, 1, 250, 558);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panelChat.add(textArea);
		
		JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 241, 539);
		panelChat.add(scrollPane);
		
		// Chat 'send' button
		btnSend = new JButton("Send ");
		btnSend.setBounds(10, 557, 241, 29);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg = textArea.getText();
				try {
					out.writeObject(new Data(clientName + " : " + msg));
					System.out.println("Client sends : " + msg); // useful for debugging 
					textArea.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		panelChat.add(btnSend);
		
		// Toolbar + show UI
		setupToolbar();
		this.setVisible(true);
	  }

	public void run() {
		try {
			while (true) {
				data = (Data) in.readObject();
				switch(data.getType()) {
				case SEQUENCESLIST:
					sequences = data.getListSeq();
					System.out.println("sequence received");
					break;
				case MESSAGESLIST:
					messages = data.getListMes();
					System.out.println("message received : " + messages);
					textArea.setText("");
					for(String s : messages) {
						this.printText(s);
					}
					break;
				default:
					System.err.println("Client-Server communication error");
					break;
				}					
				System.out.println("Waiting for the last update");
		        repaint();
		        Thread.sleep(100);
		      }
		    }
	    catch(Exception e) { e.printStackTrace();}
	  }
	
	
	// Methods
	
	// test
	public void printText(String s) {
		textArea.append(s + "\n");
	}
	
	public void sizeUpdate(int n) {
		modifySize.setText(""+n);
	}

	// Toolbar
	public void setupToolbar() {
		toolBar.setBounds(10, 0, 724, 37);
		contentPane.add(toolBar);
		
		// Pointer color
		btnColor.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnColor.setForeground(drawingPanel.pointerColor);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JColorChooser colorChooser = new JColorChooser();
				class ColorListener implements ActionListener{
					public void actionPerformed(ActionEvent e) {
						Color c = colorChooser.getColor();
						drawingPanel.pointerColor = c;
						btnColor.setForeground(c);
					}
				}
				JDialog cDialog = JColorChooser.createDialog(null, "Please choose the color of the pointer", false, colorChooser, new ColorListener(), null);
				cDialog.setVisible(true);
			}			
		});
		toolBar.add(btnColor);		
		
		// Background color
		btnBackground.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnBackground.setBackground(drawingPanel.colorBackground);
		btnBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JColorChooser backgroundChooser = new JColorChooser();
				class BackgroundListener implements ActionListener{
					public void actionPerformed(ActionEvent e) {
						Color c = backgroundChooser.getColor();
						drawingPanel.colorBackground = c;
						btnBackground.setBackground(c);
					}
				}
				JDialog cDialog = JColorChooser.createDialog(null, "Please choose the color of the background", false, backgroundChooser, new BackgroundListener(), null);
				cDialog.setVisible(true);
			}			
		});
		toolBar.add(btnBackground);
		
		// New
		btnNew.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnNew.setBackground((Color) null);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
			          out.writeObject(new Data(2));
			          drawingPanel.newSeqAcc();
				} catch(IOException e1) {
					// TODO Auto-generated catch block
			        e1.printStackTrace();
				}
			}
		});
		toolBar.add(btnNew);
		
		// Undo
		btnUndo.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnUndo.setBackground((Color) null);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(new Data(0));
					System.out.println("Client undo");
					drawingPanel.newSeqAcc();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			}
		});
		toolBar.add(btnUndo);
		
		// Redo
		btnRedo.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnRedo.setBackground((Color) null);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(new Data(1));
					System.out.println("Client redo");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(btnRedo);
		
		// Brush size
		textSize.setEditable(false);
		textSize.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textSize.setBackground((Color) null);
		toolBar.add(textSize);
	    final Dimension dimTextSize = new Dimension(toolBar.getWidth()/10, toolBar.getHeight());
	    textSize.setPreferredSize(dimTextSize);
	    textSize.setMinimumSize(dimTextSize);
	    textSize.setMaximumSize(dimTextSize);

		modifySize.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		modifySize.setBackground((Color) null);
		toolBar.add(modifySize);
	    final Dimension dimModifySize = new Dimension(toolBar.getWidth()/18, toolBar.getHeight());
	    modifySize.setPreferredSize(dimModifySize);
	    modifySize.setMinimumSize(dimModifySize);
	    modifySize.setMaximumSize(dimModifySize);
	    modifySize.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					try{
						int inputSize = Integer.parseInt(modifySize.getText());
						drawingPanel.setPointerSize(inputSize);
					}catch(NumberFormatException nfe) {}					
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					modifySize.setText("");
					sizeUpdate(drawingPanel.getPointerSize());
				}				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		btnSizePlus.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnSizePlus.setBackground((Color) null);
		btnSizePlus.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			drawingPanel.sizePlus();
			sizeUpdate(drawingPanel.getPointerSize());
			}
		});
		toolBar.add(btnSizePlus);
		
		btnSizeMinus.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnSizeMinus.setBackground((Color) null);
		btnSizeMinus.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			drawingPanel.sizeMinus();
			sizeUpdate(drawingPanel.getPointerSize());
			}
		});
		toolBar.add(btnSizeMinus);
		
		// Save
		btnSave.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnSave.setBackground((Color) null);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SaveWindow frameSaisie = new SaveWindow(drawingPanel);				
			}
		});
		toolBar.add(btnSave);	
	}
	

	
	public class DrawingPanel extends JPanel { 
		
		// Attributes
		private static final long serialVersionUID = 1L;
		private int pointerSize = 10;
		public Color pointerColor = Color.black;
		public Color colorBackground = Color.white;
		public int x = 2;
		public int y = 2;
		public Sequence seqAcc = new Sequence();
		private static final int MaxPointerSize = 100;
		
		// Constructor
		public DrawingPanel() {
			this.setPreferredSize(new Dimension(900,800));
			this.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent event) 
				{	
					seqAcc = new Sequence(pointerSize,pointerColor);
					x = event.getX();
					y = event.getY();
					seqAcc.addPoint(x,y);
					repaint();
				}
				
				@Override
				public void mouseReleased(MouseEvent event)
				{
					//listeSequences.add(seqAcc);
					try {
						out.writeObject(new Data(seqAcc));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
				}
				
			});
			this.addMouseMotionListener(new MouseMotionListener() {
				
				public void mouseDragged(MouseEvent event) {
					   x = event.getX();
					   y = event.getY();
					   seqAcc.addPoint(x,y);
					   repaint(); 
				   }

				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
			});
			repaint();
		  }
		
		// Methods
		public void newSeqAcc() {
		  seqAcc = new Sequence(pointerSize,pointerColor);
		}
		
		public void sizePlus() {
			if(pointerSize<MaxPointerSize) {
				pointerSize++;
			}
		}
		
		public void sizeMinus() {
			if(pointerSize>2) {
				pointerSize--;
			}
		}
		
		public int getPointerSize() {
			return pointerSize;
		}
		
		public boolean setPointerSize(int n) {
			if((n>1) && (n<=MaxPointerSize)) {
				pointerSize = n;
				return true;
			}else {
				return false;
			}
		}
		
		@Override
		public void paintComponent(Graphics g){
			g.setColor(colorBackground);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(Sequence s:sequences) {
				g.setColor(s.brushColor);
				int r = s.brushSize;
				for(Point p:s.points) {
					g.fillOval(p.x-r/2,p.y-r/2,r,r);
				}
			}
			for(Point p:seqAcc.points) { // To improve UX and fluidity when drawing 
				g.setColor(seqAcc.brushColor);
				int r = seqAcc.brushSize;
				g.fillOval(p.x-r/2,p.y-r/2,r,r);
			}
		}	  
	}
}
 
