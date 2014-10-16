package authentication;

import gui.Gui;

public class Driver 
{
	private static Authentication client;
	private static Authentication server;
	
	private static char[] clientKey;
	private static char[] serverKey;
	
	private static char[] clientMessage;
	private static char[] serverMessage;
	
	static Gui gui = new Gui();
	
	public static void authentication()
	{	
		//generate keys
		gui.displayLogAppend("Generating keys for authentication...\n");
		
		System.out.println("Generating keys...");
	
		
		gui.displayLogAppend("Start key exchanges\n");
		System.out.println("start key exchanges");
		//get publicDatakey
		gui.displayLogAppend("Getting publicDatakey...\n");
		System.out.println("get publicDatakey");
	
		anthenClient();
		anthenServer();
		
		gui.displayLogAppend("Server key:" +serverKey+"\n");
		gui.displayLogAppend("Client key:"+clientKey+"\n");
		
		System.out.println("Server key:" + serverKey);
		System.out.println("Client key:" + clientKey);
			
		// Step 0
		//Receive partnerDatakey
		gui.displayLogAppend("Receive publicDatakey\n");
		System.out.println("Receive publicDatakey");
		clientReceivePartnerDataKey(serverKey);
		serverReceivePartnerDataKey(clientKey);
		
		// Step 0
		//get PublicSignatureKey
		gui.displayLogAppend("Get PublicSignatureKey\n");
		System.out.println("get PublicSignatureKey");
		clientSendPublicSignatureKey();
		serverSendPublicSignatureKey();
					
		
		// Step 1
		//Receive partnerDatakey
		gui.displayLogAppend("Receive PublicSignatureKey\n");
		System.out.println("Receive PublicSignatureKey");
		clientReceivePartnerSignatureKey(serverKey);
		serverReceivePartnerSignatureKey(clientKey);
		
		// Step 1
		//get FinalKey
		gui.displayLogAppend("Get FinalKey\n");
		System.out.println("get FinalKey");
		clientSendPublicFinalKey();
		serverSendPublicFinalKey();
		
		
		// Step 2
		//Receive FinalKey
		gui.displayLogAppend("Receive FinalKey\n");
		System.out.println("Receive FinalKey");
		clientReceivePartnerFinalKey(serverKey);
		serverReceivePartnerFinalKey(clientKey);
					
		gui.displayLogAppend("End key exchanges\n");
		System.out.println("end key exchanges");
		gui.displayLogAppend("Client public keys:\n");
		System.out.println("client public keys:");
		client.viewPublicKeys();
		gui.displayLogAppend("server version of the client's public keys:\n");
		System.out.println("server version of the client's public keys:");
		server.viewPartnerPublicKeys();
		
		gui.displayLogAppend("server public keys:\n");
		System.out.println("server public keys:");
		server.viewPublicKeys();
		gui.displayLogAppend("client version of the server's public keys:\n");
		System.out.println("client version of the server's public keys:");
		client.viewPartnerPublicKeys();

		gui.displayLogAppend("Starting authentication\n");
		System.out.println("Starting authentication");
		
		gui.displayLogAppend("Client is sending hello message to the server\n");
		System.out.println("client is sending hello message to the server");
		setClientHelloMessage();
		
		gui.displayLogAppend("Server recieves client hello message and sends responce\n");
		System.out.println("server recieves client hello message and sends responce");
		serverResponceToHello(clientMessage);
		
		gui.displayLogAppend("Client recieves server responce message and sends second client message\n");
		System.out.println("client recieves server responce message and sends second client message");
		clientResponceToServer(serverMessage);
		
		gui.displayLogAppend("Server recieves second client message\n");
		System.out.println("server recieves second client message");
		if(serverRecieveSecondClientMessage(clientMessage))
		{
			gui.displayLogAppend("\nThe connection is authenticated and a symetric session key has been established\n");
			System.out.println("the connection is authenticated and a symetric session key has been established");
			gui.displayLogAppend("The client version of the shared session key is: "+client.getSessionKey()+"\n");
			System.out.println("the client version of the shared session key is: " + client.getSessionKey());
			gui.displayLogAppend("the server version of the shared session key is: "+server.getSessionKey()+"\n");
			System.out.println("the server version of the shared session key is: " + server.getSessionKey());
		}
		else
		{
			gui.displayLogAppend("Error in authentication\n");
			System.out.println("error in authentication");
		}
		
		System.out.println("Done");
		
		return;
	}
	
	public static void anthenClient(){
		client = new Authentication();
		client.generateKeyPairs();
		clientKey = client.sendPublicDataKey();
	}
	public static void anthenServer(){
		server = new Authentication();
		server.generateKeyPairs();
		serverKey = server.sendPublicDataKey();
	}
	
	public static char[] clientKey(){
		return clientKey;
	}
	
	public static char[] serverKey(){
		return serverKey;
	}
	
	public static void clientReceivePartnerDataKey(char[] serverKey){
		client.receivePartnerDataKey(serverKey);
	}
	
	public static void serverReceivePartnerDataKey(char[] clientKey){
		server.receivePartnerDataKey(clientKey);
	}
	
	public static void clientSendPublicSignatureKey(){
		clientKey = client.sendPublicSignatureKey();
	}
	
	public static void serverSendPublicSignatureKey(){
		serverKey =  server.sendPublicSignatureKey();
	}
	
	public static void clientReceivePartnerSignatureKey(char [] serverKey){
		client.receivePartnerSignatureKey(serverKey);
	}
	
	public static void serverReceivePartnerSignatureKey(char [] clientKey){
		server.receivePartnerSignatureKey(clientKey);
	}
	
	public static void clientSendPublicFinalKey(){
		clientKey = client.sendPublicFinalKey();
	}
	
	public static void serverSendPublicFinalKey(){
		serverKey = server.sendPublicFinalKey();
	}
	
	public static void clientReceivePartnerFinalKey(char [] serverKey){
		client.receivePartnerFinalKey(serverKey);
	}
	
	public static void serverReceivePartnerFinalKey(char [] clientKey){
		server.receivePartnerFinalKey(clientKey);
	}
	
	public static void setClientHelloMessage(){
		clientMessage = client.clientHelloMessage();
	}
	
	public static void serverResponceToHello(char[] clientMessage){
		serverMessage = server.serverResponceToHello(clientMessage);
	}
	
	public static char[] clientMessage(){
		return clientMessage;
	}
	
	public static char[] serverMessage(){
		return serverMessage;
	}
	
	public static void clientResponceToServer(char[] serverMessage){
		clientMessage = client.clientResponceToServer(serverMessage);
	}
	
	public static Boolean serverRecieveSecondClientMessage(char[] clientMessage){
		return server.serverRecieveSecondClientMessage(clientMessage);
	}
	
}
