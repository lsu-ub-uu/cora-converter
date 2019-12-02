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

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import se.uu.ub.cora.converter.starter.ConverterModuleStarter;
import se.uu.ub.cora.converter.starter.ConverterModuleStarterImp;
import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterProvider {

	private static Map<String, ConverterFactory> converterFactories = new HashMap<>();
	private static ConverterModuleStarter starter = new ConverterModuleStarterImp();
	private static Logger log = LoggerProvider.getLoggerForClass(ConverterProvider.class);

	private ConverterProvider() {
		// prevent constructor from ever being called even by reflection
		throw new UnsupportedOperationException();
	}

	public static Converter getConverter(String converterName) {
		ensureConverterFactoryIsSet();
		ConverterFactory converterFactory = converterFactories.get(converterName);
		if (converterFactory == null) {
			throw new ConverterInitializationException(
					"No implementations found for " + converterName + " converter.");
		}
		return converterFactory.factorConverter();
	}

	private static synchronized void ensureConverterFactoryIsSet() {
		if (converterFactories.isEmpty()) {
			log.logInfoUsingMessage("ConverterProvider starting...");
			getConverterFactoryImpUsingModuleStarter();
			log.logInfoUsingMessage("ConverterProvider started");
		}
	}

	private static void getConverterFactoryImpUsingModuleStarter() {
		Iterable<ConverterFactory> converterFactoryImplementations = ServiceLoader
				.load(ConverterFactory.class);
		converterFactories = starter
				.startUsingConverterFactoryImplementations(converterFactoryImplementations);
	}

	/**
	 * Sets a ConverterFactory that will be used to factor converters. This possibility to set a
	 * ConverterFactory is provided to enable testing of converting in other classes and is not
	 * intented to be used in production. The ConverterFactory to use should be provided through an
	 * implementation of ConverterFactory in a seperate java module.
	 * 
	 * @param converterFactory
	 *            A ConverterFactory to use to create converters for testing
	 */

	public static void setConverterFactory(String converterName,
			ConverterFactory converterFactory) {
		ConverterProvider.converterFactories.put(converterName, converterFactory);
	}

	static void setStarter(ConverterModuleStarter starter) {
		ConverterProvider.starter = starter;
	}

	static void resetConverterFactories() {
		converterFactories.clear();
	}

	static ConverterModuleStarter getStarter() {
		return starter;
	}
}
