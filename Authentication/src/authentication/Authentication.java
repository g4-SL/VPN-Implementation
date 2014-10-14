package authentication;


import java.awt.Frame;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.PartialResultException;


public class Authentication 
{
	private SessionIDgen SessionGen = new SessionIDgen();
	private SecureRandom secRand = new SecureRandom();
	private int exponent = 0;
	private KeyPair myPublicKeyPair = null;
	private KeyPair mySigningKeyPair = null;
	private PublicKey partnerPubKey = null;
	private PublicKey partnerSigKey = null;
	private String sessionID = null;
	private BigInteger sessionKey = null;
	
	private final static int rsaPubKeySize = 800;
	private final static int rsaSigKeySize = 1024;
	//the signing key needs to be 11 bytes (88 bits) larger than the public key
	
//	private final static int pValue = 47;
//	private final static int gValue = 71;
//	private final static int XaValue = 9;
//	private final static int XbValue = 14;
//	
//	private final static int pValue = 47;
//	private final static int gValue = 71;
	private final static int pValue = 1376410991;
	private final static int gValue = 2069551451;
	
	public String buildMessage()
	{
		String retVal = new String();
		//setExponent();
		
		return retVal;
	}
	
	
//	public static String getGAmodP()
//	{
//		String retVal;
//		int a; //PRIVATE exponent value
//		a = rand.nextInt();
//		
//		BigInteger p = new BigInteger(Integer.toString(pValue));
//		BigInteger g = new BigInteger(Integer.toString(gValue));
//		BigInteger Xa = new BigInteger(Integer.toString(XaValue));
//		BigInteger Xb = new BigInteger(Integer.toString(XbValue));
//		
//		
//		Xa = g.modPow(new BigInteger(Integer.toString(a)), p);
//		retVal = Xa.toString();
//		return retVal;
//				
//	}
	
	public BigInteger getGAmodP()
	{
		//String retVal;
		//int a; //PRIVATE exponent value
		//a = rand.nextInt();
		
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(Integer.toString(gValue));
		BigInteger Xa;// = new BigInteger(Integer.toString(XaValue));
		
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
		//retVal = Xa.toString();
		return Xa;
				
	}
	
	public BigInteger getGAmodP( int gValue)
	{
		//String retVal;
		//int a; //PRIVATE exponent value
		//a = rand.nextInt();
		
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(Integer.toString(gValue));
		BigInteger Xa;// = new BigInteger(Integer.toString(XaValue));
		
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
		//retVal = Xa.toString();
		return Xa;
				
	}
	
	public BigInteger getGAmodP( BigInteger gValue)
	{
		//String retVal;
		//int a; //PRIVATE exponent value
		//a = rand.nextInt();
		
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(gValue.toString());
		BigInteger Xa;
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
		//retVal = Xa.toString();
		return Xa;
				
	}
	
//	public int getExponent()
//	{
//		return exponent;
//	}
	
	public void setExponent()
	{
		exponent = secRand.nextInt();//gives a secure random number. all 2^32 possible int values are used
		//NOTE: is is not an issue if a negative exponent is generated
	}
	
	public DataFrame sendPubPublicKey()
	{
		DataFrame myFrame = new DataFrame();
		myFrame.data = myPublicKeyPair.getPublic().getEncoded();
		
		return myFrame;
	}
	
	public DataFrame sendSigPublicKey()
	{
		DataFrame myFrame = new DataFrame();
		myFrame.data = mySigningKeyPair.getPublic().getEncoded();
		
		return myFrame;
	}
	
	public void receivePublicKey(DataFrame receivedFrame)
	{
		byte[] rPubKey = receivedFrame.data;
		X509EncodedKeySpec ks = new X509EncodedKeySpec(rPubKey);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerPubKey = kf.generatePublic(ks);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InvalidKeySpecException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveSigKey(DataFrame receivedFrame)
	{
		byte[] rPubKey = receivedFrame.data;
		X509EncodedKeySpec ks = new X509EncodedKeySpec(rPubKey);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerSigKey = kf.generatePublic(ks);
		} 
		catch (NoSuchAlgorithmException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InvalidKeySpecException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String viewPrivateKey()
	{
		return myPublicKeyPair.getPrivate().toString();
	}
	
	public String viewExponent()
	{
		return Integer.valueOf(exponent).toString();
	}
	
	public String viewPublicKey()
	{
		return myPublicKeyPair.getPublic().toString();
	}
	
	public String viewPartnerPublicKey()
	{
		return partnerPubKey.toString();
	}
	
	public String getSessionKey()
	{
		return sessionKey.toString();
	}
	
	public void generateKeyPair()
	{
		KeyPairGenerator kpg1;
		KeyPairGenerator kpg2;
		try 
		{
			kpg1 = KeyPairGenerator.getInstance("RSA");
			kpg1.initialize(rsaPubKeySize, secRand);
			myPublicKeyPair = kpg1.generateKeyPair();
			kpg2 = KeyPairGenerator.getInstance("RSA");
			kpg2.initialize(rsaSigKeySize, secRand);
			mySigningKeyPair = kpg2.generateKeyPair();
		//	PrivateKey priv = myKeyPair.getPrivate();
		//	PublicKey pub = myKeyPair.getPublic();
		//	System.out.println(pub.toString());
		//	System.out.println(priv.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No RSA algorithem found");
		}
	}
	
	public byte[] padMessage(int keySize, String message )
	{
		byte[] retVal;
		int size;
		String temp = new String(message);
		if(keySize==1)
		{
			size = rsaPubKeySize;
		}
		else
		{
			size = rsaSigKeySize;
		}
		for(int i=message.length();i<(size/8 -11);i++)
		{
			temp = temp + "0";
		}
		retVal = temp.getBytes();
		
		return retVal;
	}
	
	public DataFrame encryptWithMyPrivateKey(String text)
	{//signing key (large key)
		DataFrame retVal = new DataFrame();
		//System.out.println(mySigningKeyPair.getPrivate().toString());
		retVal.data = encrypt(mySigningKeyPair.getPrivate(), text);
		return retVal;
	}
	
	public DataFrame encryptWithPartnerPublicKey(String text)
	{//public key (small key)
		DataFrame retVal = new DataFrame();
		//System.out.println(partnerPubKey.toString());
		retVal.data = encrypt(partnerPubKey, text);
		return retVal;
	}
	
	public byte[] decryptWithPartnerPublicKey(DataFrame message)
	{//signing key (large key)
		byte[] retVal = null;
		//System.out.println(partnerSigKey.toString());
		retVal = decrypt(partnerSigKey, message.data);
		return retVal;
	}
	
	public byte[] decryptWithMyPrivateKey(DataFrame message)
	{//public key (small key)
		byte[] retVal = null;
		//System.out.println(myPublicKeyPair.getPrivate().toString());
		retVal = decrypt(myPublicKeyPair.getPrivate(), message.data);
		return retVal;
	}
	
	private static byte[] encrypt(Key encryptionKey, String text)
	{
		byte[] retVal = null;
		Cipher rsa;
		try 
		{
			//System.out.println("the text is " + text.length() + " bytes long");
			rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.ENCRYPT_MODE, encryptionKey);
			retVal = rsa.doFinal(text.getBytes());
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in rsa encryption");
		}
		return retVal;
	}
	
	private static byte[] decrypt(Key decryptionKey, byte[] buffer)
	{
		byte[] retVal = null;
		Cipher rsa;
		//System.out.println(decryptionKey.toString());
		try 
		{
			rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, decryptionKey);
			byte[] utf8 = rsa.doFinal(buffer);
			retVal = utf8;
			//retVal = new String(utf8,"UTF8");//.getBytes();
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in rsa decryption");
		}
		return retVal;
	}
	
	public DataFrame clientHelloMessage()
	{
		DataFrame retVal = new DataFrame();
		String data = null;

		sessionID = SessionGen.nextSessionId();
	//	System.out.println("client sessionID: \"" + sessionID + "\"");
		//byte[] publicKey = sendPublicKey().data;
		data = sessionID;// + publicKey.toString();
	//	System.out.println("client data: \"" + data + "\"");
		
		retVal.data = data.getBytes();
	//	System.out.println("client hello retVal is: \"" + new String(retVal.data) + "\"");
		return retVal;
	}
	
	public DataFrame serverResponceToHello(DataFrame hello)
	{
		DataFrame retVal = new DataFrame();
		sessionID = SessionGen.nextSessionId();
		System.out.println("server sessionID: \"" + sessionID + "\"");
		//byte[] clientMessage = hello.data;
		System.out.println("recovering client sessionID");
		
		//String clientSessionID = Arrays.copyOfRange(hello.data, 0, sessionID.length()).toString();
		String clientSessionID = new String(hello.data);
		System.out.println("server version of client sessionID: \"" + clientSessionID + "\"");
		//DataFrame clientPublickKeyDataFrame = new DataFrame();
		//System.out.println("recovering client public key");
		
		//clientPublickKeyDataFrame.data = Arrays.copyOfRange(hello.data, sessionID.length(), hello.data.length);
		
		//System.out.println("inserting client public key to partnerPublicKey");
		//receivePublicKey(clientPublickKeyDataFrame);	//insert the client publick key into the partnerPublicKey variable
		//System.out.println("setting exponent");
		//setExponent();
		BigInteger Xb = getGAmodP();
		
		String messageToClient = clientSessionID + Xb;
		
		System.out.println("encryting message to client");
		

		System.out.println("unencrypted message Size: " + messageToClient.length());
		byte[] partnerEncrypted = encrypt(partnerPubKey, messageToClient);
		System.out.println("encrypted Size: " + partnerEncrypted.length);
		System.out.println("signing message to client");
		byte[] signedMessageToClient = encrypt(mySigningKeyPair.getPrivate(),new String(partnerEncrypted));
		System.out.println("serverSignedMessage Size: " + signedMessageToClient.length);
		messageToClient = sessionID + new String(signedMessageToClient);
		retVal.data = messageToClient.getBytes();
		return retVal;
	}
	
	public DataFrame clientResponceToServer(DataFrame serverMessage) throws IllegalStateException
	{
		DataFrame retVal = new DataFrame();
		String serverSessionID = new String(Arrays.copyOfRange(serverMessage.data, 0, sessionID.length()));
		//DataFrame serverSignedMessage = new DataFrame();
		byte[] serverSignedMessage = Arrays.copyOfRange(serverMessage.data, sessionID.length(), serverMessage.data.length);
		System.out.println("decrypting server message");
		System.out.println("serverSignedMessage Size: " + serverSignedMessage.length);
		byte[] serverMessageMyPubKey = decrypt(partnerSigKey, serverSignedMessage);
		System.out.println("server signature removed");
		byte[] decryptedServerMessage = decrypt(myPublicKeyPair.getPrivate(),serverMessageMyPubKey);
		System.out.println("server message decrypted");
		
		if( !(new String(Arrays.copyOfRange(decryptedServerMessage, 0, sessionID.length())).equals(sessionID)) )
		{
			throw new IllegalStateException("SessionID mismatch");
		}
		System.out.println("session ID's match");
		
		//setExponent();
		BigInteger Xb = new BigInteger(Arrays.copyOfRange(decryptedServerMessage, sessionID.length(), decryptedServerMessage.length));
		sessionKey = getGAmodP(Xb);
		
		BigInteger Xa = getGAmodP();
		
		String messageToServer = serverSessionID + Xa;
		byte[] partnerEncrypted = encrypt(partnerPubKey, messageToServer);
		retVal.data = encrypt(mySigningKeyPair.getPrivate(),new String(partnerEncrypted));
		
		
		return retVal;
	}
	
	public void serverRecieveSecondClientMessage(DataFrame clientMessage) throws IllegalStateException
	{
		byte[] unsignedClientMessage = decrypt(partnerSigKey, clientMessage.data);
		byte[] decryptedClientMessage = decrypt(myPublicKeyPair.getPrivate(),unsignedClientMessage);
		
		if(!(new String(Arrays.copyOfRange(decryptedClientMessage, 0, sessionID.length())).equals(sessionID)))
		{
			throw new IllegalStateException("SessionID mismatch");
		}
		
		BigInteger Xa = new BigInteger(Arrays.copyOfRange(decryptedClientMessage, sessionID.length(), decryptedClientMessage.length));
		sessionKey = getGAmodP(Xa);
		
	}
	
	private int hcf(int a, int h)
	{	//used for calculating co-primes
		int temp;
		while(true)
		{
			temp = a%h;
			if(0==temp)
			{
				return h;
			}
			a=h;
			h=temp;
		}
	}
	
	private boolean checkCoPrime(int num1, int num2)
	{	//used for calculating co-primes
		boolean retVal = false;
		
		if(1==hcf(num1,num2))
		{
			retVal = true;
		}
		return retVal;
	}
	
	public void printListOfCoPrimes(int n)
	{	//prints a list of n pairs of co-prime numbers
		//these co-primes can be used as the public g and p values
		Random unsecureRandom = new Random();
		for(int i=0;i<n;i++)
		{
			int num1,num2;
			do
			{
				num1 = unsecureRandom.nextInt();
			}while(0>num1);
			do
			{
				num2 = unsecureRandom.nextInt();
			}while(0>num2);
			boolean result = checkCoPrime(num1, num2);
			if(result)
			{
				System.out.println(num1 + " and " + num2 + " are co-prime");
			}
			else
			{
				System.out.println(num1 + " and " + num2 + " are not co-prime");
			}
		}
			
	}
	
//	
//	public static void main(String[] argv)
//	{
//		System.out.println(SessionGen.nextSessionId());
//		System.out.println(System.getProperty("user.name"));
//		//System.out.println(client());
//		
//		Client myClient = new Client();
//		
//		System.out.println(myClient.getGAmodP());
//		
//		return;
//	}
//	
//	private static String server()
//	{
//		String retVal = new String();
//		String serverUsername = System.getProperty("user.name");
//		return retVal;
//	}
	
//	private static String client()
//	{
//		String retVal = new String();
//		String clientUsername = System.getProperty("user.name");
//		//int g = 71;	//public base of g^a mod p
//		//int p = 47;	//public mod value in g^a mod p
//		
//		int a; //PRIVATE exponent value
//		a = rand.nextInt();
//		
//		BigInteger p = new BigInteger(Integer.toString(pValue));
//		BigInteger g = new BigInteger(Integer.toString(gValue));
//		BigInteger Xa = new BigInteger(Integer.toString(XaValue));
//		BigInteger Xb = new BigInteger(Integer.toString(XbValue));
//		
//		
//		Xa = g.modPow(new BigInteger(Integer.toString(a)), p);
//		retVal = Xa.toString();
//		try {
//			createKey();
//		
//		
//		int bitLength = 512; 
//		SecureRandom rand = new SecureRandom();
//		p = BigInteger.probablePrime(bitLength, rand);
//		g = BigInteger.probablePrime(bitLength, rand);
//		
//		DHPublicKeySpec key = createSpecificKey(p,g);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return retVal;
//	}
//	
//	private static void createKey() throws NoSuchAlgorithmException, InvalidKeySpecException
//	{
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
//		kpg.initialize(512);
//		KeyPair kp = kpg.generateKeyPair();
//		KeyFactory kFactory = KeyFactory.getInstance("DiffieHellman");
//		
//		DHPublicKeySpec kspec = (DHPublicKeySpec) kFactory.getKeySpec(kp.getPublic(), DHPublicKeySpec.class);
//		
//	}
//	
//	public static DHPublicKeySpec createSpecificKey(BigInteger p, BigInteger g) throws Exception {
//	    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
//
//	    DHParameterSpec param = new DHParameterSpec(p, g);
//	    kpg.initialize(param);
//	    KeyPair kp = kpg.generateKeyPair();
//
//	    KeyFactory kfactory = KeyFactory.getInstance("DiffieHellman");
//
//	    DHPublicKeySpec kspec = (DHPublicKeySpec) kfactory.getKeySpec(kp.getPublic(),
//	        DHPublicKeySpec.class);
//	    return kspec;	//kspec is a specification of a DiffieHelman key
//	  }
}
