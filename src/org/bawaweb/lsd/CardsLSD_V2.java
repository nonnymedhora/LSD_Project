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
 * Generates a Random (8 * 8) LSD Matrix of Playing Cards
 * from denominations 	- Jack [J], Queen [Q], King [K], Ace [A]
 * and of suits 		- ♠, ♥, ♦, ♣	dark	(Spades, Hearts, Diamonds, Clubs)
 * 						- ♤, ≡, ♢, ♧ 	light	(Spades, Hearts??, Diamonds, Clubs
 *
 * each suit			==> 		occurs once per column, per row
 * 
 * ----------------------------------------------
 * 
 * Sample End-Output Runs
 * 	______________________________________
	| A♣ | K♦ | J♢ | K♠ | Q♥ | A♧ | J♤ | Q≡ | 
	______________________________________
	| Q♥ | J♣ | A♧ | K♦ | Q♤ | J≡ | A♠ | K♢ | 
	______________________________________
	| J♦ | Q♧ | J≡ | Q♥ | A♢ | K♤ | K♣ | A♠ | 
	______________________________________
	| A♠ | J≡ | Q♣ | J♢ | A♧ | K♦ | K♥ | Q♤ | 
	______________________________________
	| Q♢ | K♠ | A♤ | J♧ | K♦ | A♣ | Q≡ | J♥ | 
	______________________________________
	| J♤ | A♢ | Q♥ | A♣ | K≡ | Q♠ | J♧ | K♦ | 
	______________________________________
	| K♧ | A♤ | K♦ | Q≡ | J♠ | Q♥ | A♢ | J♣ | 
	______________________________________
	| K≡ | Q♥ | K♠ | A♤ | J♣ | J♢ | Q♦ | A♧ | 
	______________________________________
 * ===============================================================
 	______________________________________
	| K♤ | J≡ | A♢ | Q♠ | J♣ | K♥ | Q♦ | A♧ | 
	______________________________________
	| K♢ | K♥ | J≡ | A♧ | Q♤ | J♦ | Q♣ | A♠ | 
	______________________________________
	| A♣ | K♠ | J♥ | K♤ | J♦ | Q♧ | A≡ | Q♢ | 
	______________________________________
	| A♠ | J♦ | K♧ | Q♣ | K♢ | Q≡ | A♤ | J♥ | 
	______________________________________
	| J♦ | Q♧ | K♠ | A♢ | Q≡ | A♤ | K♥ | J♣ | 
	______________________________________
	| Q♥ | A♣ | Q♤ | J♦ | A♧ | K♢ | J♠ | K≡ | 
	______________________________________
	| Q≡ | A♢ | Q♦ | J♥ | K♠ | A♣ | J♧ | K♤ | 
	______________________________________
	| J♧ | Q♤ | A♣ | K≡ | A♥ | J♠ | K♢ | Q♦ | 
	______________________________________
	===========================================================
	______________________________________
	| Q≡ | A♦ | J♢ | K♣ | A♥ | K♠ | Q♤ | J♧ | 
	______________________________________
	| A♧ | A♤ | J♠ | Q♢ | Q≡ | J♥ | K♣ | K♦ | 
	______________________________________
	| Q♥ | K♢ | A♣ | J♦ | K♧ | J≡ | Q♠ | A♤ | 
	______________________________________
	| K♠ | J♣ | Q≡ | J♤ | A♦ | A♧ | K♥ | Q♢ | 
	______________________________________
	| J♦ | J♠ | A♤ | K♥ | K♣ | Q♢ | A♧ | Q≡ | 
	______________________________________
	| J♢ | K♥ | K♦ | Q♧ | Q♤ | A♣ | A≡ | J♠ | 
	______________________________________
	| A♤ | Q♧ | Q♥ | A≡ | J♠ | K♦ | J♢ | K♣ | 
	______________________________________
	| K♣ | Q≡ | K♧ | A♠ | J♢ | Q♤ | J♦ | A♥ | 
	______________________________________

 * 
 *
 */
public class CardsLSD_V2 {
	
	private static final int size = 8;
	private final static Map<Integer,Character> numMap = new HashMap<Integer,Character>();
	static {
		numMap.put( 1, 'J');	//jack
		numMap.put( 2, 'Q');	//queen
		numMap.put( 3, 'K');	//king
		numMap.put( 4, 'A');	//ace
		
		numMap.put( 5,	'J' );	//'j';	CAPS appear better
		numMap.put( 6,	'Q' );	//'q';
		numMap.put( 7, 	'K' );	//'k';
		numMap.put( 8,	'A' );	//'a';
	}
	
	private final static Map<Integer,Character> suitMap = new HashMap<Integer,Character>();
	static {
		suitMap.put( 1, '\u2660');	//spades	black
		suitMap.put( 2, '\u2665');	//hearts
		suitMap.put( 3, '\u2666');	//diamonds
		suitMap.put( 4, '\u2663');	//clubs		
		
		suitMap.put( 5,	'\u2664');	//spades	white
		suitMap.put( 6,	'\u2261');	//hearts			appearing as ≡
		suitMap.put( 7,	'\u2662');	//diamonds
		suitMap.put( 8,	'\u2667');	//clubs
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
		System.out.println("______________________________________");
		for (int row = 0; row < matrix[0].length; row++) {
			printArray(matrix[row]);
			System.out.println("______________________________________");
		}
	}

	
	public static void main(String[] args) {
		
		LSD lsd = new LSD(size);
		
		int[][] numMatrix 	= lsd.generateLSD();
		int[][] suitMatrix 	= lsd.generateLSD();
		String[][] theMatrix = new String[size][size];
		theMatrix = merge(numMatrix,suitMatrix);
		
		/*
		
		
		
		
		
		
		
		boolean ok 			= false;
		
		
		List<String> contentsList 	= new ArrayList<String>();
		
		while ( !ok ) {
			if ( !checkMatrixEquality(numMatrix, suitMatrix) ) {
				ok = true;
				
				for( int i = 0; i < size; i++) {
					if( !ok ) {
						numMatrix 		= lsd.generateLSD();
						suitMatrix 		= lsd.generateLSD();
						contentsList 	= new ArrayList<String>();
						break;
					}
					for( int j = 0; j < size; j++ ) {
						int m1 = numMatrix[i][j];		//	for num
						int m2 = suitMatrix[i][j];		// for suit
						
						char num 	= numMap.get(m1);
						char suit 	= suitMap.get(m2);
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
		}*/
		
		printMatrix( theMatrix );
	}

	private static String[][] merge(int[][] numMatrix, int[][] suitMatrix) {
		String[][] aMatrix = new String[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int m1 = numMatrix[i][j]; 	// for num
				int m2 = suitMatrix[i][j]; 	// for suit

				char num = numMap.get(m1);
				char suit = suitMap.get(m2);
				String content = String.valueOf(num) + String.valueOf(suit);
				aMatrix[i][j]=content;
			}
		}
		return aMatrix;
	}

}
