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
		String tokenString = "";
		String tokenChar = "";

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
			// ASCII 126 is tilde character (~) for comments
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


			// check for character literal
			if (ch == '\'') {
				// reading next char using nested if technique
				int charBegin = columnNo;
				ascii = readChar.read();
				ch = (char) ascii;
				columnNo++;
				if (ch == '\\') {
					ascii = readChar.read();
					ch = (char) ascii;
					columnNo++;
					// double backslashes
					if (ch == '\\') {
						ascii = readChar.read();
						ch = (char) ascii;
						columnNo++;
						if (ch != '\'') {
							tokenChar = toString("LEXICAL ERROR", lineNo, columnNo);
						}
						else 
							tokenChar = toString("CHAR", lineNo, charBegin);
					}
					// single quote must be preceded by a backslash char
					else if (ch == '\'') {
						ascii = readChar.read();
						ch = (char) ascii;
						columnNo++;
						if (ch != '\'') {
							tokenChar = toString("LEXICAL ERROR", lineNo, columnNo);
						}
						else 
							tokenChar = toString("CHAR", lineNo, charBegin);
					}
				}
				else if (ch != '\'' && ch != '\n') {
					ascii = readChar.read();
					ch = (char) ascii;
					columnNo++;
					if (ch != '\'' || ch == '\n') {
						tokenChar = toString("LEXICAL ERROR", lineNo, columnNo);
					}
					else 
						tokenChar = toString("CHAR", lineNo, charBegin);
				}
				else {
					tokenChar = toString("LEXICAL ERROR", lineNo, columnNo);
				}
			}

			// initialising String literal with the given expression
			else if (ch == '"') {
				int strBegin = columnNo;
				tokenString += ch;
				while (ascii != 10 && ascii != -1) {
					ascii = readChar.read();
					ch = (char) ascii;
					columnNo++;
					tokenString += ch;

					// end of String
					if (ch == '"' && (tokenString.charAt(tokenString.length() - 2) != '\\')) {
						//tokenString = toString(tokenString, lineNo, columnNo - tokenString.length() + 1);
						// check if it is a valid String or not
						if (isString(tokenString)) {
							// is a valid string 
							tokenString = toString("STRING", lineNo, columnNo - tokenString.length() + 1);
							break;
						}
						else {
							tokenString = toString("LEXICAL ERROR", lineNo, strBegin);
							break;
						}
					}	
					// end of line or stream and could find the last double quote
					else if (ascii == 10 || ascii == -1) {
						tokenString = toString("LEXICAL ERROR", lineNo, strBegin);
						lineNo++;
						columnNo = 0;
					}
				}
			}

			// temporary initialising identifier's string
			else if ((isLowerCaseCharacter(ch) || isDecDigit(ch) || ch == '!' || ch == '*' || ch == '/' || ch == ':' ||
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
				if (!tempIdToken.isEmpty()) {
					tempIdToken = toString("IDENTIFIER", lineNo, columnNo - tempIdToken.length());
				}
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

	public static boolean isString(String s) {
		for (int i = 1; i < s.length() - 1; i++) {
			char c = s.charAt(i);
			// if the character is a backslash, the next character must be either a quotation mark or backslash
			if (c == '\\') {
				i++;
				if (s.charAt(i) == '"') {
					if (i == s.length() - 1)
						return false;
				}
				else if (s.charAt(i) == '\\')
					continue;
				else
					return false;
			}
		}
		return true;
	}

	public static boolean isBracket(char c) {
		return c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}';
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
		return Character.isLowerCase(c);
	}

	public static boolean isKeyword(String s) {
		return s.equals("define") || s.equals("let") || s.equals("cond") || s.equals("if") || s.equals("begin") ||
				s.equals("true") || s.equals("false");
	}

	public static boolean isDecDigit(char c) {
		return Character.isDigit(c);
	}

	public static String toString(String token, int lineNo, int columnNo) {
		System.out.println(token + " " + lineNo + ":" + columnNo);
		return "";
	}
}