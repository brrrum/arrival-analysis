package extension;

import org.apache.commons.math3.distribution.ZipfDistribution;

public class AdditionalReader 
{
    private projectVariables projectvar = new projectVariables();
    private double exponent;
    // top = 10
    public int ZipfReader(int top, String[] a) 
    {
	exponent = projectvar.ZEXPONENT;
	int index = top + new ZipfDistribution(a.length - top, exponent).sample();
	return index;
		
    }

}
