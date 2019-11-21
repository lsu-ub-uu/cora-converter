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
import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterModuleStarterImp implements ConverterModuleStarter {
	private Logger loggerForClass = LoggerProvider
			.getLoggerForClass(ConverterModuleStarterImp.class);
	private Map<String, ConverterFactory> foundConverterFactories;

	@Override
	public Map<String, ConverterFactory> startUsingConverterFactoryImplementations(
			Iterable<ConverterFactory> converterFactoryImplementations) {
		foundConverterFactories = new HashMap<>();
		addFoundFactories(converterFactoryImplementations);
		throwErrorIfNotOne();
		return foundConverterFactories;
	}

	private void throwErrorIfNotOne() {
		logAndThrowErrorIfNone();
	}

	private void addFoundFactories(Iterable<ConverterFactory> converterFactoryImplementations) {
		for (ConverterFactory converterFactory : converterFactoryImplementations) {
			addFoundFactory(converterFactory);
		}
	}

	private void addFoundFactory(ConverterFactory converterFactory) {
		String converterFactoryName = converterFactory.getName();
		logInfoMessage(converterFactory, converterFactoryName);
		throwErrorIfMoreThanOneImplementationFoundForFactoryName(converterFactoryName);
		foundConverterFactories.put(converterFactoryName, converterFactory);
	}

	private void logInfoMessage(ConverterFactory converterFactory, String converterFactoryName) {
		loggerForClass.logInfoUsingMessage(converterFactory.getClass().getSimpleName()
				+ " found as implementation for ConverterFactory with name "
				+ converterFactoryName);
	}

	private void throwErrorIfMoreThanOneImplementationFoundForFactoryName(
			String converterFactoryName) {
		if (implementationForFactoryAlreadyAdded(converterFactoryName)) {
			String errorMessage = "More than one implementations found for ConverterFactory with name "
					+ converterFactoryName;
			loggerForClass.logFatalUsingMessage(errorMessage);
			throw new ConverterInitializationException(errorMessage);
		}
	}

	private boolean implementationForFactoryAlreadyAdded(String converterFactoryName) {
		return foundConverterFactories.containsKey(converterFactoryName);
	}

	private void logAndThrowErrorIfNone() {
		if (foundConverterFactories.isEmpty()) {
			String errorMessage = "No implementations found for ConverterFactory";
			loggerForClass.logFatalUsingMessage(errorMessage);
			throw new ConverterInitializationException(errorMessage);
		}
	}
}
