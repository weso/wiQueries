<%@ page session="false"%>
<html>
<head>
<title>Ranks</title>
<link rel="stylesheet" type="text/css"
	href="/webindex/static/foundation/css/foundation.min.css" />
<link rel="stylesheet" type="text/css"
	href="/webindex/static/foundation/css/app.css" />

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
		<div class="large-6 columns">
			<h3 class="right">Ranks</h3>
		</div>
		<hr />
	</header>
	<div class="row">
		<div class="large-12 columns">
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
						<td>${rankMap.data.global.value}</td>
						<td>${rankMap.data.global.position}</td>
					</tr>
					<tr class="subindex">
						<td>Readiness</td>
						<td>${rankMap.data.readiness.value}</td>
						<td>${rankMap.data.readiness.position}</td>
					</tr>
					<tr>
						<td>Communications Infrastructure</td>
						<td>${rankMap.data.comunicationsInfrastructure.value}</td>
						<td>${rankMap.data.comunicationsInfrastructure.position}</td>
					</tr>
					<tr>
						<td>Institutional Infrastructure</td>
						<td>${rankMap.data.institutionalInfrastructure.value}</td>
						<td>${rankMap.data.institutionalInfrastructure.position}</td>
					</tr>
					<tr class="subindex">
						<td>The Web</td>
						<td>${rankMap.data.web.value}</td>
						<td>${rankMap.data.web.position}</td>
					</tr>
					<tr>
						<td>Web Use</td>
						<td>${rankMap.data.webUse.value}</td>
						<td>${rankMap.data.webUse.position}</td>
					</tr>
					<tr>
						<td>Web Content</td>
						<td>${rankMap.data.webContent.value}</td>
						<td>${rankMap.data.webContent.position}</td>
					</tr>
					<tr class="subindex">
						<td>Impact</td>
						<td>${rankMap.data.impact.value}</td>
						<td>${rankMap.data.impact.position}</td>
					</tr>
					<tr>
						<td>Social Impact</td>
						<td>${rankMap.data.socialImpact.value}</td>
						<td>${rankMap.data.socialImpact.position}</td>
					</tr>
					<tr>
						<td>Economic Impact</td>
						<td>${rankMap.data.economicImpact.value}</td>
						<td>${rankMap.data.economicImpact.position}</td>
					</tr>
					<tr>
						<td>Political Impact</td>
						<td>${rankMap.data.politicalImpact.value}</td>
						<td>${rankMap.data.politicalImpact.position}</td>
					</tr>
				</tbody>
			</table>
		</div>
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
	<script src="/webindex/static/foundation/js/foundation.min.js"></script>
	<script>
		$(document).foundation();
	</script>
</body>
</html>
