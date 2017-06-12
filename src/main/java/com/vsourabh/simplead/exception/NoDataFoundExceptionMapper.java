package com.vsourabh.simplead.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.vsourabh.simplead.model.ErrorMessage;

@Provider
public class NoDataFoundExceptionMapper implements ExceptionMapper<NoDataFoundException>{

	public NoDataFoundExceptionMapper() {
	}

	@Override
	public Response toResponse(NoDataFoundException ex) {
		ErrorMessage message = new ErrorMessage(ex.getMessage(), 404, 
				"No Advertise campaign available for the partner, Please contact Administrator");
		return Response.status(Status.NOT_FOUND).entity(message).build();
	}

}
