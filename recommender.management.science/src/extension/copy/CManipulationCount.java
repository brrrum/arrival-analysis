package extension.copy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;



public class CManipulationCount {
	
	private CAdditionalReader additionalReader = new CAdditionalReader();
	private CExponent exponent = new CExponent();
	
	public int[] manipulated(int range, int copies) 
	{
		int[] anArray = new int[copies];
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=0; i<range; i++)
		{
			numbers.add(i+1);
		}
		Collections.shuffle(numbers);
		for(int i=0; i<copies; i++)
		{
			//System.out.println(numbers.get(i));
			anArray[i]=numbers.get(i);
		}

		Arrays.sort(anArray);
		return anArray;
	}

}
