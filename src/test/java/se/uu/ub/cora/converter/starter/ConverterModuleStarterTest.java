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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.converter.ConverterFactory;
import se.uu.ub.cora.converter.ConverterInitializationException;
import se.uu.ub.cora.converter.spy.ConverterFactorySpy;
import se.uu.ub.cora.converter.spy.log.LoggerFactorySpy;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterModuleStarterTest {
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "ConverterModuleStarterImp";
	List<ConverterFactory> converterFactoryImplementations;
	private ConverterModuleStarterImp starter;

	@BeforeMethod
	public void beforeMethod() {
		loggerFactorySpy = LoggerFactorySpy.getInstance();
		loggerFactorySpy.resetLogs(testedClassName);
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		converterFactoryImplementations = new ArrayList<>();
		starter = new ConverterModuleStarterImp();

	}

	@Test(expectedExceptions = ConverterInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "No implementations found for ConverterFactory")
	public void testNoFoundImplementationThrowException() throws Exception {
		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
	}

	@Test
	public void testErrorIsLoggedWhenNoImplementations() throws Exception {
		try {
			starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
		} catch (Exception e) {

		}
		assertEquals(loggerFactorySpy.getFatalLogMessageUsingClassNameAndNo(testedClassName, 0),
				"No implementations found for ConverterFactory");
	}

	@Test
	public void testForFoundOneImplementation() throws Exception {
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy("XML");
		converterFactoryImplementations.add(converterFactorySpy);

		Map<String, ConverterFactory> converterFactories = starter
				.startUsingConverterFactoryImplementations(converterFactoryImplementations);
		assertEquals(converterFactories.size(), 1);
		assertSame(converterFactories.get("XML"), converterFactorySpy);

	}

	@Test
	public void testLoggingNormalStartupWithOneImplementation() throws Exception {
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy("XML");
		converterFactoryImplementations.add(converterFactorySpy);

		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);

		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterFactorySpy found as implementation for ConverterFactory with name XML");
		assertEquals(loggerFactorySpy.getNoOfInfoLogMessagesUsingClassName(testedClassName), 1);
	}

	@Test
	public void testForFoundMultipleImplementations() throws Exception {
		ConverterFactorySpy xmlConverterFactorySpy = new ConverterFactorySpy("XML");
		converterFactoryImplementations.add(xmlConverterFactorySpy);
		ConverterFactorySpy jsonConverterFactorySpy = new ConverterFactorySpy("JSON");
		converterFactoryImplementations.add(jsonConverterFactorySpy);

		Map<String, ConverterFactory> converterFactories = starter
				.startUsingConverterFactoryImplementations(converterFactoryImplementations);
		assertEquals(converterFactories.size(), 2);
		assertSame(converterFactories.get("XML"), xmlConverterFactorySpy);
		assertSame(converterFactories.get("JSON"), jsonConverterFactorySpy);

	}

	@Test
	public void testLoggingNormalStartupWithMultipleImplementation() throws Exception {
		ConverterFactorySpy xmlConverterFactorySpy = new ConverterFactorySpy("XML");
		converterFactoryImplementations.add(xmlConverterFactorySpy);
		ConverterFactorySpy jsonConverterFactorySpy = new ConverterFactorySpy("JSON");
		converterFactoryImplementations.add(jsonConverterFactorySpy);

		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);

		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterFactorySpy found as implementation for ConverterFactory with name XML");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"ConverterFactorySpy found as implementation for ConverterFactory with name JSON");

		assertEquals(loggerFactorySpy.getNoOfInfoLogMessagesUsingClassName(testedClassName), 2);
	}

	@Test(expectedExceptions = ConverterInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "More than one implementations found for ConverterFactory with name XML")
	public void testMoreThanOneImplementationWithSameNameThrowException() throws Exception {
		converterFactoryImplementations.add(new ConverterFactorySpy("XML"));
		converterFactoryImplementations.add(new ConverterFactorySpy("XML"));

		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
	}

	@Test
	public void testErrorIsLoggedWhenMoreThanOneImplementations() throws Exception {
		try {
			converterFactoryImplementations.add(new ConverterFactorySpy("XML"));
			converterFactoryImplementations.add(new ConverterFactorySpy("XML"));
			starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
		} catch (Exception e) {

		}
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterFactorySpy found as implementation for ConverterFactory with name XML");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"ConverterFactorySpy found as implementation for ConverterFactory with name XML");

		assertEquals(loggerFactorySpy.getFatalLogMessageUsingClassNameAndNo(testedClassName, 0),
				"More than one implementations found for ConverterFactory with name XML");
	}

}
