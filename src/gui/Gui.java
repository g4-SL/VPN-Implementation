package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import clientServer.VPN;

/*
 * Author: Group 8
 * Date: October 11, 2014
 * Course: EECE 412, Assignment 3
 * Purpose: Implements GUI and initiates Client-Server connection.
 * Version: Added send and received text fields.
 */

public class Gui {
	private JFrame 		mainFrame;
	private JLabel 		headerLabel;
	private JLabel 		statusLabel;
	private JPanel 		controlPanel;
	private JPanel 		messagePanel;
	private JPanel 		userTypePanel;
	private String		ipAdd;
	private String		portNum;
	private String		hostName;
	private String		message;
	private String		sharedKeyClient;
	private String		sharedKeyServer;

    private JTextField 	displayMsgField;
    
    private static VPN myVPN = new VPN();
	
	public Gui(){
		prepareGUI();
	}
	
	private void prepareGUI(){
        mainFrame = new JFrame("VPN EECE 412");
        mainFrame.setSize(600,500);
        mainFrame.setLayout(new FlowLayout());
       
        statusLabel = new JLabel("",JLabel.CENTER); 
        statusLabel.setSize(350,50);
        
        mainFrame.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
  	        System.exit(0);
           }        
        }); 
        
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3,1));       
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridLayout(4,1));
        
        userTypePanel = new JPanel();
        userTypePanel.setLayout(new FlowLayout());

        mainFrame.add(userTypePanel);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);          
    }
	
	private void showLayout(){     

      final JPanel panel = new JPanel();
      panel.setBackground(Color.CYAN);
      panel.setSize(400,500);

      CardLayout layout = new CardLayout();
//      layout.setHgap(10);
//      layout.setVgap(10);
      panel.setLayout(layout);        
      
      //---------------- message box ---------------------------------------------//
      
      final JLabel messageLabel = new JLabel("Enter data to be sent");
      final JTextField messageTextField = new JTextField(40);
      final JLabel displayMsgLabel = new JLabel("Data as received");
      displayMsgField = new JTextField(40);
      messagePanel.add(messageLabel);
      messagePanel.add(messageTextField);
      messagePanel.add(displayMsgLabel);
      messagePanel.add(displayMsgField);

      //----------- SERVER -------------------------------------//
      
      JPanel serverPanel = new JPanel();
      GroupLayout serverLayout = new GroupLayout(serverPanel);
      serverLayout.setAutoCreateGaps(true);
      serverLayout.setAutoCreateContainerGaps(true);
      
      JLabel portNumLabel = new JLabel("Enter port number");
      final JTextField portNumText = new JTextField(40);
      JLabel sharedKeyServerLabel = new JLabel("Enter shared secret value");
      final JTextField sharedKeyServerText = new JTextField(20);
      JButton connectServerBtn = new JButton("Connect");
      JButton cancelServerBtn = new JButton("Cancel");
      JButton sendServerMessageBtn = new JButton("Send");
      
      serverLayout.setHorizontalGroup(serverLayout.createSequentialGroup()
		 .addGroup(serverLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(portNumLabel)
	         .addComponent(portNumText)
	         .addComponent(sharedKeyServerLabel)
	         .addComponent(sharedKeyServerText)
	         .addGroup(serverLayout.createSequentialGroup()
        		 .addComponent(connectServerBtn)
                 .addComponent(cancelServerBtn)
                 .addComponent(sendServerMessageBtn)
    		 )
         )      
      );
      
      serverLayout.setVerticalGroup(serverLayout.createSequentialGroup()
         .addComponent(portNumLabel)
         .addComponent(portNumText)
         .addComponent(sharedKeyServerLabel)
	     .addComponent(sharedKeyServerText)
         	.addGroup(serverLayout.createParallelGroup(
               GroupLayout.Alignment.LEADING)
               .addComponent(connectServerBtn)
               .addComponent(cancelServerBtn)
               .addComponent(sendServerMessageBtn)
            )                                
      );
      
      connectServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNum = portNumText.getText();
    		  sharedKeyServer = sharedKeyServerText.getText();
    		  //message = messageTextField.getText(); // moved this line to Send Button Listener
    		  statusLabel.setText("Port number: " + portNum + " Shared Key: " + sharedKeyServer);
    		  
    		  // Call VPN package to set up the server
    		  int portNumber = Integer.parseInt(portNum);
    		  myVPN.runServerThread(portNumber);
		 }          
      });
      
      /**
       * Currently, this method does not close the server. (Do we need it, anyway?)
       */
      cancelServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNumText.setText("");
    		  sharedKeyServerText.setText("");
    		  statusLabel.setText("Cancel server");
		 }          
      });
      
      sendServerMessageBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNumText.setText("");
    		  //message = messageTextField.getText();
    		  statusLabel.setText("Sending server message");

    		  System.out.println("You clicked SEND SERVER button");   		  
		 }          
      });
      
      
      
      //----------- CLIENT -------------------------------------//
      
      JPanel clientPanel = new JPanel();
      GroupLayout clientLayout = new GroupLayout(clientPanel);
      clientLayout.setAutoCreateGaps(true);
      clientLayout.setAutoCreateContainerGaps(true);
      
      JLabel ipAddLabel = new JLabel("Enter IP address");
      final JTextField ipAddText = new JTextField(40);      
      JLabel hostNameLabel = new JLabel("Enter host name");
      final JTextField hostNameText = new JTextField(20);
      JLabel sharedKeyClientLabel = new JLabel("Enter shared secret value");
      final JTextField sharedKeyClientText = new JTextField(20);
      JButton connectClientBtn = new JButton("Connect");
      JButton cancelClientBtn = new JButton("Cancel");
      JButton sendClientMessageBtn = new JButton("Send");
      
      clientLayout.setHorizontalGroup(clientLayout.createSequentialGroup()
		 .addGroup(clientLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(ipAddLabel)
	         .addComponent(ipAddText)
	         .addComponent(hostNameLabel)
	         .addComponent(hostNameText)
	         .addComponent(sharedKeyClientLabel)
	         .addComponent(sharedKeyClientText)
	         .addGroup(clientLayout.createSequentialGroup()
        		 .addComponent(connectClientBtn)
                 .addComponent(cancelClientBtn)
                 .addComponent(sendClientMessageBtn)              
    		 )
         )      
      );
      
      clientLayout.setVerticalGroup(clientLayout.createSequentialGroup()
         .addComponent(ipAddLabel)
         .addComponent(ipAddText)
         .addComponent(hostNameLabel)
         .addComponent(hostNameText)
         .addComponent(sharedKeyClientLabel)
	     .addComponent(sharedKeyClientText)
         	.addGroup(clientLayout.createParallelGroup(
               GroupLayout.Alignment.LEADING)
               .addComponent(connectClientBtn)
               .addComponent(cancelClientBtn)   
               .addComponent(sendClientMessageBtn)
            )                                
      );
      
      connectClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAdd = ipAddText.getText();
    		  hostName = hostNameText.getText();
    		  sharedKeyClient = sharedKeyClientText.getText();
    		  message = messageTextField.getText();
    		  statusLabel.setText("IP: " + ipAdd + " and host name: " + hostName + " Shared Key: " + sharedKeyClient);
    		  
    		  // Call VPN package to set up the client
    		  int ipNumber = Integer.parseInt(ipAdd);
    		  myVPN.runClientThread(ipNumber, hostName);		  
		 }          
      });
      
      /**
       * Currently, this method does not close the client. (Do we need it, anyway?)
       */
      cancelClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAddText.setText("");
    		  hostNameText.setText("");
    		  sharedKeyClientText.setText("");
    		  statusLabel.setText("Cancel client");
		 }          
      });
      
      sendClientMessageBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAddText.setText("");
    		  hostNameText.setText("");
    		  sharedKeyClientText.setText("");
    		  message = messageTextField.getText();
    		  statusLabel.setText("Sending client message");
    		  
    		  //System.out.println("You clicked SEND CLIENT button");
    		  
    		  myVPN.sendClientMessage();
		 }          
      });

      serverPanel.setLayout(serverLayout);
      clientPanel.setLayout(clientLayout);
      panel.add("Server", serverPanel);
      panel.add("Client", clientPanel);
      
      
      //---------------- user type selection -------------------------------------//
      
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
	  
      userTypePanel.add(listComboScrollPane);
      userTypePanel.add(selectBtn);
	  controlPanel.add(panel);
	  controlPanel.add(messagePanel);
	  controlPanel.add(statusLabel);

      mainFrame.setVisible(true);  
   }
	
	public String getPortNum(){
		return portNum;
	}
	
	public String getIpAdd(){
		return ipAdd;
	}
	
	public String getHostName(){
		return hostName;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void displayMessage(String input){
		displayMsgField.setText(input);
	}
	
	public String getSharedKeyClient(){
		return sharedKeyClient;
	}
	
	public String getSharedKeyServer(){
		return sharedKeyServer;
	}
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	Gui showGUI = new Gui();      
    	showGUI.showLayout();
    	
    	// Pass the current GUI object to the VPN
    	myVPN.setGUI(showGUI);
    
    }
}
