package es.weso.business;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;

import es.weso.data.PeopleData;
import es.weso.model.Person;

/**
 * Class to get data of a specific {@link Person}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 10/04/2013
 */
public class PeopleManagement {

	private PeopleData data;

	public PeopleManagement() {
		try {
			data = new PeopleData();
		} catch (IOException e) {
			NotYetConnectedException ex = new NotYetConnectedException();
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}

	/**
	 * Gets the data of a {@link Person}
	 * 
	 * @param nick
	 *            The nick of that {@link Person}
	 * @return The data of a {@link Person}
	 */
	public Person getPerson(String nick) {
		return data.getPerson(nick);
	}

}
