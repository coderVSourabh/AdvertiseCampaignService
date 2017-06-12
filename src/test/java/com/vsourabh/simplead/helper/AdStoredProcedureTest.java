package com.vsourabh.simplead.helper;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vsourabh.simplead.model.AdBean;

public class AdStoredProcedureTest {
	AdStoredProcedure adStoreProc;
	AdBean ad;
	
	@Before
	public void setUp() {	
		adStoreProc = new AdStoredProcedure();
		adStoreProc.setProcName("SaveAdCampaign");
		ad = new AdBean("1_part", 90, "First Ad", LocalDateTime.now(), 1);
	}
	
	@After
	public void tearDown() {
		adStoreProc = null;
		ad = null;
	}
	
	@Test
	public void testActiveAd() {		
		JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
		assertTrue(!adStoreProc.executeSaveProc(mockTemplate, ad));
	}

}
