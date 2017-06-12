package com.vsourabh.simplead.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vsourabh.simplead.contract.JdbcModule;
import com.vsourabh.simplead.model.AdBean;

public class AdHelperTest {
	
	AdBean adActive, adInActive;
	Map<String, Map<Integer, AdBean>> map = new HashMap<>();
	Map<Integer, AdBean> admap = new HashMap<>();
	
	@Before
	public void setUp() {	
		adActive = new AdBean("1_part", 90, "First Ad", LocalDateTime.now(), 1);
		adInActive = new AdBean("1_part", 90, "Second Ad", LocalDateTime.parse("2017-06-11T10:11:30"), 12);
		admap.put(adActive.getAdId(), adActive);
		admap.put(adInActive.getAdId(), adInActive);
		map.put(adActive.getPartnerId(), admap);
	}
	
	@After
	public void tearDown() {	
		adActive = null;
		adInActive = null;
		admap = null;
		map = null;
	}
	
	@Test
	public void testActiveAd() {
		assertTrue(AdHelper.isActiveAd(adActive));
	}
	
	@Test
	public void testInActiveAd() {
		assertTrue(!AdHelper.isActiveAd(adInActive));
	}
	
	@Test
	public void testActiveAdInMap() {
		assertTrue(AdHelper.isActiveAdInMap(map.get(adActive.getPartnerId())));
	}
	
	@Test
	public void testInActiveAdInMap() {
		admap.remove(1);
		assertTrue(!AdHelper.isActiveAdInMap(admap));
	}
	
	@Test
	public void testActiveAdFromMap() {
		assertNotNull(AdHelper.getActiveAdID(admap));
	}
	
	@Test
	public void testInActiveAdFromMap() {
		admap.remove(1);
		assertNull(AdHelper.getActiveAdID(admap));
	}
	
	@Test
	public void testGetPartnerMap() {
		assertNotNull(AdHelper.getPartnerMapAd(map, "1_part"));
	}
	
	@Test
	public void testGetPartnerMapNA() {
		assertNull(AdHelper.getPartnerMapAd(map, "2_part"));
	}
	
	@Test
	public void testGetActiveAd() {
		assertNotNull(AdHelper.getAd(map, "1_part",0));
		assertNotNull(AdHelper.getAd(map, "1_part",1));
	}
	
	@Test
	public void testGetNullForInactiveAd() {
		admap.remove(1);
		assertNull(AdHelper.getAd(map, "2_part", 0));
		assertNull(AdHelper.getAd(map, "2_part", 12));
		assertNull(AdHelper.getAd(map, "1_part", 0));
		assertNull(AdHelper.getAd(map, "1_part", 12));
	}
	
	@Test
	public void testGetActiveAdList() {
		assertNotNull(AdHelper.getPartnerAdInList(map, true));
		assertEquals(1, AdHelper.getPartnerAdInList(map, true).size());
		admap.remove(1);
		assertEquals(0, AdHelper.getPartnerAdInList(map, true).size());
	}
	
	@Test
	public void testGetAllAdList() {
		assertNotNull(AdHelper.getPartnerAdInList(map, false));
		assertEquals(2, AdHelper.getPartnerAdInList(map, false).size());
		admap.remove(1);
		assertEquals(1, AdHelper.getPartnerAdInList(map, false).size());
	}
	
	@Test
	public void testGetActiveAdMap() {
		JdbcModule mockModule = mock(JdbcModule.class);
		List<AdBean> list = new ArrayList<>(admap.values());
		stub(mockModule.getAllAds(true)).toReturn(list);
		assertNotNull(AdHelper.getPartnerAdInMap(mockModule));
		assertEquals(1, AdHelper.getPartnerAdInMap(mockModule).size());
		assertEquals(2, AdHelper.getPartnerAdInMap(mockModule).get("1_part").size());
		
		list = new ArrayList<>();
		stub(mockModule.getAllAds(true)).toReturn(list);
		assertNotNull(AdHelper.getPartnerAdInMap(mockModule));
		assertEquals(0, AdHelper.getPartnerAdInMap(mockModule).size());
	}
}
