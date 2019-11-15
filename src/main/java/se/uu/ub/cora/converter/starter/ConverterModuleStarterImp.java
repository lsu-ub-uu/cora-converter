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
import se.uu.ub.cora.converter.ConverterInitializationException;
import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterModuleStarterImp implements ConverterModuleStarter {
	private Logger loggerForClass = LoggerProvider
			.getLoggerForClass(ConverterModuleStarterImp.class);
	private ConverterFactory foundConverterFactory = null;
	private int numberOfImplementations = 0;

	@Override
	public ConverterFactory startUsingConverterFactoryImplementations(
			Iterable<ConverterFactory> converterFactoryImplementations) {
		chooseAndCountFactories(converterFactoryImplementations);
		throwErrorIfNotOne();
		return foundConverterFactory;
	}

	private void throwErrorIfNotOne() {
		logAndThrowErrorIfNone();
		logAndThrowErrorIfMoreThanOne();
	}

	private void chooseAndCountFactories(
			Iterable<ConverterFactory> converterFactoryImplementations) {
		for (ConverterFactory converterFactory : converterFactoryImplementations) {
			numberOfImplementations++;
			foundConverterFactory = converterFactory;
			loggerForClass.logInfoUsingMessage(foundConverterFactory.getClass().getSimpleName()
					+ " found as implemetation for ConverterFactory");
		}
	}

	private void logAndThrowErrorIfNone() {
		if (numberOfImplementations == 0) {
			String errorMessage = "No implementations found for ConverterFactory";
			loggerForClass.logFatalUsingMessage(errorMessage);
			throw new ConverterInitializationException(errorMessage);
		}
	}

	private void logAndThrowErrorIfMoreThanOne() {
		if (numberOfImplementations > 1) {
			String errorMessage = "More than one implementations found for ConverterFactory";
			loggerForClass.logFatalUsingMessage(errorMessage);
			throw new ConverterInitializationException(errorMessage);
		}
	}

}
