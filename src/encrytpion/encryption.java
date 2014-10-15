package encrytpion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;



//CBC
public class encryption{
    private static ArrayList<String> log_en = new ArrayList<String>();
    private static ArrayList<String> log_de = new ArrayList<String>();
    
    private static String key;
	public static int Z = 40;//40
	public static int SPLIT = 7;
	
    public static void main(String[] args) throws IOException {
    	/*
    	while(true){
    		//ecc4f1f4666ca02250aaf055185f1036
	    	String plaintext = "ABCFdsabcdSg;,df gvflkdgmdf;lzgmflkmlsdmflsdmfosdfldsjflkdsjflsdjfl;ksdajflksadffkl;mldfgm;ldfl;,fdgde";
	    	key = "99999999999999999999999999999999999999999999999999999999999999999999999sNFkjsDNfkjdsnfmlsdfnsdlkfmdsllkjf9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
	       	String ciphertext = encrypt(plaintext,key);
	    	System.out.println("Ciphertext:"+ciphertext+"\n");
	    	System.out.println("Plaintext:"+decrypt(ciphertext,key));
	    	System.out.println("=======================================================================================================");
	    	//System.out.println("Brute Force: " +bruteF(ciphertext));
    	}*/
    }
    
	public static String encrypt(String plaintext, String key_in){
		log_en.clear();
		log_en.add("Key: "+key_in+"\n");
		ArrayList<Integer> hash = new ArrayList();
		hash(md5(key_in),hash);
		log_en.add("md5(key): "+ md5(key_in)+"\n");
		log_en.add("hash(md5(key)): "+hash+"\n");
		
		String ciphertext = "";
		int i,j=0;
		String tmp = "";
		for ( i = 0 ; i < plaintext.length();i+=SPLIT){
			if (i+SPLIT < plaintext.length()){
				tmp = en_loop(plaintext.substring(i,i+SPLIT),hash);
			}
			else{
				tmp = en_loop(plaintext.substring(i,plaintext.length()),hash);
			}
			ciphertext += tmp;
		}
		log_en.add("Ciphertext: " + ciphertext);
		
		return ciphertext;
	}
	
	public static String en_loop(String plaintext, ArrayList<Integer> hash){
		//Scanner in = new Scanner(System.in);
		String ciphertext = "";
		//System.out.print("Please enter the key:\n");
		
		int IV = 0 + (int)(Math.random() * ((10000000 - 0) + 1));
		int c = IV;
		int i,key;
		for (i=0;i<plaintext.length();i++){
			key = hash.get(i%hash.size());
			//System.out.println(i%hash.size());
			//System.out.println(Integer.toBinaryString(plaintext.charAt(i)));
			//System.out.printf("Before XORed: %d\n" ,(int)plaintext.charAt(i));p
			//System.out.printf("After XORed: %d\n",plaintext.charAt(i)^c);
			c = shift(plaintext.charAt(i)^c,key,1);
			//System.out.printf("After shifting: %d\n",c);
			//System.out.println(c_string(c));
			ciphertext += c_string(c);
			log_en.add("shift(plaintext("+i+")) with key "+key+" : "+c+"\nCiphertext: "+ciphertext+"\n");
		}
		ciphertext = c_string(IV)+ciphertext;
		log_en.add("Add IV to ciphertext: "+ciphertext+"\n");
		
		return ciphertext;
	}
	
	public static String decrypt(String ciphertext, String key_in){
		log_de.clear();
		ArrayList<Integer> hash = new ArrayList();
		hash(md5(key_in),hash);
		log_de.add("md5(key): "+ md5(key_in)+"\n");
		log_de.add("hash(md5(key)): "+hash+"\n");
		
		String plaintext = "";
		int i;
		String tmp = "";
		for ( i = 0 ; i< ciphertext.length();i+=(SPLIT+1)*Z){
			if (i+(SPLIT+1)*Z < ciphertext.length()){
				tmp = de_loop(ciphertext.substring(i,i+(SPLIT+1)*Z),hash);
				//System.out.println(ciphertext.substring(i,i+8*Z));
			}
			else{
				tmp = de_loop(ciphertext.substring(i,ciphertext.length()),hash);
				//System.out.println(ciphertext.substring(i,ciphertext.length()));
			}
			plaintext+=tmp;
		}
		
		return plaintext;

	}
	
	public static String de_loop(String ciphertext,ArrayList<Integer> hash){
		String plaintext = "";
		int key,m = 0;

		for (int i = ciphertext.length()-Z,j= hash.size()-((ciphertext.length()/Z)%hash.size()-1); i >=Z; i-= Z,j++){
			key = hash.get(hash.size() - j%hash.size()-1);
			m = shift(Integer.parseInt(ciphertext.substring(i,i+Z), 2),key,2);
			m ^= Integer.parseInt(ciphertext.substring(i-Z,i), 2);
			plaintext = (char) m + plaintext;
			log_de.add("shift(ciphertext("+i+"-"+(i+Z)+")) with key "+key+" : "+m+"\nPlaintext: "+plaintext+"\n");
		}
		return plaintext;
	}
	
	public static int shift(int c, int key, int in){
		if (in == 1){
			c += key;
		}
		else{
			c -= key;
		}
		return c;
	}
	public static String c_string(int c){
		String c_string = "";
		for (int i = 0 ; i < Z - Integer.toBinaryString(c).length();i++)
			c_string += '0';
		c_string += Integer.toBinaryString(c);
		return c_string;
	}
	
	public static String md5(String str){
		try{
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] messageDigest = md.digest(str.getBytes());
	        BigInteger number = new BigInteger(1, messageDigest);
	        String hashtext = number.toString(16);
	
	        while (hashtext.length() < 32) {
	            hashtext = "0" + hashtext;
	        }
	        return hashtext;
		}
        catch (NoSuchAlgorithmException e) {
        	return "";
        }
	}
	
	public static void hash(String str, ArrayList<Integer> hash){
		//System.out.println("String: "+str);
		// Has to be length of 10
		int i,j,tmp;
		for (i=0;i<str.length();i++){
			if (i == str.length() - 1)
				tmp = str.charAt(i);
			else
				tmp = str.charAt(i) + str.charAt(i+1);
			for (j=0;j<i;j++){
				if (tmp + hash.get(j) > 100000000){
					tmp -= 100000000;
					tmp /= 7;
				}
				tmp += hash.get(j);
			}
			hash.add(tmp);
		}
		if (hash.size() < 10){
			int tmp_length = hash.size();
			for (i=0;i < 10-tmp_length;i++){
				if (tmp_length == 0)	
					hash.add(0);
				else
					hash.add(hash.get(i));
			}
		}
		else if (hash.size() > 10){
			while (hash.size() > 10)
				hash.remove(0);
		}

		//System.out.println("Size of Hash: " + hash.size());
		String hash_str = "";
		for (i=0;i<hash.size();i++){
			//System.out.println("Value of Hash: " + (int) hash.get(i));
			hash_str += Integer.toString(hash.get(i));
		}
	}
	/*
	public static String bruteF(String ciphertext){
		String plaintext = "";

		int key = 0 ;
		int m = 0;
		while (!plaintext.equalsIgnoreCase("aBcDeFz123490'-+$%^&*")){
			plaintext = "";
			for (int i = ciphertext.length()-Z ; i >=Z; i-= Z){
				m = shift(Integer.parseInt(ciphertext.substring(i,i+Z), 2),key,2);
				m ^= Integer.parseInt(ciphertext.substring(i-Z,i), 2);
				plaintext = (char) m + plaintext;
			}
			System.out.println(plaintext);
			key++; 
		}
		
		return plaintext;
	}*/
	
	public ArrayList<String> returnLog_en(){
		return log_en;
	}
	public ArrayList<String> returnLog_de(){
		return log_de;
	}
}
