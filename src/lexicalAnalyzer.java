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

			// if char is a keyword like "define", "let", "cond", "if", "begin"
			if (isComment(ch)) {
				while (ascii != 10) {
					ascii = readChar.read();
				}
				lineNo++;
				columnNo = 1;
				continue;
			}
			if (isLowerCaseCharacter(ch)) {
				token += ch;
				// temporary initialising identifier's string
			} else if(isLowerCaseCharacter(ch) || isDecDigit(ch) || ch == '!' || ch == '*' || ch == '/' || ch == ':' ||
					ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '.' || ch == '+' || ch == '-') {
				tempIdToken += ch;
			} else if (isBracket(ch)) {
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
				tempIdToken = toString(tempIdToken, lineNo, columnNo - tempIdToken.length());
			} else {
				token = toString("LEXICAL ERROR", lineNo, columnNo);
			}
			System.out.println(ch);
			columnNo++;
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

	public static boolean isIdentifier(String s) {
		if (isKeyword(s) == false) {
			if (isLowerCaseCharacter(s.charAt(0)) || s.charAt(0) == '!' || s.charAt(0) == '*' || s.charAt(0) == '/' || s.charAt(0) == ':' ||
					s.charAt(0) == '<' || s.charAt(0) == '=' || s.charAt(0) == '>' || s.charAt(0) == '?') {
				for (int i = 1; i < s.length(); i++) {
					if (isLowerCaseCharacter(s.charAt(i)) == false || isDecDigit(s.charAt(i)) == false || s.charAt(i) != '.' || 
							s.charAt(i) != '+' || s.charAt(i) != '-') {
						return false;
					}
				}
				return true;
			}
			else
				return false;
		}
		else
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
		if (s.equals("define") || s.equals("let") || s.equals("cond") || s.equals("if") || s.equals("begin") || s.equals("true") || s.equals("false")) {
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
