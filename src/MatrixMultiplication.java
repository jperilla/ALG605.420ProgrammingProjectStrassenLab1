/*
 * Julie Garcia
 * 
 * Copyright (c) Julie Garcia 2016
 * 
 * ALG 605.420.81.FA16
 * 
 * Programming Project #1 - Strassen Algorithm Lab
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/** 
 * 
 * @author Julie Garcia
 * 
 * @version 1.0.0 26 Sept 2016
 * 
 * This class provides a comparison of two different matrix multiplication algorithms.
 * The first algorithm is ordinary matrix multiplication, which take O(n^3) time to run.
 * The second algorithm is the Strassen algorithm for matrix multiplication, which
 * takes O(n^log7) to run. You can see from the output that the Strassen Algorithm does 
 * less multiplications and the gap gets bigger as the input matrix order gets bigger
 * 
 * Design:
 * 
 * What I learned:
 * 
 * What I would do differently:
 * 
 * Efficiency, Time and Space Analysis: 
 * 
 * How is this useful to Bioinformatics: This is my first Bioinformatics Course, so I don't have
 * a lot of experience, but I did some research. 
 *
 */
public class MatrixMultiplication {

	public static void main(String[] args) {

		// Validate input, must be one argument
        if (args.length < 1) {
            throw new IllegalArgumentException(
            			"Path to input file must be entered as argument.");
        }
        
        String inputFileName = args[0];
        File outputFile = setupOutputFile(inputFileName);
        
        try (BufferedReader br = 
        		new BufferedReader(new FileReader(inputFileName))) {
        	 String strLine;

             while ((strLine = br.readLine()) != null 
            		 	&& strLine.length()!=0) {
            	 
                 // Read in order
                 int order = Integer.parseInt(strLine);                 
                 
                 // Read input matrices
                 int[][] matrixOne = readNextMatrix(br, order);                 
                 int[][] matrixTwo = readNextMatrix(br, order);  
                 strLine = br.readLine();

                 // Check to see if order is a power of two
	             if((order & (order - 1)) == 0) { 
	            	 
	                 // Do ordinary matrix  multiplication first
	                 int size = matrixOne.length;
	                 int[][] outputMatrixOrdinary = new int [size][size];
	                 int numMultiplicationsOrdinary 
	                 		= mutliplyMatricesOrdinary(matrixOne, matrixTwo,
	                 				outputMatrixOrdinary);
	                 
	                 // Do Strassen algorithm for matrix multiplication
	                 int[][] outputMatrixStrassen = new int [size][size];
	                 int numMultiplicationsStrassen 
	                 		= mutliplyMatricesStrassen(matrixOne, matrixTwo,
	                 				outputMatrixStrassen);
                 
	         		writeToOutputFile(outputFile, matrixOne, matrixTwo, 
							outputMatrixOrdinary, numMultiplicationsOrdinary, 
							outputMatrixStrassen, 
							numMultiplicationsStrassen);
                 }
                 else {
                	 writeErrorToOutputFile(outputFile, 
                			 "Order " + order + " must be a power of 2.", 
                			 matrixOne, matrixTwo);
                 }
         		
             }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        catch (ArrayIndexOutOfBoundsException e) {
        	writeErrorToOutputFile(outputFile, 
        			"File Format Error see README file for proper input file format.", 
        			null, null);
        }
		
	}
 
	/**
	 * This function creates a new output file if it doesn't exist
	 * and sets up the output file with a title.
	 * 
	 * @param inputFileName The name of the input file, should include the path if it's
	 * not in the same directory
	 * @return The output file
	 */
	private static File setupOutputFile(String inputFileName) {
		
		String outputFileName = inputFileName.substring(0, 
				inputFileName.lastIndexOf("Input.")) + "Output.txt";
		
        File outputFile = new File(outputFileName);
		
        // Initialize output file
        try (Writer writer = new BufferedWriter(
        		new FileWriter(outputFile.getAbsoluteFile()))) {
        	
        	// Check if file exists, if not create it
        	if (!outputFile.exists()) {
        		outputFile.createNewFile();
    		}
        	
        	// Title of Output file
		    writer.write("Julie Garcia Lab 1 Strassen Algorithm Output");
		    writer.write("\r\n");
		    writer.write("\r\n");
        } catch (IOException e1) {
			e1.printStackTrace();
		}
        
        return outputFile;
	}	
	
	/** 
	 * This function reads the next matrix in the buffer
	 * into nextMatrix and returns it.
	 *  
	 * @param br The buffered reader to read the matrix from
	 * @param order the order or size of the matrix, must be square (nxn)
	 * @return the next matrix read in from the file
	 * @throws IOException
	 */
	private static int[][] readNextMatrix(BufferedReader br, int order) 
								throws IOException {
		String strLine;
		int[][] nextMatrix = new int[order][order];
		 for(int i = 0; i < order; i++) {
			 strLine = br.readLine();
			 String[] strArray = strLine.split("\\s+");
			 for(int j = 0; j < strArray.length; j++) {
				    nextMatrix[i][j] = Integer.parseInt(strArray[j]);
				}
		 }
		return nextMatrix;
	}
	 
	/**
	 * This function uses the ordinary method of multiplying matrices
	 * with an Theta(n^3) running time cost. It returns the number of 
	 * multiplications when this is run in order to compare to other 
	 * methods
	 * 
	 * @param A First matrix to multiply
	 * @param B Second matrix to multiply
	 * @param C Output Matrix after multiplication (A X B)
	 * @return The total number of multiplications executed
	 */
	private static int mutliplyMatricesOrdinary(int[][] A, 
							int[][] B, int[][] C) {
		
		// Use this to compare methods
		int numberOfMultiplications = 0;
		
		 // Get order
		int size = A.length;
		
		// Multiply matrices into outputMatrix
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				C[i][j] = 0;
				for(int k = 0; k < size; k++) {
					C[i][j] = C[i][j] 
											+ A[i][k] 
											* B[k][j];
					numberOfMultiplications++;
				}
			}
		}
		
		return numberOfMultiplications;
	}
	 
	/** 
	 * This function is the recursive Multiply function that
	 * multiplies matrices using the Strassen method. The
	 * running time cost is O(n^lg7)
	 * 
	 * @param A First matrix to multiply
	 * @param B Second matrix to multiply
	 * @param C Output Matrix after multiplication (A X B)
	 * @return The total number of multiplications executed
	 */
	private static int mutliplyMatricesStrassen(int[][] A, int[][] B, int[][] C) {
		
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
            int[][] A11 = createSubMatrix(A, 0, 0, half);
            int[][] A12 = createSubMatrix(A, 0, half, half);
            int[][] A21 = createSubMatrix(A, half, 0, half);
            int[][] A22 = createSubMatrix(A, half, half, half);
            
            int[][] B11 = createSubMatrix(B, 0 , 0, half);
            int[][] B12 = createSubMatrix(B, 0 , half, half);
            int[][] B21 = createSubMatrix(B, half, 0, half);
            int[][] B22 = createSubMatrix(B, half, half, half);          
 
            // Create 10 S matrices, of size order/2 x order/2
            // the sum or difference of the sub-matrices created above
            int [][] S1 = subtract(B12, B22);
            int [][] S2 = add(A11, A12);
            int [][] S3 = add(A21, A22);
            int [][] S4 = subtract(B21, B11);
            int [][] S5 = add(A11, A22);
            int [][] S6 = add(B11, B22);
            int [][] S7 = subtract(A12, A22);
            int [][] S8 = add(B21, B22);
            int [][] S9 = subtract(A11, A21);
            int [][] S10 = add(B11, B12);
            
            // Create 7 Product matrices
            int [][] P1 = new int[half][half];
            int [][] P2 = new int[half][half];
            int [][] P3 = new int[half][half];
            int [][] P4 = new int[half][half];
            int [][] P5 = new int[half][half];
            int [][] P6 = new int[half][half];
            int [][] P7 = new int[half][half];
            numMultiplications += mutliplyMatricesStrassen(A11, S1, P1);
            numMultiplications += mutliplyMatricesStrassen(S2, B22, P2);
            numMultiplications += mutliplyMatricesStrassen(S3, B11, P3);
            numMultiplications += mutliplyMatricesStrassen(A22, S4, P4);
            numMultiplications += mutliplyMatricesStrassen(S5, S6, P5);
            numMultiplications += mutliplyMatricesStrassen(S7, S8, P6);
            numMultiplications += mutliplyMatricesStrassen(S9, S10, P7);
            
            // Create result matrix
            int [][] C11 = add(subtract(add(P5, P4), P2), P6);
            int [][] C12 = add(P1, P2);
            int [][] C21 = add(P3, P4);
            int [][] C22 = subtract(subtract(add(P5, P1), P3), P7);
            
            // Join C11, C12, C21, C22 into one result C matrix and return
            join(C11, C12, C21, C22, C);
        }
		
		return numMultiplications;
	}
 
	/**
	 * This function creates a sub-matrix from the given matrix
	 * starting at row and column, with size = size.
	 * 
	 * @param matrix The matrix to sub-matrix
	 * @param row The starting row of the sub-matrix
	 * @param col The starting column of the sub-matrix
	 * @param size The size of the sub-matrix
	 * @return The sub-matrix created
	 */
	private static int[][] createSubMatrix(int[][] matrix, int row, int col, int size) {
		int [][] subMatrix = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				subMatrix[i][j] = matrix[i+row][j+col];
			}
		}
		return subMatrix;
	}

	/**
	 * This function adds two matrices (A + B).
	 * 
	 * @param A First matrix to add
	 * @param B Second matrix to add
	 * @return Output matrix (A + B)
	 */
	private static int[][] add(int[][] A, int[][] B) {
		int size = A.length;
		int [][] C = new int[size][size];
		for (int i = 0; i < size; i++) {
	        for (int j = 0; j < size; j++) {
	            C[i][j] = A[i][j] + B[i][j];
	        }
	    }
		return C;
	}

	/**
	 * This function subtracts two matrices (A + B).
	 * 
	 * @param A First matrix to subtract
	 * @param B Second matrix to subtract
	 * @return Output matrix (A - B)
	 */
	private static int[][] subtract(int[][] A, int[][] B) {
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
	/**
	 * This function joins four matrices (quadrants) into one matrix C.
	 * 
	 * @param c11 Top left matrix
	 * @param c12 Top right matrix
	 * @param c21 Bottom left matrix
	 * @param c22 Bottom right matrix
	 * @param c The resulting matrix
	 */
	private static void join(int[][] c11, int[][] c12, int[][] c21, int[][] c22, int[][] c) {
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

	// This function writes the input for each matrix, the output for ordinary
	// and Strassen algorithms and the number of multiplications for each
	/**
	 * This funciton writes the input for each matrix, the output for
	 * ordiniary matrix mutliplication, the output for Strassen matrix
	 * multiplication and the number of multiplications executed for each
	 * method.
	 * 
	 * @param outputFile Output file to write to
	 * @param matrixOne The first input matrix, to be reiterated in output file
	 * @param matrixTwo The second input matrix, to be reiterated in input file
	 * @param outputMatrixOrdinary The output matrix from ordinary matrix multiplication
	 * @param numMultiplicationsOrdinary The number of multiplications executed during ordinary matrix multiplication
	 * @param outputMatrixStrassen The output matrix from Strassen matrix multiplication
	 * @param numMultiplicationsStrassen The number of multiplications executed during Strassen matrix multiplication
	 */
	private static void writeToOutputFile(File outputFile, int[][] matrixOne,
			int[][] matrixTwo, int[][] outputMatrixOrdinary, int numMultiplicationsOrdinary, 
			int[][] outputMatrixStrassen, int numMultiplicationsStrassen) {

		
        try (Writer writer = new BufferedWriter(
        			new FileWriter(outputFile.getAbsoluteFile(), true))) {
		    
        	// Write input matrices to file
		    int size = matrixOne.length;
		    writeInputMatricesToOutputFile(matrixOne, matrixTwo, writer);
		    
		    // Write results to file
		    writer.write("Result ORDINARY Matrix Mutliplication\r\n");
		    writer.write("Number of Multiplications = " 
		    				+ numMultiplicationsOrdinary + "\r\n");
		    
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(outputMatrixOrdinary[i][j]) + " ");
				}
				writer.write("\r\n");
			}
		    
		    writer.write("Result STRASSEN Matrix Mutliplication\r\n");
		    writer.write("Number of Multiplications = " 
		    				+ numMultiplicationsStrassen + "\r\n");
		    for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					writer.write(Integer.toString(outputMatrixStrassen[i][j]) + " ");
				}
				writer.write("\r\n");
			}
		    
		    // Spacing
			writer.write("\r\n");
			writer.write("\r\n");
        } catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	/**
	 * This function writes the input matrices to the output file.
	 * 
	 * @param matrixOne First input matrix
	 * @param matrixTwo Second input matrix
	 * @param writer File write
	 * @throws IOException
	 */
	private static void writeInputMatricesToOutputFile(int[][] matrixOne, 
				int[][] matrixTwo, Writer writer)	throws IOException {
		int size = matrixOne.length;
		
		writer.write("Input\r\nOrder = " + size + "\r\n");
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
	}
	
    /**
     * This function writes and error to the output file
     * 
     * @param outputFile The output file to be written to
     * @param error The error message
     * @param matrixOne The first matrix, if one was properly read in, if not this can be null
     * @param matrixTwo The second matrix, if one was properly read in, if not this can be null
     */
	private static void writeErrorToOutputFile(File outputFile, String error, 
							int[][] matrixOne, int[][] matrixTwo) {
		
		try (Writer writer = new BufferedWriter(
				new FileWriter(outputFile.getAbsoluteFile(), true))) {

			if(matrixOne != null && matrixTwo != null) {
				writeInputMatricesToOutputFile(matrixOne, matrixTwo, writer);
			}
			
		    writer.write("Result: ERROR - " + error + "\r\n\r\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

}
