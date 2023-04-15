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
		String tokenNumber = "";
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

			// temporary initialising keyword's, number's and identifier's strings
			else if ((isLowerCaseCharacter(ch) || isDecDigit(ch) || isBinDigit(ch) || isHexDigit(ch) || ch == '!' || ch == '*' || ch == '/' || ch == ':' ||
					ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '.' || ch == '+' || ch == '-')) {
				token += ch;
				tokenNumber += ch;
				// first char must not be a digit for ID token
				if (!(tempIdToken.isEmpty() && isDecDigit(ch))) {
					tempIdToken += ch;
				}
				
			} else if (isKeyword(token)) {
				tempIdToken = "";
				tokenNumber = "";
				tokenString = "";
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
			} else if(isNumber(tokenNumber)) {
				tempIdToken = "";
				tokenString = "";
				if (!tokenNumber.isEmpty()) {
					tokenNumber = toString("NUMBER", lineNo, columnNo - tokenNumber.length());
				}
				// since this literal expression and brackets expression are in different if statements we might not print bracket after a number
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
			} else if (isIdentifier(tempIdToken)) {
				tokenNumber = "";
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
			} else if(ch == ' ') {
				if(!tokenNumber.isEmpty()) {
					tokenNumber = toString("LEXICAL ERROR", lineNo, columnNo - tokenNumber.length());
				}
				else if(!tempIdToken.isEmpty()) {
					tempIdToken = toString("LEXICAL ERROR", lineNo, columnNo - tempIdToken.length());
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

	public static boolean isNumber(String s) {
		//boolean validNumber = true;
		if (s.isEmpty())
			return false;
		if (s.length() == 1 && isDecDigit(s.charAt(0)))
			return true;
		// a check for hexadecimal and binary integer
		if (s.charAt(0) == '0' && s.length() > 2) {
			// check for hexadecimal literal
			if (s.charAt(1) == 'x') {
				if (isHexDigit(s.charAt(2))) {
					for (int i = 3; i < s.length(); i++) {
						if (!isHexDigit(s.charAt(i)))
							return false;
					}
				}
				else
					return false;
			}
			// check for binary literal
			else if (s.charAt(1) == 'b') {
				if (isBinDigit(s.charAt(2))) {
					for (int i = 3; i < s.length(); i++) {
						if (!isBinDigit(s.charAt(i)))
							return false;
					}
				}
				else
					return false;
			}
		}
		// a check for decimal signed and floating point numbers with no points
		else if ((s.charAt(0) == '+' || s.charAt(0) == '-' || isDecDigit(s.charAt(0))) && !s.contains(".")) {
			int exp = 0;
			for (int i = 1; i < s.length(); i++) {
				if (!isDecDigit(s.charAt(i)) && (i != s.length() - 1)) {
					exp = i;
					break;
				}
				// it is a decimal integer
				else if ((i == s.length() - 1) && isDecDigit(s.charAt(i)))
					return true;
			}
			// floating number
			if (s.charAt(exp) == 'e' || s.charAt(exp) == 'E') {
				exp++;
				if ((s.charAt(exp) == '+' || s.charAt(exp) == '-' || isDecDigit(s.charAt(exp)))) {
					for (int i = exp + 1; i < s.length(); i++) {
						if (!isDecDigit(s.charAt(i)))
							return false;
					}
					return true;
				}
			}
			else 
				return false;
		}
		// check for floating point number with a point
		else if ((s.charAt(0) == '+' || s.charAt(0) == '-' || isDecDigit(s.charAt(0)) || s.charAt(0) == '.') && s.contains(".")) {
			int indexOfPoint = 0;
			int exp = 0;
			// check prefix
			if (s.charAt(0) == '+' || s.charAt(0) == '-' || isDecDigit(s.charAt(0)) || s.charAt(0) == '.') {
				if (s.charAt(0) == '.')
					indexOfPoint = 0;
				else { 
					for (int i = 1; i < s.length(); i++) {
						if (!isDecDigit(s.charAt(i))) {
							indexOfPoint = i;
							break;
						}
					}
				}
				if(s.charAt(indexOfPoint) == '.') {
					if (!isDecDigit(s.charAt(++indexOfPoint))) {
						return false;
					}
					for (int i = indexOfPoint; i < s.length(); i++) {
						if (!isDecDigit(s.charAt(i)) && (i != s.length() - 1)) {
							exp = i;
							break;
						}
						else if ((i == s.length() - 1) && isDecDigit(s.charAt(i)))
							return true;
					}
					// floating number
					if (s.charAt(exp) == 'e' || s.charAt(exp) == 'E') {
						exp++;
						if ((s.charAt(exp) == '+' || s.charAt(exp) == '-' || isDecDigit(s.charAt(exp)))) {
							for (int i = exp + 1; i < s.length(); i++) {
								if (!isDecDigit(s.charAt(i)))
									return false;
							}
							return true;
						}
					}
					else 
						return false;
				}
				else
					return false;
			}
		}
		return false;
	}
	
	public static boolean isIdentifier(String s) {
		boolean validChar = true;
		if (!isKeyword(s) && !s.isEmpty()) {
			// check first rightmost BNF choice
			if ((s.charAt(0) == '+' || s.charAt(0) == '-' || s.charAt(0) == '.') && s.length() == 1) 
				return true;
			else if (isLowerCaseCharacter(s.charAt(0)) || s.charAt(0) == '!' || s.charAt(0) == '*' || s.charAt(0) == '/'
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

	public static boolean isBinDigit(char c) {
		return c == '0' || c == '1';
	}
	
	public static boolean isDecDigit(char c) {
		return Character.isDigit(c);
	}

	public static boolean isHexDigit(char c) {
		return Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' ||
				c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F';
	}
	
	public static String toString(String token, int lineNo, int columnNo) {
		if (token.equals("LEXICAL ERROR")) {
			System.out.println(token + " " + lineNo + ":" + columnNo);
			System.exit(1);
		}
		System.out.println(token + " " + lineNo + ":" + columnNo);
		return "";
	}
}