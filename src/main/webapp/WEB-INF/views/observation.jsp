<%@ page session="false"%>
<html>
<head>
<title>Observation</title>
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
			<h3 class="right">Observation</h3>
		</div>
		<hr />
	</header>
	<div class="row">
		<section class="section large-12 columns">
			${observation.label} (<a class="uri"
				href="${observation.indicatorUri}">${observation.indicatorName}</a>
			at <a class="uri" href="${observation.areaUri}">${observation.areaName}</a>):
			<strong>${observation.value}</strong>
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
	<script src="/webindex/static/foundation/js/foundation.min.js"></script>
	<script>
		$(document).foundation();
	</script>
</body>
</html>
