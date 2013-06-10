package biweekly.io.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Automatically unfolds lines of text as they are read.
 * @author Michael Angstadt
 */
public class FoldedLineReader extends BufferedReader {
	private String lastLine;

	/**
	 * @param reader the reader object to wrap
	 */
	public FoldedLineReader(Reader reader) {
		super(reader);
	}

	/**
	 * @param text the text to read
	 */
	public FoldedLineReader(String text) {
		this(new StringReader(text));
	}

	/**
	 * Reads the next non-empty line.
	 * @return the next non-empty line or null of EOF
	 * @throws IOException
	 */
	private String readNonEmptyLine() throws IOException {
		String line;
		do {
			line = super.readLine();
		} while (line != null && line.length() == 0);
		return line;
	}

	/**
	 * Reads the next line, unfolding it if necessary.
	 * @return the next line or null if EOF
	 * @throws IOException if there's a problem reading from the reader
	 */
	@Override
	public String readLine() throws IOException {
		String wholeLine = (lastLine == null) ? readNonEmptyLine() : lastLine;
		lastLine = null;
		if (wholeLine == null) {
			return null;
		}

		//long lines are folded
		StringBuilder wholeLineSb = new StringBuilder(wholeLine);
		while (true) {
			String line = readNonEmptyLine();
			if (line == null) {
				break;
			} else if (line.length() > 0 && Character.isWhitespace(line.charAt(0))) {
				//the line was folded

				int lastWhitespace = 1;
				while (lastWhitespace < line.length() && Character.isWhitespace(line.charAt(lastWhitespace))) {
					lastWhitespace++;
				}
				wholeLineSb.append(line.substring(lastWhitespace));
			} else {
				lastLine = line;
				break;
			}
		}
		return wholeLineSb.toString();
	}
}
