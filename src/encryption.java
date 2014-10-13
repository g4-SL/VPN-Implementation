import java.io.IOException;
import java.util.Scanner;



//CBC
public class encryption{
	public static int IV = 'A';
    public static void main(String[] args) throws IOException {
    	String plaintext = "ABCDEFZ";
    	String ciphertext = encrypt(plaintext);
    	System.out.println("Ciphertext:"+ciphertext+"\n");
    	System.out.println("Plaintext:"+decrypt(ciphertext));
    }
    
	public static String encrypt(String plaintext){
		Scanner in = new Scanner(System.in);
		String ciphertext = "";
		System.out.print("Please enter the key:\n");
		int key = in.nextInt();
		int c = IV;
		int i;
		for (i=0;i<plaintext.length();i++){
			//System.out.println(Integer.toBinaryString(plaintext.charAt(i)));
			//System.out.printf("Before XORed: %d\n" ,(int)plaintext.charAt(i));
			//System.out.printf("After XORed: %d\n",plaintext.charAt(i)^c);
			c = shift(plaintext.charAt(i)^c,key,1);
			//System.out.println(c_string(c));
			ciphertext += c_string(c);
		}
		System.out.println();
		
		return c_string(IV)+ciphertext;
	}
	
	public static String decrypt(String ciphertext){
		String plaintext = "";
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter the key:");
		int key = in.nextInt();
		int m = 0;
		for (int i = ciphertext.length()-7 ; i >=7; i-= 7){
			//System.out.println(Integer.parseInt(ciphertext.substring(i,i+7), 2));
			m = shift(Integer.parseInt(ciphertext.substring(i,i+7), 2),key,2);
			//System.out.printf("%d XOR %d\n",m,Integer.parseInt(ciphertext.substring(i-7,i), 2));
			m ^= Integer.parseInt(ciphertext.substring(i-7,i), 2);
			plaintext = (char) m + plaintext;
		}
		
		return plaintext;
	}
	
	public static int shift(int c, int key, int in){
		if (in == 1)
			c += key;
		else
			c -= key;
		return c;
	}
	public static String c_string(int c){
		String c_string = "";
		for (int i = 0 ; i < 7 - Integer.toBinaryString(c).length();i++)
			c_string += '0';
		c_string += Integer.toBinaryString(c);
		return c_string;
	}
	
}
