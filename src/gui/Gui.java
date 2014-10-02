package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Gui {
	private JFrame 		mainFrame;
	private JLabel 		headerLabel;
	private JLabel 		statusLabel;
	private JPanel 		controlPanel;
	private String		ipAdd;
	
	public Gui(){
		prepareGUI();
	}
    
//    private void prepareGUI(){
//        mainFrame = new JFrame("VPN EECE 412");
//        mainFrame.setSize(400,400);
//        mainFrame.setLayout(new GridLayout(3, 1));
//        mainFrame.addWindowListener(new WindowAdapter() {
//           public void windowClosing(WindowEvent windowEvent){
//              System.exit(0);
//           }        
//        });    
//        headerLabel = new JLabel("", JLabel.CENTER);        
//        statusLabel = new JLabel("",JLabel.CENTER);    
//
//        statusLabel.setSize(350,100);
//
//        controlPanel = new JPanel();
//        controlPanel.setLayout(new FlowLayout());
//
//        mainFrame.add(headerLabel);
//        mainFrame.add(controlPanel);
//        mainFrame.add(statusLabel);
//        mainFrame.setVisible(true);  
//    }
    
//    private void showLayout(){
//
//        headerLabel.setText("Enter IP address"); 
//        final JTextField ipAddTextField = new JTextField(20);
//
//        JButton connectBtn = new JButton("Connect");        
//        JButton cancelBtn = new JButton("Cancel");
//        cancelBtn.setHorizontalTextPosition(SwingConstants.LEFT);   
//        
//        connectBtn.addActionListener(new ActionListener() {
//           public void actionPerformed(ActionEvent e) {
//        	   ipAdd = ipAddTextField.getText();
//        	   statusLabel.setText("Connect Button clicked\n IP: " + ipAdd);
//           }          
//        });
//
//        cancelBtn.addActionListener(new ActionListener() {
//           public void actionPerformed(ActionEvent e) {
//        	   statusLabel.setText("Cancel Button clicked.");
//           }
//        });
//
//        controlPanel.add(ipAddTextField);  
//        controlPanel.add(connectBtn);
//        controlPanel.add(cancelBtn);     
//
//        mainFrame.setVisible(true);  
//     }
//    
	private void prepareGUI(){
        mainFrame = new JFrame("VPN EECE 412");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));

        headerLabel = new JLabel("", JLabel.CENTER);        
        statusLabel = new JLabel("",JLabel.CENTER); 
        statusLabel.setSize(350,100);
        
        mainFrame.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
  	        System.exit(0);
           }        
        }); 
        
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);          
    }
	
	private void showLayout(){
      headerLabel.setText("VPN Assignment");      

      final JPanel panel = new JPanel();
      panel.setBackground(Color.CYAN);
      panel.setSize(300,300);

      CardLayout layout = new CardLayout();
      layout.setHgap(10);
      layout.setVgap(10);
      panel.setLayout(layout);        

      JPanel serverPanel = new JPanel(new FlowLayout());

      serverPanel.add(new JLabel("Enter port number"));
      serverPanel.add(new JTextField(20));
      serverPanel.add(new JButton("Connect"));
      serverPanel.add(new JButton("Cancel"));    

      JPanel clientPanel = new JPanel(new FlowLayout());

      clientPanel.add(new JLabel("Enter IP address"));
      clientPanel.add(new JTextField(20));
      clientPanel.add(new JButton("Connect"));
      clientPanel.add(new JButton("Cancel")); 

      panel.add("Server", serverPanel);
      panel.add("Client", clientPanel);
      
	  final DefaultComboBoxModel panelName = new DefaultComboBoxModel();

      panelName.addElement("Server");
      panelName.addElement("Client");
     	  
	  final JComboBox listCombo = new JComboBox(panelName);    
      listCombo.setSelectedIndex(0);

      JScrollPane listComboScrollPane = new JScrollPane(listCombo);    

      JButton selectBtn = new JButton("Select");

      selectBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { 
            String data = "";
            if (listCombo.getSelectedIndex() != -1) {  
               CardLayout cardLayout = (CardLayout)(panel.getLayout());
               cardLayout.show(panel, 
               (String)listCombo.getItemAt(listCombo.getSelectedIndex()));	               
            }              
            statusLabel.setText(data);
         }
      }); 
	  
      controlPanel.add(listComboScrollPane);
      controlPanel.add(selectBtn);
	  controlPanel.add(panel);

      mainFrame.setVisible(true);  
   }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	Gui showGUI = new Gui();      
    	showGUI.showLayout();
    }
}

