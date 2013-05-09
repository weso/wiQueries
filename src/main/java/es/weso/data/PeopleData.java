package es.weso.data;

import java.io.IOException;
import java.util.NoSuchElementException;

import es.weso.model.Person;
import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

/**
 * Class to get data of a specific {@link Person} from the
 * {@link JenaMemcachedClient}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 27/03/2013
 */
public class PeopleData extends AbstractDataClient {

	public PeopleData() throws IOException {
		client = JenaMemcachedClient.create();
	}

	/**
	 * Gets the data of a {@link Person}
	 * 
	 * @param nick
	 *            The nick of that {@link Person}
	 * @return The data of a {@link Person}
	 */
	public Person getPerson(String nick) {
		try {
			return querySolutionToPerson(client.executeQuery(
					Conf.getQuery("person", nick)).next());
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Person has no data", e);
		}
	}
}
