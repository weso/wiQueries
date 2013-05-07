<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>${country.name}</title>
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
<body
	onLoad='init("${country.code_iso_alpha2}", ${country.lat}, ${country.lon});'>

	<header class="row">
		<div class="large-6 columns">
			<img src="/webindex/static/web-index.png" alt="Web Index"> <a
				href="http://thewebindex.org" class="row">thewebindex.org</a>
		</div>
		<div class="large-6 columns">
			<h5 class="right">
				2012 Rank <span id="rank">${country.ranks.global.position}</span>
			</h5>
		</div>
		<hr />
	</header>

	<div class="row">
		<div class="large-6 columns">
			<table id="scores">
				<thead>
					<tr>
						<th></th>
						<th>Score</th>
						<th>Rank</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Overall Index</td>
						<td>${country.ranks.global.value}</td>
						<td>${country.ranks.global.position}</td>
					</tr>
					<tr class="subindex">
						<td>Readiness</td>
						<td>${country.ranks.readiness.value}</td>
						<td>${country.ranks.readiness.position}</td>
					</tr>
					<tr>
						<td>Communications Infrastructure</td>
						<td>${country.ranks.comunicationsInfrastructure.value}</td>
						<td>${country.ranks.comunicationsInfrastructure.position}</td>
					</tr>
					<tr>
						<td>Institutional Infrastructure</td>
						<td>${country.ranks.institutionalInfrastructure.value}</td>
						<td>${country.ranks.institutionalInfrastructure.position}</td>
					</tr>
					<tr class="subindex">
						<td>The Web</td>
						<td>${country.ranks.web.value}</td>
						<td>${country.ranks.web.position}</td>
					</tr>
					<tr>
						<td>Web Use</td>
						<td>${country.ranks.webUse.value}</td>
						<td>${country.ranks.webUse.position}</td>
					</tr>
					<tr>
						<td>Web Content</td>
						<td>${country.ranks.webContent.value}</td>
						<td>${country.ranks.webContent.position}</td>
					</tr>
					<tr class="subindex">
						<td>Impact</td>
						<td>${country.ranks.impact.value}</td>
						<td>${country.ranks.impact.position}</td>
					</tr>
					<tr>
						<td>Social Impact</td>
						<td>${country.ranks.socialImpact.value}</td>
						<td>${country.ranks.socialImpact.position}</td>
					</tr>
					<tr>
						<td>Economic Impact</td>
						<td>${country.ranks.economicImpact.value}</td>
						<td>${country.ranks.economicImpact.position}</td>
					</tr>
					<tr>
						<td>Political Impact</td>
						<td>${country.ranks.politicalImpact.value}</td>
						<td>${country.ranks.politicalImpact.position}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="large-6 columns country_name">
			<h1 id="title">${country.name}</h1>
			<img src="/webindex/static/flags/blank.gif"
				class="flag flag-${country.code_iso_alpha2}" alt="Flag" />
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
								<c:forEach items="${country.observations}" var="observation">
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
		<section class="section large-4 columns">
			<h5>Map</h5>
			<div class="content">
				<div id="world-map"></div>
			</div>
		</section>
	</div>
	<footer class="row">
		<p>
			See this information in other formats:
			<script>
			var baseUri = document.URL.replace(/\/$/,'');
			document.write('<a href="' + baseUri + '.ttl">As Turtle</a> |');
			document.write(' <a href="' + baseUri + '.rdf">As RDF/XML</a> |');
			document.write(' <a href="' + baseUri + '.json">As JSON</a> |');
			document.write(' <a href="' + baseUri + '.xml">As XML</a>');
		</script>
		</p>
	</footer>
	<script>
    	document.write('<script src=/webindex/static/foundation/js/vendor/'
    	+ ('__proto__' in {} ? 'zepto' : 'jquery')
    	+ '.js><\/script>');
	</script>
	<script type="text/javascript"
		src="/webindex/static/map/js/jquery-jvectormap-1.2.2.min.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/onregionselected.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/wescountry.js"></script>
	<script type="text/javascript"
		src="/webindex/static/map/js/world.map.js"></script>
	<script type="text/javascript" src="/webindex/static/country/script.js"></script>
	<script src="/webindex/static/foundation/js/foundation.min.js"></script>
	<script>
  		$(document).foundation();
	</script>
</body>
</html>
