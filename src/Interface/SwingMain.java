package Interface;

import Classements.*;
import Divers.KeyboardInput;
import BaseDeDonnees.*;
import ModeProgramme.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SwingMain extends JFrame implements ActionListener{

  public static final String OUTPUT_STRING = 
        "                                                                                     "
      + "                                                                                   \n"
      + "\n\n\n\n\n\n\n\n";
	private JPanel contentPane;
	JMenuBar menuBar;
	JMenu menuEquipes, menuImportation, menuCalculClassements;
	JMenuItem menuItem11, menuItem21, menuItem22, menuItem23, 
	menuItem31, menuItem32, menuItem33, menuItem34, menuItem35, menuItem36, menuItem37, menuItem38, menuItem39;
	private JMenuItem space_1;
	private JMenuItem space_2;
	private String lastButton="";
	private int lastInt=-1; 
	private JPanel[] panel;
	private JButton[] btnValider;
	private JTextField txt111, txt112, txt21, txt22, txt23, txt3;
	private JTextPane txtpn111, txtpn112, txtpn21, txtpn22;
	private JTextPane[] choicetxt;
	private JTextArea output;
  private JFrame outputFrame;
  private JScrollPane scroll;
	
//	public static void setKeyboard(Keyboard k){
//		keyboard=k;
//		keyboard.input.add("1");
//	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain frame = new SwingMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**Create the menu bar*/
	public void addMenu(){
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar);
			

			//menu 1
			menuEquipes = new JMenu("Gestion des équipes");
			menuEquipes.setMnemonic(KeyEvent.VK_G);
			menuBar.add(menuEquipes);
			
				//menu 1.1
				menuItem11 = new JMenuItem("Affectations des numeros de doigts aux equipes");
				menuItem11.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_E, ActionEvent.ALT_MASK));
				menuItem11.setActionCommand("11");
				menuItem11.addActionListener(this);
				menuEquipes.add(menuItem11);
			
			space_1 = new JMenuItem();
			menuBar.add(space_1);
			space_1.setEnabled(false);
				
			
			//menu 2
			menuImportation = new JMenu("Donnees des doigts electroniques");
			menuImportation.setMnemonic(KeyEvent.VK_D);
			menuBar.add(menuImportation);
			
				//menu 2.1
				menuItem21 = new JMenuItem("Acquisition des listes de pointages a partir d'un fichier CSV SI-Config",
	                    KeyEvent.VK_I);
				menuItem21.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_I, ActionEvent.ALT_MASK));
				menuItem21.setActionCommand("21");
				menuItem21.addActionListener(this);
				menuImportation.add(menuItem21);
				
				//menu 2.2
				menuItem22 = new JMenuItem("Verification des pointages des equipes",
	                    KeyEvent.VK_V);
				menuItem22.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_V, ActionEvent.ALT_MASK));
				menuItem22.setActionCommand("22");
				menuItem22.addActionListener(this);
				menuImportation.add(menuItem22);
				
				//menu 2.3
				menuItem23 = new JMenuItem("Afficher la liste des equipes sans pointages",
	                    KeyEvent.VK_A);
				menuItem23.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_A, ActionEvent.ALT_MASK));
				menuItem23.addActionListener(this);
				menuItem23.setActionCommand("23");
				menuImportation.add(menuItem23);
			
			space_2 = new JMenuItem();
			menuBar.add(space_2);
			space_2.setEnabled(false);
				
			
			//menu 3
			menuCalculClassements = new JMenu("Calcul des classements");
			menuCalculClassements.setMnemonic(KeyEvent.VK_C);
			menuBar.add(menuCalculClassements);		
			
				//menu 3.1
				menuItem31 = new JMenuItem("Scratch",
	                    KeyEvent.VK_1);
				menuItem31.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_1, ActionEvent.ALT_MASK));
				menuItem31.setActionCommand("31");
				menuItem31.addActionListener(this);
				menuCalculClassements.add(menuItem31);
				
				//menu 3.2
				menuItem32 = new JMenuItem("Entreprise",
	                    KeyEvent.VK_2);
				menuItem32.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_2, ActionEvent.ALT_MASK));
				menuItem32.setActionCommand("32");
				menuItem32.addActionListener(this);
				menuCalculClassements.add(menuItem32);
				
				//menu 3.3
				menuItem33 = new JMenuItem("Ecoles",
	                    KeyEvent.VK_3);
				menuItem33.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_3, ActionEvent.ALT_MASK));
				menuItem33.setActionCommand("33");
				menuItem33.addActionListener(this);
				menuCalculClassements.add(menuItem33);
				
				//menu 3.4
				menuItem34 = new JMenuItem("Femmes",
	                    KeyEvent.VK_4);
				menuItem34.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_4, ActionEvent.ALT_MASK));
				menuItem34.setActionCommand("34");
				menuItem34.addActionListener(this);
				menuCalculClassements.add(menuItem34);
				
				//menu 3.5
				menuItem35 = new JMenuItem("Mixtes",
	                    KeyEvent.VK_5);
				menuItem35.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_5, ActionEvent.ALT_MASK));
				menuItem35.setActionCommand("35");
				menuItem35.addActionListener(this);
				menuCalculClassements.add(menuItem35);
				
				//menu 3.6
				menuItem36 = new JMenuItem("Meilleur Grimpeur",
	                    KeyEvent.VK_6);
				menuItem36.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_6, ActionEvent.ALT_MASK));
				menuItem36.setActionCommand("36");
				menuItem36.addActionListener(this);
				menuCalculClassements.add(menuItem36);
				
				//menu 3.7
				menuItem37 = new JMenuItem("Meilleur Sprinteur",
	                    KeyEvent.VK_7);
				menuItem37.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_7, ActionEvent.ALT_MASK));
				menuItem37.setActionCommand("37");
				menuItem37.addActionListener(this);
				menuCalculClassements.add(menuItem37);
				
				//menu 3.8
				menuItem38 = new JMenuItem("Hommes",
	                    KeyEvent.VK_8);
				menuItem38.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_8, ActionEvent.ALT_MASK));
				menuItem38.setActionCommand("38");
				menuItem38.addActionListener(this);
				menuCalculClassements.add(menuItem38);
				
				//menu 3.9
				menuItem39 = new JMenuItem("Etudiant",
	                    KeyEvent.VK_9);
				menuItem39.setAccelerator(KeyStroke.getKeyStroke(
				   KeyEvent.VK_9, ActionEvent.ALT_MASK));
				menuItem39.setActionCommand("39");
				menuItem39.addActionListener(this);
				menuCalculClassements.add(menuItem39);
				
		//Elements of the different submenus
		panel = new JPanel[24];//The only existing elements are those of a submenu (11,21,22,23,3).
		btnValider = new JButton[24];
		choicetxt = new JTextPane[24];
	  //Submenu 11
		panel[11] = new JPanel();
    contentPane.add(panel[11]);
		    
	    txtpn111 = new JTextPane();
	    txtpn111.setText("N° de dossard : ");
	    txtpn111.setEditable(false);
	    panel[11].add(txtpn111);
	    
	    txt111 = new JTextField();
	    txt111.setText("");
	    txt111.setHorizontalAlignment(JTextField.RIGHT);
	    panel[11].add(txt111);
	    txt111.setColumns(10);
	    
	    txtpn112 = new JTextPane();
	    txtpn112.setText("N° de doigt : ");
	    txtpn112.setEditable(false);
	    panel[11].add(txtpn112);
	    
	    txt112 = new JTextField();
	    txt112.setText("");
	    txt112.setHorizontalAlignment(JTextField.RIGHT);
	    panel[11].add(txt112);
	    txt112.setColumns(10);
	    
	    btnValider[11] = new JButton("Valider");
	    btnValider[11].setActionCommand("Valider11");
	    btnValider[11].addActionListener(this);
	    panel[11].add(btnValider[11]);
	    
	    panel[11].setVisible(false);
	    
    //Submenu 21
    panel[21] = new JPanel();
    contentPane.add(panel[21]);
        
      txtpn21 = new JTextPane();
      txtpn21.setText("Chemin d'acces au fichier a importer :");
      txtpn21.setEditable(false);
      panel[21].add(txtpn21);
      
      txt21 = new JTextField();
      txt21.setText("");
      txt21.setHorizontalAlignment(JTextField.RIGHT);
      txt21.setDragEnabled(true);
      panel[21].add(txt21);
      txt21.setColumns(10);
      
      btnValider[21] = new JButton("Envoyer");
      btnValider[21].setActionCommand("Valider21");
      btnValider[21].addActionListener(this);
      panel[21].add(btnValider[21]);
      
      panel[21].setVisible(false);
      
    //Submenu 22
    panel[22] = new JPanel();
    contentPane.add(panel[22]);
        
      txtpn22 = new JTextPane();
      txtpn22.setText("N° de dossard : ");
      txtpn22.setEditable(false);
      panel[22].add(txtpn22);
      
      txt22 = new JTextField();
      txt22.setText("");
      txt22.setHorizontalAlignment(JTextField.RIGHT);
      panel[22].add(txt22);
      txt22.setColumns(10);
      
      btnValider[22] = new JButton("Valider");
      btnValider[22].setActionCommand("Valider22");
      btnValider[22].addActionListener(this);
      panel[22].add(btnValider[22]);
      
      panel[22].setVisible(false);
    
    //Submenu 23
    panel[23] = new JPanel();
    contentPane.add(panel[23]);
        
      choicetxt[23] = new JTextPane();
      choicetxt[23].setText("Jour a verifier : ");
      choicetxt[23].setEditable(false);
      panel[23].add(choicetxt[23]);
      
      txt23 = new JTextField();
      txt23.setText("");
      txt23.setHorizontalAlignment(JTextField.RIGHT);
      panel[23].add(txt23);
      txt23.setColumns(10);
      
      btnValider[23] = new JButton("Valider");
      btnValider[23].setActionCommand("Valider23");
      btnValider[23].addActionListener(this);
      panel[23].add(btnValider[23]);
      
      panel[23].setVisible(false);
      
    //Submenu 3i, i=1..9
    panel[3] = new JPanel();
    contentPane.add(panel[3]);
        
      choicetxt[3] = new JTextPane();
      choicetxt[3].setText("Classement à calculer : ");
      choicetxt[3].setEditable(false);
      panel[3].add(choicetxt[3]);
      
      txt3 = new JTextField();
      txt3.setText("");
      txt3.setHorizontalAlignment(JTextField.RIGHT);
      panel[3].add(txt3);
      txt3.setColumns(10);
      
      btnValider[3] = new JButton("Valider");
      btnValider[3].setActionCommand("Valider3");
      btnValider[3].addActionListener(this);
      panel[3].add(btnValider[3]);
      
      panel[3].setVisible(false);
      
    //Dialog zone for the output.
    output = new JTextArea ("Bienvenue sur le site du Raid!\n"+OUTPUT_STRING);
    output.setEditable(false);
    scroll = new JScrollPane (output, 
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    contentPane.add(scroll);
    
    //Keep on updating the output.
    new Thread(new Runnable(){
      public void run(){
        Keyboard.KEYBOARD.updateOutput(output);
      }
    }).start();
	}
	
	public void addSubMenu(int h, int s){
	  if (h!=-1){
	    panel[h].setVisible(false);
	  }
	  if (!Keyboard.KEYBOARD.currentChoice.isEmpty()){
	    if (s==3){
	      choicetxt[s].setText("Classement à calculer : "+Keyboard.KEYBOARD.getChoices());
	    } else if (s==23){
	      choicetxt[s].setText("Jour a verifier : "+Keyboard.KEYBOARD.getChoices());
	    } else {
	      System.out.println("\nWarning : currentChoices should be empty.");
	      Keyboard.KEYBOARD.getChoices();
	    }
	  }
	  panel[s].setVisible(true);
	}
	
	/** Enter a message in the fake keyboard*/
	private void hitInt(int i){
    Keyboard.KEYBOARD.myPut(""+i);
	}
	private void hit(String s){
    Keyboard.KEYBOARD.myPut(s);
//    System.out.println(s);
  }
	
	public void actionPerformed(ActionEvent e) {
	  int currentAction;
	  //Let's test if it is a validation button
	  if (e.getActionCommand().length()>2){
	    switch (e.getActionCommand()){
	    case "Valider11" : {
	      try{
	        //Ley's test if the text is a number.
	        Integer.parseInt(txt111.getText());
	        if(txt112.getText().equals("")){
	          hit(txt111.getText());
	          Keyboard.KEYBOARD.waitUntilReady();
	          hit("a");
	        } else {
	          Integer.parseInt(txt112.getText());
	          hit(txt111.getText());
	          Keyboard.KEYBOARD.waitUntilReady();
	          hit(txt112.getText());
	        }
	      } catch (Exception pasUnNombre){
	        System.out.println("Le texte tape par l'utilisateur doit etre un nombre.");
	        hit("r");
	        panel[lastInt].setVisible(false);
	      }
	      break;
	    }
	    case "Valider21" : {
	      hit(txt21.getText());
	    }
	    case "Valider22" : {
        hit(txt22.getText());
      }
	    case "Valider23" : {
        hit(txt23.getText());
      }
	    case "Valider3" : {
        hit(txt3.getText());
      }
	    }
	    return;
	  }
	  int h=lastInt;//Panel to hide.
	  if (e.getActionCommand()==lastButton){
	    return;
	  } else {
	    lastInt=Integer.parseInt(e.getActionCommand());
	    lastButton=e.getActionCommand();
	  }
		//System.out.println(e.getActionCommand());
		if (Keyboard.KEYBOARD.modeActuel!="MODE_DEFAULT"){
//		Return to the main menu
		  hit("r");
		  Keyboard.KEYBOARD.waitUntilReady();
		}
		currentAction = Integer.parseInt(e.getActionCommand());
		hitInt(currentAction / 10);
		Keyboard.KEYBOARD.waitUntilReady();
		hitInt(currentAction % 10);
		Keyboard.KEYBOARD.waitUntilReady();
		//We only created one submenu for all CALCUL_CLASSEMENTS submenus (31 to 39)
		if (currentAction>30){
		  currentAction=3;
		}
		if (h>30){
      h=3;
    }
		addSubMenu(h,currentAction);
	}

	/**
	 * Create the frame.
	 */
	public SwingMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 300);
		contentPane = new JPanel();
		setTitle("Calcul de classements pour le Raid de l'X");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		addMenu();
	}

}
