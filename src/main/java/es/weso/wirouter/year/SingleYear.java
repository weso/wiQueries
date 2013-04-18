package es.weso.wirouter.year;


public class SingleYear extends YearExpr {
	private final Integer year;
	public SingleYear(Integer year) {
		this.year = year;
	}
	
	@Override public String toString() {
		return year.toString();
	}
	
}
