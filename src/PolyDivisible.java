

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class PolyDivisible
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File f = new File("PolyDivisibleNumbers.txt");
		PrintWriter out = new PrintWriter(f);
			
		findNumbers(out);
		out.close();
	}
	
	public static void findNumbers(PrintWriter out)
	{
		//keeps track of how many numbers have been found
		int found = 0;
		
		//start at 1 and count up
		int curInt = 1;
		
		//start as an int
		while(curInt != -2147483648)
		{
			if(isPolyDivisibleInt(curInt))
			{
				out.println(curInt);
				found = found + 1;
				if(found%100 == 0)
				{
					System.out.println(found);
				}
				else if(found == 20456)
				{
					break;
				}
			}
			curInt = curInt + 1;
		}
		
		//rollover number to a long
		long curLong = 2147483648L;
		
		while(curLong != -9223372036854775808L)
		{
			if(isPolyDivisibleLong(curLong))
			{
				out.println(curLong);
				found = found + 1;
				if(found%100 == 0)
				{
					System.out.println(found);
				}
				else if(found == 20456)
				{
					break;
				}
			}
			curLong = curLong + 1;
		}
		
		
		//convert over to BigInteger
		BigInteger bigCur = new BigInteger( "9223372036854775808");
		
		while(true)
		{
			if(isPolyDivisibleBig(bigCur))
			{
				out.println(bigCur);
				found = found + 1;
				if(found%100 == 0)
				{
					System.out.println(found);
				}
				else if(found == 20456)
				{
					break;
				}
			}
			bigCur = bigCur.add(BigInteger.ONE);
		}
	}
	
	/**
	 * Checks if a base10 number stored as a BigInteger is polydivisible
	 * (A number is polydivisible if each for each index, the number formed by the
	 * digits to the left of and including the current index is evenly divisible by
	 * the current index)
	 * @param num value to check
	 * @return true if it is, and false if it isnt
	 */
	public static boolean isPolyDivisibleBig(BigInteger num)
	{
		String numString = num.toString();
		
		//polydivisible numbers can't be only one digit
		if(numString.length() == 1)
		{
			return false;
		}
		
		//for each index, check if the number formed by the digits to the 
		//left of and including the current index is evenly divisible by its
		//position in the overall number.
		//It should be noted that when checking is a figure is polydivisible,
		//the indices start counting from one. This explains why it is always i+1
		for(int i = 0; i < numString.length(); i++)
		{
			//store the number made of the digits to the left of and including the current index
			String tmp = numString.substring(0,i+1);
			
			//convert it to a BigInteger
			BigInteger subNumber = new BigInteger(tmp);
			
			//store the index value as a BigInteger
			BigInteger index = new BigInteger("" + (i + 1));
			
			//if there is a remainder then this number is not polyDivisible
			if(!subNumber.mod(index).equals(BigInteger.ZERO))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a base10 number stored as a long is polydivisible
	 * @param num value to check
	 * @return true if it is, and false if it isnt
	 */
	public static boolean isPolyDivisibleLong(long num)
	{
		String numString = "" + num;
		
		if(numString.length() == 1)
		{
			return false;
		}
		
		for(int i = 0; i < numString.length(); i++)
		{
			String tmp = numString.substring(0,i+1);
			
			long subNumber = Long.parseLong(tmp);
			int index = i + 1;
			
			if(subNumber%index != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a base10 number stored as an int is polydivisible
	 * @param num value to check
	 * @return true if it is, and false if it isnt
	 */
	public static boolean isPolyDivisibleInt(int num)
	{
		String numString = "" + num;
		
		if(numString.length() == 1)
		{
			return false;
		}
		
		for(int i = 0; i < numString.length(); i++)
		{
			String tmp = numString.substring(0,i+1);
			
			int subNumber = Integer.parseInt(tmp);
			int index = i + 1;
			
			if(subNumber%index != 0)
			{
				return false;
			}
		}
		
		return true;
	}
}