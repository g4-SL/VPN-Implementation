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

public class Gui {
	private JFrame 		mainFrame;
	private JLabel 		headerLabel;
	private JLabel 		statusLabel;
	private JPanel 		controlPanel;
	
	public Gui(){
		prepareGUI();
	}
	
	private void prepareGUI(){
        mainFrame = new JFrame("VPN EECE 412");
        mainFrame.setSize(600,500);
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

      JPanel serverPanel = new JPanel();
      GroupLayout serverLayout = new GroupLayout(serverPanel);
      serverLayout.setAutoCreateGaps(true);
      serverLayout.setAutoCreateContainerGaps(true);
      
      JLabel portNumLabel = new JLabel("Enter port number");
      JTextField portNumText = new JTextField(20);
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
      );
      
      JPanel clientPanel = new JPanel();
      GroupLayout clientLayout = new GroupLayout(clientPanel);
      clientLayout.setAutoCreateGaps(true);
      clientLayout.setAutoCreateContainerGaps(true);
      
      JLabel ipAddLabel = new JLabel("Enter IP address");
      JTextField ipAddText = new JTextField(20);
      JButton connectClientBtn = new JButton("Connect");
      JButton cancelClientBtn = new JButton("Cancel");
      
      clientLayout.setHorizontalGroup(clientLayout.createSequentialGroup()
		 .addGroup(clientLayout.createParallelGroup(
			 GroupLayout.Alignment.LEADING)
	         .addComponent(ipAddLabel)
	         .addComponent(ipAddText)
	         .addGroup(clientLayout.createSequentialGroup()
        		 .addComponent(connectClientBtn)
                 .addComponent(cancelClientBtn) 
    		 )
         )      
      );
      
      clientLayout.setVerticalGroup(clientLayout.createSequentialGroup()
         .addComponent(ipAddLabel)
         .addComponent(ipAddText)
         	.addGroup(clientLayout.createParallelGroup(
               GroupLayout.Alignment.LEADING)
               .addComponent(connectClientBtn)
               .addComponent(cancelClientBtn)       
            )                                
      );

      serverPanel.setLayout(serverLayout);
      clientPanel.setLayout(clientLayout);
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

