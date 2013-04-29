package es.weso.wirouter.year;

import java.util.Collection;
import java.util.Collections;


public class SingleYear extends YearExpr {
	private final Integer year;
	public SingleYear(Integer year) {
		this.year = year;
	}
	
	@Override public String toString() {
		return year.toString();
	}

	@Override
	public Collection<String> getYears() {
		return Collections.singleton(year.toString());
	}
	
}
