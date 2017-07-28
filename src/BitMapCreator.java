import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;
import java.util.regex.PatternSyntaxException;

public class BitMapCreator
{	
	private static String fileName;
	private static Scanner keyboard;
	private static String[][] bitMap;
	private static int column;
	private static int row;
	
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
	
	/*
	 * This method create a grid when the command "I" is called. As the command
	 * should have 2 more arguments, at first the method checks if the analyzed
	 * command string contains 3 words. If so, it checks the number of columns
	 * rows as the must be between 1 and 250. If they do not exceed the threshold 
	 * then a grid is created.
	 */
	private static void createGrid(String[] spec, int count)
	{
		if (spec.length != 3)
		{
			System.err.println("The command at line: " + count + " is invalid.");
			System.exit(0);
		}
		else
		{
			/*
			 * Try-catch that prevents the position parameters (column and row)
			 * to be something different that an int.
			 */
			try
			{
				column = Integer.parseInt(spec[1]);
				row = Integer.parseInt(spec[2]);
				if ((column >=1 && column <=250) && (row >=1 && row <= 250))
				{
					bitMap = new String[row][column];
					for (int i = 0; i< row; i++)
					{
						for (int j = 0; j< column; j++)
						{
							bitMap[i][j] = "O";
						}
					}
				}
			
				else
				{
					System.err.println("Error at line " + count + ": The max size is 250 and min is 0.");
					System.exit(0);
				}
			}
			catch (NumberFormatException e)
			{
				System.err.println("Error at line " + count + ": The command is invalid.");
				System.err.println("The two parameters column and row size must be int");
				System.exit(0);
			}
		}		
	}
	
	/*
	 * This method clear the table, setting all the pixels to O.
	 * As the command is only composed by 'word' "C" (with no arguments),
	 * the method checks if the length of the array of string spec is
	 * only 1 value long. If so it clears the table.
	 */
	private static void clearGrid(String[] spec, int count)
	{
		if(spec.length==1)
		{
			for (int i = 0; i< row; i++)
			{
				for (int j = 0; j< column; j++)
				{
					bitMap[i][j] = "O";
				}
			}
		}
		else
		{
			System.err.println("Error at line " + count + ": The command is invalid.");
			System.exit(0);
		}
	}
	
	/*
	 * This method change one single pixel on the table. 
	 * The command "L" has 3 parameters (column, row and colour),
	 * so the method checks if the string array spec is 4 value long. 
	 * As the user can make a mistake and call the command with 
	 * a number of row and column that exceeds the size of the create array,
	 * a "try-catch" is used to throw the 'ArrayIndexOutOfBoundsException' exception.
	 */
	private static void changePixel(String[] spec, int count)
	{
		if(spec.length==4)
		{
			/*
			 * Checks if the colour parameter is a single capital letter.
			 */
			if ((spec[3].length() == 1) && (Character.isUpperCase(spec[3].charAt(0))))
			{
				/*
				 * Try-catch that prevents the position parameters (column and row)
				 * to be something different that an int.
				 */
				try
				{
					int posRow = Integer.parseInt(spec[2]);
					int posColumn = Integer.parseInt(spec[1]);
					
					/*
					 * Handle IndexOutOfBound Exception for bitmap size.
					 */					
					try
					{
						bitMap[posRow-1][posColumn-1] = spec[3];
					}
					catch (ArrayIndexOutOfBoundsException ex)
					{
						System.err.println("Error at line " + count + ": limit of the grid exceeded.");
						System.exit(0);
					}
					}
				catch (NumberFormatException e)
				{
					System.err.println("Error at line " + count + ": The command is invalid.");
					System.err.println("The parameters of column position and row position must be int");
					System.exit(0);
				}
			}
			else
			{
				System.err.println("Error at line " + count + ": The command is invalid.");
				System.err.println("The colour parameter muse be a capital letter");
				System.exit(0);
			}
		}
		else
		{
			System.err.println("Error at line " + count + ": The command is invalid.");
			System.exit(0);
		}
		
		
	}
	
	/*
	 * This method shows the table.
	 * As the command "S" has no parameters, it checks if the
	 * command line has only 1 word. If so, the table is printed
	 * in a new file called output.txt.
	 */
	
	private static void showGrid(String[] spec, int count)
	{
		if (spec.length == 1)
		{
			BufferedWriter writer = null;
			try
			{
				writer = new BufferedWriter(new FileWriter("examples/output.txt"));			
				for (int i=0; i< row; i++)
				{
					for (int j = 0; j< column; j++)
					{
						writer.write(bitMap[i][j] + "  ");
					}
					writer.newLine();
				}
			}
			catch (IOException e)
			{
				System.err.println("An error occured while writing on output file");
				System.exit(0);
			}
			finally
			{
				try
				{
					if (writer != null)
						writer.close();
				}
				catch (IOException e)
				{
					System.err.println("An error occured while writing on output file");
					System.exit(0);
				}
			}
		}
		else
		{
			System.err.println("Error at line " + count + ": The command is invalid.");
			System.exit(0);
		}
	}
	
	/*
	 * This method draws a vertical segment of colour C in column X between rows Y1 and Y2 (inclusive).
	 * As the command "V" should have 4 parameters (column, row Y1, row Y2 and colour), it checks if the 
	 * command line has 5 words. If so, the method checks that Y1 is less that Y2, otherwise it inverts
	 * the two values, so that the vertical line is still drawn. 
	 */

	private static void verticalLine(String[] spec, int count)
	{
		if (spec.length == 5)
		{
			/*
			 * Checks if the colour parameter is a single capital letter.
			 */
			if ((spec[4].length() == 1) && (Character.isUpperCase(spec[4].charAt(0))))
			{
				/*
				 * Try-catch that prevents the position parameters (column and row)
				 * to be something different that an int.
				 */
				try
				{
					int posColumn = Integer.parseInt(spec[1]);
					int fromRow = Integer.parseInt(spec[2]);
					int toRow = Integer.parseInt(spec[3]);
					if (fromRow > toRow)
					{
						int temp = fromRow;
						fromRow = toRow;
						toRow = temp;
					}
					String colour = spec[4];
					/*
					 * Handle IndexOutOfBound Exception for bitmap size.
					 */
					try
					{
						for (int i = fromRow-1; i<toRow; i++)
						{
							bitMap[i][posColumn-1] = colour;
						}
					}
					catch (ArrayIndexOutOfBoundsException ex)
					{
						System.err.println("Error at line " + count + ": limit of the grid exceeded.");
						System.exit(0);
					}
				}
				catch (NumberFormatException e)
				{
					System.err.println("Error at line " + count + ": The command is invalid.");
					System.err.println("The parameters of column X, row Y1 and rowY2 must be int");
					System.exit(0);
				}
			}
			else
			{
				System.err.println("Error at line " + count + ": The command is invalid.");
				System.err.println("The colour parameter muse be a capital letter");
				System.exit(0);
			}
		}
		else
		{
			System.err.println("Error at line " + count + ": The command is invalid.");
			System.exit(0);
		}
	}
	
	/*
	 * This method draws a horizontal segment of colour C in row Y between columns X1 and X2 (inclusive).
	 * As the command "H" should have 4 parameters (column X1, column X2, row and colour), it checks if the 
	 * command line has 5 words. If so, the method checks that X1 is less that X2, otherwise it inverts
	 * the two values, so that the horizontal line is still drawn. 	
	 */
	private static void horizontalLine(String[] spec, int count)
	{
		if (spec.length == 5)
		{
			/*
			 * Checks if the colour parameter is a single capital letter.
			 */
			if ((spec[4].length() == 1) && (Character.isUpperCase(spec[4].charAt(0))))
			{
				/*
				 * Try-catch that prevents the position parameters (column and row)
				 * to be something different that an int.
				 */
				try
				{
					int posRow = Integer.parseInt(spec[3]);
					int fromColumn = Integer.parseInt(spec[1]);
					int toColumn = Integer.parseInt(spec[2]);
					
					if (fromColumn > toColumn)
					{
						int temp = fromColumn;
						fromColumn = toColumn;
						toColumn = temp;
					}
					String colour = spec[4];
					/*
					 * Handle IndexOutOfBound Exception for bitmap size.
					 */
					try
					{
						for (int i = fromColumn-1; i<toColumn; i++)
						{
							bitMap[posRow-1][i] = colour;
						}
					}
					catch (ArrayIndexOutOfBoundsException ex)
					{
						System.err.println("Error at line " + count + ": limit of the grid exceeded.");
						System.exit(0);
					}
				}
				catch (NumberFormatException e)
				{
					System.err.println("Error at line " + count + ": The command is invalid.");
					System.err.println("The parameters of column X1, column X2 and row Y must be int");
					System.exit(0);
				}
			
			}
			else
			{
				System.err.println("Error at line " + count + ": The command is invalid.");
				System.err.println("The colour parameter muse be a capital letter");
				System.exit(0);
			}
			
		}
		else
		{
			System.err.println("Error at line " + count + ": The command is invalid.");
		}
	}
	public static void main(String[] args) throws FileNotFoundException
	{
		int countLine = 0;
		System.out.println("Type the input file name, please.");
		keyboard = new Scanner(System.in);
		fileName = keyboard.nextLine();
		checkFile(fileName);
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			
			/*
			 * loop for each line until EOF.
			 */
			
			while (line!=null)
			{
				String[] lineSpecs = null;
				//Empty lines are also counted to make the user understand better where the 
				// error is located. 
				countLine++;
				//Empty lines are ignored. 
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
						System.exit(0);
					}
									
				/*
				 * The first command of the specs file should be the one that creates the grid. 
				 * So if the reader finds in the first line another command then it returns an error. 
				 */
				
					if (countLine==1)
					{
						if (lineSpecs[0].equals("I"))
						{
							createGrid(lineSpecs, countLine);
							
						}
						else
						{
							System.err.println("The first command should be I ..");
							System.exit(0);
						}
					}
					else
					{
						/*
						 * Switch-case to see which command is called at each line. 
						 * Every method has two parameters, the line specs which is
						 * the command line, and countLine. the variable countLine
						 * allows to tell the user at which line the error in the spec file
						 * is located. 
						 */
						switch (lineSpecs[0])
						{
						case ("C"):
							clearGrid(lineSpecs, countLine);
							break;
						case ("L"):
							changePixel(lineSpecs, countLine);
							break;
						case ("V"):
							verticalLine(lineSpecs, countLine);
							break;					
						case ("H"):
							horizontalLine(lineSpecs,countLine);
							break;
						case ("S"):
							showGrid(lineSpecs, countLine);
							break;
						case ("I"):
							createGrid(lineSpecs, countLine);
							break;
						}
					}
				
				}
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException ex)
		{
			System.err.println("Cannot read file");
		}
	}
}
