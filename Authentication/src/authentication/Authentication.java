package authentication;


import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Authentication 
{
	private SessionIDgen SessionGen = new SessionIDgen();
	private SecureRandom secRand = new SecureRandom();
	private int exponent = 0;
//	private KeyPair myPublicKeyPair = null;
//	private KeyPair mySigningKeyPair = null;
//	private PublicKey partnerPubKey = null;
//	private PublicKey partnerSigKey = null;
	private String sessionID = null;
	private BigInteger sessionKey = null;
//	private Signature mySignature = null;
//	private Signature partnerSignature = null;
	
	private Signature mySignatureSign =  null;
	private Signature partnerSignatureVerify =  null;
	
	
	private final static int rsaKeySize = 1024;
	private KeyPair keyPairData = null;
	private KeyPair keyPairSignature = null;
	private KeyPair keyPairFinal = null;
	
	private PublicKey partnerDataKey = null;
	private PublicKey partnerSignatureKey = null;
	private PublicKey partnerFinalKey = null;
	
	
//	private final static int rsaSigKeySize = 1024;
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
	
//	public String buildMessage()
//	{
//		String retVal = new String();
//		//setExponent();
//		
//		return retVal;
//	}
	
	
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
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(Integer.toString(gValue));
		BigInteger Xa;
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
		return Xa;
	}
	
	public BigInteger getGAmodP( int gValue)
	{
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(Integer.toString(gValue));
		BigInteger Xa;
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
		return Xa;
	}
	
	public BigInteger getGAmodP( BigInteger gValue)
	{
		BigInteger p = new BigInteger(Integer.toString(pValue));
		BigInteger g = new BigInteger(gValue.toString());
		BigInteger Xa;
		
		Xa = g.modPow(new BigInteger(Integer.toString(exponent)), p);
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
	
	public byte[] sendPublicKeys()
	{
		byte[] setOfKeys = null;
		
		int length  = 0;
		int keyPairDataLength = keyPairData.getPublic().getEncoded().length;
		int keyPairSignatureLength = keyPairSignature.getPublic().getEncoded().length;
		int keyPairFinalLength = keyPairFinal.getPublic().getEncoded().length;
		length = keyPairDataLength + keyPairSignatureLength + keyPairFinalLength;
		byte[] keySet = new byte[ length ];
		System.arraycopy(keyPairData.getPublic().getEncoded(), 0, keySet, 0, keyPairDataLength);
		System.arraycopy(keyPairSignature.getPublic().getEncoded(), 0, keySet, keyPairDataLength, keyPairSignatureLength);
		System.arraycopy(keyPairFinal.getPublic().getEncoded(), 0, keySet, keyPairSignatureLength, keyPairFinalLength);		
		setOfKeys = keySet;
		
		return setOfKeys;
	}
	
	public byte[] sendPublicDataKey()
	{
		return keyPairData.getPublic().getEncoded();
	}
	
	public byte[] sendPublicSignatureKey()
	{
		return keyPairSignature.getPublic().getEncoded();
	}
	
	public byte[] sendPublicFinalKey()
	{
		return keyPairFinal.getPublic().getEncoded();
	}
	
	public void receivePublicKeys(byte[] receivedKeys)
	{
		//byte[] rPubKey = receivedFrame.data;
		int keyPairDataLength = keyPairData.getPublic().getEncoded().length;
		int keyPairSignatureLength = keyPairSignature.getPublic().getEncoded().length;
		int keyPairFinalLength = keyPairFinal.getPublic().getEncoded().length;
		
		byte[] partnerDataKeyRaw = Arrays.copyOfRange(receivedKeys, 0, keyPairDataLength);
		byte[] partnerSignatureKeyRaw = Arrays.copyOfRange(receivedKeys, keyPairDataLength, keyPairSignatureLength);
	//	byte[] partnerFinalKeyRaw = Arrays.copyOfRange(receivedKeys, keyPairSignatureLength, keyPairFinalLength);
		
		X509EncodedKeySpec ksDataKey = new X509EncodedKeySpec(partnerDataKeyRaw);
		X509EncodedKeySpec ksSignatureKey = new X509EncodedKeySpec(partnerSignatureKeyRaw);
	//	X509EncodedKeySpec ksFinalKey = new X509EncodedKeySpec(partnerFinalKeyRaw);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerDataKey = kf.generatePublic(ksDataKey);
			partnerSignatureKey = kf.generatePublic(ksSignatureKey);
	//		partnerFinalKey = kf.generatePublic(ksFinalKey);
			
			partnerSignatureVerify =  Signature.getInstance("SHA1withRSA");
			partnerSignatureVerify.initVerify(partnerSignatureKey);
			
		} 
		catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error reading keys");
		} 
		
	}
	
	public void receivePartnerDataKey(byte[] receivedKey)
	{
		X509EncodedKeySpec ksDataKey = new X509EncodedKeySpec(receivedKey);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerDataKey = kf.generatePublic(ksDataKey);
			
		} 
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error reading data key");
		} 
		
	}
	
	public void receivePartnerSignatureKey(byte[] receivedKey)
	{
		X509EncodedKeySpec ksSignatureKey = new X509EncodedKeySpec(receivedKey);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerSignatureKey = kf.generatePublic(ksSignatureKey);
			
			partnerSignatureVerify =  Signature.getInstance("SHA1withRSA");
			partnerSignatureVerify.initVerify(partnerSignatureKey);
			
		} 
		catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error reading Signature key");
		} 
		
	}
	
	public void receivePartnerFinalKey(byte[] receivedKey)
	{
		X509EncodedKeySpec ksFinalKey = new X509EncodedKeySpec(receivedKey);
		KeyFactory kf;
		try 
		{
			kf = KeyFactory.getInstance("RSA");
			partnerFinalKey = kf.generatePublic(ksFinalKey);
		} 
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error reading keys");
		} 
		
	}
	
//	public void receiveSigKey(DataFrame receivedFrame)
//	{
//		byte[] rPubKey = receivedFrame.data;
//		X509EncodedKeySpec ks = new X509EncodedKeySpec(rPubKey);
//		KeyFactory kf;
//		try 
//		{
//			kf = KeyFactory.getInstance("RSA");
//			partnerSigKey = kf.generatePublic(ks);
//			partnerSignature = Signature.getInstance("SHA1withRSA");
//			partnerSignature.initVerify(partnerSigKey);
//		} 
//		catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//	}
	
//	public String viewPrivateKey()
//	{
//		return myPublicKeyPair.getPrivate().toString();
//	}
	
	public String viewExponent()
	{
		return Integer.valueOf(exponent).toString();
	}
	
	public void viewPublicKeys()
	{
		System.out.println("keyPairData");
		System.out.println(keyPairData.getPublic().toString());
		System.out.println("keyPairSignature");
		System.out.println(keyPairSignature.getPublic().toString());
		System.out.println("keyPairFinal");
		System.out.println(keyPairFinal.getPublic().toString());
	}
	
//	public String viewSigKey()
//	{
//		return mySigningKeyPair.getPublic().toString();
//	}
	
	public void viewPartnerPublicKeys()
	{
		System.out.println("partnerDataKey");
		System.out.println(partnerDataKey.toString());
		System.out.println("partnerSignatureKey");
		System.out.println(partnerSignatureKey.toString());
		System.out.println("partnerFinalKey");
		System.out.println(partnerFinalKey.toString());
	}
	
//	public String viewPartnerSigKey()
//	{
//		return partnerSigKey.toString();
//	}
	
	public String getSessionKey()
	{
		return sessionKey.toString();
	}
	
	public String viewMySig()
	{
		return mySignatureSign.toString();
	}
	
	public String viewPartnerSig()
	{
		return partnerSignatureVerify.toString();
	}
	
	public void generateKeyPairs()
	{
		//KeyPairGenerator kpg;
		//KeyPairGenerator kpg2;
		try 
		{
			
			KeyPairGenerator kpg;
			//SecureRandom secRand = new SecureRandom();
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(rsaKeySize, new SecureRandom());
			keyPairData = kpg.generateKeyPair();
			kpg.initialize(rsaKeySize, new SecureRandom());
			keyPairSignature = kpg.generateKeyPair();
			kpg.initialize((2*rsaKeySize + 88), new SecureRandom());	//this key is 2*n+88 where n is the size of the smaller keys
			keyPairFinal = kpg.generateKeyPair();
			
			
			mySignatureSign =  Signature.getInstance("SHA1withRSA");
			mySignatureSign.initSign(keyPairSignature.getPrivate(),secRand);
			
			//do this once we have the partnerSignaturePublicKey
		//	Signature sigVer =  Signature.getInstance("SHA1withRSA");
		//	sigVer.initVerify(keyPairSignature.getPublic());
			
//			kpg = KeyPairGenerator.getInstance("RSA");
//			kpg.initialize(rsaKeySize, new SecureRandom());
//			myPublicKeyPair = kpg1.generateKeyPair();
//			kpg2 = KeyPairGenerator.getInstance("RSA");
//			kpg2.initialize(rsaSigKeySize, secRand);
//			mySigningKeyPair = kpg2.generateKeyPair();
//			
//			
//			mySignature =  Signature.getInstance("SHA1withRSA");
//			mySignature.initSign(mySigningKeyPair.getPrivate());
//			
		//	PrivateKey priv = myKeyPair.getPrivate();
		//	PublicKey pub = myKeyPair.getPublic();
		//	System.out.println(pub.toString());
		//	System.out.println(priv.toString());
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("problem generating keys");
		}
	}
	
//	public byte[] padMessage(int keySize, String message )
//	{
//		byte[] retVal;
//		int size;
//		String temp = new String(message);
//		if(keySize==1)
//		{
//			size = rsaPubKeySize;
//		}
//		else
//		{
//			size = rsaSigKeySize;
//		}
//		for(int i=message.length();i<(size/8 -11);i++)
//		{
//			temp = temp + "0";
//		}
//		retVal = temp.getBytes();
//		
//		return retVal;
//	}
	
	public byte[] encryptAndSignData(byte[] data)
	{
		Cipher rsa;
		byte[] encryptedMessage = null;
		
		try 
		{
			rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.ENCRYPT_MODE, partnerDataKey);
			//System.out.println(new String(data));
			byte[] dataEncrypted = rsa.doFinal(data);
			
			byte[] buffer = dataEncrypted;//text.getBytes();
			
			mySignatureSign.update(buffer);
			byte[] realSig = mySignatureSign.sign();
			
			//Concatenate data
			byte[] message = new byte[buffer.length + realSig.length];
			System.arraycopy(buffer, 0, message, 0, buffer.length);
			System.arraycopy(realSig, 0, message, buffer.length, realSig.length);
			
			//encrypt concatenated data
			rsa.init(Cipher.ENCRYPT_MODE,partnerFinalKey);
			encryptedMessage = rsa.doFinal(message);
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SignatureException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptedMessage;
	}
	
	public byte[] decryptAndVerifyData(byte[] encryptedMessage)
	{
		byte[] text = null;
		
		do
		{
			Cipher rsa;
			try 
			{
				rsa = Cipher.getInstance("RSA");
				rsa.init(Cipher.DECRYPT_MODE, keyPairFinal.getPrivate());
				
				byte[] message = rsa.doFinal(encryptedMessage);
				
				byte[] buffer = Arrays.copyOfRange(message, 0, rsaKeySize/8);
				byte[] realSig = Arrays.copyOfRange(message, rsaKeySize/8, message.length);
				
				partnerSignatureVerify.update(buffer);
				boolean result = partnerSignatureVerify.verify(realSig);
				if(!result)
				{
					System.out.println("the message not is verified");
					break;
				//	System.out.println("the message is: " + new String(buffer));
				}
				
				rsa.init(Cipher.DECRYPT_MODE, keyPairData.getPrivate());
				text = rsa.doFinal(buffer);
				
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}while(false);
		return text;
	}
	
	
//	private static byte[] encrypt(Key encryptionKey, String text)
//	{
//		byte[] retVal = null;
//		Cipher rsa;
//		try 
//		{
//			//System.out.println("the text is " + text.length() + " bytes long");
//			rsa = Cipher.getInstance("RSA");
//			rsa.init(Cipher.ENCRYPT_MODE, encryptionKey);
//			retVal = rsa.doFinal(text.getBytes());
//		} 
//		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("error in rsa encryption");
//		}
//		return retVal;
//	}
	
//	private static byte[] decrypt(Key decryptionKey, byte[] buffer)
//	{
//		byte[] retVal = null;
//		Cipher rsa;
//		//System.out.println(decryptionKey.toString());
//		try 
//		{
//			rsa = Cipher.getInstance("RSA");
//			rsa.init(Cipher.DECRYPT_MODE, decryptionKey);
//			//byte[] utf8 = rsa.doFinal(buffer);
//			retVal = rsa.doFinal(buffer);
//			//retVal = utf8;
//			//retVal = new String(utf8,"UTF8");//.getBytes();
//		} 
//		catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("error in rsa decryption");
//		}
//		return retVal;
//	}
//	
//	private byte[] signData(byte[] buffer)
//	{
//		byte[] retVal = null;
//		
//		try 
//		{
//			mySignature.update(buffer);
//			retVal = mySignature.sign();
//		} 
//		catch (SignatureException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return retVal;
//	}
//	
//	private boolean verifyData(byte[] buffer)
//	{
//		boolean retVal = false;
//		
//		try 
//		{
//			partnerSignature.update(buffer);
//			retVal = partnerSignature.verify(buffer);
//		} 
//		catch (SignatureException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return retVal;
//	}
//
	//TODO: add id necessary

	public byte[] clientHelloMessage()
	{
		byte[] retVal = null;
		String data = null;

		sessionID = SessionGen.nextSessionId();
		System.out.println("client sessionID: \"" + sessionID + "\"");
		//byte[] publicKey = sendPublicKey().data;
		data = sessionID;// + publicKey.toString();
	//	System.out.println("client data: \"" + data + "\"");
		
		retVal = data.getBytes();
	//	System.out.println("client hello retVal is: \"" + new String(retVal.data) + "\"");
		return retVal;
	}
	
	public byte[] serverResponceToHello(byte[] hello)
	{
		byte[] retVal = null;
		sessionID = SessionGen.nextSessionId();
		System.out.println("server sessionID: \"" + sessionID + "\"");
		//byte[] clientMessage = hello.data;
		System.out.println("recovering client sessionID");
		
		//String clientSessionID = Arrays.copyOfRange(hello.data, 0, sessionID.length()).toString();
		String clientSessionID = new String(hello);
		System.out.println("server version of client sessionID: \"" + clientSessionID + "\"");
		//DataFrame clientPublickKeyDataFrame = new DataFrame();
		//System.out.println("recovering client public key");
		
		//clientPublickKeyDataFrame.data = Arrays.copyOfRange(hello.data, sessionID.length(), hello.data.length);
		
		//System.out.println("inserting client public key to partnerPublicKey");
		//receivePublicKey(clientPublickKeyDataFrame);	//insert the client publick key into the partnerPublicKey variable
		//System.out.println("setting exponent");
		setExponent();
		BigInteger Xb = getGAmodP();
		
		String messageToClient = clientSessionID + Xb;
		
		System.out.println("encryting message to client: \"" + messageToClient + "\"");
		
		retVal = encryptAndSignData(messageToClient.getBytes());

//		System.out.println("unencrypted message Size: " + messageToClient.length());
//		byte[] partnerEncrypted = encrypt(partnerPubKey, messageToClient);
//		System.out.println("encrypted Size: " + partnerEncrypted.length);
//		System.out.println("signing message to client");
//		byte[] signedMessageToClient = encrypt(mySigningKeyPair.getPrivate(),new String(partnerEncrypted));
//		System.out.println("serverSignedMessage Size: " + signedMessageToClient.length);
//		messageToClient = sessionID + new String(signedMessageToClient);
//		retVal.data = messageToClient.getBytes();
		return retVal;
	}
	
	public byte[] clientResponceToServer(byte[] serverMessage) throws IllegalStateException
	{
		byte[] retVal = null;
		String serverSessionID = new String(Arrays.copyOfRange(serverMessage, 0, sessionID.length()));
		byte[] encryptedServerMessage = Arrays.copyOfRange(serverMessage, sessionID.length(), serverMessage.length);
	
//		byte[] serverSignedMessage = Arrays.copyOfRange(serverMessage.data, sessionID.length(), serverMessage.data.length);
//		System.out.println("decrypting server message");
//		System.out.println("serverSignedMessage Size: " + serverSignedMessage.length);
//		byte[] serverMessageMyPubKey = decrypt(partnerSigKey, serverSignedMessage);
//		System.out.println("server signature removed");
//		byte[] decryptedServerMessage = decrypt(myPublicKeyPair.getPrivate(),serverMessageMyPubKey);

		byte[] decryptedServerMessage = decryptAndVerifyData(encryptedServerMessage);
		System.out.println("server message decrypted: \"" + new String(decryptedServerMessage) + "\"");
		
		if( null ==  decryptedServerMessage)
		{
			throw new IllegalStateException("SessionID mismatch");
		}
		System.out.println("session ID's match");
		
		//setExponent();
		BigInteger Xb = new BigInteger(Arrays.copyOfRange(decryptedServerMessage, sessionID.length(), decryptedServerMessage.length));
		sessionKey = getGAmodP(Xb);
		
		BigInteger Xa = getGAmodP();
		
		String messageToServer = serverSessionID + Xa;
//		byte[] partnerEncrypted = encrypt(partnerPubKey, messageToServer);
//		retVal.data = encrypt(mySigningKeyPair.getPrivate(),new String(partnerEncrypted));
//		
		
		retVal = encryptAndSignData(messageToServer.getBytes());
		
		return retVal;
	}
	
	public boolean serverRecieveSecondClientMessage(byte[] clientMessage) throws IllegalStateException
	{
		boolean retVal = false;
//		byte[] unsignedClientMessage = decrypt(partnerSigKey, clientMessage.data);
//		byte[] decryptedClientMessage = decrypt(myPublicKeyPair.getPrivate(),unsignedClientMessage);
		byte[] decryptedClientMessage = decryptAndVerifyData(clientMessage);
		if(null == decryptedClientMessage)
		{
			throw new IllegalStateException("SessionID mismatch");
		}
		else
		{	
			BigInteger Xa = new BigInteger(Arrays.copyOfRange(decryptedClientMessage, sessionID.length(), decryptedClientMessage.length));
			sessionKey = getGAmodP(Xa);
			retVal = true;
		}
		return retVal;
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
