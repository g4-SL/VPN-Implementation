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

	private static Gui gui = new Gui();
	private static boolean VERBOSE = true;

	// *********** Client ***********************
	private static Socket client;
	private static DataOutputStream output;
	private static BufferedReader input;
	private static PrintWriter outputClient;
	// *********** Server ***********************
	private static ServerSocket server;
	private static Socket clientSocket;
	private static BufferedReader in;
	private static PrintWriter out;


	public VPN(){
		System.out.println("* VPN package is speaking");
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

				System.out.println("* Client thread is running on port " + portNumber + " host " + hostName);

				try {

					// *********** Initialize the Client *****************

					// Open a socket
					client = new Socket(hostName, portNumber);

					// Setup an input stream to receive response from the server
					input = new BufferedReader(new InputStreamReader( client.getInputStream() ));

					// Setup an output stream to send data to the server
					outputClient = new PrintWriter(client.getOutputStream(), true);

					// Client handles incoming data here:
					String received;
					while( (received = input.readLine()) != null){
						System.out.println("Client received: " + received);
						gui.displayMessage(received);
					}

					// *********** Close the socket and streams ***********
					//output.close();
					//input.close();
					//client.close();
					//System.out.println("* Client closed");

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

	/**
	 * Server Thread.
	 */
	public static void runServerThread(final int portNumber){

		(new Thread() {

			@Override
			public void run(){

				System.out.println("* Server thread is running on port " + portNumber);

				// *********** Server ***********************
				
				try {
					server = new ServerSocket(portNumber);
					clientSocket = server.accept();
					in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					out = new PrintWriter(clientSocket.getOutputStream(), true);

					// Handle incoming data here
					String incomingData;
					while ((incomingData = in.readLine()) != null) {
						System.out.println(" Server received: " + incomingData);

						gui.displayMessage(incomingData);

						//out.println("Server echoed: " + incomingData); // echo
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

	/**
	 * Send a message from client to server.
	 */
	public void sendClientMessage(){
		
		outputClient.println(gui.getMessage());
		
		//System.out.println("You typed: " + gui.getMessage());
		//System.out.println("* Client sent out some stuff");
	}
	
	public void sendServerMessage(){
		
		out.println(gui.getMessage());
		
		//System.out.println("You typed: " + gui.getMessage());
		//System.out.println("* Server sent out some stuff");
	}

}
