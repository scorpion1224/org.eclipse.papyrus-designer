/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/*
 * Created on 05/10/2006
 */
package org.eclipse.papyrus.designer.languages.java.reverse.ast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * <p>
 * This class was generated automatically by javacc, do not edit.
 * </p>
 * <p>
 * Parse Java 1.5 source code and creates Abstract Syntax Tree classes.
 * </p>
 * <p>
 * <b>Note:</b> To use this parser asynchronously, disable de parser cache by calling the method {@link setCacheParser} with <code>false</code> as argument.
 * </p>
 *
 * @author J�lio Vilmar Gesser
 */
public final class JavaParser {

	private static ASTParser parser;

	private static boolean cacheParser = true;

	private JavaParser() {
		// hide the constructor
	}

	/**
	 * Changes the way that the parser acts when starts to parse. If the
	 * parser cache is enabled, only one insance of this object will be
	 * used in every call to parse methods.
	 * If this parser is intend to be used asynchonously, the cache must
	 * be disabled setting this flag to <code>false</code>.
	 * By default, the cache is enabled.
	 *
	 * @param value
	 *            <code>false</code> to disable the parser instance cache.
	 */
	public static void setCacheParser(boolean value) {
		cacheParser = value;
		if (!value) {
			parser = null;
		}
	}

	/**
	 * Parses the Java code contained in the {@link InputStream} and returns
	 * a {@link CompilationUnit} that represents it.
	 *
	 * @param in
	 *            {@link InputStream} containing Java source code
	 * @param encoding
	 *            encoding of the source code
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseException
	 *             if the source code has parser errors
	 */
	public static CompilationUnit parse(InputStream in, String encoding) throws ParseException {
		if (cacheParser) {
			if (parser == null) {
				parser = new ASTParser(in, encoding);
			} else {
				parser.reset(in, encoding);
			}
			return parser.CompilationUnit();
		}
		return new ASTParser(in, encoding).CompilationUnit();
	}

	/**
	 * Parses the Java code contained in the {@link InputStream} and returns
	 * a {@link CompilationUnit} that represents it.
	 *
	 * @param in
	 *            {@link InputStream} containing Java source code
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseException
	 *             if the source code has parser errors
	 */
	public static CompilationUnit parse(InputStream in) throws ParseException {
		return parse(in, null);
	}

	/**
	 * Parses the Java code contained in a {@link File} and returns
	 * a {@link CompilationUnit} that represents it.
	 *
	 * @param file
	 *            {@link File} containing Java source code
	 * @param encoding
	 *            encoding of the source code
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseException
	 *             if the source code has parser errors
	 * @throws IOException
	 */
	public static CompilationUnit parse(File file, String encoding) throws ParseException, IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			return parse(in, encoding);
		} finally {
			in.close();
		}
	}

	/**
	 * Parses the Java code contained in a {@link File} and returns
	 * a {@link CompilationUnit} that represents it.
	 *
	 * @param file
	 *            {@link File} containing Java source code
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseException
	 *             if the source code has parser errors
	 * @throws IOException
	 */
	public static CompilationUnit parse(File file) throws ParseException, IOException {
		return parse(file, null);
	}

	/**
	 * Parses the Java code contained in the {@link InputStream} and returns
	 * a {@link CompilationUnit} that represents it.
	 *
	 * @param in
	 *            {@link InputStream} containing Java source code
	 * @param encoding
	 *            encoding of the source code
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseException
	 *             if the source code has parser errors
	 */
	public static CompilationUnit parse(Reader in) throws ParseException {
		if (cacheParser) {
			if (parser == null) {
				parser = new ASTParser(in);
			} else {
				parser.ReInit(in);
			}
			return parser.CompilationUnit();
		}
		return new ASTParser(in).CompilationUnit();
	}


}
