package extension;

import java.io.File;

public class projectVariables 
{
    String PROJECT_PATH = "C" + ":" + File.separator + "eclipse-projects" + File.separator + "workspace" + File.separator + "reviewprobabilistic" + File.separator;
    
    String strRep = Double.toString(0);
    String strDirectory = Double.toString(0.9);
    String crDirectory = "probability= ".concat(strDirectory);
    String finalDirectory = strRep.concat(crDirectory);
    String SOURCE_PATH = PROJECT_PATH + finalDirectory + "/"; 
    final double ZEXPONENT = 1.4;
    final double webExponent = 5/7; // for exponent value = 2.4
    public boolean randomModel = true;
    

}
