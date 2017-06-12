package com.vsourabh.simplead.helper;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vsourabh.simplead.contract.JdbcModule;
import com.vsourabh.simplead.model.AdBean;
import com.vsourabh.simplead.model.ErrorMessage;

/**
 * 
 * @author Sourabh
 * AdHelper is a Util class
 */
public class AdHelper {
	
	
	static final Logger log = Logger.getLogger(AdHelper.class.getName());
	
	/**
	 * Check if Ad is active or not
	 * and return a boolean
	 */	
	public static boolean isActiveAd(AdBean adBean) {
		boolean result = false;
		LocalDateTime dtTimeNow = LocalDateTime.now();
		LocalDateTime dtCreation = adBean.getCreationTime().plusSeconds(adBean.getDuration());
		if (dtTimeNow.isBefore(dtCreation))
			result = true;
		
		log.info("SimpleAd:method isActiveAd:" + result);
		return result;
	}
	
	/**
	 * Checks for Active Ad in a Map
	 * @param map : Map where we need to search active campaign
	 * @return : true or false based on active Ad is found or not
	 */
	public static boolean isActiveAdInMap(Map<Integer, AdBean> map) {
		Boolean result = false;
		for(Entry<Integer, AdBean> entry : map.entrySet())
			result = result || isActiveAd(entry.getValue());
		log.info("SimpleAd:method isActiveAdInMap:" + result);
		return result;
	}
	
	/**
	 * To Get the AdBean from Map
	 * @param map Input map containg all Ads
	 * @return Returns Active Ad for the partner
	 */
	public static AdBean getActiveAdID(Map<Integer, AdBean> map) {
		if (map != null)
			for(Entry<Integer, AdBean> entry : map.entrySet())
				if(isActiveAd(entry.getValue()))
					return entry.getValue();
		log.info("SimpleAd:method getActiveAdID: null");
		return null;
	}
	
	/**
	 * Error Message Generator
	 * @param id: partner ID for which request was sent
	 * @param actionMessage: error message
	 * @param description: error description
	 * @return returns a Respose object which is sent to client
	 */
	public static Response badRequestResponse(String id, String actionMessage, String description) {
		log.info("SimpleAd:method badRequestResponse");
		ErrorMessage errMsg = new ErrorMessage("partner_id: " + id +  actionMessage,
				400, description);
		return Response.status(Status.BAD_REQUEST).entity(errMsg).build();
	}
	
	public static boolean isEmptyMap(Map<String, Map<Integer, AdBean>> map) {
		if(map == null || map.isEmpty())
			return true;
		log.info("SimpleAd:method isEmptyMap: false");
		return false;
	}
	
	/**
	 * Returns a Map of all ad campaign of a partner 
	 * @param map : Input is a Map of all partners
	 * @param partnerID : Partner Unique ID
	 * @return: Map of all ad campaign of a particular partner
	 */
	public static Map<Integer, AdBean> getPartnerMapAd(Map<String, Map<Integer, AdBean>> map, String partnerID) {
		Map<Integer, AdBean> adMap = null;
		if(!isEmptyMap(map) && map.containsKey(partnerID)) {
			adMap = map.get(partnerID);
		}
		log.info("SimpleAd:method getPartnerMapAd " + adMap);
		return adMap;
	}
	
	/**
	 * Add Map to our local server Cache for faster 
	 * retrival from it than calling database always
	 * @param map: Map where Ad should be added
	 * @param ad: The Ad campaign which needs to be added in map
	 */
	public static void addAd(Map<String, Map<Integer, AdBean>> map, AdBean ad) {
		log.info("SimpleAd:method addAd ");
		Map<Integer, AdBean> adMap = getPartnerMapAd(map, ad.getPartnerId());
		if(adMap == null)
			adMap = new HashMap<>();

		int size = adMap.size();
		if(ad.getAdId() == 0)
			adMap.put(size + 1, ad);
		else
			adMap.put(ad.getAdId(), ad);
		map.put(ad.getPartnerId(), adMap);
	}
	
	/**
	 * Get an active ad for a partner, if there are multiple active ad campaign partner can send the 
	 * unique ad campaign ID to get that ad if it is active
	 * @param map : Server layer cache of all ads
	 * @param partnerID: partner for which we need to get active Ad
	 * @param adID: if valued other than 0 we will return that particular ad of partner if active
	 * @return: returns ad of the partner if Active
	 */
	public static AdBean getAd(Map<String, Map<Integer, AdBean>> map, String partnerID, Integer adID) {
		log.info("SimpleAd:method getAd ");
		
		Map<Integer, AdBean> adMap =  getPartnerMapAd(map, partnerID);
		if(adMap == null)
			return null;
		if(adMap != null && adID != 0 &&  adMap.containsKey(adID) && adMap.size() > 1)
			return adMap.get(adID);
		else 
			return getActiveAdID(adMap);
	}
	
	/**
	 * Generate a list of Ad campaign for sending to client
	 * @param map: server side cache of Ads
	 * @param isActive: true then client needs only active ad otherwise all ads
	 * @return: list of all ads based on client request
	 */
	public static List<AdBean> getPartnerAdInList(Map<String, Map<Integer, AdBean>> map, boolean isActive) {
		List<AdBean> list = new ArrayList<>();
		if(isActive) {
			map.forEach((k,v) -> list.addAll(v.entrySet().stream()
					.filter(e-> isActiveAd(e.getValue()))
					.map(Map.Entry::getValue)
					.collect(Collectors.toList())));
		} else
			map.forEach((k,v) -> v.forEach((a,b) -> list.add(b)));
		log.info("SimpleAd:method getPartnerAdInList " + list.toString());
		return list;
	}
	
	/**
	 * gets the ads to update server cache in map 
	 * @param jdbcModule: used to call jdbcModule
	 * @return: Map of objects
	 */
	public static Map<String, Map<Integer, AdBean>> getPartnerAdInMap(JdbcModule jdbcModule) {
		List<AdBean> list = jdbcModule.getAllAds(true);
		Map<String, Map<Integer, AdBean>> map = new HashMap<>();
		list.forEach(e -> {
			String pID = e.getPartnerId();
			Map<Integer, AdBean> adMap;
			if(map.containsKey(pID))
				adMap = map.get(pID);
			else 
				adMap = new HashMap<>();
			adMap.put(e.getAdId(), e);
			map.put(pID, adMap);
		});
		log.info("SimpleAd:method getPartnerAdInMap " + map.toString());
		return map;
	}
}
