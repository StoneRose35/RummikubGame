package ch.sr35.rummikub.common.utils;

import java.util.Random;

public class HexStringHelper {
	public static String getHexString(short stringSize) {
		Random r = new Random();
		byte[] b=new byte[stringSize];
		r.nextBytes(b);
		char[] hexDigits = new char[stringSize*2];
		for (int c=0;c<stringSize;c++)
		{
			
			hexDigits[2*c] = Character.forDigit((b[c] >> 4) & 0xF, 16);
			hexDigits[2*c+1] = Character.forDigit((b[c] & 0xF), 16);
		}
		final String name=new String(hexDigits);
		return name;
	}
}
