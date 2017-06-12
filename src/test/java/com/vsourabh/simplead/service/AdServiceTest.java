package com.vsourabh.simplead.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vsourabh.simplead.contract.JdbcModule;
import com.vsourabh.simplead.model.AdBean;;

public class AdServiceTest {
	
	AdBean adActive, adInActive;
	Map<String, Map<Integer, AdBean>> map = new HashMap<>();
	Map<Integer, AdBean> admap = new HashMap<>();
	AdService adService;
	JdbcModule mockModule;
	
	@Before
	public void setUp() {	
		adService = new AdService();
		mockModule = mock(JdbcModule.class);
		adService.setJdbcModule(mockModule);
		
		adActive = new AdBean("1_part", 90, "First Ad", LocalDateTime.now(), 1);
		adInActive = new AdBean("2_part", 90, "Second Ad", LocalDateTime.parse("2017-06-11T10:11:30"), 12);
		admap.put(adActive.getAdId(), adActive);
		admap.put(adInActive.getAdId(), adInActive);
		map.put(adActive.getPartnerId(), admap);
		map.put(adInActive.getPartnerId(), admap);
		
	}
	
	@After
	public void tearDown() {	
		adService = null;
		mockModule = null;
		
		adActive = null;
		adInActive = null;
		admap = null;
		map = null;
	}
	
	@Test
	public void testSaveAd() {
		when(mockModule.saveAd(adActive)).thenReturn(true);
		assertTrue(adService.saveAd(adActive));
		
		when(mockModule.saveAd(adInActive)).thenReturn(false);
		assertTrue(!adService.saveAd(adInActive));
		
		List<AdBean> list = new ArrayList<>(admap.values());
		when(mockModule.getAllAds(true)).thenReturn(list);
		
		when(mockModule.saveAd(adInActive)).thenReturn(true);
		assertTrue(adService.saveAd(adInActive));
		
		assertTrue(!adService.saveAd(adActive));
	}
	
	@Test
	public void testGetAd() {
		when(mockModule.getAd("1_part", 0)).thenReturn(adActive);
		assertEquals(adService.getAd("1_part", 0), adActive);
		
		when(mockModule.getAd("2_part", 0)).thenReturn(null);
		assertEquals(adService.getAd("2_part", 0), null);
		
		List<AdBean> list = new ArrayList<>(admap.values());
		when(mockModule.getAllAds(true)).thenReturn(list);
		adService.saveAd(adActive);
		
		assertEquals(adService.getAd("1_part", 0), adActive);
	}
	
	@Test
	public void testUpdateAd() {
		when(mockModule.updateAd(adActive)).thenReturn(true);
		assertEquals(adService.updateAd(adActive), true);
		assertTrue(adService.updateAd(adActive));
	}
	@Test
	public void testGetAllAd() {
		admap.put(2, new AdBean("3_part", 90, "Third Ad", LocalDateTime.now(), 2));
		List<AdBean> list = new ArrayList<>(admap.values());
		when(mockModule.getAllAds(false)).thenReturn(list);
		assertNotNull(adService.getAllAd());
		assertEquals(adService.getAllAd().size(), 3);
	}
	
	@Test
	public void testGetAllActiveAd() {
		admap.put(2, new AdBean("3_part", 90, "Third Ad", LocalDateTime.now(), 2));
		List<AdBean> list = new ArrayList<>(admap.values());
		when(mockModule.getAllAds(true)).thenReturn(list);
		
		assertNotNull(adService.getAllActiveAd());
		assertEquals(adService.getAllActiveAd().size(), 2);
	}
}
