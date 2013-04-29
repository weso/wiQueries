package es.weso.wirouter.year;

import java.util.Collection;

/**
 * Abstract class of year expressions
 * They can be: single years or a range
 * @author labra
 *
 */
public abstract class YearExpr {
	
	public abstract Collection<String> getYears();

}
