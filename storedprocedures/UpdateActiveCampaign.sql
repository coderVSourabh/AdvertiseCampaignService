DELIMITER $$
DROP PROCEDURE IF EXISTS UpdateActiveCampaign $$
/**
* This is a stored procedure to update the active AdCampaign in table
* i.e. if Ad duration is over then it mark it as in active
*
*/
CREATE PROCEDURE UpdateActiveCampaign()
BEGIN
	DECLARE myvar INT;
    DECLARE tempVar INT;
    DECLARE id VARCHAR(250);
    DECLARE oldCreationTime TIMESTAMP;
	DECLARE durationDiff INT;

	SELECT COUNT(*) INTO myvar FROM AdPartners;
    WHILE myvar > 0 DO
		SET myvar = myvar - 1;
		SELECT partnerID INTO id FROM AdPartners LIMIT myvar,1;
        SELECT COUNT(*) INTO tempVar FROM AdContent WHERE adPartnerID = id AND isActive = 0;
        IF tempVar = 1 THEN
			SELECT TIMESTAMPADD(SECOND, duration, creationTime) INTO oldCreationTime 
						FROM AdContent WHERE adPartnerID = id AND isActive = 0;
			SELECT TIMESTAMPDIFF(SECOND, CURRENT_TIMESTAMP, oldCreationTime) INTO durationDiff;
			IF durationDiff <=0 THEN
				UPDATE AdContent SET isActive = 1 WHERE adPartnerID = id;
				COMMIT;
			END IF;
		END IF;
    END WHILE;
END $$
DELIMITER ;