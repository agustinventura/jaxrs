package es.aguasnegras.jaxrs.domain.services;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public interface CustomerService {

	@POST
	@Consumes("application/xml")
	public abstract Response createCustomer(InputStream is);

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public abstract StreamingOutput getCustomer(@PathParam("id") int id);

	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public abstract void updateCustomer(@PathParam("id") int id, InputStream is);

}