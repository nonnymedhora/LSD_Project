/**
 * 
 */
package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Navroz
 * 
 * Generates a Random (4 * 4) LSD Matrix of Playing Cards
 * from denominations 	- Jack [J], Queen [Q], King [K], Ace [A]
 * and of suits 		- ♠, ♥, ♦, ♣		(Spades, Hearts, Diamonds, Clubs)
 * each denomination	==> 		occurs once per column, per row
 * each suit			==> 		occurs once per column, per row
 * 
 * ----------------------------------------------
 * 
 * Sample End-Output Runs
 * 
 * ____________________
 * | Q♠ | A♥ | J♣ | K♦ | 
 * _____________________
 * | J♦ | K♣ | Q♥ | A♠ | 
 * _____________________
 * | A♣ | Q♦ | K♠ | J♥ |
 * ____________________
 * | K♥ | J♠ | A♦ | Q♣ |
 * _____________________
 * 
 * ==============================
 * ____________________
 * | Q♠ | J♦ | K♥ | A♣ |
 * _____________________
 * | K♣ | A♥ | Q♦ | J♠ |
 * _____________________
 * | A♦ | K♠ | J♣ | Q♥ |
 * _____________________
 * | J♥ | Q♣ | A♠ | K♦ |
 * _____________________
 * 
 * ============================== 
 * ____________________
 * | A♣ | K♠ | J♦ | Q♥ |
 * _____________________
 * | K♥ | A♦ | Q♠ | J♣ |
 * _____________________
 * | Q♦ | J♥ | K♣ | A♠ |
 * _____________________
 * | J♠ | Q♣ | A♥ | K♦ |
 * _____________________
 * 
 *  ==============================
 *  ____________________
 * | Q♥ | J♠ | K♦ | A♣ |
 * _____________________
 * | A♦ | K♣ | J♥ | Q♠ |
 * _____________________
 * | K♠ | A♥ | Q♣ | J♦ |
 * _____________________
 * | J♣ | Q♦ | A♠ | K♥ |
 * _____________________
 * 
 * ==============================
 * _____________________
 * | A♣ | K♦ | Q♥ | J♠ |
 * _____________________
 * | Q♦ | J♣ | A♠ | K♥ |
 * _____________________
 * | J♥ | Q♠ | K♣ | A♦ |
 * _____________________
 * | K♠ | A♥ | J♦ | Q♣ |
 * _____________________
 *
 */
public class CardsLSD {
	
	private final static Map<Integer,Character> numMap = new HashMap<Integer,Character>();
	static {
		numMap.put( 1, 'J');	//jack
		numMap.put( 2, 'Q');	//queen
		numMap.put( 3, 'K');	//king
		numMap.put( 4, 'A');	//ace
	}
	
	private final static Map<Integer,Character> suitMap = new HashMap<Integer,Character>();
	static {
		suitMap.put( 1, '\u2660');	//spades
		suitMap.put( 2, '\u2665');	//hearts
		suitMap.put( 3, '\u2666');	//diamonds
		suitMap.put( 4, '\u2663');	//clubs		
	}
	
	private static boolean checkMatrixEquality(int[][] m1, int[][] m2) {
		boolean isEqual = true;
		int m1Length = m1.length;
		int m2Length = m2.length;
		if( m1Length != m2Length) {
			return false;
		}
		
		int size = m1Length;
		for(int i = 0; i < size; i++) {
			int m1ArrLength = m1[i].length;
			int m2ArrLength = m2[i].length;
			if( (m1ArrLength != m2ArrLength) || (m1ArrLength != size) ) {
				return false;
			}
			
			for(int j = 0; j < size; j++) {
				if( m1[i][j] != m2[i][j] ) {
					return false;
				}
			}
		}
		
		return isEqual;
	}
	
	private static void printArray(final String[] anArray) {
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < anArray.length; i++) {
			if( i == anArray.length - 1) {
				x.append("| "+anArray[i] + " | ");
			} else {
				x.append("| "+anArray[i] + " ");
			}
		}
		System.out.println(x.toString());
	}
	
	private static void printMatrix(final String[][] matrix) {
		System.out.println("\nLSD [" + matrix[0].length + "] is\n");
		System.out.println("_____________________");
		for (int row = 0; row < matrix[0].length; row++) {
			printArray(matrix[row]);
			System.out.println("_____________________");
		}
	}

	public static void main(String[] args) {
		final int size = 4;
		LSD lsd = new LSD(size);
		
		int[][] numMatrix = lsd.generateLSD();
		int[][] suitMatrix = lsd.generateLSD();
		boolean ok = false;
		
		String[][] theMatrix = new String[size][size];
		List<String> contentsList = new ArrayList<String>();
		
		while ( !ok ) {
			if ( !checkMatrixEquality(numMatrix, suitMatrix) ) {
				ok = true;
				
				for( int i = 0; i < size; i++) {
					if( !ok ) {
						numMatrix = lsd.generateLSD();
						suitMatrix = lsd.generateLSD();
						contentsList = new ArrayList<String>();
						break;
					}
					for( int j = 0; j < size; j++ ) {
						int m1 = numMatrix[i][j];		//	for num
						int m2 = suitMatrix[i][j];		// for suit
						
						char num = numMap.get(m1);
						char suit = suitMap.get(m2);
						String content = String.valueOf(num) + String.valueOf(suit); 
						
						if( !contentsList.contains( content ) ) {
							contentsList.add( content );
							theMatrix[i][j] = content;
						} else {
							ok = false;
							break;
						}
					}
				}
				
			} else {
				ok = false;
				suitMatrix = lsd.generateLSD();
			} 
		}
		
		printMatrix( theMatrix );
	}

}
