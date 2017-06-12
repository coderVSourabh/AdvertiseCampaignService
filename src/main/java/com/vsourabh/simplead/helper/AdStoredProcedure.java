package com.vsourabh.simplead.helper;

import java.sql.Types;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.vsourabh.simplead.model.AdBean;

/**
 * 
 * @author Sourabh
 * AdStoredProcedure
 * This is manageed by Spring Dependency Injection
 * It is used to call the save stored procedure when new ad is created
 */

public class AdStoredProcedure {
	
	private String procName;
	static final Logger log = Logger.getLogger(AdStoredProcedure.class.getName());
	
	@Autowired
	public void setProcName(String procName) {
		this.procName = procName;
		log.info("SimpleAd:method setProcName:" + procName);
	}

	public boolean executeSaveProc(JdbcTemplate jdbcTemplate, AdBean ad) {

		boolean result = false;
		//Pass jdbcTemlate and name of the stored Procedure.
		SaveStoredProcedure savedStoredProc = new SaveStoredProcedure(jdbcTemplate, procName);

		//Sql parameter mapping
		SqlParameter partnerID = new SqlParameter("id", Types.VARCHAR);
		SqlParameter createdTime = new SqlParameter("createdTime", Types.TIMESTAMP);
		SqlParameter adMessage = new SqlParameter("adMessage", Types.VARCHAR);
		SqlParameter adTime = new SqlParameter("adTime", Types.INTEGER);
		SqlOutParameter success = new SqlOutParameter("success", Types.BOOLEAN);
		SqlOutParameter adID = new SqlOutParameter("advID", Types.INTEGER);
		SqlParameter[] paramArray = {partnerID, createdTime, adMessage, adTime, success, adID};

		savedStoredProc.setParameters(paramArray);
		savedStoredProc.compile();

		//Call stored procedure
		Map<String, Object> storedProcResult = savedStoredProc.execute(ad.getPartnerId(), ad.getCreationTime(), ad.getAdContent(), ad.getDuration());
		if(storedProcResult.containsKey("success")) 
			result = (boolean) storedProcResult.get("success");
		
		if(result)
			ad.setAdId((int)storedProcResult.get("advID"));
		
		log.info("SimpleAd:method executeSaveProc:" + result);
		
		return result;
	}
	
	private final class SaveStoredProcedure extends StoredProcedure {
		public SaveStoredProcedure(JdbcTemplate jdbcTemplate, String name) {
			super(jdbcTemplate, name);
			setFunction(false);
			log.info("SimpleAd:method SaveStoredProcedure");
		}
	}
}
