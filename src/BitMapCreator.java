import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class BitMapCreator {

	private static String fileName;
	private static Scanner keyboard;
	
	/*
	 * This method is used to check if
	 * the string given corresponds to the name of
	 * an existing file. If not, the program will 
	 * continuously ask for a correct file name	 * 
	 */
	private static void checkFile(String file)
	{
		File f = new File(file);
		if (!(f.exists() && !f.isDirectory()))
		{
			System.out.println("File not found, please retry.");
			fileName = keyboard.nextLine();
			checkFile(fileName);
		}	
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		int countLine = 0;
		Scanner readInput;
		/*
		 * Ask the user to type the file name containing the specs for creating 
		 * the bitmap
		 */
		System.out.println("Type the input file name, please.");
		keyboard = new Scanner(System.in);
		fileName = keyboard.nextLine();
		checkFile(fileName);
		/*
		 * Catch IOException for the BufferedReader function. 
		 */
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			//trim the read line so that the reader ignores the spaces at the beginning
			//and at the end of the line. 
			//String trimmedFirstLine = line.trim();
			//Create array of string containing each word of the line split by one or more spaces. 
			//String[] lineSpecs = trimmedFirstLine.split("\\s+");
			
			/*
			 * loop for each line until EOF.
			 */
			
			while (line!=null)
			{
				String[] lineSpecs = null;
				countLine++;
				if(line.length()>0)
				{
					//trim the read line so that the reader ignores the spaces at the beginning
					String trim = line.trim();
					try
					{
						//Create array of string containing each word of the line split by one or more spaces. 
						lineSpecs = trim.split("\\s+");
					}
					catch (PatternSyntaxException ex)
					{
						System.err.println("ERROR");
					}
					
				}
				
				/*
				 * The first command of the specs file should be the one that creates the grid. 
				 * So if the reader finds in the first line another command then it returns an error. 
				 */
				if (countLine==1)
				{
					if (lineSpecs[0].equals("I"))
					{
						//create grid
						
					}
					else
					{
						System.err.println("The first command should be I ..");
					}
				}
				else
				{
					switch (lineSpecs[0])
					{
					case ("C"):
						//clear grid
						break;
					case ("L"):
						//change pixel
						break;
					case ("V"):
						//vertical
						break;					
					case ("H"):
						//horizontal
						break;
					case ("S"):
						//show
						break;
					case ("I"):
						//create grid
						break;
					}
				}
				line = reader.readLine();
			}
		}
		/*
		 * Catch IOException for the BufferedReader function. 
		 */
		catch (IOException ex)
		{
			System.err.println("Cannot read file");
		}
	}
}
