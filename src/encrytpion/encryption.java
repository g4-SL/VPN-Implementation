package encrytpion;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.ArrayList;


/*
 * Author: Group 8
 * Last Modified: October 15, 2014
 * Course: EECE 412, Assignment 3
 * Purpose: Encrypt plaintext and decrypt ciphertext
 * Version: Encrypt very long plaintext
 */
public class encryption{
    private static ArrayList<String> log_en = new ArrayList<String>();
    private static ArrayList<String> log_de = new ArrayList<String>();

    private static int en_counter = 0;
    private static int de_counter = 0;
	public static int Z = 40;
	public static int SPLIT = 7;
	
	// This splits the plaintext and encrypts each segment.
	public static String encrypt(String plaintext, String key_in){
		en_counter = 0;
		log_en.clear();
		log_en.add("Key: "+key_in+"\n");
		
		ArrayList<Integer> hash = new ArrayList();
		hash(md5(key_in),hash);
		log_en.add("md5(key): "+ md5(key_in)+"\n");
		log_en.add("hash(md5(key)): "+hash+"\n");
		
		String ciphertext = "";
		int i,j=0;
		String tmp = "";
		
		// Split the plaintext
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
	
	// This encrypt the message with a length of SPLIT or less than SPLIT.
	public static String en_loop(String plaintext, ArrayList<Integer> hash){
		String ciphertext = "";

		int IV = 0 + (int)(Math.random() * ((Math.pow(2, 30) - 0) + 1)); // IV is randomly generated in the range [0,2^30]
		int c = IV;
		int i,key;
		for (i=0;i<plaintext.length();i++){
			key = hash.get(i%hash.size());
			// CBC Operation + shifting
			c = shift(plaintext.charAt(i)^c,key,1);
			// Convert the encrypted value into binary		
			ciphertext += c_string(c);
			
			log_en.add("shift(plaintext("+en_counter+")) with key "+key+" : "+c+"\nCiphertext: "+ciphertext+"\n");
			en_counter++;
		}
		ciphertext = c_string(IV)+ciphertext;
		log_en.add("Add IV to ciphertext: "+ciphertext+"\n");
		
		return ciphertext;
	}
	
	// This splits the ciphertext and decrypts each segment.
	public static String decrypt(String ciphertext, String key_in){
		de_counter = 0;
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
				de_counter += i+(SPLIT+1)*Z;
				tmp = de_loop(ciphertext.substring(i,i+(SPLIT+1)*Z),hash);
			}
			else{
				de_counter = ciphertext.length();
				tmp = de_loop(ciphertext.substring(i,ciphertext.length()),hash);
			}
			plaintext+=tmp;
			log_de.add("Plaintext: "+plaintext+"\n");
		}
		
		return plaintext;

	}
	
	// This decrypt the message with a length of SPLIT or less than SPLIT.
	public static String de_loop(String ciphertext,ArrayList<Integer> hash){
		String plaintext = "";
		int key,m = 0;

		for (int i = ciphertext.length()-Z,j= hash.size()-((ciphertext.length()/Z)%hash.size()-1); i >=Z; i-= Z,j++){
			key = hash.get(hash.size() - j%hash.size()-1);
			m = shift(Integer.parseInt(ciphertext.substring(i,i+Z), 2),key,2);
			m ^= Integer.parseInt(ciphertext.substring(i-Z,i), 2);
			plaintext = (char) m + plaintext;
			
			de_counter -= Z;
			log_de.add("shift(ciphertext("+de_counter+"-"+(de_counter+Z)+")) with key "+key+" : "+m+"\nPlaintext: "+(char)m+"\n");
		}
		return plaintext;
	}
	
	// Caesar Cipher shifting
	public static int shift(int c, int key, int in){
		if (in == 1){
			c += key;
		}
		else{
			c -= key;
		}
		return c;
	}
	
	// Convert a decimal integer into binary
	public static String c_string(int c){
		String c_string = "";
		for (int i = 0 ; i < Z - Integer.toBinaryString(c).length();i++)
			c_string += '0';
		c_string += Integer.toBinaryString(c);
		return c_string;
	}
	
	// Compute the md5 value of a string
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
	
	// Hash a string into an Integer array with a length of 10
	public static void hash(String str, ArrayList<Integer> hash){
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

	}
	
	public ArrayList<String> returnLog_en(){
		return log_en;
	}
	
	public ArrayList<String> returnLog_de(){
		return log_de;
	}
}
