import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;



public class GUI {
	
	private int textNum = 0; // Number of texts
	private int sepNum = 0; // Number of separators
	private JFrame frame; //main window
	private JButton save, undo, changeFileNames, credits; // Various buttons
//	private JTextField texts[]; //The component in which to write our text
//	private JLabel labels[];
//	private String[] strings; //The text to write on the files
//	private String[] fileNames; // The prefered names for the files
//	private ArrayList<String> filePaths;
	private ArrayList<TFCComponent> components;
	private BufferedReader bufR;
	private String workingFileDirectory = "";
	private String configName = "config.tfc";
	private JLabel imgLabel;
	private ImageIcon icon;
	private JPanel pn_MAIN; //Container for all the components
	private JPanel pn_buttons;
	private JPanel pn_optionButtons;
	private JPanel pn_empty;
	private JPanel pn_textFields;
	private JPanel pn_center;
	private JPanel pn_logo;
	private File debug = new File("Debug.txt");
	private int posX = 50, posY = 50;
	private String lastFileAdded;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public GUI() {
		File f = new File(configName);
		if(!f.exists()) { // If the file config.cfg doesn't exist, it's created with default values
			File tempFile = new File("teste.txt");
			PrintWriter createFile;
			try { // Trying to create a file in the folder of the executable
				createFile = new PrintWriter(tempFile, "UTF-8");
				createFile.write("");
				createFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(tempFile.exists()) { // If the creation was successful
				try {
					tempFile.delete();
					String temp = new File(GUI.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					temp = temp.replace("\\Text File Creator.jar", "");
					temp = temp.replace("\\Text File Creator.exe", "");
					temp = temp.replace("\\bin", "");
					
					createFile = new PrintWriter(f, "UTF-8");
					createFile.write("File*" + temp + "\\text1.txt" + System.lineSeparator() +
							"File*" + temp + "\\text2.txt" + System.lineSeparator() +
							"File*" + temp + "\\text3.txt*?" + System.lineSeparator() +
							"Pos*" + (int)(screenSize.getWidth()/2 - (screenSize.getWidth()/5)) + "*" + (int)(screenSize.getHeight()/2 - screenSize.getHeight()/5) + System.lineSeparator());
					createFile.close();
					createFile = new PrintWriter(new File("text1.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
					createFile = new PrintWriter(new File("text2.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
					createFile = new PrintWriter(new File("text3.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
				} catch (IOException | URISyntaxException e) {
					printError(e, "Criação dos ficheiros ");
					JOptionPane.showMessageDialog(null, "Error creating files (The program might not have permission to create the files in the directory)");
					e.printStackTrace();
				}
			} else { // If the creation wasn't successful
				try {
					String appDataPath = System.getenv("APPDATA");
					appDataPath = appDataPath + "\\TFC";
					File directory = new File(appDataPath);
					if(!directory.exists()) {
						boolean b = directory.mkdir();
						if(!b) {
							JOptionPane.showMessageDialog(null, "Error creating directory in AppData");
							System.out.println("Error creating directory in AppData");
							System.exit(10);
						}
					}
					
					// Verify if a config.tfc file already exists in the directory
					try (Stream<Path> walk = Files.walk(Paths.get("C:\\projects"))) {

						List<String> result = walk.map(x -> x.toString())
								.filter(fi -> fi.endsWith("config.tfc")).collect(Collectors.toList());

						result.forEach(System.out::println);
						
						if(!result.isEmpty()) { // if the file exists, save it in the configName String, initialize the Main frame and skip creating the config and text files
							configName = result.get(0);
							initializeMainFrame();
							return;
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					tempFile = new File(appDataPath + "\\teste.txt");
					createFile = new PrintWriter(tempFile, "UTF-8");
					createFile.write("");
					createFile.close();
					if(!tempFile.exists()) {
						JOptionPane.showMessageDialog(null, "Error creating files in AppData");
						System.out.println("Error creating files in AppData");
						System.exit(11);
					}
					tempFile.delete();
					workingFileDirectory = appDataPath;
					String temp = appDataPath;
					configName = workingFileDirectory + "\\" + configName;
					f = new File(configName);
					debug = new File(workingFileDirectory + "\\" + "Debug.txt");
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					temp = temp.replace("\\Text File Creator.jar", "");
					temp = temp.replace("\\Text File Creator.exe", "");
					temp = temp.replace("\\bin", "");
					
					createFile = new PrintWriter(f, "UTF-8");
					createFile.write("File*" + temp + "\\text1.txt" + System.lineSeparator() +
							"File*" + temp + "\\text2.txt" + System.lineSeparator() +
							"File*" + temp + "\\text3.txt*?" + System.lineSeparator() +
							"Pos*" + (int)(screenSize.getWidth()/2 - (screenSize.getWidth()/5)) + "*" + (int)(screenSize.getHeight()/2 - screenSize.getHeight()/5) + System.lineSeparator());
					createFile.close();
					createFile = new PrintWriter(new File(temp + "\\" + "text1.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
					createFile = new PrintWriter(new File(temp + "\\" + "text2.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
					createFile = new PrintWriter(new File(temp + "\\" + "text3.txt"), "UTF-8");
					createFile.write("");
					createFile.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		initializeMainFrame();

	}
	
	public GUI(String arg0) {
		configName = arg0;
		initializeMainFrame();
	}
	
	private void initializeMainFrame() {
		try {
			textNum = 0;
			sepNum = 0;
			frame = new JFrame(configName);
			List<Image> icons = new ArrayList<>();
			icons.add(ImageIO.read(new File("images/Text File Creator.png")));
			frame.setIconImages(icons);
			frame.getContentPane().setForeground(SystemColor.control);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addComponents();
			BufferedImage bimg = ImageIO.read(new File("images/logo.png"));
			frame.setBounds(posX, posY, Math.max(500, bimg.getWidth() + 20), 105 + bimg.getHeight() + 50 * textNum + 61 * sepNum);
			frame.setVisible(true);
			
		} catch (IOException e) {
			printError(e, "initializeMainFrame() (Can be the images folder missing)");
			e.printStackTrace();
		}
	}
	

	public void addComponents() {
		initializeArrays();
		
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	JDialog d = new JDialog(frame, "Close Program", true);
	        	d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        	d.setLayout(new GridLayout(0, 1));
	        	
	        	JPanel contentPane1 = (JPanel) d.getContentPane();
	    	    int condition1 = JComponent.WHEN_IN_FOCUSED_WINDOW;
	    	    InputMap inputMap1 = contentPane1.getInputMap(condition1);
	    	    ActionMap actionMap1 = contentPane1.getActionMap();
	        	
	        	String enter1 = "enter";
	    	    inputMap1.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter1);
	    	    actionMap1.put(enter1, new AbstractAction() {

	    	        /**
	    			 * 
	    			 */
	    			private static final long serialVersionUID = 3531215686146911120L;

	    			@Override
	    	        public void actionPerformed(ActionEvent arg0) {
	    	        	setPosInConfig();
	    	        	System.exit(0);
	    	        }
	    	    });
	        	
	        	JPanel d_north = new JPanel(), d_south = new JPanel();
	        	d.add(d_north);
	        	d.add(d_south);
	        	
	        	JLabel label = new JLabel("Are you sure you want to exit the program?");
	        	d_north.add(label);
	        	
	        	JButton yes = new JButton("Yes");
	        	d_south.add(yes);
	        	yes.addActionListener(new ActionListener() {
	        		
	        		@Override
	        		public void actionPerformed(ActionEvent arg0) {
	        			setPosInConfig();
	        			System.exit(0);
	        		}
	        		
	        	});
	        	
	        	JButton no = new JButton("No");
	        	d_south.add(no);
	        	no.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setPosInConfig();
						d.dispose();
					}
	        		
	        	});
	        	
	        	d.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/4),
						 (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
						 300, 120);
	        	d.setVisible(true);
		        setPosInConfig();
		    }
		});
		
		JPanel contentPane = (JPanel) frame.getContentPane();
	    int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
	    InputMap inputMap = contentPane.getInputMap(condition);
	    ActionMap actionMap = contentPane.getActionMap();

	    String enter = "enter";
	    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
	    actionMap.put(enter, new AbstractAction() {

	        /**
			 * 
			 */
			private static final long serialVersionUID = 3531215686146911120L;

			@Override
	        public void actionPerformed(ActionEvent arg0) {
	        	setPosInConfig();
				
				for(TFCComponent comp: components) {
					if(comp.getClass().equals(new TFCText().getClass())) {
						TFCText temp = (TFCText) comp;
						temp.saveTextChanges();
						if(!temp.wasChanged()) {
							continue;
						}
						try {
							PrintWriter pw = new PrintWriter(new File(temp.getPath()), "UTF-8");
							pw.write(temp.getText());
							pw.close();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Algo correu mal com o path do ficheiro " + temp.getName());
							printError(e, "Main frame button to save all the texts ");
							e.printStackTrace();
						}
					}
				}
	        }
	    });
	    
	    String esc = "escape";
	    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), esc);
	    actionMap.put(esc, new AbstractAction() {

	        /**
			 * 
			 */
			private static final long serialVersionUID = 7677328953922863724L;

			@Override
	        public void actionPerformed(ActionEvent arg0) {
	        	JDialog d = new JDialog(frame, "Close Program", true);
	        	d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        	d.setLayout(new GridLayout(0, 1));
	        	
	        	JPanel d_north = new JPanel(), d_south = new JPanel();
	        	d.add(d_north);
	        	d.add(d_south);
	        	
	        	JLabel label = new JLabel("Are you sure you want to exit the program?");
	        	d_north.add(label);
	        	
	        	JPanel contentPane2 = (JPanel) d.getContentPane();
	    	    int condition2 = JComponent.WHEN_IN_FOCUSED_WINDOW;
	    	    InputMap inputMap2 = contentPane2.getInputMap(condition2);
	    	    ActionMap actionMap2 = contentPane2.getActionMap();
	        	
	        	String enter1 = "enter";
	    	    inputMap2.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter1);
	    	    actionMap2.put(enter1, new AbstractAction() {

	    	        /**
	    			 * 
	    			 */
	    			private static final long serialVersionUID = 3531215686146911120L;

	    			@Override
	    	        public void actionPerformed(ActionEvent arg0) {
	    	        	setPosInConfig();
	    	        	System.exit(0);
	    	        }
	    	    });
	        	
	        	JButton yes = new JButton("Yes");
	        	d_south.add(yes);
	        	yes.addActionListener(new ActionListener() {
	        		
	        		@Override
	        		public void actionPerformed(ActionEvent arg0) {
	        			setPosInConfig();
	        			System.exit(0);
	        		}
	        		
	        	});
	        	
	        	JButton no = new JButton("No");
	        	d_south.add(no);
	        	no.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						d.dispose();
					}
	        		
	        	});
	        	
	        	d.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/4),
						 (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
						 300, 120);
	        	d.setVisible(true);
	        }
	    });
		
		pn_MAIN = new JPanel();
		pn_MAIN.setBorder(new LineBorder(new Color(238, 238, 238), 10));
		
		pn_MAIN.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pn_MAIN.setLayout(new BorderLayout(0, 0));
		
		pn_logo = new JPanel();
		pn_MAIN.add(pn_logo, BorderLayout.NORTH);
		pn_logo.setLayout(new BorderLayout(0, 0));
		try {
			BufferedImage img = ImageIO.read(new File("images/logo.png"));
			icon = new ImageIcon(img);
			imgLabel = new JLabel(icon);
		} catch (IOException e2) {
			printError(e2, "Adding the image ");
			e2.printStackTrace();
		}
		pn_logo.add(imgLabel, BorderLayout.WEST);
		
		
		
		pn_center = new JPanel();
		pn_MAIN.add(pn_center, BorderLayout.CENTER);
		pn_center.setLayout(new BorderLayout(0, 0));
		
		
		pn_textFields = new JPanel();
		pn_center.add(pn_textFields, BorderLayout.CENTER);
		pn_textFields.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		pn_empty = new JPanel();
		pn_center.add(pn_empty, BorderLayout.SOUTH);
		
		// Adding the texts and separators to the main panel
		for(TFCComponent comp: components) {
			comp.dropComponents(pn_textFields);
			comp.getNameLabel().setBackground(new Color(240, 240, 240));
		}
		
		pn_buttons = new JPanel();
		pn_MAIN.add(pn_buttons, BorderLayout.SOUTH);
		pn_buttons.setLayout(new GridLayout(0, 2, 0, 0));
		
		pn_optionButtons = new JPanel();
		pn_buttons.add(pn_optionButtons);
		
		credits = new JButton("Credits"); // Main frame credits button
		pn_optionButtons.add(credits);
		credits.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame creditsFrame = new JFrame("Credits");
				creditsFrame.getContentPane().setLayout(new GridLayout(0, 1));
				creditsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				JLabel creditsLabel1 = new JLabel("Aplication developed by André Garcia");
				creditsLabel1.setHorizontalAlignment(JLabel.CENTER);
				creditsLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
				JLabel creditsLabel2 = new JLabel("Special thanks to Luis Garcia");
				creditsLabel2.setHorizontalAlignment(JLabel.CENTER);
				creditsLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
				
				JPanel ai = new JPanel();
				ai.setLayout(new GridLayout(0, 1));
				
				JLabel creditsLabel3 = new JLabel("Made with Java Swing, 2020");
				creditsLabel3.setHorizontalAlignment(JLabel.CENTER);
				creditsLabel3.setFont(new Font("Arial", Font.PLAIN, 12));
				JLabel creditsLabel4 = new JLabel("Version 1.2.0 Beta");
				creditsLabel4.setHorizontalAlignment(JLabel.CENTER);
				creditsLabel4.setFont(new Font("Arial", Font.PLAIN, 12));
				
				creditsFrame.getContentPane().add(new JLabel());
				creditsFrame.getContentPane().add(creditsLabel1);
				creditsFrame.getContentPane().add(new JLabel());
				creditsFrame.getContentPane().add(creditsLabel2);
				creditsFrame.getContentPane().add(new JLabel());
				creditsFrame.add(ai);
				creditsFrame.getContentPane().add(new JLabel());
				
				ai.add(creditsLabel3);
				ai.add(creditsLabel4);
				
				creditsFrame.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/2) - 50,
									   (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
									   300, 200);
				creditsFrame.setVisible(true);
			}
			
		});
		
		undo = new JButton("Undo"); // Main frame Undo button
		pn_optionButtons.add(undo);
		undo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JDialog d = new JDialog(frame, "Close Program", true);
	        	d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        	d.setLayout(new GridLayout(0, 1));
	        	
	        	JPanel d_north = new JPanel(), d_south = new JPanel();
	        	d.add(d_north);
	        	d.add(d_south);
	        	
	        	JLabel label = new JLabel("Are you sure you want to undo?");
	        	d_north.add(label);
	        	
	        	JButton yes = new JButton("Yes");
	        	d_south.add(yes);
	        	yes.addActionListener(new ActionListener() {
	        		
	        		@Override
	        		public void actionPerformed(ActionEvent arg0) {
	        			setPosInConfig();
	        			for(TFCComponent comp: components) {
	        				if(comp.getClass().equals(new TFCText().getClass())) {
	        					TFCText temp = (TFCText)comp;
	        					temp.resetText();
	        				}
	        			}
	        			d.dispose();
	        		}
	        		
	        	});
	        	
	        	JButton no = new JButton("No");
	        	d_south.add(no);
	        	no.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						d.dispose();
					}
	        		
	        	});
	        	
	        	d.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/4),
						 (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
						 250, 120);
	        	d.setVisible(true);
			}
			
		});
		
		changeFileNames = new JButton("Files"); // Main frame Files button
		pn_optionButtons.add(changeFileNames);
		changeFileNames.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPosInConfig();
				JDialog filesFrame = new JDialog(frame, "File Names", true);
				filesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				JPanel newPn_MAIN = new JPanel();
				newPn_MAIN.setLayout(new BorderLayout());
				newPn_MAIN.setBorder(new LineBorder(new Color(238, 238, 238), 10));
				
				JPanel newPn_desc = new JPanel();
				newPn_desc.setLayout(new GridLayout(1, 0));
				newPn_desc.setSize(300, 45);
				newPn_MAIN.add(newPn_desc, BorderLayout.NORTH);
				
				JPanel newPn_names = new JPanel();
				newPn_names.setLayout(new GridLayout(0, 1));
				newPn_names.setSize(300, 45*textNum);
				newPn_MAIN.add(newPn_names, BorderLayout.CENTER);
				
				JPanel newPn_buttons = new JPanel();
				newPn_buttons.setLayout(new GridLayout(0, 2, 5, 0));
				newPn_MAIN.add(newPn_buttons, BorderLayout.SOUTH);
				
				JPanel newPn_controlButtons = new JPanel();
				newPn_controlButtons.setLayout(new GridLayout(0, 1, 0, 5));
				newPn_buttons.add(newPn_controlButtons);
				
				JTextField txt = new JTextField("Text Files");
				txt.setEditable(false);
				txt.setBackground(new Color(138, 235, 127));
				txt.setHorizontalAlignment(JTextField.CENTER);
				newPn_desc.add(txt);
				JTextField sep = new JTextField("Separators");
				sep.setEditable(false);
				sep.setBackground(new Color(84, 169, 227));
				sep.setHorizontalAlignment(JTextField.CENTER);
				newPn_desc.add(sep);
				
				newPn_names.add(new JLabel());
				ArrayList<JTextField> names = new ArrayList<>();
				JTextField name;
				for(TFCComponent comp: components) {
					if(comp.getClass().equals(new TFCText().getClass())) {
						TFCText temp = (TFCText) comp;
						name = new JTextField(temp.getName());
						name.setBackground(new Color(138, 235, 127));
					} else {
						TFCSeparator temp = (TFCSeparator) comp;
						name = new JTextField(temp.getName());
						name.setBackground(new Color(84, 169, 227));
					}
					names.add(name);
					newPn_names.add(name);
				}
				JLabel l = new JLabel();
				l.setMaximumSize(new Dimension(0, 20));
				newPn_names.add(new JLabel());
				
				JButton addText = new JButton("Add New"); // "Files Frame" add new button
				newPn_controlButtons.add(addText);
				addText.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JDialog addNewFrame = new JDialog(filesFrame, "Add new", true);
						addNewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						JPanel anPn_MAIN = new JPanel();
						addNewFrame.add(anPn_MAIN);
						anPn_MAIN.setLayout(new GridLayout(0, 1, 5, 5));
						
						JPanel d_north = new JPanel(), d_south = new JPanel();
						anPn_MAIN.add(d_north);
						anPn_MAIN.add(d_south);
			        	
			        	JLabel label = new JLabel("Do you want a new Text File or Separator?");
			        	d_north.add(label);
			        	
			        	JButton textFile = new JButton("Text File"); // "Add new file Frame" add new file button
			        	d_south.add(textFile);
			        	textFile.addActionListener(new ActionListener() {
			        		
			        		@Override
			        		public void actionPerformed(ActionEvent arg0) {
								JFileChooser chooser = new JFileChooser();
								chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
								if(lastFileAdded != null)
									chooser.setCurrentDirectory(new File(lastFileAdded));
								chooser.setFileFilter(new FileNameExtensionFilter("Text File (.txt)","txt"));
								String fileName = "";
								
								
								if(chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) {
								    fileName = chooser.getSelectedFile().toString();
								} else {
									return;
								}
								
								
								if(chooser.getFileFilter().getDescription().equals("Text File (.txt)")) {
									if (!fileName .endsWith(".txt")) {
										for(TFCComponent comp: components) {
											if(comp.getClass().equals(new TFCText().getClass())) {
												if(comp.getName().equals(new File(fileName).getName())) {
													JOptionPane.showMessageDialog(addNewFrame, "A file with this name already exists, please choose another");
													return;
												}
											}
										}
										fileName += ".txt";
									} else {
										for(TFCComponent comp: components) {
											if(comp.getClass().equals(new TFCText().getClass())) {
												String name = new File(fileName).getName();
												if(comp.getName().equals(name.substring(0, name.length() - 4))) {
													JOptionPane.showMessageDialog(addNewFrame, "A file with this name already exists, please choose another");
													return;
												}
											}
										}
									}
								} else if(chooser.getFileFilter().getDescription().equals("All Files")) {
									if (fileName.endsWith(".txt")) {
										for(TFCComponent comp: components) {
											if(comp.getClass().equals(new TFCText().getClass())) {
												String name = new File(fileName).getName();
												if(comp.getName().equals(name.substring(0, name.length() - 4))) {
													JOptionPane.showMessageDialog(addNewFrame, "A file with this name already exists, please choose another");
													return;
												}
											}
										}
									} else {
										if(fileName.endsWith(".")) {
											JOptionPane.showMessageDialog(addNewFrame, "Invalid name, can't end with \".\"");
											return;
										}
										File file = new File(fileName);
										if(!file.getName().contains(".")) {
											for(TFCComponent comp: components) {
												if(comp.getClass().equals(new TFCText().getClass())) {
													if(comp.getName().equals(new File(fileName).getName())) {
														JOptionPane.showMessageDialog(addNewFrame, "A file with this name already exists, please choose another");
														return;
													}
												}
											}
											fileName += ".txt";
										} else {
											String name = file.getName().substring(0, file.getName().lastIndexOf("."));
											for(TFCComponent comp: components) {
												if(comp.getClass().equals(new TFCText().getClass())) {
													if(comp.getName().equals(name)) {
														JOptionPane.showMessageDialog(addNewFrame, "A file with this name already exists, please choose another");
														return;
													}
												}
											}
										}
									}
							    }
								
								for(TFCComponent comp: components) {
									if(comp.getName().equals(fileName)) {
										JOptionPane.showMessageDialog(filesFrame, "That file name already exists, please choose another one");
										return;
									}
								}
								
								File f = new File(fileName);
								String path = f.getAbsolutePath();
								
								
								
								try {
									// Creating the new file if it doesn't already exist
									PrintWriter pw;
									if(!new File(path).exists()) {
										pw = new PrintWriter(new File(path), "UTF-8");
										pw.write("");
										pw.close();
									}
									
									// Addind the file path to the config file
									BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
									String content = "", line = br.readLine();
									while (line != null) {
										if(line.contains("*?")) {
											line = line.replace("*?", "");
										}
										content = content + line + System.lineSeparator();
										line = br.readLine();
									}
									content = content + "File*" + path + "*?" + System.lineSeparator();
									br.close();
									pw = new PrintWriter(new File(configName), "UTF-8");
									pw.write(content);
									pw.close();
									
//									smallFrame.dispose();
//									filesFrame.dispose();
									
									
								} catch (IOException e1) {
									printError(e1, "Creating a new text file ");
									e1.printStackTrace();
								}
								
								String num = JOptionPane.showInputDialog("Which position? (1 to " + (int)(components.size() + 1) + ")");
								
								try {
							        int newPos = Integer.parseInt(num);
							        System.out.println("New pos is: " + num);
							        if(newPos > components.size()+1 || newPos < 1) {
							        	JOptionPane.showMessageDialog(addNewFrame, "The number selected is not on the given range");
							        }
							        if(newPos != components.size()+1) {
								        // Changing the order of the files in the config file
								        bufR = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
								        String line = bufR.readLine(), content = "";
								        int i = 1;
								        System.out.println("Starting...");
								        while(line != null) {
								        	System.out.println("line is: " + line + "\ni is: " + i);
								        	if(!line.startsWith("Separator*") && !line.startsWith("File*")) {
								        		System.out.println("It's a pos, adding and moving on");
								        		content = content + line + System.lineSeparator();
								        		line = bufR.readLine();
								        		continue;
								        	}
								        	if(i == newPos) {
								        		System.out.println("This is the position we want to put it in, putting the line to order");
								        		content = content + "File*" + path + System.lineSeparator();
								        	}
								        	if(line.equals("File*" + path) || line.equals("File*" + path + "*?")) {
								        		System.out.println("This is the new line, we got to the end, we can finish");
								        		break;
								        	}
								        	System.out.println("Normal line");
							        		content = content + line + System.lineSeparator();
							        		line = bufR.readLine();
								        	i++;
								        }
								        
								        PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
								        pw.write(content);
								        pw.close();
								        bufR.close();
									}
							        frame.dispose();
									filesFrame.dispose();
									initializeMainFrame();
							        
							    } catch (NumberFormatException nfe) {
							    	JOptionPane.showMessageDialog(addNewFrame, "You didnt input a number");
									return;
							    } catch (IOException e1) {
							    	e1.printStackTrace();
							    }
			        		}
			        		
			        	});
			        	
			        	JButton separator = new JButton("Separator"); // "Add new file Frame" add new separator button
			        	d_south.add(separator);
			        	separator.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								String answer = JOptionPane.showInputDialog(addNewFrame, "What's the name of the new Separator?");
								if(answer.equals("")) {
									JOptionPane.showMessageDialog(addNewFrame, "Name can't be empty!");
									return;
								}
								try {
									bufR = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
									String line = bufR.readLine(), content = "";
									while(line != null) {
										content = content + line + System.lineSeparator();
										line = bufR.readLine();
									}
									answer = answer.toUpperCase();
									content = content + "Separator*" + answer;
									PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
									pw.write(content);
									pw.close();
									bufR.close();
//									frame.dispose();
//									filesFrame.dispose();
//									addNewFrame.dispose();
//									initializeMainFrame();
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								String num = JOptionPane.showInputDialog("Which position? (1 to " + (int)(components.size() + 1) + ")");
								
								try {
							        int newPos = Integer.parseInt(num);
							        System.out.println("New pos is: " + num);
							        if(newPos > components.size()+1 || newPos < 1) {
							        	JOptionPane.showMessageDialog(addNewFrame, "The number selected is not on the given range");
							        }
							        if(newPos != components.size()+1) {
								        // Changing the order of the files in the config file
								        bufR = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
								        String line = bufR.readLine(), content = "";
								        int i = 1;
								        System.out.println("Starting...");
								        while(line != null) {
								        	System.out.println("line is: " + line + "\ni is: " + i);
								        	if(!line.startsWith("Separator*") && !line.startsWith("File*")) {
								        		System.out.println("It's a pos, adding and moving on");
								        		content = content + line + System.lineSeparator();
								        		line = bufR.readLine();
								        		continue;
								        	}
								        	if(i == newPos) {
								        		System.out.println("This is the position we want to put it in, putting the line to order");
								        		content = content + "Separator*" + answer + System.lineSeparator();
								        	}
								        	if(line.equals("Separator*" + answer)) {
								        		System.out.println("This is the new line, we got to the end, we can finish");
								        		break;
								        	}
								        	System.out.println("Normal line");
							        		content = content + line + System.lineSeparator();
							        		line = bufR.readLine();
								        	i++;
								        }
								        
								        PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
								        pw.write(content);
								        pw.close();
								        bufR.close();
									}
							        frame.dispose();
									filesFrame.dispose();
									initializeMainFrame();
							        
							    } catch (NumberFormatException nfe) {
							    	JOptionPane.showMessageDialog(addNewFrame, "You didnt input a number");
									return;
							    } catch (IOException e1) {
							    	e1.printStackTrace();
							    }
								
							}
			        		
			        	});
			        	
			        	JButton cancel = new JButton("Cancel");
			        	d_south.add(cancel);
			        	cancel.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								setPosInConfig();
								addNewFrame.dispose();
							}
			        		
			        	});
			        	
			        	
			        	
			        	
			        	addNewFrame.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/4),
								 (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
								 300, 120);
			        	addNewFrame.setVisible(true);
				        setPosInConfig();
					}
					
				});
				
				JButton removeText = new JButton("Remove"); // "Files" frame Remove button
				newPn_controlButtons.add(removeText);
				removeText.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JDialog removeFrame = new JDialog(filesFrame, "Choose the file(s) to remove", true);
						removeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						JPanel rmPn_MAIN = new JPanel();
						removeFrame.add(rmPn_MAIN);
						rmPn_MAIN.setLayout(new GridLayout(0, 1, 5, 5));
						rmPn_MAIN.setBorder(new LineBorder(new Color(238, 238, 238), 10));
						
						ArrayList<JCheckBox> rb = new ArrayList<>();
						for(TFCComponent comp: components) {
							JCheckBox check = new JCheckBox(comp.getName());
							rmPn_MAIN.add(check);
							rb.add(check);
						}
						
						
						JButton rButton = new JButton("Remove"); // "Remove Frame" remove button
						rmPn_MAIN.add(rButton);
						rButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								ArrayList<String> rLines = new ArrayList<>();
								boolean lastFileAddedWasRemoved = false;
								
								int i = 0;
								for(JCheckBox cb: rb) {
									System.out.println("Check box: " + cb.getText());
									if(cb.isSelected()) {
										System.out.println("It was selected");
										TFCComponent comp = components.get(i);
										System.out.println("Got comp: " + comp);
										if(comp.getClass().equals(new TFCSeparator().getClass())) {
											System.out.println("It's a separator");
											TFCSeparator temp = (TFCSeparator) comp;
											rLines.add(temp.getInFileName());
											System.out.println("Added to rPaths: " + temp.getInFileName());
										} else if(comp.getClass().equals(new TFCText().getClass())) {
											System.out.println("It's a text");
											TFCText temp = (TFCText) comp;
											rLines.add("File*" + temp.getPath());
											System.out.println("Added to rPaths: File*" + temp.getPath());
										}
									}
									i++;
								}
								// Deleting the file in the system
								System.out.println("Gonna delete file if is text");
								for(String s: rLines) {
									System.out.println("String: " + s);
									if(!s.startsWith("Separator*")) {
										System.out.println("It's a text");
										File f = new File(s.replace("File*", ""));
										boolean b = f.delete();
										System.out.println("Deleted " + s + " = " + b);
									}
								}
								
								// Deleting the path from the config file
								System.out.println("Gonna delete line from config");
								try {	
									BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
									String content1 = "", line1 = br1.readLine();
									boolean toAdd = true, isFirst = true;
									System.out.println("Gonna add line or not");
									while (line1 != null) {
										System.out.println("Line is: " + line1);
										if(!line1.startsWith("Pos*")) {
											System.out.println("It's NOT a pos");
											if(rLines.contains(line1) || rLines.contains(line1.replace("*?", ""))) {
												System.out.println("It's one to remove");
												toAdd = false;
												rLines.remove(line1);
												rLines.remove(line1.replace("*?", ""));
											}
											
											if(toAdd) {
												System.out.println("Gonna add this one");
												if(line1.startsWith("File*") && lastFileAddedWasRemoved && isFirst) {
													System.out.println("The last file added was removed, putting *? on this one");
													line1 = line1 + "*?";
													isFirst = false;
												}
												content1 = content1 + line1 + System.lineSeparator();
												System.out.println("Added the line");
											} else {
												System.out.println("Line not added");
												toAdd = true;
											}
										} else {
											System.out.println("It's a pos, adding it");
											content1 = content1 + line1 + System.lineSeparator();
										}
										line1 = br1.readLine();
									}
									br1.close();
									PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
									pw.write(content1);
									pw.close();
								} catch(IOException e1) {
									printError(e1, "Deleting the path from the config file ");
									e1.printStackTrace();
								}
								
								removeFrame.dispose();
								filesFrame.dispose();
								frame.dispose();
								initializeMainFrame();
							}
							
						});
						removeFrame.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/2) - 50,
								  			  (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
								  			300, 90 + 30*textNum + 30*sepNum);
						removeFrame.setVisible(true);
					}
				});
				
				JButton orderText = new JButton("Order"); // "Files frame" Order button
				newPn_controlButtons.add(orderText);
				orderText.addActionListener(new ActionListener() {

					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JDialog orderFrame = new JDialog(filesFrame, "Choose the component to move", true);
						orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						JPanel orPn_MAIN = new JPanel(), orPn_north = new JPanel();
						orPn_MAIN.setLayout(new BorderLayout());
						orPn_MAIN.setBorder(new LineBorder(new Color(238, 238, 238), 10));
						orderFrame.getContentPane().add(orPn_MAIN);
						orPn_MAIN.add(orPn_north, BorderLayout.CENTER);
						orPn_north.setLayout(new GridLayout(0, 1, 0, 5));
						
//						JPanel orPn_left = new JPanel(), orPn_right = new JPanel();
//						orPn_right.setLayout(new GridLayout(0, 1, 0, 5));
//						orPn_left.setLayout(new GridLayout(0, 1, 0, 5));
//						orPn_north.add(orPn_left, BorderLayout.CENTER);
//						orPn_north.add(orPn_right, BorderLayout.EAST);
						
						ArrayList<JRadioButton> rbs = new ArrayList<>();
						ButtonGroup group = new ButtonGroup();
						
						for(TFCComponent comp: components) {
//							orPn_left.add(new JLabel(i + ": " + comp.getName()));
//							orPn_right.add(new JTextField(Integer.toString(i)));
							if(comp.getClass().equals(new TFCText().getClass())) {
								JRadioButton rb = new JRadioButton(comp.getName());
								group.add(rb);
								rbs.add(rb);
								orPn_north.add(rb);
							} else if(comp.getClass().equals(new TFCSeparator().getClass())) {
								JRadioButton rb = new JRadioButton(comp.getName() + "   (sep)");
								group.add(rb);
								rbs.add(rb);
								orPn_north.add(rb);
							}
						}
						orPn_north.add(new JLabel());
						
						JButton submit = new JButton("Submit");
						orPn_MAIN.add(submit, BorderLayout.SOUTH);
						submit.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								TFCComponent comp = null;
								int i = 0;
								for(JRadioButton b: rbs) {
									if(b.isSelected()) {
										comp = components.get(i);
										System.out.println("Chosen is: " + comp);
										break;
									}
									i++;
								}
								if(comp == null) {
									JOptionPane.showMessageDialog(orderFrame, "No component selected");
									return;
								} else {
									String num = JOptionPane.showInputDialog(orderFrame, "Which position? (1 to " + components.size() + ")");
									try {
								        int newPos = Integer.parseInt(num);
								        System.out.println("New pos is: " + num);
								        if(newPos > components.size() || newPos < 1) {
								        	JOptionPane.showMessageDialog(orderFrame, "The number selected is not on the given range");
								        }
								        
								        // Changing the order of the files in the config file
								        bufR = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
								        String line = bufR.readLine(), content = "";
								        i = 1;
								        System.out.println("Starting...");
//								        while(line != null) {
//								        	System.out.println("line is: " + line);
//								        	if(!line.startsWith("Separator*") && !line.startsWith("File*")) {
//								        		System.out.println("It's a pos, adding and moving on");
//								        		content = content + line + System.lineSeparator();
//								        		line = bufR.readLine();
//								        		continue;
//								        	}
//								        	if(line.equals(comp.getInFileName())) {
//								        		System.out.println("This is the line we want to order, skip it");
//								        		i++;
//								        		line = bufR.readLine();
//								        		continue;
//								        	}
//								        	if(i == pos) {
//								        		System.out.println("This is the position we want to put it in, putting both");
//								        		content = content + comp.getInFileName() + System.lineSeparator() + line + System.lineSeparator();
//								        	} else {
//								        		System.out.println("Normal line");
//								        		content = content + line + System.lineSeparator();
//								        	}
//								        	
//								        	line = bufR.readLine();
//								        	i++;
//								        }
								        boolean wasNotDoneYet = true;
								        while(line != null) {
								        	System.out.println("line is: " + line + "\ni is: " + i);
								        	if(!line.startsWith("Separator*") && !line.startsWith("File*")) {
								        		System.out.println("It's a pos, adding and moving on");
								        		content = content + line + System.lineSeparator();
								        		line = bufR.readLine();
								        		continue;
								        	}
								        	if(i == newPos) {
								        		System.out.println("This is the position we want to put it in, putting the line to order");
								        		wasNotDoneYet = false;
								        		content = content + comp.getInFileName() + System.lineSeparator();
								        	}
								        	if(line.equals(comp.getInFileName())) {
								        		System.out.println("This is the line we want to order, skip it");
								        		line = bufR.readLine();
								        		continue;
								        	}
								        	System.out.println("Normal line");
							        		content = content + line + System.lineSeparator();
							        		line = bufR.readLine();
								        	i++;
								        }
								        if(wasNotDoneYet) {
								        	System.out.println("(after loop)This is the position we want to put it in, putting the line to order");
							        		wasNotDoneYet = false;
							        		content = content + comp.getInFileName() + System.lineSeparator();
								        }
								        
								        PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
								        pw.write(content);
								        pw.close();
								        bufR.close();
								        orderFrame.dispose();
								        filesFrame.dispose();
								        frame.dispose();
								        initializeMainFrame();
								        
								    } catch (NumberFormatException nfe) {
								    	JOptionPane.showMessageDialog(orderFrame, "You didnt input a number");
										return;
								    } catch (IOException e1) {
								    	e1.printStackTrace();
								    }
									
									
								}
							}
							
						});
						
						orderFrame.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/2) - 50,
					  			  (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
					  			400, 50 + 30 + 29*textNum + 30*sepNum);
						orderFrame.setVisible(true);
					}
					
				});
				
				JButton setNames = new JButton("Submit names"); // "Files frame" set changed names button
				newPn_buttons.add(setNames);
				setNames.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							frame.dispose();
							String content = "", line = "", var = "", oldPath = "", pathSegments = "", newPath = "", name = "";
							BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
							line = reader.readLine();
							Scanner s;
							int i = 0;
							for(TFCComponent comp: components) { // Here is done a cicle through the components and the lines of the config file
								while(!line.startsWith("File*") && !line.startsWith("Separator*")) {
									content = content + line + System.lineSeparator();
									line = reader.readLine();
								}
								
								if(comp.getClass().equals(new TFCText().getClass())) {
									TFCText temp = (TFCText) comp;
									if(!temp.getName().equals(names.get(i).getText())) {
										// Changing the name of the file in the config file
										if(line.contains("*?")) {
											line = line.replace("*?", "");
											var = "*?";
										}
										
										oldPath = line.replace("File*", "");
										pathSegments = oldPath.replace("\\", ",.-");
										s = new Scanner(pathSegments);
										s.useDelimiter(",.-");
										
										newPath = s.next();
										while(s.hasNext()) {
											name = s.next();
											if(s.hasNext()) {
												newPath = newPath + "\\" + name;
											} else {
												break;
											}
										}
										
										name = name.replace(temp.getName(), names.get(i).getText());
										newPath = newPath + "\\" + name + var;
										line = line.replace(oldPath, newPath);
										
										// Giving the text file in the system the new name
										File tempFile = new File(temp.getPath());
										File tempFile2 = new File(newPath.replace("*?", ""));
										tempFile.renameTo(tempFile2);
										
										s.close();
									}
								} else if(comp.getClass().equals(new TFCSeparator().getClass())) {
									TFCSeparator temp = (TFCSeparator) comp;
									if(!temp.getName().equals(names.get(i).getText())) {
										// Changing the name of the separator in the config file
										name = names.get(i).getText();
										name = name.toUpperCase();
										newPath = line.replace(temp.getName(), name);
										line = newPath;
									}
								}
								
								content = content + line + System.lineSeparator();
								i++;
								line = reader.readLine();
								var = "";
							}
							while(line != null) {
								content = content + line + System.lineSeparator();
								line = reader.readLine();
							}
							FileWriter writer = new FileWriter(configName);
							writer.write(content);
							writer.close();
							reader.close();
							filesFrame.dispose();
							frame.dispose();
							initializeMainFrame();
						} catch(IOException e) {
							printError(e, "\"Files\" frame set changed names button " + e.getStackTrace());
							e.printStackTrace();
						}						
					}
				});
				
				
				
				filesFrame.getContentPane().add(newPn_MAIN);
				
				filesFrame.setBounds((int)frame.getBounds().getX() + (int)(frame.getBounds().getWidth()/2) - 50,
						 (int)frame.getBounds().getY() + (int)(frame.getBounds().getHeight()/2) - 50,
						 300, 200 + 25 * textNum + 25 * sepNum);
				filesFrame.setVisible(true);
			}
			
		});
		
		
		save = new JButton("Save"); // Main frame button to save all the texts
		pn_buttons.add(save);
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPosInConfig();
				
				for(TFCComponent comp: components) {
					if(comp.getClass().equals(new TFCText().getClass())) {
						TFCText temp = (TFCText) comp;
						temp.saveTextChanges();
						if(!temp.wasChanged()) {
							continue;
						}
						try {
							PrintWriter pw = new PrintWriter(new File(temp.getPath()), "UTF-8");
							pw.write(temp.getText());
							pw.close();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Algo correu mal com o path do ficheiro " + temp.getName());
							printError(e, "Main frame button to save all the texts ");
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		
		
		frame.getContentPane().add(pn_MAIN);
	}
	
	private void initializeArrays() {
		try {
			bufR = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
			BufferedReader bufR2;
			components = new ArrayList<>();
			String temp = "", temp2 = "", line = "", name = "", text = "", path = "", lineDivider = "";
			File file;
			Scanner s;
			
			line = bufR.readLine();
			
			while(line != null) {
				if(line.startsWith("File*")) { // The line is a file line
					textNum++;
					
					// Getting the path of the file
					path = line.replace("File*", "");
					
					// Checking if this was the last file added
					if(line.contains("*?")) {
						path = path.replace("*?", "");
						lastFileAdded = path;
					}
					
					// Getting the name of the file
					lineDivider = path.replace("\\", ",.-");
					s = new Scanner(lineDivider);
					s.useDelimiter(",.-");
					while(s.hasNext()) {
						temp = s.next();
					}
					s.close();
					temp = temp.replace(".", ",.-");
					s = new Scanner(temp);
					s.useDelimiter(",.-");
					temp2 = s.next();
					while(s.hasNext()) {
						name = name + temp2;
						temp2 = "." + s.next();
						if(!s.hasNext()) {
							break;							
						}
					}
					s.close();
					
					// Getting the text inside the file
					file = new File(path);
					bufR2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
					text = bufR2.readLine();
					if(text == null) {
						text = "";
					}
					bufR2.close();
					
					TFCText tfctext = new TFCText(name, text, path, line);
					components.add(tfctext);
				} else if(line.startsWith("Pos*")) { // The line is a position of the window line
					line = line.replace("Pos*", "");
					line = line.replace("*", " ");
					s = new Scanner(line);
					posX = new Integer(s.next());
					posY = new Integer(s.next());
					s.close();
				} else if(line.startsWith("Separator*")) { // The line is a separator line
					sepNum++;
					line = line.replace("*", ",.-");
					s = new Scanner(line);
					s.useDelimiter(",.-");
					s.next();
					name = s.next();
					s.close();
					
					TFCSeparator tfcseparator = new TFCSeparator(name);
					components.add(tfcseparator);
				}
				
				name = "";
				line = bufR.readLine();
			}
			bufR.close();
						
		} catch (IOException e1) {
			printError(e1, "InitializeArrays() ");
			e1.printStackTrace();
		}	
	}
	
	private void setPosInConfig() {
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(configName), "UTF-8"));
			String contents = "", line;
			line = bf.readLine();
			while(line != null) {
				if(!line.startsWith("Pos*")) {
					contents = contents + line + System.lineSeparator();
				}
				line = bf.readLine();
			}
			contents = contents + "Pos*" + frame.getX() + "*" + frame.getY() + System.lineSeparator();
			PrintWriter pw = new PrintWriter(new File(configName), "UTF-8");
			pw.write(contents);
			bf.close();
			pw.close();
		} catch (IOException e) {
			printError(e, "setPosInConfig() ");
			e.printStackTrace();
		}
	}
	
	private void printError(Exception e, String s) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(debug, "UTF-8");
			s = s + " " + e.getMessage();
			pw.write(s);
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void printError(String s) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(debug, "UTF-8");
			pw.write(s);
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length == 0) {
			GUI gui = new GUI();
		} else {
			GUI gui = new GUI(args[0]);
		}
	}
}
