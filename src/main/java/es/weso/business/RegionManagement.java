package es.weso.business;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.Collection;

import es.weso.data.RegionData;
import es.weso.model.Country;
import es.weso.model.Region;

/**
 * Class to get data of a specific {@link Region}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 08/05/2013
 */
public class RegionManagement {

	private RegionData data;

	public RegionManagement() {
		try {
			data = new RegionData();
		} catch (IOException e) {
			NotYetConnectedException ex = new NotYetConnectedException();
			ex.setStackTrace(e.getStackTrace());
			throw ex;
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
		return data.getRegionCountries(regionName);
	}

	/**
	 * Gets the data of a {@link Region} in a specific year
	 * 
	 * @param regionName
	 *            The name of the {@link Region}
	 * @param year
	 *            The year to be checked
	 * @return The information of the {@link Region}
	 */
	public Region getRegion(String regionName, String year) {
		return data.getRegion(regionName, year);
	}

}
