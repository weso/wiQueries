package es.weso.model;

/**
 * Defines the methods needed to serialize the data as linked data
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 12/04/2013
 */
public interface LinkedDataResource {
	/**
	 * Returns the RDF representation of the object
	 */
	public String asRDF();

	/**
	 * Returns the TTL representation of the object
	 */
	public String asTTL();
}
