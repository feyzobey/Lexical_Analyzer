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
		while (ascii != -1) {
			ascii = readChar.read();
			// ASCII 10 is new line character
			if (ascii == 10) {
				lineNo++;
				columnNo = 1;
				continue;
			}
			// cast to char
			char ch = (char) ascii;
			// if char is a whitespace
			if (ascii == 32) {
				columnNo++;
				continue;
			}
			// if char is a comment
			if (isComment(ch)) {
				while (ascii != 10) {
					ascii = readChar.read();
				}
				lineNo++;
				columnNo = 1;
				continue;
			}
			// temporary initialising identifier's string
			if (isLowerCaseCharacter(ch) || isDecDigit(ch) || ch == '!' || ch == '*' || ch == '/' || ch == ':' ||
					ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '.' || ch == '+' || ch == '-') {
				token += ch;
				tempIdToken += ch;
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
			} else {
				token = toString("LEXICAL ERROR", lineNo, columnNo);
			}
			// System.out.println(ch);
			columnNo++;
		}
		readChar.close();
	}

	public static boolean isBracket(char c) {
		Character ch = (Character) c;
		if (ch.equals('(') || ch.equals(')') || ch.equals('[') || ch.equals(']') || ch.equals('{') || ch.equals('}')) {
			return true;
		}
		return false;
	}

	public static boolean isIdentifier(String s) {
		boolean validChar = true;
		if (!isKeyword(s) && !s.isEmpty()) {
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
					validChar = false;
				}
				return validChar;
			} 
			return false;
		} 
		return false;
	}

	public static boolean isLowerCaseCharacter(char c) {
		if (Character.isLowerCase(c)) {
			return true;
		}
		return false;
	}

	public static boolean isComment(char c) {
		if (c == '~') {
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

	public static boolean isKeyword(String s) {
		if (s.equals("define") || s.equals("let") || s.equals("cond") || s.equals("if") || s.equals("begin")
				|| s.equals("true") || s.equals("false")) {
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

	public static String toString(String token, int lineNo, int columnNo) {
		System.out.println(token + " " + lineNo + ":" + columnNo);
		return "";
	}
}