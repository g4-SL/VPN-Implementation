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
 * Last Modified: October 12, 2014
 * Course: EECE 412, Assignment 3
 * Purpose: Implements GUI and initiates Client-Server connection.
 * Version: Added send and received text fields.
 */

public class Gui {
	private JFrame 		mainFrame;
	private JLabel 		headerLabel;
	private JLabel 		statusLabel;
	private JPanel 		controlPanel;
	private JPanel 		userTypePanel;
	private String		ipAdd;
	private String		portNum;
	private String		hostName;

	private String		serverMsg;
	private String		clientMsg;
	private String		sharedKeyClient;
	private String		sharedKeyServer;

	private boolean		isServerConnected;	//to check if message can be sent over
	private boolean		isClientConnected;	//to check if message can be sent over
	
    private JTextField 	displayMsgFieldOnServer;
    private JTextField 	displayMsgFieldOnClient;
    
    private static VPN myVPN = new VPN();
    
	public Gui(){
		prepareGUI();
	}

	private void prepareGUI(){
        mainFrame = new JFrame("VPN EECE 412");
        mainFrame.setSize(600,800);
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
        
        userTypePanel = new JPanel();
        userTypePanel.setLayout(new FlowLayout());

        mainFrame.add(userTypePanel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);          
    }
	
	private void showLayout(){     

      final JPanel panel = new JPanel();
      panel.setBackground(Color.CYAN);
      panel.setSize(400,500);

      CardLayout layout = new CardLayout();
      panel.setLayout(layout);        
      
      //---------------- message box ---------------------------------------------//

      // for the server
      final JPanel serverMsgPanel = new JPanel();
      final JLabel serverHeaderLabel = new JLabel("MESSAGE FROM THE SERVER");
      final JLabel serverMsgLabel = new JLabel("Enter your message here");
      final JTextField serverMsgTextField = new JTextField(40);
      final JLabel serverDisplayMsgLabel = new JLabel("Received message");
      
      serverMsgPanel.setLayout(new GridLayout(6,1));
      displayMsgFieldOnServer = new JTextField(40);
      displayMsgFieldOnServer.setText("Wait for received message to display here");
      serverMsgPanel.add(serverHeaderLabel);
      serverMsgPanel.add(serverMsgLabel);
      serverMsgPanel.add(serverMsgTextField);
      serverMsgPanel.add(serverDisplayMsgLabel);
      serverMsgPanel.add(displayMsgFieldOnServer);

      
      // for the client
      final JPanel clientMsgPanel = new JPanel();
      final JLabel clientHeaderLabel = new JLabel("MESSAGE FROM THE CLIENT");
      final JLabel clientMsgLabel = new JLabel("Enter your message here");
      final JTextField clientMsgTextField = new JTextField(40);
      final JLabel clientDisplayMsgLabel = new JLabel("Received message");

      clientMsgPanel.setLayout(new GridLayout(6,1));
      displayMsgFieldOnClient = new JTextField(40);
      displayMsgFieldOnClient.setText("Wait for received message to display here");
      clientMsgPanel.add(clientHeaderLabel);
      clientMsgPanel.add(clientMsgLabel);
      clientMsgPanel.add(clientMsgTextField);
      clientMsgPanel.add(clientDisplayMsgLabel);
      clientMsgPanel.add(displayMsgFieldOnClient);
      
      

      //----------- SERVER -------------------------------------//
      
      JPanel serverPanel = new JPanel();
      GroupLayout serverLayout = new GroupLayout(serverPanel);
      serverLayout.setAutoCreateGaps(true);
      serverLayout.setAutoCreateContainerGaps(true);
      
      JLabel portNumLabel = new JLabel("Enter port number");
      final JTextField portNumText = new JTextField(40);
      JLabel sharedKeyServerLabel = new JLabel("Enter shared key");
      final JTextField sharedKeyServerText = new JTextField(20);
      JButton connectServerBtn = new JButton("Connect");
      JButton keyServerBtn = new JButton("Set key");
      JButton sendServerMessageBtn = new JButton("Send");
      JButton cancelServerBtn = new JButton("Cancel");
      
      serverLayout.setHorizontalGroup(serverLayout.createSequentialGroup()
		 .addGroup(serverLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(portNumLabel)
	         .addComponent(portNumText)
	         .addComponent(sharedKeyServerLabel)
	         .addComponent(sharedKeyServerText)
	         .addGroup(serverLayout.createSequentialGroup()
        		 .addComponent(connectServerBtn)
        		 .addComponent(keyServerBtn)
                 .addComponent(sendServerMessageBtn)
                 .addComponent(cancelServerBtn)
    		 )
    		 .addComponent(serverMsgPanel)
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
        		 .addComponent(keyServerBtn)
                 .addComponent(sendServerMessageBtn)
                 .addComponent(cancelServerBtn)
            )
            .addComponent(serverMsgPanel)	                                
      );
      
      connectServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNum = portNumText.getText();
    		  sharedKeyServer = sharedKeyServerText.getText();
    		  if(portNum.equals(""))
				  statusLabel.setText("port number is missing");
    		  //else if(sharedKeyServer.equals(""))
				//  statusLabel.setText("shared key is missing");
    		  else{
        		  /*** might need to change logic of checking if server is connected later on ***/
        		  isServerConnected = true;	
        		  
        		  // Call VPN package to set up the server
        		  int portNumber = Integer.parseInt(portNum);
        		  myVPN.runServerThread(portNumber);
        		  
        		  statusLabel.setText("Port number: " + portNum);
    		  }
		 }          
      });
      
	/**
	 * Currently, this button does not close the server. (Do we need it, anyway?)
	 */
      cancelServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  isServerConnected = false;	
    		  portNumText.setText("");
    		  sharedKeyServerText.setText("");
    		  statusLabel.setText("Cancel server");
		 }          
      });
      
      keyServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  sharedKeyServer = sharedKeyServerText.getText();
    	  }
      });
      
      sendServerMessageBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNumText.setText("");
    		  //sharedKeyServerText.setText("");
    		  
    		  if(isServerConnected){
    			  serverMsg = serverMsgTextField.getText();
	    		  statusLabel.setText("Sending server message: " + serverMsg);

	    		  myVPN.sendServerMessage();
	    		  System.out.println("You clicked SEND SERVER button");   
    		  }
    		  else{
    			  serverMsg = "";
    			  statusLabel.setText("Connect to a client before sending a message" + clientMsg); //clientMsg is there to check if string is cleared
    		  }
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
      JLabel sharedKeyClientLabel = new JLabel("Enter shared key");
      final JTextField sharedKeyClientText = new JTextField(20);
      JButton connectClientBtn = new JButton("Connect");
      JButton keyClientBtn = new JButton("Set key");
      JButton sendClientMessageBtn = new JButton("Send");
      JButton cancelClientBtn = new JButton("Cancel");
      
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
        		 .addComponent(keyClientBtn)
        		 .addComponent(sendClientMessageBtn)   
                 .addComponent(cancelClientBtn)           
    		 )
    		 .addComponent(clientMsgPanel)
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
               .addComponent(keyClientBtn)
               .addComponent(sendClientMessageBtn)   
               .addComponent(cancelClientBtn)      
            ) 
  		 .addComponent(clientMsgPanel)                                 
      );
      
      connectClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAdd = ipAddText.getText();
    		  hostName = hostNameText.getText();
    		  sharedKeyClient = sharedKeyClientText.getText();
    		  if(ipAdd.equals(""))
				  statusLabel.setText("IP address is missing");
    		  else if(hostName.equals(""))
				  statusLabel.setText("host name is missing");
    		  //else if(sharedKeyClient.equals(""))
				//  statusLabel.setText("shared key is missing");
    		  else{
				  /*** might need to change logic of checking if client is connected later on ***/
				  isClientConnected = true;	
				  
	    		  // Call VPN package to set up the client
	    		  int ipNumber = Integer.parseInt(ipAdd);
	    		  myVPN.runClientThread(ipNumber, hostName);	
	    		  
				  statusLabel.setText("IP: " + ipAdd + " and host name: " + hostName + " Shared Key: " + sharedKeyClient);
    		  }
		 }          
      });
      
      cancelClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  isClientConnected = false;	
    		  ipAddText.setText("");
    		  hostNameText.setText("");
    		  sharedKeyClientText.setText("");
    		  statusLabel.setText("Cancel client");
		 }          
      });
      
      keyClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  sharedKeyClient = sharedKeyClientText.getText();
    	  }          
      });
      
      sendClientMessageBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAddText.setText("");
    		  hostNameText.setText("");
    		  sharedKeyClient = sharedKeyClientText.getText();
    		  //sharedKeyClientText.setText("");
    		  
    		  if(isClientConnected){
    			  clientMsg = clientMsgTextField.getText();
    			  statusLabel.setText("Sending client message: " + clientMsg);
    		  
    			  myVPN.sendClientMessage();
        		  System.out.println("You clicked SEND CLIENT button");
    		  }
    		  else{
    			  clientMsg = "";
    			  statusLabel.setText("Connect to a client before sending a message" + serverMsg); //serverMsg is there to check if string is cleared
    		  }
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
//	  controlPanel.add(messagePanel);
	  controlPanel.add(statusLabel);

      mainFrame.setVisible(true);  
   }



	public String getIpAdd(){
		return ipAdd;
	}

	public String getHostName(){
		return hostName;
	}
	
	public String getServerMsg(){
		return serverMsg;
	}
	
	public String getClientMsg(){
		return clientMsg;
	}
	
	public void displayMsgOnServer(String input){
		displayMsgFieldOnServer.setText(input);
	}
	
	public void displayMsgOnClient(String input){
		displayMsgFieldOnClient.setText(input);
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
