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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
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
import se.uu.ub.cora.converter.spy.ExternallyConvertibleToStringConverterSpy;
import se.uu.ub.cora.converter.spy.StringToExternallyConvertibleConverterSpy;
import se.uu.ub.cora.converter.spy.log.LoggerFactorySpy;
import se.uu.ub.cora.converter.starter.ConverterModuleStarter;
import se.uu.ub.cora.converter.starter.ConverterModuleStarterImp;
import se.uu.ub.cora.converter.starter.ConverterModuleStarterSpy;
import se.uu.ub.cora.logger.LoggerProvider;

public class ConverterProviderTest {
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "ConverterProvider";
	private String converterName = "xml0";
	private ConverterModuleStarter defaultStarter;

	@BeforeTest
	public void beforeTest() {
		loggerFactorySpy = LoggerFactorySpy.getInstance();
		loggerFactorySpy.resetLogs(testedClassName);
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		defaultStarter = ConverterProvider.getStarter();
		ConverterProvider.resetConverterFactories();
	}

	@BeforeMethod
	public void beforeMethod() {
		loggerFactorySpy = LoggerFactorySpy.getInstance();
		loggerFactorySpy.resetLogs(testedClassName);
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		ConverterProvider.resetConverterFactories();
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
	public void testStartingOfProviderFactoryCanOnlyBeDoneByOneThreadAtATime() throws Exception {
		Method declaredMethod = ConverterProvider.class
				.getDeclaredMethod("ensureConverterFactoryIsSet");
		assertTrue(Modifier.isSynchronized(declaredMethod.getModifiers()));
	}

	@Test
	public void testConverterFactoriesAreLoadedOnlyOnce() throws Exception {
		ConverterModuleStarterSpy startAndSetConverterModuleStarterSpy = startAndSetConverterModuleStarterSpy(
				1);
		ConverterFactorySpy converterFactorySpy = new ConverterFactorySpy("xml0");
		ConverterProvider.setConverterFactory("xml0", converterFactorySpy);

		ConverterProvider.getExternallyConvertibleToStringConverter("xml0");
		ConverterProvider.getStringToExternallyConvertibleConverter("xml0");

		assertFalse(startAndSetConverterModuleStarterSpy.startWasCalled);
	}

	private ConverterModuleStarterSpy startAndSetConverterModuleStarterSpy(
			int noOfConverterFactoriesToReturn) {
		ConverterModuleStarter starter = new ConverterModuleStarterSpy(
				noOfConverterFactoriesToReturn);
		ConverterProvider.setStarter(starter);
		return (ConverterModuleStarterSpy) starter;
	}

	@Test
	public void testConverterModuleStarterIsCalledOnGetDataElementConverter() throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);

		ConverterProvider.getExternallyConvertibleToStringConverter(converterName);
		assertTrue(starter.startWasCalled);
	}

	@Test
	public void testConverterModuleStarterIsCalledOnGetStringConverter() throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);

		ConverterProvider.getStringToExternallyConvertibleConverter(converterName);
		assertTrue(starter.startWasCalled);
	}

	@Test
	public void testLoggingOnGetDataElementConverter() {
		startAndSetConverterModuleStarterSpy(1);
		ConverterProvider.getExternallyConvertibleToStringConverter(converterName);

		assertStartupLoggin();
	}

	private void assertStartupLoggin() {
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"ConverterProvider starting...");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"ConverterProvider started");
	}

	@Test
	public void testLoggingOnGetStringConverter() {
		startAndSetConverterModuleStarterSpy(1);
		ConverterProvider.getStringToExternallyConvertibleConverter(converterName);

		assertStartupLoggin();
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
	public void testErrorIsThrownWhenNoImplementationsAreFoundDataElement() throws Exception {
		startAndSetConverterModuleStarterSpy(0);
		Exception caughtException = null;
		try {
			ConverterProvider.getExternallyConvertibleToStringConverter(converterName);
		} catch (Exception e) {
			caughtException = e;
		}

		assertErrorAndLog(caughtException);
	}

	private void assertErrorAndLog(Exception caughtException) {
		assertTrue(caughtException instanceof ConverterInitializationException);
		assertEquals(caughtException.getMessage(),
				"No implementations when loading, thrown by SPY");
	}

	@Test
	public void testErrorIsThrownWhenNoImplementationsAreFoundString() throws Exception {
		startAndSetConverterModuleStarterSpy(0);
		Exception caughtException = null;
		try {
			ConverterProvider.getStringToExternallyConvertibleConverter(converterName);
		} catch (Exception e) {
			caughtException = e;
		}

		assertErrorAndLog(caughtException);
	}

	@Test
	public void testConverterFactoryImplementationsArePassedOnToStarterDataElement()
			throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);

		ConverterProvider.getExternallyConvertibleToStringConverter(converterName);

		Iterable<ConverterFactory> iterable = starter.converterFactoryImplementations;
		assertTrue(iterable instanceof ServiceLoader);
	}

	@Test
	public void testConverterFactoryImplementationsArePassedOnToStarterString() throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);

		ConverterProvider.getStringToExternallyConvertibleConverter(converterName);

		Iterable<ConverterFactory> iterable = starter.converterFactoryImplementations;
		assertTrue(iterable instanceof ServiceLoader);
	}

	@Test
	public void testConverterFactoryReturnsDifferentFactoriesBasedOnName() throws Exception {

		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(4);

		ExternallyConvertibleToStringConverterSpy converter1 = (ExternallyConvertibleToStringConverterSpy) ConverterProvider
				.getExternallyConvertibleToStringConverter(converterName);
		ExternallyConvertibleToStringConverterSpy converter2 = (ExternallyConvertibleToStringConverterSpy) ConverterProvider
				.getExternallyConvertibleToStringConverter("xml1");
		StringToExternallyConvertibleConverterSpy converter3 = (StringToExternallyConvertibleConverterSpy) ConverterProvider
				.getStringToExternallyConvertibleConverter("xml2");
		StringToExternallyConvertibleConverterSpy converter4 = (StringToExternallyConvertibleConverterSpy) ConverterProvider
				.getStringToExternallyConvertibleConverter("xml3");

		assertNotSame(converter1.factoryName, converter2.factoryName);
		assertNotSame(converter3.factoryName, converter4.factoryName);
		assertEquals(starter.converterFactories.size(), 4);
	}

	@Test
	public void testIfExceptionIsThrownWhenDataElementToStringConverterImplementationNotFoundAfterLoading()
			throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);
		makeSureErrorIsThrownWhenImplementationDataElementToStringConverterNotFoundAfterLoading();
		assertEquals(starter.converterFactories.size(), 1);

	}

	private void makeSureErrorIsThrownWhenImplementationDataElementToStringConverterNotFoundAfterLoading() {
		String converterImplementationName = "NonexistingConverter";
		Exception caughtException = null;
		try {

			ConverterProvider.getExternallyConvertibleToStringConverter(converterImplementationName);
		} catch (Exception e) {
			caughtException = e;
		}
		assertTrue(caughtException instanceof ConverterInitializationException);
		assertEquals(caughtException.getMessage(),
				"No implementations found for " + converterImplementationName + " converter.");

	}

	@Test
	public void testIfExceptionIsThrownWhenStringToDataElementConverterImplementationNotFoundAfterLoading()
			throws Exception {
		ConverterModuleStarterSpy starter = startAndSetConverterModuleStarterSpy(1);
		makeSureErrorIsThrownWhenImplementationStringToDataElementConverterNotFoundAfterLoading();
		assertEquals(starter.converterFactories.size(), 1);

	}

	private void makeSureErrorIsThrownWhenImplementationStringToDataElementConverterNotFoundAfterLoading() {
		String converterImplementationName = "NonexistingConverter";
		Exception caughtException = null;
		try {

			ConverterProvider.getStringToExternallyConvertibleConverter(converterImplementationName);
		} catch (Exception e) {
			caughtException = e;
		}
		assertTrue(caughtException instanceof ConverterInitializationException);
		assertEquals(caughtException.getMessage(),
				"No implementations found for " + converterImplementationName + " converter.");

	}

}
