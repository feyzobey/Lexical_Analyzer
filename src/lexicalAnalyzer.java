/*
 * FEYZULLAH ASILLIOGLU  150121021
 * MOHAMAD NAEL AYOUBI   150120997
 * KADIR BAT             150120012
 * 
 * CSE2260 Principles of Programming Languages - Project 1
 * Lexical Analyser (Scanner)
 * Scanning (DFA) as a set of nested case is our technique.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class lexicalAnalyzer {

	public static void main(String[] args) throws IOException {

		System.out.println("Enter the name of the input file: ");
		Scanner scanFileName = new Scanner(System.in);
		String input = scanFileName.nextLine();
		scanFileName.close();

		File inputFile = new File(input);
		FileReader fileReader = new FileReader(inputFile);
		// read char by char. Reads characters from another Reader
		BufferedReader readChar = new BufferedReader(fileReader);

		// to trace lexemes
		int lineNo = 1;
		int columnNo = 1;
		String token = "";
		int ascii = 0;
		// -1 means end of character stream
		while (true) {
			ascii = readChar.read();
			// ASCII 10 is new line character
			if (ascii == -1) {
				break;
			}
			if (ascii == 10) {
				lineNo++;
				columnNo = 1;
				continue;
			}
			// ascii 126 is tilde character (~)
			if (ascii == 126) {
				while (ascii != 10) {
					ascii = readChar.read();
				}
				lineNo++;
				columnNo = 1;
				continue;
			}
			// cast to char
			char ch = (char) ascii;
			token += ch;
			// temporary initialising identifier's string
			if (bracket(ch, lineNo, columnNo)) {
				token = "";
			} else if (identifier(token, lineNo, columnNo)) {
				token = "";
			} else if (keyword(token, lineNo, columnNo)) {
				token = "";
			} else if (ascii != 32 && ascii != 13) {
				// ascii 13 is carriage return, ascii 32 is space
				token = toString("LEXICAL ERROR", lineNo, columnNo);
			}
			// System.out.println(ch);
			columnNo++;
		}
		readChar.close();
	}
	
	public static boolean bracket(char c, int lineNo, int columnNo) {
		if (c == '(') {
			toString("LEFTPAR", lineNo, columnNo);
			return true;
		}
		if (c == ')') {
			toString("RIGHTPAR", lineNo, columnNo);
			return true;
		}
		if (c == '[') {
			toString("LEFTSQUAREB", lineNo, columnNo);
			return true;
		}
		if (c == ']') {
			toString("RIGHTSQUAREB", lineNo, columnNo);
			return true;
		}
		if (c == '{') {
			toString("LEFTCURLYB", lineNo, columnNo);
			return true;
		}
		if (c == '}') {
			toString("RIGHTCURLYB", lineNo, columnNo);
			return true;
		}
		return false;
	}

	public static boolean keyword(String s, int lineNo, int columnNo) {
		if (s.equals("define")) {
			toString("DEFINE", lineNo, columnNo);
			return true;
		}
		if (s.equals("let")) {
			toString("LET", lineNo, columnNo);
			return true;
		}
		if (s.equals("cond")) {
			toString("COND", lineNo, columnNo);
			return true;
		}
		if (s.equals("if")) {
			toString("IF", lineNo, columnNo);
			return true;
		}
		if (s.equals("begin")) {
			toString("BEGIN", lineNo, columnNo);
			return true;
		}
		if (s.equals("true") || s.equals("false")) {
			toString("BOOLEAN", lineNo, columnNo);
			return true;
		}
		return false;
	}
	
	public static boolean identifier(String s, int lineNo, int columnNo) {
		if (isIdentifier(s)) {
			toString("IDENTIFIER", lineNo, columnNo);
			return true;
		}
		return false;
	}

	public static boolean isBracket(char c) {
		if (c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}') {
			return true;
		}
		return false;
	}

	public static boolean isKeyword(String s) {
		if (s.equals("define") || s.equals("let") || s.equals("cond") || s.equals("if") || s.equals("begin")
				|| s.equals("true") || s.equals("false")) {
			return true;
		}
		return false;
	}

	public static boolean isSpecialChar(char c) {
		if (c == '!' || c == '*' || c == '/' || c == ':' ||
			c == '<' || c == '=' || c == '>' || c == '?' ||
			c == '.' || c == '+' || c == '-') {
			return true;
		}
		return false;
	}

	public static boolean isDecDigit(char c) {
		if (Character.isDigit(c)) {
			return true;
		}
		return false;
	}

	public static boolean isIdentifier(String s) {
		boolean validIdentifier = true;
		if (s.isEmpty()) {
			return false;
		}
		if (isKeyword(s)) {
			return false;
		}
		// check first rightmost BNF choice
		if (isLowerCaseCharacter(s.charAt(0)) || s.charAt(0) == '!' || s.charAt(0) == '*' || s.charAt(0) == '/'
				|| s.charAt(0) == ':' ||
				s.charAt(0) == '<' || s.charAt(0) == '=' || s.charAt(0) == '>' || s.charAt(0) == '?') {
			// second rightmost BNF choice
			for (int i = 1; i < s.length(); i++) {
				if (isLowerCaseCharacter(s.charAt(i)) || isDecDigit(s.charAt(i)) || s.charAt(i) == '.' ||
						s.charAt(i) == '+' || s.charAt(i) == '-') {
					continue;
				}
				validIdentifier = false;
			}
			return validIdentifier;
		}
		return false;
	}

	public static boolean isLowerCaseCharacter(char c) {
		if (Character.isLowerCase(c)) {
			return true;
		}
		return false;
	}

	public static boolean isString(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return true;
		}
		return false;
	}

	public static String toString(String token, int lineNo, int columnNo) {
		System.out.println(token + " " + lineNo + ":" + columnNo);
		return "";
	}
}