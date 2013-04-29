package es.weso.wirouter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.weso.wirouter.year.RangeYear;
import es.weso.wirouter.year.SingleYear;
import es.weso.wirouter.year.YearExpr;

public class YearRouteParser {

	// Small grammar
	String year = "\\d+";
	String range = "range\\(((\\d+)-(\\d+))\\)";

	String yearExpr = "(" + range + "|" + year + ")";

	String yearExprList = yearExpr + "(," + yearExpr + ")*";

	public List<YearExpr> parseRoute(String s) {
		if (s.matches(yearExprList)) {
			List<YearExpr> list = new ArrayList<YearExpr>();
			Matcher m = Pattern.compile(yearExpr).matcher(s);

			while (m.find()) {
				YearExpr c = parseYearExpr(m.group());
				list.add(c);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Cannot parse year expression: "
					+ s);
		}
	}

	YearExpr parseYearExpr(String s) {
		YearExpr e;
		if (s.matches(range))
			e = extractRange(s);
		else
			e = extractYear(s);
		return e;
	}

	RangeYear extractRange(String s) {
		Matcher m = Pattern.compile(range).matcher(s);
		m.find();
		Integer startYear = Integer.parseInt(m.group(2));
		Integer endYear = Integer.parseInt(m.group(3));
		return new RangeYear(startYear, endYear);
	}

	SingleYear extractYear(String s) {
		Integer year = Integer.parseInt(s);
		return new SingleYear(year);
	}

}
