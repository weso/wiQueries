package es.weso.data;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import es.weso.model.Country;
import es.weso.model.Observation;
import es.weso.model.Organization;
import es.weso.model.Person;
import es.weso.util.JenaMemcachedClient;

/**
 * Abstract classClass with utility methods to get data from the
 * {@link JenaMemcachedClient}
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 08/05/2013
 */
public abstract class AbstractDataClient {

	protected JenaMemcachedClient client;

	/**
	 * Converts a {@link QuerySolution} into an {@link Observation}
	 * 
	 * @param qs
	 *            The {@link QuerySolution} to be converted
	 * @return The resulting {@link Observation}
	 */
	protected Observation querySolutionToObservation(QuerySolution qs) {
		Observation obs = new Observation();
		obs.setAreaName(getString(qs.getLiteral("name")));
		obs.setAreaUri(getUri(qs.getResource("area")));
		obs.setIndicatorName(getString(qs.getLiteral("indicatorLabel")));
		obs.setIndicatorUri(getUri(qs.getResource("indicator")));
		obs.setValue(getDouble(qs.getLiteral("value")));
		obs.setYear(getString(qs.getLiteral("year")));
		obs.setUri(getUri(qs.getResource("obs")));
		obs.setLabel(getString(qs.getLiteral("label")));
		return obs;
	}

	/**
	 * Converts a {@link QuerySolution} into a {@link Country}
	 * 
	 * @param qs
	 *            The {@link QuerySolution} to be converted
	 * @return The resulting {@link Country}
	 */
	protected Country querySolutionToCountry(QuerySolution qs) {
		Country country = new Country();
		country.setCode_iso_alpha3(getString(qs.getLiteral("isoCode3")));
		country.setCode_iso_alpha2(getString(qs.getLiteral("isoCode2")));
		country.setName(getString(qs.getLiteral("name")));
		country.setUri(getUri(qs.getResource("country")));
		country.setLat(getDouble(qs.getLiteral("lat")));
		country.setLon(getDouble(qs.getLiteral("lon")));
		country.setRegionName(getString(qs.getLiteral("regionName")));
		country.setRegionUri(getUri(qs.getResource("region")));
		return country;
	}

	/**
	 * Converts a {@link QuerySolution} into a {@link Person}
	 * 
	 * @param qs
	 *            The {@link QuerySolution} to be converted
	 * @return The resulting {@link Person}
	 */
	protected Person querySolutionToPerson(QuerySolution qs) {
		Person person = new Person();
		person.setUri(getUri(qs.getResource("person")));
		person.setFamilyName(getString(qs.getLiteral("familyName")));
		person.setGivenName(getString(qs.getLiteral("givenName")));
		person.setImage(getUri(qs.getResource("image")));
		person.setName(getString(qs.getLiteral("name")));
		person.setTitle(getString(qs.getLiteral("title")));
		person.setWebpage(getUri(qs.getResource("webPage")));
		return person;
	}

	/**
	 * Converts a {@link QuerySolution} into an {@link Organization}
	 * 
	 * @param qs
	 *            The {@link QuerySolution} to be converted
	 * @return The resulting {@link Organization}
	 */
	protected Organization querySolutionToOrganization(QuerySolution qs) {
		Organization organization = new Organization();
		organization.setName(getString(qs.getLiteral("name")));
		organization.setUri(getUri(qs.getResource("organization")));
		organization.setWebpage(getUri(qs.getResource("webpage")));
		return organization;
	}

	/**
	 * Gets an URI from a {@link Resource}
	 * 
	 * @param res
	 *            The {@link Resource} to get the URI from
	 * @return The URI from the {@link Resource}
	 */
	private String getUri(Resource res) {
		return res == null ? "" : res.getURI();
	}

	/**
	 * Gets a string from a {@link Literal}
	 * 
	 * @param lit
	 *            The {@link Literal} to get the string from
	 * @return The string from the {@link Literal}
	 */
	private String getString(Literal lit) {
		return lit == null ? "" : lit.getString();
	}

	/**
	 * Gets a double from a {@link Literal}
	 * 
	 * @param lit
	 *            The {@link Literal} to get the double from
	 * @return The double from the {@link Literal}
	 */
	private Double getDouble(Literal lit) {
		return lit == null ? null : lit.getDouble();
	}

}
