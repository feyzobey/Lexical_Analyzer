/*
 * FEYZULLAH ASILLIOGLU  150121021
 * MOHAMAD NAEL AYOUBI   150120997
 * KADIR BAT             150120012
 * 
 *    CSE2260 Principles of Programming Languages - Project 1
 *    Lexical Analyser (Scanner)
 *    Scanning (DFA) as a set of nested case is our technique.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class lexicalAnalyzer {

	// global variables to trace chars and tokens
	static int lineNo = 0;
	static int columnNo = 0;
	static String token = "";
	
	public static void main(String[] args) throws IOException {

		System.out.println("Enter the name of the input file: ");
		Scanner scanFileName = new Scanner(System.in);
		String input = scanFileName.nextLine();
		System.out.println(input);
		scanFileName.close();
		
		File inputFile = new File(input);
		FileReader fileReader = new FileReader(inputFile);
		// read char by char. Reads characters from another Reader
		BufferedReader readChar = new BufferedReader(fileReader);
		
		int ascii = 0;
		ascii = readChar.read();
		// -1 means end of character stream
		while (ascii != -1) {
			char ch = (char) ascii;
			System.out.print(ch);
			ascii = readChar.read();
		}
	}
	
//	public boolean isBracket(char c) {
//
//	}
//
//	public boolean isNumber(char c) {
//
//	}
//
//	public boolean isBoolean(String s) {
//
//	}
//
//	public boolean isCharacter(char c) {
//
//	}
//
//	public boolean isString(String s) {
//
//	}
//
//	public boolean isKeyword(String s) {
//
//	}
//
//	public boolean isIdentifier(String s) {
//
//	}

}
