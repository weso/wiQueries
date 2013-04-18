package es.weso.wirouter.year;

import es.weso.wirouter.WIRouteException;


public class RangeYear extends YearExpr {
	private final Integer startYear;
	private final Integer endYear;

	/**
	 * Constructor of RangeYear checks that years in the range are valid
	 * @param startYear
	 * @param endYear
	 * @throws WIRouteException
	 */
	public RangeYear(Integer startYear, Integer endYear) throws WIRouteException {
		if (endYear > startYear) {
			this.startYear = startYear;
			this.endYear = endYear;
		} else
			throw new WIRouteException("Start year " + startYear + " must be before " + endYear);
	}
	
	@Override public String toString() {
		return startYear + "-" + endYear;
	}
	
}
