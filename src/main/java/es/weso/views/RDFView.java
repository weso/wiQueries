package es.weso.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

import es.weso.annotations.LinkedDataCollection;
import es.weso.annotations.LinkedDataEntity;
import es.weso.annotations.LinkedDataUri;
import es.weso.util.Conf;

/**
 * A view that returns data in RDF
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 12/04/2013
 */
public class RDFView extends AbstractLinkedDataView {

	public static final String DEFAULT_CONTENT_TYPE = "application/rdf+xml";

	public RDFView() {
		setContentType(DEFAULT_CONTENT_TYPE);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Object rdfObject = getRDFObject(model);
		LinkedDataEntity entity = rdfObject.getClass().getAnnotation(
				LinkedDataEntity.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writePrefixes(baos);
		if (entity != null) {
			entityToRdf(rdfObject, entity.type(), baos);
		}
		baos.write("</rdf:RDF>".getBytes());
		writeToResponse(response, baos);
	}

	/**
	 * Writes the prefixes to be used in the RDF
	 * 
	 * @param baos
	 *            The output stream
	 * @throws IOException
	 */
	private void writePrefixes(ByteArrayOutputStream baos) throws IOException {
		baos.write("<?xml version=\"1.0\"?>\n".getBytes());
		baos.write("<rdf:RDF".getBytes());
		for (Map.Entry<String, String> entry : Conf.getPrefixes().entrySet()) {
			baos.write(("\n\txmlns:" + entry.getKey() + "=\""
					+ entry.getValue() + "\"").getBytes());
		}
		baos.write(">\n".getBytes());
	}

	/**
	 * Gets the object to be written from the model
	 * 
	 * @param model
	 *            Combined output {@link Map} (never null), with dynamic values
	 *            taking precedence over static attributes
	 * @return The object to be written
	 */
	private Object getRDFObject(Map<String, Object> model) {
		Object rdfObject = null;
		for (Object obj : model.values()) {
			if (obj instanceof BindingResult) {
				rdfObject = ((BindingResult) obj).getTarget();
				break;
			}
		}
		return rdfObject;
	}

	/**
	 * Writes the main entity to RDF
	 * 
	 * @param rdfObject
	 *            The object to be written
	 * @param type
	 *            The type of the object to be written
	 * @param baos
	 *            The output stream
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private void entityToRdf(Object rdfObject, String type,
			ByteArrayOutputStream baos) throws IllegalAccessException,
			InvocationTargetException, IOException {
		String uri = writeUri(rdfObject, type, baos);
		if (uri == null) {
			for (Method method : rdfObject.getClass().getMethods()) {
				LinkedDataCollection colAnnotation = method
						.getAnnotation(LinkedDataCollection.class);
				if (colAnnotation != null) {
					Collection<Object> objs = (Collection<Object>) method
							.invoke(rdfObject);
					for (Object obj : objs) {
						entityToRdf(obj, type, baos);
					}
				}
			}
		} else {
			writeProperties(baos, uri);
			baos.write((getTabs(1) + "</" + type + ">\n").getBytes());
			writeInverseProperties(baos, uri);
		}
	}

	/**
	 * Write the properties of the entity
	 * 
	 * @param baos
	 *            The output stream
	 * @param uri
	 *            The uri of the entity
	 * @throws IOException
	 */
	private void writeProperties(ByteArrayOutputStream baos, String uri)
			throws IOException {
		ResultSet rs = getDirectProperties(uri);
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			baos.write((getTabs(2) + "<" + getResource(qs, "p")).getBytes());
			writeLangTag(qs, "lang", baos);
			baos.write(">".getBytes());
			baos.write(getLiteral(qs, "l").getBytes("UTF-8"));
			baos.write(("</" + getResource(qs, "p") + ">\n").getBytes());
		}
	}

	/**
	 * Write the inverse properties of the entity
	 * 
	 * @param baos
	 *            The output stream
	 * @param uri
	 *            The uri of the entity
	 * @throws IOException
	 */
	private void writeInverseProperties(ByteArrayOutputStream baos, String uri)
			throws IOException, UnsupportedEncodingException {
		ResultSet rs = getInverseProperties(uri);
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			baos.write((getTabs(1) + "<rdf:Description rdf:about=\"")
					.getBytes());
			baos.write(qs.getResource("s").toString().getBytes());
			baos.write(">\n".getBytes());
			baos.write((getTabs(2) + "<" + getResource(qs, "p")).getBytes());
			baos.write((" rdf:resource=\"" + uri + "\" />\n").getBytes());
			baos.write((getTabs(1) + "</rdf:Description>\n").getBytes());
		}
	}

	@Override
	protected void writeLangTag(QuerySolution qs, String varName,
			ByteArrayOutputStream baos) throws IOException {
		Literal l = qs.getLiteral(varName);
		if (l != null) {
			String lang = l.getString();
			if (lang != null && !lang.trim().isEmpty()) {
				baos.write((" xml:lang=\"" + lang + "\"").getBytes());
			}
		}
	}

	/**
	 * Writes the URI of an object
	 * 
	 * @param rdfObject
	 *            The object to get the URI from
	 * @param type
	 *            The type of the object
	 * @param baos
	 *            The output stream
	 * @return The URI of the object
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private String writeUri(Object rdfObject, String type,
			ByteArrayOutputStream baos) throws IllegalAccessException,
			InvocationTargetException, IOException {
		String uri = null;
		for (Method method : rdfObject.getClass().getMethods()) {
			LinkedDataUri uriAnnotation = method
					.getAnnotation(LinkedDataUri.class);
			if (uriAnnotation != null) {
				uri = method.invoke(rdfObject).toString();
				baos.write((getTabs(1) + "<" + type + " rdf:about=\"" + uri + "\">\n")
						.getBytes());
				break;
			}
		}
		return uri;
	}
}