package es.weso.wirouter.country;

import java.util.ArrayDeque;
import java.util.Collection;

import es.weso.business.RegionManagement;
import es.weso.model.Country;

public class NamedRegion extends CountryExpr {
	private final String name;

	public NamedRegion(String name) {
		this.name = name;
	}

	public String toString() {
		return "region(" + name + ")";
	}

	@Override
	public Collection<String> getCountryCodes() {
		Collection<Country> countries = new RegionManagement()
				.getRegionCountries(name);
		Collection<String> codes = new ArrayDeque<String>(countries.size());
		for (Country country : countries) {
			codes.add(country.getCode_iso_alpha2());
		}
		return codes;
	}
}
