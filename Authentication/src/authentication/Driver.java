package authentication;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
	/*	Authentication client =  new Authentication();
		Authentication server =  new Authentication();
		
//		System.out.println(SessionGen.nextSessionId());
//		System.out.println(System.getProperty("user.name"));
		//System.out.println(client());
		
		client.setExponent();	//set exponent for DH
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
		
		client.generateKeyPair();	//generate keyPairs
		server.generateKeyPair();
		
		//get public keys
		DataFrame clientPubKey = client.sendPubPublicKey();
		DataFrame serverPubKey = server.sendPubPublicKey();

		//get public signing keys
		DataFrame clientSigKey = client.sendSigPublicKey();
		DataFrame serverSigKey = server.sendSigPublicKey();
//		//the public key is ~33 bytes long
	
		//Exchange the public keys
		server.receivePublicKey(clientPubKey);
		client.receivePublicKey(serverPubKey);
		
		//exchange signature keys, a Signature object is created
		server.receiveSigKey(clientSigKey);
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
	//	System.out.println("the padded message of length is: \"" + paddedMessage.length() + "\"");
		DataFrame testMessage = new DataFrame();
		testMessage.data = paddedMessage.getBytes();
	//	testMessage = client.encryptWithPartnerPublicKey(new String(testMessage.data));
	//	byte[] originalEncryption = testMessage.data;
	//	System.out.println("the encrypted message of length: \"" + testMessage.data.length + "\"");
	//	testMessage = client.encryptWithMyPrivateKey(new String(testMessage.data));
		//System.out.println("the encrypted message is: \"" + new String(testMessage.data) + "\"");
	//	System.out.println("the signed message of length: \"" + testMessage.data.length + "\"");
	
		testMessage.data = client.signMessage(testMessage.data); 
	
		byte[] decryptedMessage;
		if(server.verifyMessage(testMessage.data))
		{
			System.out.println("the message is verified");
			decryptedMessage = testMessage.data;
		}
		else
		{
			System.out.println("the message is not verified");
			//break;
			decryptedMessage = testMessage.data;
		}
	//	decryptedMessage = server.decryptWithPartnerPublicKey(testMessage);
	//	System.out.println("the unsigned message of length: \"" + decryptedMessage.length + "\"");
	//	testMessage.data = decryptedMessage;
		//if(!decryptedMessage.equals(originalEncryption))
		{
			//System.out.println("testMessage.data does not match original encryption");
	//		for(int i=0;i<decryptedMessage.length;i++)
			{
	//			if(decryptedMessage[i]!=originalEncryption[i])
				{
	//				System.out.println(i + " " + decryptedMessage[i] + " " + originalEncryption[i]);
				}
			}
			//break;
		}
	//	decryptedMessage = server.decryptWithMyPrivateKey(testMessage);
		System.out.println("the unencrypted message of length: \"" + testMessage.data.length + "\"");
		System.out.println("the server decrypted message is: \"" + new String(decryptedMessage) + "\"");
		
		System.out.println(client.viewSigKey());
		System.out.println(server.viewPartnerSigKey());
		
		System.out.println(client.viewMySig());
		System.out.println(server.viewPartnerSig());
	*/	
			
		Authentication client = new Authentication();
		Authentication server = new Authentication();
		
		//generate keys
		System.out.println("generate keys");
		client.generateKeyPairs();
		server.generateKeyPairs();
		
		//get publicDatakey
		System.out.println("get publicDatakey");
		byte[] clientKey = client.sendPublicDataKey();
		byte[] serverKey = server.sendPublicDataKey();
			
		//Receive partnerDatakey
		System.out.println("Receive partnerDatakey");
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
					
		
	//	client.viewPublicKeys();
	//	server.viewPartnerPublicKeys();
		
//		String message = "this is a test";
//		System.out.println("send message");
//		byte[] encryptedMessage = client.encryptAndSignData(message.getBytes());
//		System.out.println("decrypte message");
//		byte[] recoveredMessage = server.decryptAndVerifyData(encryptedMessage);
//		System.out.println(new String(recoveredMessage));
		
		byte[] clientServerMessage = client.clientHelloMessage();
		clientServerMessage = server.serverResponceToHello(clientServerMessage);
		clientServerMessage = client.clientResponceToServer(clientServerMessage);
		server.serverRecieveSecondClientMessage(clientServerMessage);
		
		System.out.println(client.getSessionKey());
		System.out.println(server.getSessionKey());
		/*
		try 
		{
			int rsaKeySize = 1024;
			KeyPairGenerator kpg;
			SecureRandom secRand = new SecureRandom();
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(rsaKeySize, secRand);
			KeyPair keyData = kpg.generateKeyPair();
			KeyPair keyFoo = kpg.generateKeyPair();
			kpg.initialize((2*rsaKeySize + 88), new SecureRandom());	//this key is 2*n+88 where n is the size of the smaller keys
			KeyPair keyFinal = kpg.generateKeyPair();
			
			
			Signature sigSign =  Signature.getInstance("SHA1withRSA");
			sigSign.initSign(keyFoo.getPrivate(),secRand);
			Signature sigVer =  Signature.getInstance("SHA1withRSA");
			sigVer.initVerify(keyFoo.getPublic());
			
			String text = "this is a test";
			
			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.ENCRYPT_MODE, keyData.getPublic());
			byte[] dataEncrypted = rsa.doFinal(text.getBytes());
			
			byte[] buffer = dataEncrypted;//text.getBytes();
			sigSign.update(buffer);
			byte[] realSig = sigSign.sign();
			System.out.println(realSig.length);
			
			//Concatenate data
			byte[] message = new byte[buffer.length + realSig.length];
			System.arraycopy(buffer, 0, message, 0, buffer.length);
			System.arraycopy(realSig, 0, message, buffer.length, realSig.length);
			
			rsa.init(Cipher.ENCRYPT_MODE,keyFinal.getPublic());
			byte[] encryptedMessage = rsa.doFinal(message);
			
			
			rsa.init(Cipher.DECRYPT_MODE, keyFinal.getPrivate());
			message = rsa.doFinal(encryptedMessage);
			
			buffer = Arrays.copyOfRange(message, 0, rsaKeySize/8);
			realSig = Arrays.copyOfRange(message, rsaKeySize/8, message.length);
			
			sigVer.update(buffer);
			boolean result = sigVer.verify(realSig);
			if(result)
			{
				System.out.println("the message is verified");
			//	System.out.println("the message is: " + new String(buffer));
			}
			else
			{
				System.out.println("the message not is verified");
				break;
			}
			
			rsa.init(Cipher.DECRYPT_MODE, keyData.getPrivate());
			byte[] decryptedMessage = rsa.doFinal(buffer);
			
			System.out.println("the message is: " + new String(decryptedMessage));
			
		} 
		catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
	//	System.out.println(server.verifyMessage(client.signMessage(new String("foo").getBytes())));
		
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
