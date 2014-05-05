package DailyMePlot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.TreeSet;

import distributionGenerator.articleRecord;

public class CumulativeCounts 
{
    private ArrayList<articleRecord> record = new ArrayList<articleRecord>();
    private ArrayList<articleRecord> cumrecord = new ArrayList<articleRecord>();
    private String FILE_PATH = 
	    "C" + ":" + File.separator + "academic" + File.separator+"recommender system 2"+File.separator+
	    "Management Science"+File.separator;
    
    public CumulativeCounts(ArrayList<articleRecord> recordpassed)
    {
	record = recordpassed;
	modifiedData();
    }
    
    public void modifiedData()
    {
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "mass-clicks" + File.separator + "cumcounts_mass.csv"));
	    //BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "philly-clicks" + File.separator + "cumcounts_philly.csv"));
	    //BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "conn-clicks" + File.separator + "cumcounts_conn.csv"));
	    //BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "cumcounts_ny.csv"));
	    //BufferedWriter out1 = new BufferedWriter(new FileWriter(FILE_PATH + "id_ny.csv"));
	    BufferedWriter out1 = new BufferedWriter(new FileWriter(FILE_PATH + "mass-clicks" + File.separator + "id_mass.csv"));
	    TreeSet<String> tree = new TreeSet<String>();
	    for(articleRecord ar: record)
	    {
		tree.add(ar.getId());
	    }
	    
	    ArrayList<String> idList = new ArrayList<String>(tree);
	    for(int i = 0; i < idList.size(); i++)
	    {
		out1.write(idList.get(i) + "\n"); 
		int count = 0; 
		for(articleRecord ar: record)
		{
		    if(idList.get(i).compareTo(ar.getId()) == 0)
		    {			
			count++;
			articleRecord crecord = new articleRecord(ar.getId(), ar.getTime());
			crecord.setCumCount(count);
			cumrecord.add(crecord);
			out.write(idList.get(i) + "," + ar.getTime() + "," + count + "\n");
		    }
		}		
		
	    }
	    out.close();
	    out1.close();
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}
    }
    
    public ArrayList<articleRecord> getData()
    {
	return cumrecord;
    }

}
