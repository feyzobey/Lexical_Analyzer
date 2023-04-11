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
	static int lineNo = 1;
	static int columnNo = 1;
	static String token = "";

	public static void main(String[] args) throws IOException {

		System.out.println("Enter the name of the input file: ");
		Scanner scanFileName = new Scanner(System.in);
		String input = scanFileName.nextLine();
		scanFileName.close();

		File inputFile = new File(input);
		FileReader fileReader = new FileReader(inputFile);
		// read char by char. Reads characters from another Reader
		BufferedReader readChar = new BufferedReader(fileReader);
		int ascii = 0;
		ascii = readChar.read();
		// -1 means end of character stream
		while (ascii != -1) {
			// ascii 10 is new line character
			if (ascii == 10) {
				lineNo++;
				columnNo = 1;
				continue;
			}
			// cast to char
			char ch = (char) ascii;
			// if char is a bracket
			if (isBracket(ch)) {
				// if char is a left bracket
				if (ch == '(') {
					token = "LEFTPAR";
				} else if (ch == ')') {
					token = "RIGHTPAR";
				} else if (ch == '[') {
					token = "LEFTSQUAREB";
				} else if (ch == ']') {
					token = "RIGHTSQUAREB";
				} else if (ch == '{') {
					token = "LEFTCURLYB";
				} else if (ch == '}') {
					token = "RIGHTCURLYB";
				}
			} else {
				token = "LEXICAL ERROR";
			}
			System.out.println(ch);
			System.out.println(toString(token, lineNo, columnNo));
			ascii = readChar.read();
		}
		readChar.close();
	}

	public static boolean isNumber(char c) {
		if (Character.isDigit(c)) {
			return true;
		}
		return false;
	}

	public static boolean isBracket(char c) {
		Character ch = (Character) c;
		if (ch.equals('(') || ch.equals(')') || ch.equals('[') || ch.equals(']') || ch.equals('{') || ch.equals('}')) {
			return true;
		}
		return false;
	}

	// public static boolean isBoolean(String s) {

	// }

	// public static boolean isCharacter(char c) {

	// }

	// public static boolean isString(String s) {

	// }

	// public static boolean isKeyword(String s) {

	// }

	// public static boolean isIdentifier(String s) {

	// }

	public static String toString(String token, int lineNo, int columnNo) {
		return token + " " + lineNo + ":" + columnNo;
	}
}
