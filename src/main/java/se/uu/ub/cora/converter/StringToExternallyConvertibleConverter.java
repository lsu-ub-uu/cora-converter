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

import se.uu.ub.cora.data.DataElement;

/**
 * StringToDataElementConverter is used to convert from a String based format to DataElement.
 * <p>
 * Implementations of StringToDataElementConverter are not expected to be thread safe.
 */
public interface StringToExternallyConvertibleConverter {
	/**
	 * Returns a DataElement containing the result of the convertion from the entered String.
	 * 
	 * @param dataString
	 *            with the string representation of an element to convert from
	 * @return result of the convertion as a DataElement
	 */
	DataElement convert(String dataString);
}
