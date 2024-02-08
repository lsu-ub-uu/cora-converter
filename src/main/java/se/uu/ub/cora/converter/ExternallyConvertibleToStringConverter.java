/*
 * Copyright 2019, 2021 Uppsala University Library
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

import se.uu.ub.cora.data.ExternallyConvertible;

/**
 * ExternallyConvertibleToStringConverter is used to convert from a DataElement to a String based
 * format.
 * <p>
 * Implementations of ExternallyConvertibleToStringConverter are not expected to be thread safe.
 */
public interface ExternallyConvertibleToStringConverter {
	/**
	 * Returns a String containing the result of the convertion from the entered dataElement.
	 * <p>
	 * If links are required user
	 * {@link Converter#convertWithLinks(ExternallyConvertible, ExternalUrls)} instead.
	 * <p>
	 * If unable to convert to String a {@link ConverterException} be thrown.
	 * 
	 * @param externallyConvertible
	 *            to convert from
	 * @return result of the convertion as a String
	 */
	String convert(ExternallyConvertible externallyConvertible);

	/**
	 * Returns a String containing the result of the convertion from the entered dataElement, with
	 * actions converted to links. If the entered DataElement represents a DataGroup with
	 * permissions are they also converted.
	 * <p>
	 * If no links are required user {@link Converter#convert(ExternallyConvertable)} instead.
	 * <p>
	 * If unable to convert to String a {@link ConverterException} should be thrown.
	 * 
	 * @param externallyConvertible
	 *            to convert from
	 * @param externalUrls
	 *            An object that holds external urls such as baseUrl and iiifUrl.
	 * @return result of the convertion as a String with links for actions
	 */
	String convertWithLinks(ExternallyConvertible externallyConvertible,
			ExternalUrls externalUrls);

}
