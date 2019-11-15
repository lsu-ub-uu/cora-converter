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

import java.util.ArrayList;
import java.util.List;

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

	@Test(expectedExceptions = ConverterInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "More than one implementations found for ConverterFactory")
	public void testMoreThanOneImplementationThrowException() throws Exception {
		converterFactoryImplementations.add(new ConverterFactorySpy());
		converterFactoryImplementations.add(new ConverterFactorySpy());

		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
	}

	@Test
	public void testErrorIsLoggedWhenMoreThanOneImplementations() throws Exception {
		try {
			converterFactoryImplementations.add(new ConverterFactorySpy());
			converterFactoryImplementations.add(new ConverterFactorySpy());
			starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);
		} catch (Exception e) {

		}
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterFactorySpy found as implemetation for ConverterFactory");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"ConverterFactorySpy found as implemetation for ConverterFactory");
		assertEquals(loggerFactorySpy.getFatalLogMessageUsingClassNameAndNo(testedClassName, 0),
				"More than one implementations found for ConverterFactory");
	}

	@Test
	public void testForFoundImplementation() throws Exception {
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy();
		converterFactoryImplementations.add(converterFactorySpy);

		ConverterFactory choosenConverterFactory = starter
				.startUsingConverterFactoryImplementations(converterFactoryImplementations);

		assertEquals(choosenConverterFactory, converterFactorySpy);
	}

	@Test
	public void testLoggingNormalStartup() throws Exception {
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy();
		converterFactoryImplementations.add(converterFactorySpy);

		starter.startUsingConverterFactoryImplementations(converterFactoryImplementations);

		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterFactorySpy found as implemetation for ConverterFactory");
		assertEquals(loggerFactorySpy.getNoOfInfoLogMessagesUsingClassName(testedClassName), 1);
	}

}
