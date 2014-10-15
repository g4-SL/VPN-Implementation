package authentication;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIDgen 
{
	private SecureRandom rand = new SecureRandom();
	
	public String nextSessionId()
	{
		return new BigInteger(130, rand).toString(32);
		//select 130 bits from a secure random bit generator
		//encode the result in base 32 as 32 = 2^5 
		//this series of random bits fits nicely into a string 26 chars long
	}
}
