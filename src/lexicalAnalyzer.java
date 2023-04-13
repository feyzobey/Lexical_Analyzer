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
import java.util.HashMap;
import java.util.Scanner;
public class lexicalAnalyzer {
	public static HashMap<String, String> keywords = new HashMap<String, String>();
	public static void main(String[] args) throws IOException {

		System.out.println("Enter the name of the input file: ");
		Scanner scanFileName = new Scanner(System.in);
		String input = scanFileName.nextLine();
		scanFileName.close();

		int lineNo = 0;
		int columnNo = 0;
		String token = "";
		// read line by line with scanner class
		Scanner scanner = new Scanner(new File(input));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] splittedLine = line.split(" ");
			if (isBracket(splittedLine[columnNo])) {
				
			}
				

			System.out.println(line);
			lineNo++;
		}
	}

	public static boolean isBracket(String s) {
		return s.charAt(0) == '(' || s.charAt(0) == ')' || s.charAt(0) == '[' || s.charAt(0) == ']'
				|| s.charAt(0) == '{' || s.charAt(0) == '}';
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