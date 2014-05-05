package distributionGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DailyMe 
{
        
    private ArrayList<articleRecord> data = new ArrayList<articleRecord>();
    private ArrayList<String> header = new ArrayList<String>();
    
    
    public DailyMe(String filepath)
    {
	prepareData(filepath);
    }
    
    private void prepareData(String filepath)
    {
	try 
	{
	    BufferedReader in = new BufferedReader(new FileReader(filepath));
	    String line = in.readLine();
	    System.out.println(line);
	    
	    int a = 0;
	    int b = line.indexOf(",", a);
	    String article = line.substring(a, b);
	    
	    while(article.compareTo("Article") != 0)
	    {
		header.add(article);
		a = b+1;
		b= line.indexOf(",", a);
		article = line.substring(a, b);		
	    }
	    header.add(article);
	    
	    a = b+1;
	    article = line.substring(a);
	    header.add(article);
	    line = in.readLine(); // first line of data
	    
	    while(line != null)
	    {
		if(line != null)
		{
		    //cookies
		    int a1 = 0;
		    int b1 = line.indexOf(",", a1);
		    String cookie = line.substring(a1, b1);
		    
		    // article id
		    a1 = b1 + 1;
		    b1 = line.indexOf(",", a1);
		    String id = line.substring(a1, b1);
		    
		    // article name
		    a1 = b1 + 1;
		    b1 = line.indexOf(",", a1);
		    String articlename = line.substring(a1, b1);
		    
		    // time
		    a1 = b1 +1;
		    String time = line.substring(a1);
		    
		    articleRecord record = new articleRecord(id, time);
		    record.setId(id);
		    data.add(record);
		    line = in.readLine();		    
		    
		}
	    }
	    
	    in.close();
	    
	    
	} 
	catch (Exception e) 
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public ArrayList<articleRecord> getData() 
    {
	return data;
    }
    
    public void clearcontents()
    {
	data.clear();
    }

}
