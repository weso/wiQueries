package es.weso.data;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.hp.hpl.jena.query.ResultSet;

import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.Region;
import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

/**
 * Class to get data of a specific {@link Region} from the
 * {@link JenaMemcachedClient}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 08/05/2013
 */
public class RegionData extends AbstractDataClient {

	public RegionData() throws IOException {
		client = JenaMemcachedClient.create();
	}

	/**
	 * Gets the data of a {@link Region} in a specific year
	 * 
	 * @param label
	 *            The name of the {@link Region}
	 * @param year
	 *            The year to be checked
	 * @return The information of the {@link Region}
	 */
	public Region getRegion(String label, String year) {
		Region region = new Region();
		region.setCountries(getRegionCountries(label));
		region.setObservations(getRegionObservations(label, year));
		try {
			Country first = region.getCountries().iterator().next();
			region.setName(first.getRegionName());
			region.setUri(first.getRegionUri());
			return region;
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Region has no countries", e);
		}
	}

	/**
	 * Gets the data of all the {@link Country countries} in a region
	 * 
	 * @param regionName
	 *            The name of the region
	 * @return All the {@link Country countries} in that region
	 */
	public Collection<Country> getRegionCountries(String regionName) {
		Collection<Country> countries = new HashSet<Country>();
		ResultSet rs = client.executeQuery(Conf.getQuery("countries.region",
				regionName));
		while (rs.hasNext()) {
			countries.add(querySolutionToCountry(rs.next()));
		}
		return countries;
	}

	/**
	 * Gets the {@link Observation}s of a {@link Region} in a specific year
	 * 
	 * @param label
	 *            The name of the {@link Region}
	 * @param year
	 *            The year to be checked
	 * @return The {@link Observation}s of that {@link Region} in that year
	 */
	private Collection<Observation> getRegionObservations(String label,
			String year) {
		ResultSet rs = client.executeQuery(Conf.getQuery("region.observations",
				label, year));
		Collection<Observation> observations = new LinkedList<Observation>();
		while (rs.hasNext()) {
			observations.add(querySolutionToObservation(rs.next()));
		}
		return observations;
	}

}
