/**
* This is a even trigger to update the active AdCampaign in table
* i.e. if Ad duration is over then it mark it as in active
* the event calls UpdateActiveCampaign stored procedure
*
*/
DELIMITER $$
DROP EVENT IF EXISTS updateAdValidityEvent $$
CREATE EVENT updateAdValidityEvent
    ON SCHEDULE EVERY 20 SECOND
    DO
      CALL UpdateActiveCampaign();
DELIMITER ;
      