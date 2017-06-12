package com.vsourabh.simplead.contract;

import java.util.List;

import com.vsourabh.simplead.model.AdBean;;

public interface JdbcModule {
	public abstract boolean saveAd(AdBean adBean);
	public abstract AdBean getAd(String partnerId, Integer adID);
	public abstract boolean updateAd(AdBean adBean);
	public abstract List<AdBean> getAllAds(boolean isActive);
}
