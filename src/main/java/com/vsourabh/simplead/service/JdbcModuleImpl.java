package com.vsourabh.simplead.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.vsourabh.simplead.contract.JdbcModule;
import com.vsourabh.simplead.helper.AdStoredProcedure;
import com.vsourabh.simplead.model.AdBean;

/**
 * @author Sourabh
 * This Class is used for Database management for Advertisements.
 * This is also created using Spring dependency management.
 * This is used for calling stored procedure and direct query for
 *  updating saving and getting the Advertise campaigns
 * 
 */
public class JdbcModuleImpl implements JdbcModule {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private AdStoredProcedure adStoredProcedure;
	
	static final Logger log = Logger.getLogger(JdbcModuleImpl.class.getName());
	
	public DataSource getDataSource() {
		return dataSource;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		log.info("SimpleAd:method setDataSource");
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	public void setAdStoredProcedure(AdStoredProcedure adStoredProcedure) {
		log.info("SimpleAd:method setAdStoredProcedure");
		this.adStoredProcedure = adStoredProcedure;
	}

	@Override
	public boolean saveAd(AdBean adBean) {
		log.info("SimpleAd:method saveAd");
		return adStoredProcedure.executeSaveProc(jdbcTemplate, adBean);
	}

	@Override
	public AdBean getAd(String partnerId, Integer adID) {
		log.info("SimpleAd:method getAd");
		String sqlQuery = "SELECT * FROM AdContent WHERE  adPartnerID = ? and isActive = ?";
		List<AdBean> list = jdbcTemplate.query(sqlQuery, new Object[] {partnerId, 0}, new AdContentRowMapper());
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public boolean updateAd(AdBean adBean) {
		log.info("SimpleAd:method updateAd");
		String sqlQuery = "UPDATE AdContent SET adContent = ? WHERE adPartnerID = ?";
		int val = jdbcTemplate.update(sqlQuery, new Object[] {adBean.getAdContent(), adBean.getPartnerId()}, new int[] {Types.VARCHAR, Types.INTEGER});
		if (val == 0)
			return false;
		else
			return true;
	}

	@Override
	public List<AdBean> getAllAds(boolean isActive) {
		log.info("SimpleAd:method getAllAds");
		String sqlQuery;
		if(isActive) {
			sqlQuery =  "SELECT * FROM AdContent WHERE isActive = ?";
			return jdbcTemplate.query(sqlQuery, new Object[] {0}, new AdContentRowMapper());
		} else {
			sqlQuery =  "SELECT * FROM AdContent";
			return jdbcTemplate.query(sqlQuery, new AdContentRowMapper());
		}
	}
	
	private static final class AdContentRowMapper implements RowMapper<AdBean> {
		public AdBean mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			return new AdBean(resultSet.getString("adPartnerID"), resultSet.getInt("duration"), 
					resultSet.getString("adContent"), resultSet.getTimestamp("creationTime").toLocalDateTime());
		}
	}
}
