package es.weso.data;

import java.io.IOException;
import java.util.NoSuchElementException;

import es.weso.model.Organization;
import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

/**
 * Class to get data of a specific {@link Organization} from the
 * {@link JenaMemcachedClient}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class OrganizationData extends AbstractDataClient {

	public OrganizationData() throws IOException {
		client = JenaMemcachedClient.create();
	}

	/**
	 * Gets data of an {@link Organization}
	 * 
	 * @param identifier
	 *            The identifier of the {@link Organization}
	 * @return The data of an {@link Organization}
	 */
	public Organization getOrganization(String identifier) {
		try {
			return querySolutionToOrganization(client.executeQuery(
					Conf.getQuery("organization", identifier, identifier))
					.next());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Organization has no data", e);
		}
	}
}
