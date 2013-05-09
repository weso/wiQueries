<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>${region.name}</title>
<link rel="stylesheet" type="text/css"
	href="/webindex/static/foundation/css/foundation.min.css" />
<link rel="stylesheet" type="text/css"
	href="/webindex/static/foundation/css/app.css" />
<link rel="stylesheet" type="text/css"
	href="/webindex/static/flags/flags.css" />

<script type="text/javascript"
	src="/webindex/static/foundation/js/vendor/custom.modernizr.js"></script>
<script type="text/javascript"
	src="/webindex/static/foundation/js/vendor/jquery.js"></script>
</head>
<body>

	<header class="row">
		<div class="large-6 columns">
			<img src="/webindex/static/web-index.png" alt="Web Index"> <a
				href="http://thewebindex.org" class="row">thewebindex.org</a>
		</div>
		<hr />
	</header>

	<div class="row">
		<section class="section large-6 columns">
			<h5>Map</h5>
			<div class="content">
				<div id="world-map"></div>
			</div>
		</section>
		<div class="large-6 columns country_name">
			<h1 id="title">${region.name}</h1>
			<img src="/webindex/static/flags/blank.gif"
				class="flag flag-${region.name}" alt="Flag" />
		</div>
	</div>
	<div class="row">
		<section class="section large-8 columns">
			<div class="section-container accordion" data-section="accordion">
				<section>
					<p class="title" data-section-title>
						<a href="#">Observations</a>
					</p>
					<div class="content" data-section-content>
						<table class="description">
							<thead>
								<tr>
									<th>Observation</th>
									<th>Value</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${region.observations}" var="observation">
									<tr>
										<td class="property"><a class="uri"
											href="${observation.indicatorUri}">${observation.indicatorName}</a>:
											${observation.label}</td>
										<td>${observation.value}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</section>
			</div>
		</section>
	</div>
	<footer class="row">
		<p>
			See this information in other formats:
			<script>
				var baseUri = document.URL.replace(/\/$/, '');
				document.write('<a href="' + baseUri + '.ttl">As Turtle</a> |');
				document
						.write(' <a href="' + baseUri + '.rdf">As RDF/XML</a> |');
				document.write(' <a href="' + baseUri + '.json">As JSON</a> |');
				document.write(' <a href="' + baseUri + '.xml">As XML</a>');
			</script>
		</p>
	</footer>
	<script>
		document.write('<script src=/webindex/static/foundation/js/vendor/'
				+ ('__proto__' in {} ? 'zepto' : 'jquery') + '.js><\/script>');
	</script>
	<script type="text/javascript"
		src="/webindex/static/map/js/jquery-jvectormap-1.2.2.min.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/onregionselected.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/wescountry.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/world.map.js"></script>
	<script type="text/javascript" src="/webindex/static/region/script.js"></script>
	<script src="/webindex/static/foundation/js/foundation.min.js"></script>
	<script>
		$(document).foundation();
	</script>
	<script>
		$(document).ready(function() {
		var countryCodes = new Array();
		<c:forEach items="${region.countries}" var="country">
			countryCodes.push('${country.code_iso_alpha2}');
		</c:forEach>
		init(countryCodes);
		});
	</script>
</body>
</html>
