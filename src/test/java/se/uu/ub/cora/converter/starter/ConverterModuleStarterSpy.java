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

import java.util.HashMap;
import java.util.Map;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.ConverterInitializationException;
import se.uu.ub.cora.converter.spy.ConverterFactorySpy;

public class ConverterModuleStarterSpy implements ConverterModuleStarter {

	public boolean startWasCalled = false;
	public Iterable<ConverterFactory> converterFactoryImplementations;
	private int noOfConverterFactories;
	public Map<String, ConverterFactory> converterFactories;

	public ConverterModuleStarterSpy(int noOfConverterFactories) {
		this.noOfConverterFactories = noOfConverterFactories;
	}

	@Override
	public Map<String, ConverterFactory> startUsingConverterFactoryImplementations(
			Iterable<ConverterFactory> converterFactoryImplementations) {
		this.converterFactoryImplementations = converterFactoryImplementations;
		startWasCalled = true;
		if (noOfConverterFactories == 0) {
			throw new ConverterInitializationException(
					"No implementations when loading, thrown by SPY");
		}
		converterFactories = new HashMap<>();
		for (int i = 0; i < noOfConverterFactories; i++) {
			converterFactories.put("xml" + i, new ConverterFactorySpy("xml" + i));
		}
		return converterFactories;
	}
}
