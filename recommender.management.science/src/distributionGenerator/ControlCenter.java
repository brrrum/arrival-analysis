package distributionGenerator;


import java.io.File;
import java.io.IOException;


public class ControlCenter 
{
    private final static int SIZE = 200;
    static double a = -1.71;
    static double b = -1.63;
    private final static double MEAN = 5.925;
    static double c = 0.44;
    static double d = 0.50;
    private final static double VARIANCE = Math.pow(0.5451, 2);
    private final static int NUM_OF_ELEMENTS = 1000;
    private static double EXPONENT = 1.4;
    // for web-based systems = 0.7
    
    
    private static final String FILE_PATH = "C" + ":" + File.separator + "academic" + File.separator+"recommender system"+File.separator+"review"+File.separator + "count distribution";
    static Distributions distributions = new Distributions();
    
    /*private static final String FILE_PATH = "C" + ":" + File.separator
	    + "academic" + File.separator + "DailyMe" + File.separator;
    
    private static final String DESFILE_PATH = "C" + ":" + File.separator
	    + "academic" + File.separator + "recommender system" + File.separator
	    + "review" + File.separator + "count analysis" + File.separator;
    
    private static String[] filepaths = new String[10];  
    */
    public static void main(String[] args) throws IOException
    {
	/*
	filepaths[0] = FILE_PATH + "jrc_conn" + File.separator + "jrc_conn.csv";
	filepaths[1] = FILE_PATH + "jrc_ny" + File.separator + "jrc_ny.csv";
	filepaths[2] = FILE_PATH + "jrc_philly" + File.separator + "jrc_philly.csv";
	filepaths[3] = FILE_PATH + "mng_colorado" + File.separator + "mng_colorado.csv";
	filepaths[4] = FILE_PATH + "mng_mass" + File.separator + "mng_mass.csv";
	
	filepaths[5] = DESFILE_PATH + "jrc_count.csv";
	filepaths[6] = DESFILE_PATH + "jrc_ny.csv";
	filepaths[7] = DESFILE_PATH + "jrc_philly.csv";
	filepaths[8] = DESFILE_PATH + "jrc_colorado.csv";
	filepaths[9] = DESFILE_PATH + "jrc_mass.csv";
	
	for(int i = 1; i <2; i++)
	{
	    DailyMe dailyme = new DailyMe(filepaths[i]);
	    new prepareData(dailyme.getData(), filepaths[5+i]);
	    dailyme.clearcontents();
	    
	}
	*/
		
	System.out.println(EXPONENT);
	Distributions distributions = new Distributions();
	distributions.d_Distributions(SIZE, NUM_OF_ELEMENTS, EXPONENT, MEAN, VARIANCE, FILE_PATH);

    }
}
