package es.aguasnegras.jaxrs.domain.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import es.aguasnegras.jaxrs.domain.model.Customer;

public abstract class BaseCustomerService implements CustomerService {

	private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();

	private AtomicInteger idCounter = new AtomicInteger();

	public BaseCustomerService() {
		super();
	}

	@Override
	public Response createCustomer(InputStream is) {
		Customer customer = readCustomer(is);
		customer.setId(idCounter.incrementAndGet());
		customerDB.put(customer.getId(), customer);
		System.out.println("Created customer " + customer.getId());
		return Response.created(URI.create("/customers/" + customer.getId()))
				.build();
	}

	@Override
	public StreamingOutput getCustomer(int id) {
		final Customer customer = customerDB.get(id);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException,
					WebApplicationException {
				outputCustomer(outputStream, customer);
			}
		};
	}
	
	@Override
	public StreamingOutput getCustomers() {
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				outputCustomers(outputStream, customerDB.values());
			}
		};
	}

	@Override
	public void updateCustomer(int id, InputStream is) {
		Customer update = readCustomer(is);
		Customer current = customerDB.get(id);
		if (current == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		current.setFirstName(update.getFirstName());
		current.setLastName(update.getLastName());
		current.setStreet(update.getStreet());
		current.setState(update.getState());
		current.setZip(update.getZip());
		current.setCountry(update.getCountry());
	}

	protected abstract Customer readCustomer(InputStream is);

	protected abstract void outputCustomer(OutputStream os, Customer cust)
			throws IOException;
	
	protected abstract void outputCustomers(OutputStream os, Collection<Customer> custs)
			throws IOException;
}