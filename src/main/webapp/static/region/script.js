function init(countryCodes) {
	console.log(countryCodes);
    drawWorldMap('#world-map', regionSelectedFunction);
    createGroup("#11CC11" , countryCodes);
    centerMapOnRegions(map , countryCodes);
}