package es.weso.wirouter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.weso.wirouter.country.CountryCode;
import es.weso.wirouter.country.CountryExpr;
import es.weso.wirouter.country.Grouping;
import es.weso.wirouter.country.NamedRegion;

public class CountryRouteParser {

	// Small grammar 
    String country = "\\w+";
    String countryList = country + "(," + country + ")*";
    String region  = "region\\((\\w+)\\)";
    String group = "group\\(("+ countryList + ")\\)";

    // Note that the order is important. If country appeared at the 
    // beginning the parser would match with country
    String comparableItem = "(" + region + "|" + group + "|" + country + ")";

    String comparableItemList = comparableItem + "(," + comparableItem + ")*" ;

	public List<CountryExpr> parseRoute(String s) {
		if (s.matches(comparableItemList)) {
			List<CountryExpr> list = new ArrayList<CountryExpr>();
			Matcher m = Pattern.compile(comparableItem).matcher(s);

			while (m.find()) {
				CountryExpr c = parseItem(m.group());
				list.add(c);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Cannot parse route");
		}
	}
	
	CountryExpr parseItem(String s) {
		CountryExpr c;
		if (s.matches(region)) {
			c = extractRegion(s);
		} else if (s.matches(group)) {
			c = extractGrouping(s);
		} else 
			c = new CountryCode(s);
		return c;
	}
	
    NamedRegion extractRegion(String s) {
        Matcher m = Pattern.compile(region).matcher(s);
        m.find();
        String name = m.group(1);
    	return new NamedRegion(name);
    }

    List<CountryCode> extractCountries(String s) {
    	List<CountryCode> cs = new ArrayList<CountryCode>();
        Matcher m = Pattern.compile(country).matcher(s);
    	while (m.find()) {
    		cs.add(new CountryCode(m.group()));
    	}
    	return cs;
    }
    
    Grouping extractGrouping(String s) {
    	Matcher m = Pattern.compile(group).matcher(s);
    	m.find();
    	String countries = m.group(1);
        List<CountryCode> cs = extractCountries(countries);  
        return new Grouping(cs);
    }

}
