package asgn2GUIs;

import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.text.DefaultCaret;

import asgn2Customers.Customer;
import asgn2Exceptions.CustomerException;
import asgn2Exceptions.LogHandlerException;
import asgn2Exceptions.PizzaException;
import asgn2Pizzas.Pizza;
import asgn2Restaurant.PizzaRestaurant;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.io.File; 
import java.awt.*;
import javax.swing.*;


/**
 * This class is the graphical user interface for the rest of the system. 
 * Currently it is a �dummy� class which extends JFrame and implements Runnable and ActionLister. 
 * It should contain an instance of an asgn2Restaurant.PizzaRestaurant object which you can use to 
 * interact with the rest of the system. You may choose to implement this class as you like, including changing 
 * its class signature � as long as it  maintains its core responsibility of acting as a GUI for the rest of the system. 
 * You can also use this class and asgn2Wizards.PizzaWizard to test your system as a whole
 * 
 * 
 * @author Person A and Person B
 *
 */
public class PizzaGUI extends javax.swing.JFrame implements Runnable, ActionListener {
	
	private static final long serialVersionUID = -7031008862559936404L;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	private PizzaRestaurant restaurant;
	
	private JPanel pnlBase;
	private JPanel pnlTwo;
	private JPanel pnlFour;
	private JPanel pnlFive;
	
	private JButton btnLoad;
	private JButton btnReset;
	private JButton btnInfo;
	private JButton btnCalc;
	private JPanel pnlBtn;
	private JTextArea pizzaDisplay; 
	private JTextArea customerDisplay; 
	private JScrollPane scrollDisplay;
	
	/**
	 * Creates a new Pizza GUI with the specified title 
	 * @param title - The title for the supertype JFrame
	 */
	public PizzaGUI(String title) {
		super(title);
	}
	private void createGUI() {
		setSize(WIDTH, HEIGHT);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	    
	    pnlBase = createPanel(Color.WHITE);
	    pnlBtn = createPanel(Color.LIGHT_GRAY);

	    
	    btnLoad = createButton("Load");
	    btnReset = createButton("Reset");
	    btnInfo = createButton("Display Information");
	    btnCalc = createButton("Display Calculation");
	    
	    pizzaDisplay = createTextArea();
	    customerDisplay = createTextArea();
	    
	    scrollDisplay = createScroll(pnlBase);
	    
	    pnlBase.setLayout(new GridLayout(0,2));
	    pnlBase.add(pizzaDisplay);
	    pnlBase.add(customerDisplay);
	    
	    layoutButtonPanel(); 
	    
	    this.getContentPane().add(scrollDisplay,BorderLayout.CENTER);
	    this.getContentPane().add(pnlBtn,BorderLayout.NORTH);
	    
	    repaint(); 
	    this.setVisible(true);
	}
	
	private JPanel createPanel(Color c) {
		JPanel jp = new JPanel();
		jp.setBackground(c);
		return jp;
	}
	
	private JButton createButton(String str) {
		JButton jb = new JButton(str); 
		jb.addActionListener(this);
		return jb; 
	}
	
	private JTextArea createTextArea() {
		JTextArea jta = new JTextArea(); 
		jta.setEditable(false);
		jta.setLineWrap(true);
		jta.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
		return jta;
	}
	private JScrollPane createScroll(JPanel jp){
		JScrollPane jsp = new JScrollPane(jp);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return jsp;
	}
	
	private void layoutButtonPanel() {
		GridBagLayout layout = new GridBagLayout();
	    pnlBtn.setLayout(layout);
	    
	    //add components to grid
	    GridBagConstraints constraints = new GridBagConstraints(); 
	    
	    //Defaults
	    constraints.fill = GridBagConstraints.NONE;
	    constraints.anchor = GridBagConstraints.CENTER;
	    constraints.weightx = 50;
	    constraints.weighty = 50;
	    
	    addToPanel(pnlBtn, btnLoad,constraints,	 0,0,1,1); 
	    addToPanel(pnlBtn, btnReset,constraints,1,0,1,1); 
	    addToPanel(pnlBtn, btnInfo,constraints,  2,0,1,1); 
	    addToPanel(pnlBtn, btnCalc,constraints,3,0,1,1); 	
	}
	
	private void addToPanel(JPanel jp,Component c, GridBagConstraints constraints, int x, int y, int w, int h) {  
	      constraints.gridx = x;
	      constraints.gridy = y;
	      constraints.gridwidth = w;
	      constraints.gridheight = h;
	      jp.add(c, constraints);
	}

	@Override
	public void run() {
		createGUI();
	}
	
	public static void main(String[] args) {
	    JFrame.setDefaultLookAndFeelDecorated(false);
	}
	String line;
	Pizza currentPizza;
	Boolean infoLoaded = false;
	@Override
	public void actionPerformed(ActionEvent e) {
		//Get event source 
		Object src=e.getSource(); 
  		//Consider the alternatives - not all active at once. 
		if (src== btnLoad){
			JButton btn = ((JButton) src);
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(btn);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				//process log file
				String filename = selectedFile.getAbsolutePath();
				restaurant = new PizzaRestaurant();
				try {
					restaurant.processLog(filename);
					infoLoaded = true;
					pizzaDisplay.setText(filename+ " Loaded");
					} catch (CustomerException | PizzaException | LogHandlerException e1) {
						JOptionPane.showMessageDialog(this,"Failed to load: " + filename + "\n" + e1.toString(),"A Warning Message",JOptionPane.ERROR_MESSAGE);
					}
			}
		} else if (src==btnReset) {
			restaurant.resetDetails();
			pizzaDisplay.setText("");
			infoLoaded = false;
		} else if (src==btnCalc) {
			//TODO
			
		} else if (src==btnInfo && infoLoaded) {
			pizzaDisplay.setText(String.format("Pizza\n%-12s %-4s %-6s %-6s %-6s","Type","Qty","Price","Cost","Profit\n"));
			try {
				for(int i = 0;i < restaurant.getNumPizzaOrders(); i++){
							currentPizza = restaurant.getPizzaByIndex(i);
							line = String.format("%-12s %-4d %-6.2f %-6.2f %-6.2f", currentPizza.getPizzaType(),currentPizza.getQuantity(), 
									currentPizza.getOrderPrice(),currentPizza.getOrderCost(), 
									currentPizza.getOrderProfit());
							pizzaDisplay.append(line + "\n");
				}
			} catch (PizzaException e1) {
				JOptionPane.showMessageDialog(this,e1.toString(),"A Warning Message",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
