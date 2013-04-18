package es.weso.wirouter.country;


public class CountryCode extends CountryExpr {
	private final String code;
	public CountryCode(String code) {
		this.code = code;
	}
	
	public String toString() {
		return code;
	}
	
}
