package distributionGenerator;

public class articleRecord 
{
    private String id;
    private String time;
    private int cumcount;
    
    
    public articleRecord(String id, String time)
    {
	this.id = id;
	this.time = time;
    }
    
    public void setId(String s)
    {
	id = new String(s);
    }
    
    public void setTime(String ti)
    {
	time = new String(ti);
    }
    
    public void setCumCount(int count)
    {
	cumcount = count;
    }
    
    public String getId()
    {
	return new String(id);
    }
    
    public String getTime()
    {
	return new String(time);
    }
    
    public int getCumCount()
    {
	return cumcount;
    }
    

}
