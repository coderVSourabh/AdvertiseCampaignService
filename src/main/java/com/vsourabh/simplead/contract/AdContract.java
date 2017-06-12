package com.vsourabh.simplead.contract;

import java.util.List;

import com.vsourabh.simplead.model.AdBean;

public interface AdContract {
	public abstract boolean saveAd(AdBean ad);
	public abstract AdBean getAd(String partnerID, Integer adID);
	public boolean updateAd(AdBean ad);
	public List<AdBean> getAllAd();
	public List<AdBean> getAllActiveAd();
}
