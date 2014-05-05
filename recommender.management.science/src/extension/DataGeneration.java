package extension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataGeneration 
{
	public static String[] readFile(String file_path)throws IOException
	{
		FileReader fr = new FileReader(file_path);
		BufferedReader txtReader = new BufferedReader(fr);

		//int numberofLines = 3;
		int numberofLines = readLines(file_path);
		String[] txtData = new String[numberofLines];

		for(int i=0; i<numberofLines; i++)
		{
			txtData[i] = txtReader.readLine();
		}

		txtReader.close();
		return txtData;
	}

	static int readLines(String file_path) throws IOException
	{
		FileReader file_to_read = new FileReader(file_path);
		BufferedReader bf = new BufferedReader(file_to_read);

		int numberofLines = 0;
		while((bf.readLine()) !=null)
		{
			numberofLines++;
		}
		bf.close();
		return numberofLines;		
	}
}
