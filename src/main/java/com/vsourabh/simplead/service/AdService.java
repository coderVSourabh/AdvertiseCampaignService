package com.vsourabh.simplead.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.vsourabh.simplead.contract.AdContract;
import com.vsourabh.simplead.contract.JdbcModule;
import com.vsourabh.simplead.helper.AdHelper;
import com.vsourabh.simplead.model.AdBean;

/**
 * 
 * @author Sourabh
 * AdService Implements the Bussiness logic for AdRequest
 * The Class is injected by Spring Inversion of Control.
 * This has logics like add, get, update Advertise Campaigns 
 *
 */
public class AdService implements AdContract {
	
	Map<String, Map<Integer, AdBean>> activeAdMap = new HashMap<>();
	JdbcModule jdbcModule;
	static final Logger log = Logger.getLogger(AdService.class.getName());
	
	@Autowired
	public void setJdbcModule(JdbcModule jdbcModule) {
		this.jdbcModule = jdbcModule;
		log.info("SimpleAd:method setJdbcModule");
	}

	@Override
	public boolean saveAd(AdBean ad) {
		String id = ad.getPartnerId();
		if(activeAdMap == null || activeAdMap.isEmpty())
			this.getAllActiveAd();
		
		if(activeAdMap.containsKey(id) && AdHelper.isActiveAdInMap((activeAdMap.get(id))))
			return false;
		
		boolean result = jdbcModule.saveAd(ad);
		if(result)
			AdHelper.addAd(activeAdMap, ad);
		log.info("SimpleAd:method saveAd: " + result);
		return result;
	}

	@Override
	public AdBean getAd(String partnerID, Integer adID) {
		AdBean ad = null;
		if (!AdHelper.isEmptyMap(activeAdMap))
			ad = AdHelper.getAd(activeAdMap, partnerID, adID);
		if(ad == null)
			ad = jdbcModule.getAd(partnerID, adID);
		log.info("SimpleAd:method getAd: " + ad);
		return ad;
	}
	
	@Override
	public boolean updateAd(AdBean ad) {
		AdHelper.addAd(activeAdMap, ad);
		jdbcModule.updateAd(ad);
		log.info("SimpleAd:method updateAd ");
		return true;
	}

	@Override
	public List<AdBean> getAllAd() {
		log.info("SimpleAd:method getAllAd ");
		return jdbcModule.getAllAds(false);
	}

	@Override
	public List<AdBean> getAllActiveAd() {
		log.info("SimpleAd:method getAllActiveAd ");
		this.activeAdMap = AdHelper.getPartnerAdInMap(jdbcModule);
		return AdHelper.getPartnerAdInList(this.activeAdMap, true);
	}
	

}