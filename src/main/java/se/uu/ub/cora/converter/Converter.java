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

/**
 * 
 */
/**
 * 
 */
package se.uu.ub.cora.converter;

import se.uu.ub.cora.data.DataElement;

/**
 * Converter is used to convert between a String based format and DataElements or vice versa.
 */
public interface Converter {
	/**
	 * Returns a String containing the result of the convertion from the entered dataElement.
	 * 
	 * @param dataElement
	 *            to convert from
	 * @return result of the convertion as a String
	 */
	String convert(DataElement dataElement);

	/**
	 * Returns a DataElement containing the result of the convertion from the entered String.
	 * 
	 * @param dataString
	 *            with the string representation of an element to convert from
	 * @return result of the convertion as a DataElement
	 */
	DataElement convert(String dataString);
}
