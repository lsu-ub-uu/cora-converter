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
package se.uu.ub.cora.converter.starter;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.spy.ConverterFactorySpy;
import se.uu.ub.cora.converter.starter.ConverterModuleStarter;

public class ConverterModuleStarterSpy implements ConverterModuleStarter {

	public boolean startWasCalled = false;
	public Iterable<ConverterFactory> converterFactoryImplementations;

	@Override
	public ConverterFactory startUsingConverterFactoryImplementations(
			Iterable<ConverterFactory> converterFactoryImplementations) {
		this.converterFactoryImplementations = converterFactoryImplementations;
		startWasCalled = true;
		return new ConverterFactorySpy();
	}

}
