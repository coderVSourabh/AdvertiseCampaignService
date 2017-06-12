package com.vsourabh.simplead.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.vsourabh.simplead.model.ErrorMessage;

/**
 * 
 * @author Sourabh
 * GenericExceptionMapper is used to handle the 
 * exception of Bad URL's which are not handled by our servelet
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable ex) {
		ErrorMessage message = new ErrorMessage(ex.getMessage(), 500, "ERROR Occured! Please contact Administrator.");
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).build();
	}

}
