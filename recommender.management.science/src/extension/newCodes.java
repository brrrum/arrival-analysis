package extension;

import java.util.Random;

public class newCodes 
{
	public int readerIndex(boolean option, int m)
	{
		int index = 0;
		Random generator = new Random();
		ReadPattern readpattern = new ReadPattern();
		
		if(option) {
			// use randomIndex
			index = generator.nextInt(m);			
		}		
		else {
			// readerIndex
			index = readpattern.readPattern(m);			
		}		
		return index;
	}

}
