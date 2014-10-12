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

public class Gui {
	private JFrame 		mainFrame;
	private JLabel 		statusLabel;
	private JPanel 		controlPanel;
	private JPanel 		userTypePanel;
	private String		ipAdd;
	private String		portNum;
	private String		hostName;
	private String		serverMsg;
	private String		clientMsg;
	private boolean		isServerConnected;	//to check if message can be sent over
	private boolean		isClientConnected;	//to check if message can be sent over

    private JTextField 	displayMsgFieldOnServer;
    private JTextField 	displayMsgFieldOnClient;
	
	public Gui(){
		prepareGUI();
	}
	
	private void prepareGUI(){
        mainFrame = new JFrame("VPN EECE 412");
        mainFrame.setSize(800,800);
        mainFrame.setLayout(new FlowLayout());
       
        statusLabel = new JLabel("",JLabel.CENTER); 
        statusLabel.setSize(350,50);
        
        mainFrame.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent windowEvent){
  	        System.exit(0);
           }        
        }); 
        
        controlPanel = new JPanel();  
        controlPanel.setLayout(new GridLayout(2,1));      
        
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
      panel.setLayout(layout); 
      
      // initializing the boolean
      isServerConnected = false;
      isClientConnected = false;
      
      //---------------- message box ---------------------------------------------//

      // for the server
      final JPanel serverMsgPanel = new JPanel();
      final JLabel serverHeaderLabel = new JLabel("MESSAGE FROM THE SERVER");
      final JLabel serverMsgLabel = new JLabel("Enter your message here");
      final JTextField serverMsgTextField = new JTextField(40);
      final JLabel serverDisplayMsgLabel = new JLabel("Received message");
      final JButton serverSendMsgBtn = new JButton("Send Message");
      serverMsgPanel.setLayout(new GridLayout(6,1));
      displayMsgFieldOnServer = new JTextField(40);
      displayMsgFieldOnServer.setText("Wait for received message to display here");
      serverMsgPanel.add(serverHeaderLabel);
      serverMsgPanel.add(serverMsgLabel);
      serverMsgPanel.add(serverMsgTextField);
      serverMsgPanel.add(serverDisplayMsgLabel);
      serverMsgPanel.add(displayMsgFieldOnServer);
      serverMsgPanel.add(serverSendMsgBtn);
      
      serverSendMsgBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  if(isServerConnected){
    			  serverMsg = serverMsgTextField.getText();
    			  statusLabel.setText("Message sent: " + serverMsg);
    		  }
    		  else{
    			  serverMsg = "";
    			  statusLabel.setText("Connect to a server before sending a message" + serverMsg); //serverMsg is there to check if string is cleared
    		  }
    	  }
      });
      
      // for the client
      final JPanel clientMsgPanel = new JPanel();
      final JLabel clientHeaderLabel = new JLabel("MESSAGE FROM THE CLIENT");
      final JLabel clientMsgLabel = new JLabel("Enter your message here");
      final JTextField clientMsgTextField = new JTextField(40);
      final JLabel clientDisplayMsgLabel = new JLabel("Received message");
      final JButton clientSendMsgBtn = new JButton("Send Message");
      clientMsgPanel.setLayout(new GridLayout(6,1));
      displayMsgFieldOnClient = new JTextField(40);
      displayMsgFieldOnClient.setText("Wait for received message to display here");
      clientMsgPanel.add(clientHeaderLabel);
      clientMsgPanel.add(clientMsgLabel);
      clientMsgPanel.add(clientMsgTextField);
      clientMsgPanel.add(clientDisplayMsgLabel);
      clientMsgPanel.add(displayMsgFieldOnClient);
      clientMsgPanel.add(clientSendMsgBtn);
      
      clientSendMsgBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  if(isClientConnected){
	    		  clientMsg = clientMsgTextField.getText();
	    		  statusLabel.setText("Message sent: " + clientMsg);
    		  }
    		  else{
    			  clientMsg = "";
    			  statusLabel.setText("Connect to a client before sending a message" + clientMsg); //clientMsg is there to check if string is cleared
    		  }
		 }          
      });

      //----------- SERVER -------------------------------------//
      
      JPanel serverPanel = new JPanel();
      GroupLayout serverLayout = new GroupLayout(serverPanel);
      serverLayout.setAutoCreateGaps(true);
      serverLayout.setAutoCreateContainerGaps(true);
      
      JLabel portNumLabel = new JLabel("Enter port number");
      final JTextField portNumText = new JTextField(40);
      JButton connectServerBtn = new JButton("Connect");
      JButton cancelServerBtn = new JButton("Cancel");
      
      serverLayout.setHorizontalGroup(serverLayout.createSequentialGroup()
		 .addGroup(serverLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(portNumLabel)
	         .addComponent(portNumText)
	         .addGroup(serverLayout.createSequentialGroup()
        		 .addComponent(connectServerBtn)
                 .addComponent(cancelServerBtn) 
    		 )
    		 .addComponent(serverMsgPanel)
         )      
      );
      
      serverLayout.setVerticalGroup(serverLayout.createSequentialGroup()
         .addComponent(portNumLabel)
         .addComponent(portNumText)
         .addGroup(serverLayout.createParallelGroup(
           GroupLayout.Alignment.LEADING)
           .addComponent(connectServerBtn)
           .addComponent(cancelServerBtn) 
		 )
         .addComponent(serverMsgPanel)
      );
      
      connectServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  portNum = portNumText.getText();
    		  if(portNum.equals(""))
				  statusLabel.setText("port number is missing");
    		  else{
        		  /*** might need to change logic of checking if server is connected later on ***/
        		  isServerConnected = true;	
        		  statusLabel.setText("Port number: " + portNum);
    		  }
		 }          
      });
      
      cancelServerBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  isServerConnected = false;	
    		  portNumText.setText("");
    		  statusLabel.setText("Cancel server");
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
      JButton connectClientBtn = new JButton("Connect");
      JButton cancelClientBtn = new JButton("Cancel");
      
      clientLayout.setHorizontalGroup(clientLayout.createSequentialGroup()
		 .addGroup(clientLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(ipAddLabel)
	         .addComponent(ipAddText)
	         .addComponent(hostNameLabel)
	         .addComponent(hostNameText)
	         .addGroup(clientLayout.createSequentialGroup()
        		 .addComponent(connectClientBtn)
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
         	.addGroup(clientLayout.createParallelGroup(
               GroupLayout.Alignment.LEADING)
               .addComponent(connectClientBtn)
               .addComponent(cancelClientBtn)       
            )   
   		 .addComponent(clientMsgPanel)                             
      );
      
      connectClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  ipAdd = ipAddText.getText();
    		  hostName = hostNameText.getText();
    		  if(ipAdd.equals(""))
				  statusLabel.setText("IP address is missing");
    		  else if(hostName.equals(""))
				  statusLabel.setText("host name is missing");
    		  else{
				  /*** might need to change logic of checking if client is connected later on ***/
				  isClientConnected = true;	
				  statusLabel.setText("IP: " + ipAdd + " and host name: " + hostName);
    		  }
		 }          
      });
      
      cancelClientBtn.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  isClientConnected = false;	
    		  ipAddText.setText("");
    		  hostNameText.setText("");
    		  statusLabel.setText("Cancel client");
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
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	Gui showGUI = new Gui();      
    	showGUI.showLayout();
    }
}

