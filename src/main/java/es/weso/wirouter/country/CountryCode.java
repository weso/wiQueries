package es.weso.wirouter.country;

import java.util.Collection;
import java.util.Collections;

public class CountryCode extends CountryExpr {
	private final String code;

	public CountryCode(String code) {
		this.code = code;
	}

	public String toString() {
		return code;
	}

	@Override
	public Collection<String> getCountryCodes() {
		return Collections.singleton(code);
	}

}
