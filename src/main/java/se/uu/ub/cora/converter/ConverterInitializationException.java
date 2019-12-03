/*
 * Copyright 2019 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.converter;

/**
 * Specific exception for converters. It is used as a exception holder for all excpetions thrown in
 * Converter package.
 */
public class ConverterInitializationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that creates a new ConverterInitializationException using a single message.
	 * 
	 * @param message
	 *            String with the exception message in clear text.
	 */
	public ConverterInitializationException(String message) {
		super(message);
	}

	/**
	 * Constructor that creates a new ConverterInitializationException using a single message and a
	 * previous excpetion.
	 * 
	 * @param message
	 *            String with the exception message in clear text.
	 * @param exception
	 *            an Exception which will be used as "caused by".
	 */
	public ConverterInitializationException(String message, Exception exception) {
		super(message, exception);
	}

}
