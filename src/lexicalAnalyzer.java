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
		String tempIdToken = "";

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
			// temporary initialising identifier's string
			if ((isLowerCaseCharacter(ch) || isDecDigit(ch) || ch == '!' || ch == '*' || ch == '/' || ch == ':' ||
					ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '.' || ch == '+' || ch == '-')) {
				token += ch;
				if (!(tempIdToken.isEmpty() && (isDecDigit(ch) || ch == '.' || ch == '+' || ch == '-'))) {
					tempIdToken += ch;
				}
			} else if (isKeyword(token)) {
				tempIdToken = "";
				if (token.equals("define")) {
					token = toString("DEFINE", lineNo, columnNo - token.length());
				} else if (token.equals("let")) {
					token = toString("LET", lineNo, columnNo - token.length());
				} else if (token.equals("cond")) {
					token = toString("COND", lineNo, columnNo - token.length());
				} else if (token.equals("if")) {
					token = toString("IF", lineNo, columnNo - token.length());
				} else if (token.equals("begin")) {
					token = toString("BEGIN", lineNo, columnNo - token.length());
				} else if (token.equals("true") || token.equals("false")) {
					token = toString("BOOLEAN", lineNo, columnNo - token.length());
				}
			} else if (isIdentifier(tempIdToken)) {
				tempIdToken = toString("IDENTIFIER", lineNo, columnNo - tempIdToken.length());
				if (ch == '(') {
					token = toString("LEFTPAR", lineNo, columnNo);
				} else if (ch == ')') {
					token = toString("RIGHTPAR", lineNo, columnNo);
				} else if (ch == '[') {
					token = toString("LEFTSQUAREB", lineNo, columnNo);
				} else if (ch == ']') {
					token = toString("RIGHTSQUAREB", lineNo, columnNo);
				} else if (ch == '{') {
					token = toString("LEFTCURLYB", lineNo, columnNo);
				} else if (ch == '}') {
					token = toString("RIGHTCURLYB", lineNo, columnNo);
				}
			} else if (isBracket(ch)) {
				token = "";
				tempIdToken = "";
				// if char is a left bracket
				if (ch == '(') {
					token = toString("LEFTPAR", lineNo, columnNo);
				} else if (ch == ')') {
					token = toString("RIGHTPAR", lineNo, columnNo);
				} else if (ch == '[') {
					token = toString("LEFTSQUAREB", lineNo, columnNo);
				} else if (ch == ']') {
					token = toString("RIGHTSQUAREB", lineNo, columnNo);
				} else if (ch == '{') {
					token = toString("LEFTCURLYB", lineNo, columnNo);
				} else if (ch == '}') {
					token = toString("RIGHTCURLYB", lineNo, columnNo);
				}
				// ascii 13 is carriage return, ascii 32 is space
			} else if (ascii != 32 && ascii != 13) {
				token = toString("LEXICAL ERROR", lineNo, columnNo);
			}
			// System.out.println(ch);
			columnNo++;
		}
		readChar.close();
	}

	public static boolean isBracket(char c) {
		return c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}';
	}

	public static boolean isIdentifier(String s) {
		boolean validChar = true;
		if (isKeyword(s)) {
			return false;
		}
		if (s.isEmpty()) {
			return false;
		}
		// check first rightmost BNF choice
		if (isLowerCaseCharacter(s.charAt(0))
				|| s.charAt(0) == '!'
				|| s.charAt(0) == '*'
				|| s.charAt(0) == '/'
				|| s.charAt(0) == ':'
				|| s.charAt(0) == '<'
				|| s.charAt(0) == '='
				|| s.charAt(0) == '>'
				|| s.charAt(0) == '?') {
			// second rightmost BNF choice
			for (int i = 1; i < s.length(); i++) {
				if (isLowerCaseCharacter(s.charAt(i)) || isDecDigit(s.charAt(i)) || s.charAt(i) == '.' ||
						s.charAt(i) == '+' || s.charAt(i) == '-') {
					continue;
				}
				validChar = false;
			}
			return validChar;
		}
		return false;
	}

	public static boolean isLowerCaseCharacter(char c) {
		return Character.isLowerCase(c);
	}

	public static boolean isString(String s) {
		return s.startsWith("\"") && s.endsWith("\"");
	}

	public static boolean isKeyword(String s) {
		return s.equals("define") || s.equals("let")
				|| s.equals("cond") || s.equals("if") || s.equals("begin")
				|| s.equals("true") || s.equals("false");
	}

	public static boolean isDecDigit(char c) {
		return Character.isDigit(c);
	}

	public static String toString(String token, int lineNo, int columnNo) {
		System.out.println(token + " " + lineNo + ":" + columnNo);
		return "";
	}
}