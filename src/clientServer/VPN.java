package clientServer;

import gui.Gui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Author: Group 8
 * Date: October 11, 2014
 * Course: EECE 412, Assignment 3
 * Purpose: Implements a client and server threads using TCP connection
 */

public class VPN{
	
	private static Gui GuiThatIWant = new Gui();
	
	
	
	public VPN(){
		System.out.println("* VPN package is speaking");
	}
	
	
	public void setGUI(Gui transporter ){
		
		GuiThatIWant = transporter;
	}
	
	public static void runClientThread(final int portNumber, final String hostName){

		(new Thread() {

			@Override
			public void run(){

				System.out.println("* Client thread is running on port " + portNumber + " host " + hostName);
				
				// *********** CLIENT ***********************		

				Socket client;
				DataOutputStream output;
				BufferedReader input;

				try {

					// *********** Initialize the Client *****************

					// Open a socket
					client = new Socket(hostName, portNumber);
					//System.out.println("* Client opens a socket");

					// Setup an input stream to receive response from the server
					input = new BufferedReader(new InputStreamReader( client.getInputStream() ));
					//System.out.println("* Client sets up Input Stream");

					// Setup an output stream to send data to the server
					output = new DataOutputStream(client.getOutputStream());
					//System.out.println("* Client sets up Output Stream ");
					

					// *********** Send/Receive some stuff ***************

					output.writeBytes("Hola, yo soy Olaf\n");
					System.out.println("* Client sent out some stuff");

					String received;
					while( (received = input.readLine()) != null){
						System.out.println("Client received: " + received);
						
						GuiThatIWant.displayMessage(received);
						
						//Gui myGui = new Gui();
						//myGui.displayMessage(received);
					}

					// *********** Close the socket and streams ***********
					output.close();
					input.close();
					client.close();
					System.out.println("* Client closed");

				}
				catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					System.out.println("*** Client Exception: unknown host");
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("*** Client IO Exception");
					e.printStackTrace();
				}

				// *********** END OF CLIENT ***********************
				
			}
		} ).start();
	}
	
	
	public static void runServerThread(final int portNumber){

		(new Thread() {

			@Override
			public void run(){

				System.out.println("* Server thread is running on port " + portNumber);

				// *********** Server ***********************

				ServerSocket server;
				try {
					server = new ServerSocket(portNumber);
					Socket clientSocket = server.accept();

					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));

					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

					String incomingData;
					while ((incomingData = in.readLine()) != null) {
						System.out.println("Server received: " + incomingData);
						out.println("Server echoes: " + incomingData); // echo
						//out.println(" Hola! Como te va?");
					}

					// Close streams and sockets
					in.close();
					out.close();
					clientSocket.close();
					server.close();
					
					System.out.println("* Server closed");

				}
				catch (IOException e) {
					System.out.println("*** Server IO Exception");
					e.printStackTrace();
				}

				// *********** End of Server ***********************
				 
			}
		} ).start();
	}
	
}
