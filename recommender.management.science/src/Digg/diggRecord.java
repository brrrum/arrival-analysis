package Digg;

public class diggRecord 
{
    private String id, time;
    private int count;
    
    public diggRecord(String id)
    {
	this.id = id;
    }
    
    public void setTime(String time)
    {
	this.time = time;
    }
    
    public void setcumcount(int count)
    {
	this.count = count;
    }
    
    public String getId()
    {
	return id;
    }
    
    public String getTime()
    {
	return time;
    }
    
    public int getcumcount()
    {
	return count;
    }

}
