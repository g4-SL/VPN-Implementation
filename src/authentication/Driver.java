package authentication;

public class Driver 
{
	
	public static void main(String[] argv)
	{	
		Authentication client = new Authentication();
		Authentication server = new Authentication();
		
		//generate keys
		System.out.println("generate keys");
		client.generateKeyPairs();
		server.generateKeyPairs();
		
		System.out.println("start key exchanges");
		//get publicDatakey
		System.out.println("get publicDatakey");
		char[] clientKey = client.sendPublicDataKey();
		char[] serverKey = server.sendPublicDataKey();
			
		//Receive partnerDatakey
		System.out.println("Receive publicDatakey");
		client.receivePartnerDataKey(serverKey);
		server.receivePartnerDataKey(clientKey);
		
		//get PublicSignatureKey
		System.out.println("get PublicSignatureKey");
		clientKey = client.sendPublicSignatureKey();
		serverKey = server.sendPublicSignatureKey();
					
		//Receive partnerDatakey
		System.out.println("Receive PublicSignatureKey");
		client.receivePartnerSignatureKey(serverKey);
		server.receivePartnerSignatureKey(clientKey);
			
		//get FinalKey
		System.out.println("get FinalKey");
		clientKey = client.sendPublicFinalKey();
		serverKey = server.sendPublicFinalKey();
		
		//Receive FinalKey
		System.out.println("Receive FinalKey");
		client.receivePartnerFinalKey(serverKey);
		server.receivePartnerFinalKey(clientKey);
					
		System.out.println("end key exchanges");
		System.out.println("client public keys:");
		client.viewPublicKeys();
		System.out.println("server version of the client's public keys:");
		server.viewPartnerPublicKeys();
		
		System.out.println("server public keys:");
		server.viewPublicKeys();
		System.out.println("client version of the server's public keys:");
		client.viewPartnerPublicKeys();


		System.out.println("starting authentication");
		
		System.out.println("client is sending hello message to the server");
		char[] clientMessage = client.clientHelloMessage();
		
		
		System.out.println("server recieves client hello message and sends responce");
		char[] serverMessage = server.serverResponceToHello(clientMessage);
		
		
		System.out.println("client recieves server responce message and sends second client message");
		clientMessage = client.clientResponceToServer(serverMessage);
		
		
		System.out.println("server recieves second client message");
		if(server.serverRecieveSecondClientMessage(clientMessage))
		{
			System.out.println("the connection is authenticated and a symetric session key has been established");
			System.out.println("the client version of the shared session key is: " + client.getSessionKey());
			System.out.println("the server version of the shared session key is: " + server.getSessionKey());
		}
		else
		{
			System.out.println("error in authentication");
		}
		
		System.out.println("Done");
		
		return;
	}
	
	
	
}
