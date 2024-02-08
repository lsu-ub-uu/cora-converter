/*
 * Copyright 2024 Uppsala University Library
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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExternalUrlsTest {
	private static final String SOME_BASE_URL = "someBaseUrl";
	private static final String SOME_IFFF_URL = "someIfffUrl";
	private ExternalUrls eUrls;

	@BeforeMethod
	public void beforeMethod() {
		eUrls = new ExternalUrls();

	}

	@Test
	public void hasBaseUrl() throws Exception {
		assertFalse(eUrls.hasBaseUrl());
		eUrls.setBaseUrl(SOME_BASE_URL);
		assertTrue(eUrls.hasBaseUrl());
	}

	@Test
	public void setGetBaseUrl() throws Exception {
		eUrls.setBaseUrl(SOME_BASE_URL);
		assertEquals(eUrls.getBaseUrl(), SOME_BASE_URL);
	}

	@Test
	public void hasIfffUrl() throws Exception {
		assertFalse(eUrls.hasIfffUrl());
		eUrls.setIfffUrl(SOME_IFFF_URL);
		assertTrue(eUrls.hasIfffUrl());
	}

	@Test
	public void setGetIfffUrl() throws Exception {
		eUrls.setIfffUrl(SOME_IFFF_URL);
		assertEquals(eUrls.getIfffUrl(), SOME_IFFF_URL);
	}

}
