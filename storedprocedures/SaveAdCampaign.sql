/**
* This is a stored procedure to store the AdCampaign in table
* and return the adID which is unique and a success flag
*
*/
DELIMITER $$
DROP PROCEDURE IF EXISTS SaveAdCampaign $$
CREATE PROCEDURE SaveAdCampaign(IN id VARCHAR(250), IN createdTime TIMESTAMP,
			IN adMessage VARCHAR(250), IN adTime INT, OUT success BOOL, OUT advID INT)
	BEGIN

	DECLARE myvar INT;
	DECLARE oldCreationTime TIMESTAMP;
	DECLARE durationDiff INT;
    SET durationDiff = -1;
    SET success = FALSE;

    SELECT COUNT(*) INTO myvar FROM AdPartners WHERE partnerID=id;
    
    IF myvar = 1 THEN
        SELECT COUNT(*) INTO myvar FROM AdContent WHERE adPartnerID = id AND isActive = 0;
        IF myvar = 1 THEN
        SELECT TIMESTAMPADD(SECOND, duration, creationTime) INTO oldCreationTime 
					FROM AdContent WHERE adPartnerID = id AND isActive = 0;
		SELECT TIMESTAMPDIFF(SECOND, CURRENT_TIMESTAMP, oldCreationTime) INTO durationDiff;
		END IF;
        IF durationDiff <=0 THEN
			UPDATE AdContent SET isActive = 1 WHERE adPartnerID = id;
            INSERT INTO AdContent (adPartnerID, creationTime, duration, isActive, adContent)
             VALUES (id, createdTime, adTime, 0, adMessage);
			SET success = TRUE;
            SET advID = LAST_INSERT_ID();
	    END IF;
	ELSE
		INSERT INTO AdPartners VALUES (id);
        INSERT INTO AdContent (adPartnerID, creationTime, duration, isActive, adContent)
             VALUES (id, createdTime, adTime, 0, adMessage);
		SET success = TRUE;
        SET advID = LAST_INSERT_ID();
    END IF;
    
	END $$
DELIMITER ;
