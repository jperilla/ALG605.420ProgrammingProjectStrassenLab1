import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class MatrixMultiplication {

	public static void main(String[] args) {

		// Validate input, must be one argument
        if (args.length < 1) {
            throw new IllegalArgumentException("Path to input file must be entered as argument.");
        }
		
        String inputFileName = args[0];
        File outputFile = SetupOutputFile(inputFileName);
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
        	 String strLine;

             // Read in lines of input file
             while ((strLine = br.readLine()) != null && strLine.length()!=0) {
            	 
                 // Read in order
                 int order = Integer.parseInt(strLine);
                 
                 // Read in and create first matrix
                 int[][] matrixOne = new int[order][order];
                 for(int i = 0; i < order; i++) {
                	 strLine = br.readLine();
                     ParseRowIntoMatrix(strLine, matrixOne, i);
                 }
                 
                 // Read in and create second matrix
                 int[][] matrixTwo = new int[order][order];
                 for(int i = 0; i < order; i++) {
                	 strLine = br.readLine();
                     ParseRowIntoMatrix(strLine, matrixTwo, i);
                 }    
                 
                 // Read in space
                 strLine = br.readLine();
         		 
                 // Do ordinary matrix  multiplication first, in order to compare later
                 int size = matrixOne.length;
                 int[][] outputMatrixOrdinary = new int [size][size];
                 int numMultiplicationsOrdinary = MutliplyMatricesOrdinary(matrixOne, matrixTwo, outputMatrixOrdinary);
                 
                 // Do Strassen algorithm for matrix multiplication
                 int[][] outputMatrixStrassen = new int [size][size];
                 int numMultiplicationsStrassen = MutliplyMatricesStrassen(matrixOne, matrixTwo, outputMatrixStrassen);
                 
                 // Write to output file
         		WriteToOutputFile(outputFile, matrixOne, matrixTwo, outputMatrixOrdinary, numMultiplicationsOrdinary, outputMatrixStrassen, numMultiplicationsStrassen);
         		
             }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	private static File SetupOutputFile(String inputFileName) {
		String outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf("Input.")) + "Output.txt";
        File outputFile = new File(outputFileName);
		
        // Initialize output file
        try (Writer writer = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()))) {
        	if (!outputFile.exists()) {
        		outputFile.createNewFile();
    		}
		    writer.write("Julie Garcia Lab 1 Strassen Algorithm Output");
		    writer.write("\r\n");
		    writer.write("\r\n");
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        return outputFile;
	}

	// This function parses one row of the input file into integers
	// and puts them into the current row of the matrix
	private static void ParseRowIntoMatrix(String row, int[][] matrixToAddTo, int currentRow) {
		
		String[] strArray = row.split("\\s+");
		
		for(int i = 0; i < strArray.length; i++) {
		    matrixToAddTo[currentRow][i] = Integer.parseInt(strArray[i]);
		}
	}

	//  This function uses the ordinary method of multiplying matrices, with an Theta(n^3) running time cost
	// It returns the number of multiplications when this is run, in order to compare to the other method
	private static int MutliplyMatricesOrdinary(int[][] matrixOne, int[][] matrixTwo, int[][] outputMatrix) {
		
		// Use this to compare methods
		int numberOfMultiplications = 0;
		
		 // Get order
		int size = matrixOne.length;
		
		// Multiply matrices into outputMatrix
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				outputMatrix[i][j] = 0;
				for(int k = 0; k < size; k++) {
					outputMatrix[i][j] = outputMatrix[i][j] + matrixOne[i][k] * matrixTwo[k][j];
					numberOfMultiplications++;
				}
			}
		}
		
		return numberOfMultiplications;
	}
	
	// This funciton is the recursive Multiply function that multiplies matrices using the Strassen method
	private static int MutliplyMatricesStrassen(int[][] A, int[][] B, int[][] C) {
		
		int size = A.length;
		int half = size/2;
		int numMultiplications = 0;
		
        // Stop at order = 1, we are at the base of the recursion tree
        if (size == 1) {
            C[0][0] = A[0][0] * B[0][0];
            numMultiplications++;
        }
        else {
        	
            // Divide the input matrices into half x half sub-matrices
            int[][] A11 = CreateSubMatrix(A, 0, 0, half);
            int[][] A12 = CreateSubMatrix(A, 0, half, half);
            int[][] A21 = CreateSubMatrix(A, half, 0, half);
            int[][] A22 = CreateSubMatrix(A, half, half, half);
            
            int[][] B11 = CreateSubMatrix(B, 0 , 0, half);
            int[][] B12 = CreateSubMatrix(B, 0 , half, half);
            int[][] B21 = CreateSubMatrix(B, half, 0, half);
            int[][] B22 = CreateSubMatrix(B, half, half, half);          
 
            // Create 10 S matrices, of size order/2 x order/2, the sum or difference of the sub-matrices created above
            int [][] S1 = Subtract(B12, B22);
            int [][] S2 = Add(A11, A12);
            int [][] S3 = Add(A21, A22);
            int [][] S4 = Subtract(B21, B11);
            int [][] S5 = Add(A11, A22);
            int [][] S6 = Add(B11, B22);
            int [][] S7 = Subtract(A12, A22);
            int [][] S8 = Add(B21, B22);
            int [][] S9 = Subtract(A11, A21);
            int [][] S10 = Add(B11, B12);
            
            // Create 7 Product matrices
            int [][] P1 = new int[half][half];
            int [][] P2 = new int[half][half];
            int [][] P3 = new int[half][half];
            int [][] P4 = new int[half][half];
            int [][] P5 = new int[half][half];
            int [][] P6 = new int[half][half];
            int [][] P7 = new int[half][half];
            numMultiplications += MutliplyMatricesStrassen(A11, S1, P1);
            numMultiplications += MutliplyMatricesStrassen(S2, B22, P2);
            numMultiplications += MutliplyMatricesStrassen(S3, B11, P3);
            numMultiplications += MutliplyMatricesStrassen(A22, S4, P4);
            numMultiplications += MutliplyMatricesStrassen(S5, S6, P5);
            numMultiplications += MutliplyMatricesStrassen(S7, S8, P6);
            numMultiplications += MutliplyMatricesStrassen(S9, S10, P7);
            
            // Create result matrix
            int [][] C11 = Add(Subtract(Add(P5, P4), P2), P6);
            int [][] C12 = Add(P1, P2);
            int [][] C21 = Add(P3, P4);
            int [][] C22 = Subtract(Subtract(Add(P5, P1), P3), P7);
            
            // Join C11, C12, C21, C22 into one result C matrix and return
            Join(C11, C12, C21, C22, C);
        }
		
		return numMultiplications;
	}

	// This function creates a submatrix from the given matrix, starting row and columna and size
	private static int[][] CreateSubMatrix(int[][] matrix, int row, int col, int size) {
		int [][] subMatrix = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				subMatrix[i][j] = matrix[i+row][j+col];
			}
		}
		return subMatrix;
	}

	// This function adds two matrices
	private static int[][] Add(int[][] A, int[][] B) {
		int size = A.length;
		int [][] C = new int[size][size];
		for (int i = 0; i < size; i++) {
	        for (int j = 0; j < size; j++) {
	            C[i][j] = A[i][j] + B[i][j];
	        }
	    }
		return C;
	}

	// This function subtracts two matrices
	private static int[][] Subtract(int[][] A, int[][] B) {
		int size = A.length;
		int [][] C = new int[size][size];
		for (int i = 0; i < size; i++) {
	        for (int j = 0; j < size; j++) {
	            C[i][j] = A[i][j] - B[i][j];
	        }
	    }
		return C;
	}
	
	// This function joins four matrices into one larger matrix
	private static void Join(int[][] c11, int[][] c12, int[][] c21, int[][] c22, int[][] c) {
		int halfSize = c11.length;
		for(int i = 0; i < halfSize; i ++) {
			for(int j=0; j < halfSize; j++) {
				c[i][j] = c11[i][j];
				c[i][j+halfSize] = c12[i][j];
				c[i+halfSize][j] = c21[i][j];
				c[i+halfSize][j+halfSize] = c22[i][j];
			}
		}
	}	

	// This function writes the input for each matrix, the output for ordinary and Strassen algorithms and the number of multiplications for each
	private static void WriteToOutputFile(File outputFile, int[][] matrixOne,
			int[][] matrixTwo, int[][] outputMatrixOrdinary, int numMultiplicationsOrdinary, int[][] outputMatrixStrassen, int numMultiplicationsStrassen) {

		
        try (Writer writer = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), true))) {
		    writer.write("Input\r\n");
		    int size = matrixOne.length;
		    writer.write("Input Matrix One\r\n");
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(matrixOne[i][j]) + " ");
				}
				writer.write("\r\n");
			}
		    writer.write("Input Matrix Two\r\n");
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(matrixTwo[i][j]) + " ");
				}
				writer.write("\r\n");
			}
		    writer.write("Result ORDINARY Matrix Mutliplication\r\n");
		    writer.write("Number of Multiplications = " + numMultiplicationsOrdinary + "\r\n");
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(outputMatrixOrdinary[i][j]) + " ");
				}
				writer.write("\r\n");
			}
		    writer.write("Result STRASSEN Matrix Mutliplication\r\n");
		    writer.write("Number of Multiplications = " + numMultiplicationsStrassen + "\r\n");
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(outputMatrixStrassen[i][j]) + " ");
				}
				writer.write("\r\n");
			}
			writer.write("\r\n");
			writer.write("\r\n");
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
