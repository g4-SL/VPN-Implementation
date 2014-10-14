package encrytpion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



//CBC
public class encryption{
	public static int IV = 'A';
	public static int Z = 40;//40
    public static void main(String[] args) throws IOException {
    	/*
    	while(true){
    		
	    	String plaintext = "sdlfkjsdflksdflksdfjklsdjflsdflksdflksdajfkldsjldsjflksdjfj";
	    	String ciphertext = encrypt(plaintext,"12345");
	    	System.out.println("Ciphertext:"+ciphertext+"\n");
	    	System.out.println("Plaintext:"+decrypt(ciphertext,"12345"));
	    	System.out.println("=======================================================================================================");
	    	//System.out.println("Brute Force: " +bruteF(ciphertext));
    	}*/
    }
    
	public static String encrypt(String plaintext, String key_in){
		//Scanner in = new Scanner(System.in);
		String ciphertext = "";
		//System.out.print("Please enter the key:\n");
		ArrayList<Integer> hash = new ArrayList();
		hash(key_in,hash);
		int c = IV;
		int i,key;
		for (i=0;i<plaintext.length();i++){
			key = hash.get(i%hash.size());
			System.out.println(i%hash.size());
			//System.out.println(Integer.toBinaryString(plaintext.charAt(i)));
			//System.out.printf("Before XORed: %d\n" ,(int)plaintext.charAt(i));p
			//System.out.printf("After XORed: %d\n",plaintext.charAt(i)^c);
			c = shift(plaintext.charAt(i)^c,key,1);
			//System.out.printf("After shifting: %d\n",c);
			//System.out.println(c_string(c));
			ciphertext += c_string(c);
		}
		System.out.println();
		
		return c_string(IV)+ciphertext;
	}
	
	public static String decrypt(String ciphertext, String key_in){
		String plaintext = "";
		//Scanner in = new Scanner(System.in);
		//System.out.print("Please enter the key:");
		ArrayList<Integer> hash = new ArrayList();
		hash(key_in,hash);
		int key,m = 0;

		for (int i = ciphertext.length()-Z,j= hash.size()-((ciphertext.length()/Z)%hash.size()-1); i >=Z; i-= Z,j++){
			key = hash.get(hash.size() - j%hash.size()-1);
			//System.out.println(Integer.parseInt(ciphertext.substring(i,i+7), 2));
			//System.out.printf("Before shifting: %d\n",Integer.parseInt(ciphertext.substring(i,i+Z), 2));
			//System.out.printf("After shifting: %d\n",shift(Integer.parseInt(ciphertext.substring(i,i+Z), 2),key,2));
			m = shift(Integer.parseInt(ciphertext.substring(i,i+Z), 2),key,2);
			//System.out.printf("%d XOR %d\n",m,Integer.parseInt(ciphertext.substring(i-7,i), 2));
			m ^= Integer.parseInt(ciphertext.substring(i-Z,i), 2);
			plaintext = (char) m + plaintext;
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
	
	public static void hash(String str, ArrayList<Integer> hash){
		//System.out.println("String: "+str);
		// Has to be length of 10
		//hi :)
		int i,j,tmp;
		for (i=0;i<str.length();i++){
			if (i == str.length() - 1)
				tmp = str.charAt(i);
			else
				tmp = str.charAt(i) + str.charAt(i+1);
			for (j=0;j<i;j++){
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
/*
		System.out.println("Size of Hash: " + hash.size());
		for (i=0;i<hash.size();i++)
			System.out.println("Value of Hash: " + (int) hash.get(i));*/
	}
	
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
	}
	
}
