package distributionGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class prepareData 
{
        
    ArrayList<articleRecord> record = new ArrayList<articleRecord>();
    
    public prepareData(ArrayList<articleRecord> recordPassed, String savefile)
    {
	record = recordPassed;
	getData(savefile);
    }
    
    public void getData(String savefile)
    {
	try 
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(savefile));
	    TreeSet<String> tree = new TreeSet<String>();
	    for(articleRecord ap: record)
	    {
		tree.add(ap.getId());
	    }
	    
	    ArrayList<String> idList = new ArrayList<String>(tree);
	    for(int i = 0; i < idList.size(); i++)
	    {
		double count = 0;
		for(articleRecord ap: record)
		{
		    if(idList.get(i).compareTo(ap.getId()) == 0)
		    {
			count++;
		    }
		    
		}
		out.write(count + "\n");
	    }
	    out.close();
	} 
	catch (IOException e) 
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
