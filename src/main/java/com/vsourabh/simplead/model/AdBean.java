package com.vsourabh.simplead.model;


import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Sourabh
 * 
 * Model Class: AdBean
 *  The AdBean has properties needed to be used 
 *  in the service for storing and retirving data
 *
 */
@XmlRootElement
public class AdBean {
	String partnerId;
	int duration;
	String adContent;
	int adId;
	LocalDateTime creationTime;
	
	public AdBean() {
		this.creationTime = LocalDateTime.now();
	}
	public AdBean(String id, int time, String content) {
		this();
		this.partnerId = id;
		this.duration = time;
		this.adContent = content;
	}
	public AdBean(String id, int time, String content, int adID) {
		this(id, time, content);
		this.adId = adID;
	}
	public AdBean(String id, int time, String content, LocalDateTime dt) {
		this(id, time, content);
		this.creationTime = dt;
	}
	public AdBean(String id, int time, String content, LocalDateTime dt, int adID) {
		this(id, time, content, dt);
		this.adId = adID;
	}

	@XmlAttribute(name="creationTime")
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	public String getPartnerId() {
		return partnerId;
	}
	@XmlAttribute(name="partner_id")
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public int getDuration() {
		return duration;
	}
	@XmlAttribute(name="duration")
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getAdContent() {
		return adContent;
	}
	@XmlAttribute(name="ad_content")
	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
}
