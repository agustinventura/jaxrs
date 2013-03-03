package es.aguasnegras.jaxrs.domain.services;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.aguasnegras.jaxrs.domain.model.Customer;

@Path("/customer")
public class CustomerService {
	private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
	private AtomicInteger idCounter = new AtomicInteger();

	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) {
		Customer customer = readCustomer(is);
		customer.setId(idCounter.incrementAndGet());
		customerDB.put(customer.getId(), customer);
		System.out.println("Created customer " + customer.getId());
		return Response.created(URI.create("/customers/" + customer.getId()))
				.build();
	}

}
