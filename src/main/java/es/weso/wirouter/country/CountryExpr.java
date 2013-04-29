package es.weso.wirouter.country;

import java.util.Collection;

/**
 * Abstract class of country expressions They can be: single countries, a group
 * of countries or a named region
 * 
 * @author labra
 * 
 */
public abstract class CountryExpr {

	public abstract Collection<String> getCountryCodes();
}
