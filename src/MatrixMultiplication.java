import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MatrixMultiplication {

	public static void main(String[] args) {

		// Validate input, must be one argument
        if (args.length < 1) {
            throw new IllegalArgumentException("Path to input file must be entered as argument.");
        }
		
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
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
         		 
                 int[][] outputMatrixOrdinary = MutliplyMatricesOrdinary(matrixOne, matrixTwo);
                 
                 int[][] outputMatrixStrassen = MutliplyMatricesStrassen(matrixOne, matrixTwo);
                 
                 // Write to output file
         		
         		System.out.println("Success!");
             }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	// This function parses one row of the input file into integers
	// and puts them into the current row of the matrix
	private static void ParseRowIntoMatrix(String row, int[][] matrixToAddTo, int currentRow) {
		
		String[] strArray = row.split("\\s+");
		
		for(int i = 0; i < strArray.length; i++) {
		    matrixToAddTo[currentRow][i] = Integer.parseInt(strArray[i]);
		}
	}
	
	// This funciton is the recursive Mutliply function that multiplies matrices using the Strassen method
	private static int[][] MutliplyMatricesStrassen(int[][] A, int[][] B) {
		
		int size = A.length;
		int half = size/2;
		int[][] C = new int [size][size];
		
        // Stop at order = 1, we are at the base of the recursion tree
        if (size == 1) {
            C[0][0] = A[0][0] * B[0][0];
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
            int [][] P1 = MutliplyMatricesStrassen(A11, S1);
            int [][] P2 = MutliplyMatricesStrassen(S2, B22);
            int [][] P3 = MutliplyMatricesStrassen(S3, B11);
            int [][] P4 = MutliplyMatricesStrassen(A22, S4);
            int [][] P5 = MutliplyMatricesStrassen(S5, S6);
            int [][] P6 = MutliplyMatricesStrassen(S7, S8);
            int [][] P7 = MutliplyMatricesStrassen(S9, S10);
            
            // Create result matrix
            int [][] C11 = Add(Subtract(Add(P5, P4), P2), P6);
            int [][] C12 = Add(P1, P2);
            int [][] C21 = Add(P3, P4);
            int [][] C22 = Subtract(Subtract(Add(P5, P1), P3), P7);
            
            // Join C11, C12, C21, C22 into one result C matrix and return
            C = Join(C11, C12, C21, C22);
        }
		
		return C;
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
	private static int[][] Join(int[][] c11, int[][] c12, int[][] c21, int[][] c22) {
		int halfSize = c11.length;
		int size = halfSize * 2;
		int [][] result = new int[size][size];
		for(int i = 0; i < halfSize; i ++) {
			for(int j=0; j < halfSize; j++) {
				result[i][j] = c11[i][j];
				result[i][j+halfSize] = c12[i][j];
				result[i+halfSize][j] = c21[i][j];
				result[i+halfSize][j+halfSize] = c22[i][j];
			}
		}
			
        return result;
	}
	
	//  This function uses the ordinary method of multiplying matrices, with an Theta(n^3) running time cost
	private static int[][] MutliplyMatricesOrdinary(int[][] matrixOne, int[][] matrixTwo) {
		
		// Use this to compare methods
		int numberOfMultiplications = 0;
		
		 // Get order
		int order = matrixOne.length;
		
		// Multiply matrices into outputMatrix
		int[][] outputMatrix = new int [order][order];
		for (int i = 0; i < order; i++) {
			for (int j = 0; j < order; j++) {
				outputMatrix[i][j] = 0;
				for(int k = 0; k < order; k++) {
					outputMatrix[i][j] = outputMatrix[i][j] + matrixOne[i][k] * matrixTwo[k][j];
					numberOfMultiplications++;
				}
			}
		}
		
		System.out.println("MutliplyMatricesOrdinary number of multiplications = " + numberOfMultiplications);
		return outputMatrix;
	}

}
