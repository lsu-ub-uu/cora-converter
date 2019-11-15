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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ServiceLoader;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import se.uu.ub.cora.converter.spy.ConverterFactorySpy;
import se.uu.ub.cora.converter.spy.log.LoggerFactorySpy;
import se.uu.ub.cora.converter.starter.ConverterModuleStarter;
import se.uu.ub.cora.converter.starter.ConverterModuleStarterImp;
import se.uu.ub.cora.converter.starter.ConverterModuleStarterSpy;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterProviderTest {
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "ConverterProvider";
	private String converterName = "xml1";
	private ConverterModuleStarter defaultStarter;

	@BeforeTest
	public void beforeTest() {
		loggerFactorySpy = LoggerFactorySpy.getInstance();
		loggerFactorySpy.resetLogs(testedClassName);
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		defaultStarter = ConverterProvider.getStarter();
	}

	@BeforeMethod
	public void beforeMethod() {
		loggerFactorySpy = LoggerFactorySpy.getInstance();
		loggerFactorySpy.resetLogs(testedClassName);
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		ConverterProvider.setConverterFactory(null);
	}

	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<ConverterProvider> constructor = ConverterProvider.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testPrivateConstructorInvoke() throws Exception {
		Constructor<ConverterProvider> constructor = ConverterProvider.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testSetConverterFactoryForUsingInAnotherTest() throws Exception {
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy();
		ConverterProvider.setConverterFactory(converterFactorySpy);

		Converter converter = ConverterProvider.getConverter(converterName);

		assertEquals(converterFactorySpy.converterName, converterName);
		assertEquals(converter, converterFactorySpy.converter);
	}

	@Test
	public void testStartingOfProviderFactoryCanOnlyBeDoneByOneThreadAtATime() throws Exception {
		Method declaredMethod = LoggerProvider.class.getDeclaredMethod("ensureLoggerFactoryIsSet");
		assertTrue(Modifier.isSynchronized(declaredMethod.getModifiers()));
	}

	@Test
	public void testConverterModuleStarterIsCalledOnGetConverter() throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy();

		ConverterProvider.getConverter(converterName);
		assertTrue(starter.startWasCalled);
	}

	private ConverterModuleStarterSpy startAndSetConverterModuleStarterSpy() {
		ConverterModuleStarter starter = new ConverterModuleStarterSpy();
		ConverterProvider.setStarter(starter);
		return (ConverterModuleStarterSpy) starter;
	}

	@Test
	public void testLoggingOnGetConverter() {
		startAndSetConverterModuleStarterSpy();

		ConverterProvider.getConverter(converterName);

		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterProvider starting...");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"ConverterProvider started");
	}

	@Test
	public void testInitUsesSetConverterModuleStarter() throws Exception {
		ConverterProvider.setStarter(defaultStarter);
		assertStarterIsConverterModuleStarter(defaultStarter);
	}

	private void assertStarterIsConverterModuleStarter(ConverterModuleStarter starter) {
		assertTrue(starter instanceof ConverterModuleStarterImp);
	}

	@Test
	public void testErrorIsThrownWhenNoImplementationsAreFound() throws Exception {
		ConverterProvider.setStarter(defaultStarter);
		makeSureErrorIsThrownAsNoImplementationsExistInThisModule();
	}

	private void makeSureErrorIsThrownAsNoImplementationsExistInThisModule() {
		Exception caughtException = null;
		try {

			ConverterProvider.getConverter(converterName);
		} catch (Exception e) {
			caughtException = e;
		}
		assertTrue(caughtException instanceof ConverterInitializationException);
		assertEquals(caughtException.getMessage(), "No implementations found for ConverterFactory");
	}

	@Test
	public void testConverterFactoryImplementationsArePassedOnToStarter() throws Exception {

		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy();

		ConverterProvider.getConverter(converterName);

		Iterable<ConverterFactory> iterable = starter.converterFactoryImplementations;
		assertTrue(iterable instanceof ServiceLoader);
	}

}
