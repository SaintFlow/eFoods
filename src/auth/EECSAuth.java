package auth;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EECSAuth extends Authenticator
{
	public EECSAuth()
	{
		// TODO Auto-generated constructor stub
	}
	
	public boolean authenticate(String username, String hash)
	{
		String EECShash = "";
		try
		{
			EECShash = javax.xml.bind.DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA1").digest((username + " shared secret").getBytes()));
			System.out.println(EECShash);
			System.out.println(hash);

		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (EECShash.equals(hash.toUpperCase()))
		{
			return true;
		}else
		{
			return false;
		}
	}

}
