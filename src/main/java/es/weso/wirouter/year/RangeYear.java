package es.weso.wirouter.year;

import java.util.ArrayDeque;
import java.util.Collection;

public class RangeYear extends YearExpr {
	private final Integer startYear;
	private final Integer endYear;

	/**
	 * Constructor of RangeYear checks that years in the range are valid
	 * 
	 * @param startYear
	 * @param endYear
	 * @throws WIRouteException
	 */
	public RangeYear(Integer startYear, Integer endYear) {
		if (endYear > startYear) {
			this.startYear = startYear;
			this.endYear = endYear;
		} else {
			throw new IllegalArgumentException("Start year " + startYear
					+ " must be before " + endYear);
		}
	}

	@Override
	public String toString() {
		return startYear + "-" + endYear;
	}

	@Override
	public Collection<String> getYears() {
		Collection<String> years = new ArrayDeque<String>(endYear - startYear
				+ 1);
		for(Integer i = startYear; i <= endYear; i++) {
			years.add(i.toString());
		}
		return years;
	}

}
