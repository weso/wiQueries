package es.weso.wirouter.country;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;

public class Grouping extends CountryExpr {
	private final List<CountryCode> countries;

	public Grouping(List<CountryCode> countries) {
		this.countries = countries;
	}

	public String toString() {
		return "group(" + countries.toString() + ")";
	}

	@Override
	public Collection<String> getCountryCodes() {
		Collection<String> codes = new ArrayDeque<String>(countries.size());
		for (CountryCode code : countries) {
			codes.addAll(code.getCountryCodes());
		}
		return codes;
	}
}
