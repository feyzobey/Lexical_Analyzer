/*
 * FEYZULLAH ASILLIOGLU  150121021
 * MOHAMAD NAEL AYOUBI   150120997
 * KADIR BAT             150120012
 * 
 * CSE2260 Principles of Programming Languages - Project 1
 * Lexical Analyser (Scanner)
 * Scanning (DFA) as a set of nested case is our technique.
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class lexicalAnalyzer {
	public static void main(String[] args) throws IOException {

		System.out.println("Enter the name of the input file: ");
		Scanner scanFileName = new Scanner(System.in);
		String input = scanFileName.nextLine();
		scanFileName.close();

		int lineNo = 1;
		int columnNo = 1;
		String token = "";
		String temp = "";

		Scanner scanner = new Scanner(new File(input));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] splittedLine = line.split(" ");
			for (int i = 0; i < splittedLine.length; i++) {
				token = splittedLine[i];
				if (isBracket(token)) {
					token = bracket(token, lineNo, columnNo);
					columnNo++;
				}
				temp = keyword(token, lineNo, columnNo);
				if (!temp.equals(token)) {
					columnNo += token.length();
					token = temp;
				}
				if (isBracket(token)) {
					token = bracket(token, lineNo, columnNo);
					columnNo++;
				}
				temp = identifier(token, lineNo, columnNo);
				if (!temp.equals(token)) {
					columnNo += token.length();
					token = temp;
				}
				if (isBracket(token)) {
					token = bracket(token, lineNo, columnNo);
					columnNo++;
				}
				columnNo++;
			}
			// System.out.println(line);
			lineNo++;
			columnNo = 1;
		}
	}

	public static String keyword(String token, int lineNo, int columnNo) {
		for (int i = token.length(); i > 0; i--) {
			String temp = token.substring(0, i);
			if (isKeyword(temp)) {
				if (temp.equals("define")) {
					toString("DEFINE", lineNo, columnNo);
					return token.substring(i, token.length());
				}
				if (temp.equals("let")) {
					toString("LET", lineNo, columnNo);
					return token.substring(i, token.length());
				}
				if (temp.equals("cond")) {
					toString("COND", lineNo, columnNo);
					return token.substring(i, token.length());
				}
				if (temp.equals("if")) {
					toString("IF", lineNo, columnNo);
					return token.substring(i, token.length());
				}
				if (temp.equals("begin")) {
					toString("BEGIN", lineNo, columnNo);
					return token.substring(i, token.length());
				}
				if (temp.equals("true") || temp.equals("false")) {
					toString("BOOLEAN", lineNo, columnNo);
					return token.substring(i, token.length());
				}
			}	
		}
		return token;
	}

	public static String bracket(String token, int lineNo, int columnNo) {
		if (token.isEmpty()) {
			return token;
		}
		char ch = token.charAt(0);
		if (ch == '(') {
			toString("LEFTPAR", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		if (ch == ')') {
			toString("RIGHTPAR", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		if (ch == '[') {
			toString("LEFTSQUAREB", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		if (ch == ']') {
			toString("RIGHTSQUAREB", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		if (ch == '{') {
			toString("LEFTCURLYB", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		if (ch == '}') {
			toString("RIGHTCURLYB", lineNo, columnNo);
			return token.substring(1, token.length());
		}
		return token;
	}

	public static String identifier(String token, int lineNo, int columnNo) {
		if (token.isEmpty()) {
			return token;
		}
		for (int i = token.length(); i > 0; i--) {
			String temp = token.substring(0, i);
			if (isIdentifier(temp)) {
				toString("IDENTIFIER", lineNo, columnNo);
				return token.substring(i, token.length());
			}
		}
		return token;
	}

	public static boolean isIdentifier(String s) {
		boolean validChar = true;
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

	public static boolean isBracket(String s) {
		if (s.isEmpty()) {
			return false;
		}
		return s.charAt(0) == '(' || s.charAt(0) == ')' || s.charAt(0) == '[' || s.charAt(0) == ']'
				|| s.charAt(0) == '{' || s.charAt(0) == '}';
	}

	public static boolean isKeyword(String s) {
		if (s.isEmpty()) {
			return false;
		}
		return s.equals("define") || s.equals("let")
				|| s.equals("cond") || s.equals("if") || s.equals("begin")
				|| s.equals("true") || s.equals("false");
	}

	public static boolean isDecDigit(char c) {
		return Character.isDigit(c);
	}

	public static void toString(String token, int lineNo, int columnNo) {
		System.out.println(token + " " + lineNo + ":" + columnNo);
	}
}