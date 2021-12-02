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

package se.uu.ub.cora.converter.spy;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.DataElementToStringConverter;
import se.uu.ub.cora.converter.StringToDataElementConverter;

public class ConverterFactorySpy implements ConverterFactory {

	public String factoryName;
	public String converterName;

	public ConverterFactorySpy(String name) {
		this.factoryName = name;
	}

	@Override
	public String getName() {
		return factoryName;
	}

	@Override
	public StringToDataElementConverter factorStringToDataElementConverter() {
		StringToDataElementConverterSpy stringToDataElementConverterSpy = new StringToDataElementConverterSpy();
		stringToDataElementConverterSpy.factoryName = factoryName;
		return stringToDataElementConverterSpy;
	}

	@Override
	public DataElementToStringConverter factorDataElementToStringConverter() {
		DataElementToStringConverterSpy dataElementToStringConverterSpy = new DataElementToStringConverterSpy();
		dataElementToStringConverterSpy.factoryName = factoryName;
		return dataElementToStringConverterSpy;
	}
}
