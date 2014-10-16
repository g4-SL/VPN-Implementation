package clientServer;

import gui.Gui;
import encrytpion.encryption;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * Author: Group 8
 * Last Modified: October 15, 2014
 * Course: EECE 412, Assignment 3
 * Purpose: Implements a client and server threads using TCP connection
 */

public class VPN{

	private static Gui gui;	

	// ----------- Client ----------------//
	private static Socket client;
	private static BufferedReader input;
	private static PrintWriter outputClient;
	// ----------- Server ----------------//
	private static ServerSocket server;
	private static Socket clientSocket;
	private static BufferedReader in;
	private static PrintWriter out;
	private static int portNumServer;
	
	private static encryption en;
	
	private static boolean serverMode = false; // true is authentication mode, false is encryption mode
	private static boolean clientMode = false; // true is authentication mode, false is encryption mode
	private static String authClientMsg;
	private static String authServerMsg;
	
	public VPN(){
		en = new encryption();
		System.out.println("VPN package is speaking");
	}

	/**
	 * Set access to GUI to display a message.
	 */
	public void setGUI(Gui transporter ){
		gui = transporter;
	}

	/**
	 * Client Thread.
	 */
	public static void runClientThread(final int portNumber, final String hostName){
		(new Thread() {
			@Override
			public void run(){
				System.out.println(" Client thread is running on port " + portNumber + " host " + hostName);
				try {

					// -------------- Initialize the Client ---------------//

					// Open a socket
					client = new Socket(hostName, portNumber);

					// Setup an input stream to receive response from the server
					input = new BufferedReader(new InputStreamReader( client.getInputStream() ));

					// Setup an output stream to send data to the server
					outputClient = new PrintWriter(client.getOutputStream(), true);

					// --------- Client handles its incoming data here ---------//
							
					String received;
					while( (received = input.readLine()) != null){

						if(clientMode == true){
							System.out.println("Client is in Authentication mode");						
							authClientMsg = received;
							System.out.println("Client msg received (copy): " + authClientMsg);
							System.out.println("Client msg received (original): " + received);
						}
						else{
							System.out.println("Client is in Encryption mode");
							System.out.println("Client encrypted msg received: " + en.decrypt(received,gui.getSharedKeyClient()));
							gui.displayMsgOnClient(en.decrypt(received,gui.getSharedKeyClient()));
						}
					}				

					// ----------- Close the client's socket and streams ----------//
					//output.close();
					//input.close();
					//client.close();
					//System.out.println(" Client closed");

				}
				catch (UnknownHostException e) {
					System.out.println("UnknownHostException: unknown host");
					System.out.println("Please enter a valid host name.");
				}
				catch (IOException e) {
					System.out.println("ConnectException: No process is listening on the remote port.");
					System.out.println("Please set up the server first.");
				}
			}
		} ).start();
	}

	/**
	 * Server Thread.
	 */
	public static void runServerThread(final int portNumber){
		(new Thread() {
			@Override
			public void run(){
				portNumServer = portNumber;
				System.out.println(" Server thread is running on port " + portNumber);
				try {
					
					// ----------------- Server ----------------------//
					server = new ServerSocket(portNumber);
					clientSocket = server.accept();
					in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					out = new PrintWriter(clientSocket.getOutputStream(), true);

					// -------- Server handles its incoming data here --------//
					
					String incomingData;
					while ((incomingData = in.readLine()) != null) {

						if(serverMode == true){
							System.out.println("Server is in Authentication mode");							
							authServerMsg = incomingData;
							System.out.println("Server msg received (copy): " + authServerMsg);
							System.out.println("Server msg received (original): " + incomingData);
						}
						else{
							System.out.println("Server is in Encryption mode");
							System.out.println("Server encrypted msg received: " + incomingData);
							gui.displayMsgOnServer(en.decrypt(incomingData, gui.getSharedKeyServer()));
						}
					}
					
					// ----------- Close the server's socket and streams ---------//
					//in.close();
					//out.close();
					//clientSocket.close();
					//server.close();

					//System.out.println("* Server closed");

				}
				catch (IOException e) {
					System.out.println("Server IO Exception. Please try again.");
					try {
						clientSocket.close();
						out.close();
						in.close();
						server.close();
						runServerThread(portNumServer);
					} catch (IOException e1) {System.out.println("test");	}
					//e.printStackTrace();
				}

			}
		} ).start();
	}

	
	/**
	 * Send a message from client to server.
	 */
	public void sendClientMessage(){
		outputClient.println(en.encrypt(gui.getClientMsg(), gui.getSharedKeyClient()));
		gui.setLogMsg(en.returnLog_en(),en.returnLog_de());
		gui.clearLogMsgClient("Message Sent.\n");
		gui.clearLogMsgServer("");
	}

	/**
	 * Send a message from server to client.
	 */
	public void sendServerMessage(){
		out.println(en.encrypt(gui.getServerMsg(),gui.getSharedKeyServer()));
		gui.setLogMsg(en.returnLog_de(),en.returnLog_en());
		gui.clearLogMsgClient("");
		gui.clearLogMsgServer("Message sent.\n");
	}
	
	/**
	 * Send a char array message from client to server.
	 * @Module: Authentication.
	 */
	public void sendAuthClientMessage(char[] charArr){		
		outputClient.println(charArr);
	}

	/**
	 * Send a char array message from server to client.
	 * @Module: Authentication.
	 */
	public void sendAuthServerMessage(char[] charArr){
		out.println(charArr);
	}
	
	/**
	 * Returns the client's received message as a String.
	 * @Module: Authentication.
	 */
	public String getAuthClientString(){
		return authClientMsg;				
	}
	
	/**
	 * Returns the server's received message as a String.
	 * @Module: Authentication.
	 */
	public String getAuthServerString(){
		return authServerMsg;
	}
	
	/*
	 * Set the client mode of operation.
	 * True is encryption, false is authentication.
	 */
	public void setClientMode(boolean mode){
		clientMode = mode;
	}
	
	/*
	 * Set the server mode of operation.
	 * True is encryption, false is authentication.
	 */
	public void setServerMode(boolean mode){
		serverMode = mode;
	}

}
