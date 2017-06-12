package com.vsourabh.simplead.resources;

import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.vsourabh.simplead.contract.AdContract;
import com.vsourabh.simplead.exception.NoDataFoundException;
import com.vsourabh.simplead.helper.AdAppContextBuilder;
import com.vsourabh.simplead.helper.AdHelper;
import com.vsourabh.simplead.model.AdBean;
/**
 * @author Sourabh
 * AdResource handles URL 
 * request for Ads 
 */

@Path("/ad")
public class AdResource {
	
	private static AdContract adService = getAdService();
	static final Logger log = Logger.getLogger(AdResource.class.getName());
	
	private static AdContract getAdService() {
		if(adService == null) {
			AdResource.adService = AdAppContextBuilder.getApplicationContext().getBean("adService", AdContract.class);
		}
		return adService;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdvertise(AdBean ad, @Context UriInfo uriInfo) 
			throws URISyntaxException {
		
		boolean adCreated = adService.saveAd(ad);
		log.info("SimpleAd:method addAdvertise: " + adCreated);
		
		if(adCreated)
			return Response.status(Status.CREATED).entity(ad).build();
		else 
			return AdHelper.badRequestResponse(ad.getPartnerId(), " Advertise POST failed",
					"Update the existing Advertise by doing PUT request, Please contact Administrator for details.");
	}

	@GET
	@Path("{partner_id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	public Response getPartnerAd(@PathParam("partner_id")  String partnerID) {
		
		AdBean ad = adService.getAd(partnerID, 0);
		
		log.info("SimpleAd:method getPartnerAd" );
		
		if(ad != null && AdHelper.isActiveAd(ad))
			return Response.status(Status.OK).entity(ad).build();
		else
			throw new NoDataFoundException("Partner_Id: " + partnerID +" has No Advertise Campaign. ");
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	public List<AdBean> getAllAd() {
		
		List<AdBean> listAd = adService.getAllAd();
		
		log.info("SimpleAd:method getAllAd" );
		
		if(listAd == null || listAd.isEmpty())
			throw new NoDataFoundException(" No Advertise Campaign available. ");
		return listAd;
	}
	
	@GET
	@Path("/active")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	public List<AdBean> getAllActiveAd() {
		
		log.info("SimpleAd:method getAllActiveAd" );
		
		List<AdBean> listAd = adService.getAllActiveAd();
		
		if(listAd == null || listAd.isEmpty())
			throw new NoDataFoundException(" No active Advertise Campaign available. ");
		return listAd;
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAdvertise(AdBean ad, @Context UriInfo uriInfo) 
			throws URISyntaxException {
		
		log.info("SimpleAd:method updateAdvertise" );
		
		boolean adCreated = adService.updateAd(ad);
		
		if(adCreated)
			return Response.status(Status.OK).entity(ad).build();
		else {
			return AdHelper.badRequestResponse(ad.getPartnerId(), " Advertise Update failed",
					"Update of Advertise for the requested partner Failed, Please contact Administrator.");
		}
	}
}