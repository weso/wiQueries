package es.weso.business;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;

import es.weso.data.OrganizationData;
import es.weso.model.Organization;

/**
 * Class to get data of a specific {@link Organization}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 09/05/2013
 */
public class OrganizationManagement {

	private OrganizationData data;

	public OrganizationManagement() {
		try {
			data = new OrganizationData();
		} catch (IOException e) {
			NotYetConnectedException ex = new NotYetConnectedException();
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}

	/**
	 * Gets the data of a {@link Organization}
	 * 
	 * @param identifier
	 *            The identifier of that {@link Organization}
	 * @return The data of a {@link Organization}
	 */
	public Organization getOrganization(String identifier) {
		return data.getOrganization(identifier);
	}

}
