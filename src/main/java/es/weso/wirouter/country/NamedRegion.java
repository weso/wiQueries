package es.weso.wirouter.country;


public class NamedRegion extends CountryExpr {
	private final String name;
	
	public NamedRegion(String name) {
		this.name = name ;
	}
	
	public String toString() {
		return "region(" + name + ")";
	}
}
