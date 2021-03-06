import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class PolyDivisible
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File f = new File("Number Lists/Base10PolyDivisibleNumbers.txt");
		PrintWriter out = new PrintWriter(f);
		
		//20456
		long start = System.nanoTime();
		findNumbers(out, 20456);
		long end = System.nanoTime();
		
		long duration = end - start;
		String lastLine = Utility.calculateTime(duration);
		
		out.println("");
		out.println(lastLine);
		out.close();
	}
	
	/**
	 * Generates a list of Polydivisible numbers.
	 * It starts the calculations as using ints, and then moves up to longs
	 * and then finally up to BigIntegers, and keeps calculating until it has found the
	 * target number of numbers
	 * It also returns how many have been found for every hundred found
	 * @param out where the list is written to
	 * @param howMany how many numbers to find before exiting
	 */
	public static void findNumbers(PrintWriter out, int target)
	{
		//keeps track of how many numbers have been found
		int found = 0;
		
		//start at 0 and count up
		int curInt = 1;
		
		//start as an int
		while(curInt >= 0)
		{
			if(isPolyDivisibleInt(curInt))
			{
				out.println(curInt);
				out.flush();
				found = found + 1;
				if(found%1000 == 0)
				{
					System.out.println(found);
				}
				if(found == target)
				{
					return;
				}
			}
			curInt = nextInt(curInt);
		}
		
		
		//rollover number to a long
		long curLong = 2100000000L;
		
		while(curLong > 0)
		{
			if(isPolyDivisibleLong(curLong))
			{
				out.println(curLong);
				out.flush();
				found = found + 1;
				if(found%1000 == 0)
				{
					System.out.println(found);
				}
				if(found == target)
				{
					return;
				}
			}
			curLong = nextLong(curLong);
		}
		
		
		//convert over to BigInteger
		BigInteger bigCur = new BigInteger("9222000000000000000");
		
		while(true)
		{
			if(isPolyDivisibleBig(bigCur))
			{
				out.println(bigCur);
				out.flush();
				found = found + 1;
				if(found%100 == 0)
				{
					System.out.println(found);
				}
				if(found == target)
				{
					return;
				}
			}
			bigCur = nextBig(bigCur);
		}
	}
	
	/**
	 * Checks if every other index (starting with the second digit)
	 * is even, because polydivisible numbers must have this form. If it isnt, it
	 * increases that index by one, to shortcut many values that would not be polydivisible
	 * with that particular index being odd
	 * @param num1 the number to look at
	 * @return a number which has better polydivisible potential
	 */
	public static int nextInt(int num1)
	{
		String num = "" + num1;
		
		if(num.length() > 2)
		{
			for(int i = 1; i < num.length(); i++)
			{
					int firstGroup = Integer.parseInt(num.substring(0,i));
					if(firstGroup%(i) != 0)
					{
						String toAdd = "" + (i-firstGroup%i);
						String zeros = "";
						String end = num.substring(i+1, num.length());
						for(int in = 0; in < end.length() + 1; in++)
						{
							zeros = zeros + "0";
						}
						
						String firstHalf = num.substring(0, i);
						int firstH = Integer.parseInt(firstHalf);
						firstH = firstH + Integer.parseInt(toAdd);
						
						try
						{
							int res = Integer.parseInt("" + firstH + zeros);
							return res;	
						}
						catch(NumberFormatException ee)
						{
							return -1;
						}
					}
			}
		}
		
		return num1 + 1;
	}
	
	public static long nextLong(long num1)
	{
		String num = "" + num1;

		if(num.length() > 2)
		{
			for(int i = 1; i < num.length(); i++)
			{
					long firstGroup = Long.parseLong(num.substring(0,i));
					if(firstGroup%(i) != 0)
					{
						String toAdd = "" + (i-firstGroup%i);
						String zeros = "";
						String end = num.substring(i+1, num.length());
						for(int in = 0; in < end.length() + 1; in++)
						{
							zeros = zeros + "0";
						}
						
						String firstHalf = num.substring(0, i);
						long firstH = Long.parseLong(firstHalf);
						firstH = firstH + Long.parseLong(toAdd);
						
						try
						{
							long res = Long.parseLong("" + firstH + zeros);
							return res;	
						}
						catch(NumberFormatException ee)
						{
							return -1;
						}
					}
			}
		}
		
		return num1 + 1;
	}
	
	public static BigInteger nextBig(BigInteger num1)
	{
		String num = "" + num1;
		
		if(num.length() > 2)
		{
			for(int i = 3; i < num.length(); i++)
			{
					BigInteger firstGroup = new BigInteger(num.substring(0,i));
					BigInteger i1 = new BigInteger("" + i);
					if(!firstGroup.mod(i1).equals(BigInteger.ZERO))
					{
						BigInteger bigger = new BigInteger("" + i);
						String toAdd = "" + (bigger.subtract(firstGroup.mod(bigger))).toString();
						String zeros = "";
						String end = num.substring(i+1, num.length());
						for(int in = 0; in < end.length() + 1; in++)
						{
							zeros = zeros + "0";
						}
						
						String firstHalf = num.substring(0, i);
						BigInteger firstH = new BigInteger(firstHalf);
						firstH = firstH.add(new BigInteger(toAdd));

						BigInteger res = new BigInteger("" + firstH + zeros);
						return res;	
					}
			}
		}
		
		return num1.add(BigInteger.ONE);
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
			return true;
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
