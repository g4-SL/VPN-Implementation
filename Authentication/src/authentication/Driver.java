package authentication;

import java.util.Arrays;

public class Driver 
{
	private static SessionIDgen SessionGen = new SessionIDgen();
//	private static final String publicModulus = "99577695919969025724882522395025255927546432972449075404438199764672724820852931393298952421194759053238504769433732843017456203715934280897297137400174876960537576026903911295077100239246758487198262373740677364699010805168966313175248886595379579358492173376998740954019117252283773269631498691756283506917";
//	private static SecureRandom secRand = new SecureRandom();
	
//	private final static int pValue = 47;
//	private final static int gValue = 71;
//	private final static int pValue = 1376410991;
//	private final static int gValue = 2069551451;
	
	public static void main(String[] argv)
	{
		do
		{
		Authentication client =  new Authentication();
		Authentication server =  new Authentication();
		
		System.out.println(SessionGen.nextSessionId());
//		System.out.println(System.getProperty("user.name"));
		//System.out.println(client());
		client.setExponent();
		server.setExponent();
		
		//int a = ;
		//int b = ;//Client myClient = new Client();
		
//		BigInteger Xa = client.getGAmodP();
//		BigInteger Xb = server.getGAmodP();
		
//		System.out.println(Xa + " " + Xb);
		
//		BigInteger sessonKeyA = client.getGAmodP(Xb);
//		BigInteger sessonKeyB = server.getGAmodP(Xa);
				
//		System.out.println(sessonKeyA + " " + sessonKeyB);
		
//		if(!sessonKeyA.equals(sessonKeyB))
//		{
//			System.out.println("Session Key Mismatch");
//		}
		
		client.generateKeyPair();
		server.generateKeyPair();
		DataFrame clientPubKey = client.sendPubPublicKey();
		DataFrame serverPubKey = server.sendPubPublicKey();

		DataFrame clientSigKey = client.sendSigPublicKey();
		DataFrame serverSigKey = server.sendSigPublicKey();
//		//the public key is ~33 bytes long
	
		//Exchange the public keys
		server.receivePublicKey(clientPubKey);
		server.receiveSigKey(clientSigKey);
		client.receivePublicKey(serverPubKey);
		client.receiveSigKey(serverSigKey);
		
		
//		System.out.println("the client public key is: " + client.viewPublicKey());
//		System.out.println("the server partner public key is: " + server.viewPartnerPublicKey());
//		System.out.println("the server public key is: " + server.viewPublicKey());
//		System.out.println("the client partner public key is: " + client.viewPartnerPublicKey());
//		System.out.println("the server exponent is: " + server.viewExponent());
//		System.out.println("the client exponent is: " + client.viewExponent());
//		
		String paddedMessage = "this is a test";
		//paddedMessage = new String(client.padMessage(1,"this is a test"));
		System.out.println("the padded message is: \"" + paddedMessage + "\"");
		System.out.println("the padded message of length is: \"" + paddedMessage.length() + "\"");
		DataFrame testMessage = client.encryptWithPartnerPublicKey(paddedMessage);
		byte[] originalEncryption = testMessage.data;
		System.out.println("the encrypted message of length: \"" + testMessage.data.length + "\"");
		testMessage = client.encryptWithMyPrivateKey(new String(testMessage.data));
		//System.out.println("the encrypted message is: \"" + new String(testMessage.data) + "\"");
		System.out.println("the signed message of length: \"" + testMessage.data.length + "\"");
		
		byte[] decryptedMessage;
		decryptedMessage = server.decryptWithPartnerPublicKey(testMessage);
		System.out.println("the unsigned message of length: \"" + decryptedMessage.length + "\"");
		testMessage.data = decryptedMessage;
		//if(!decryptedMessage.equals(originalEncryption))
		{
			//System.out.println("testMessage.data does not match original encryption");
			for(int i=0;i<decryptedMessage.length;i++)
			{
				if(decryptedMessage[i]!=originalEncryption[i])
				{
					System.out.println(i + " " + decryptedMessage[i] + " " + originalEncryption[i]);
				}
			}
			//break;
		}
		decryptedMessage = server.decryptWithMyPrivateKey(testMessage);
		System.out.println("the unencrypted message of length: \"" + testMessage.data.length + "\"");
		System.out.println("the server decrypted message is: \"" + new String(decryptedMessage) + "\"");
		
		
		
//		String foo = "this is a test";
//		byte[] fubar = foo.getBytes();
//		System.out.println(new String(fubar));
		
//		System.out.println("generating Hello");
//		DataFrame message = client.clientHelloMessage();
//	//	System.out.println("client hello is: \"" + new String(message.data) + "\"");
//		
//		System.out.println("sending Hello to server");
//		message = server.serverResponceToHello(message);
//		System.out.println("sending server responce to client");
//		message = client.clientResponceToServer(message);
//		System.out.println("sending last message to server");
//		server.serverRecieveSecondClientMessage(message);
//		
//		System.out.println(client.getSessionKey());
//		System.out.println(server.getSessionKey());
		
		
//		
//		System.out.println("length of the public key is: " + clientPubKey.toString().length());
//		
//		DataFrame message = client.ecryptWithMyPrivateKey("this is a test");
//		System.out.println("the encrypted message is: \"" + message.data.toString() + "\"");
//		String decryptedMessage = server.decryptWithPartnerPublicKey(message);
//		System.out.println("the server decrypted message is: \"" + decryptedMessage + "\"");
//		
		
		//client.printListOfCoPrimes(10);
//		
//		try {
//			//KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
//			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//			KeyPair myKeyPair = kpg.generateKeyPair();
//			PrivateKey priv = myKeyPair.getPrivate();
//			PublicKey pub = myKeyPair.getPublic();
//			//System.out.println(pub.toString());
//			//System.out.println(priv.toString());
//			
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		}while(false);
		System.out.println("Done");
		
		return;
	}
	
	
	
}
