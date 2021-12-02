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
 * Converter is used to convert from a DataElement to a String based format.
 */
public interface DataElementToStringConverter {
	/**
	 * Returns a String containing the result of the convertion from the entered dataElement. If
	 * links are required user {@link Converter#convertWithLinks(DataElement, String)} instead.
	 * 
	 * @param dataElement
	 *            to convert from
	 * @return result of the convertion as a String
	 */
	String convert(DataElement dataElement);

	/**
	 * Returns a String containing the result of the convertion from the entered dataElement, with
	 * actions converted to links. If the entered DataElement represents a DataGroup with
	 * permissions are they also converted. If no links are required user
	 * {@link Converter#convert(DataElement)} instead.
	 * 
	 * @param dataElement
	 *            to convert from
	 * @param baseUrl
	 *            A String with the baseUrl of the system to use when constructing links
	 * @return result of the convertion as a String with links for actions
	 */
	String convertWithLinks(DataElement dataElement, String baseUrl);

}
