package es.weso.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import es.weso.annotations.LinkedDataEntity;
import es.weso.annotations.LinkedDataProperty;
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
public class RDFView extends AbstractView {

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
		for (Method method : rdfObject.getClass().getMethods()) {
			LinkedDataProperty propertyAnnotation = method
					.getAnnotation(LinkedDataProperty.class);
			writeProperty(rdfObject, baos, method, propertyAnnotation);
		}
		baos.write((getTabulations(1) + "</" + type + ">\n").getBytes());
		writeInverseProperties(rdfObject, baos, uri);
	}

	/**
	 * Writes a property of the main object
	 * 
	 * @param rdfObject
	 *            The object that has the property
	 * @param baos
	 *            The output stream
	 * @param method
	 *            The method to invoke on the object to get the property
	 * @param propertyAnnotation
	 *            The annotation that tags the property
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private void writeProperty(Object rdfObject, ByteArrayOutputStream baos,
			Method method, LinkedDataProperty propertyAnnotation)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		if (propertyAnnotation != null && !propertyAnnotation.inverse()) {
			String property = method.invoke(rdfObject).toString();
			baos.write((getTabulations(2) + "<"
					+ propertyAnnotation.predicate() + ">" + property + "</"
					+ propertyAnnotation.predicate() + ">\n").getBytes());
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
				baos.write((getTabulations(1) + "<" + type + " rdf:about=\""
						+ uri + "\">\n").getBytes());
				break;
			}
		}
		return uri;
	}

	private void writeInverseProperties(Object rdfObject,
			ByteArrayOutputStream baos, String uri)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		for (Method method : rdfObject.getClass().getMethods()) {
			LinkedDataProperty propertyAnnotation = method
					.getAnnotation(LinkedDataProperty.class);
			if (propertyAnnotation != null && propertyAnnotation.inverse()) {
				Object prop = method.invoke(rdfObject);
				if (prop instanceof String || prop.getClass().isPrimitive()) {
					inversePropertyToRDF(baos, uri, prop.toString(),
							propertyAnnotation);
				} else {
					inversePropertyToRDF(baos, uri, prop, propertyAnnotation);
				}

			}
		}
	}

	private void inversePropertyToRDF(ByteArrayOutputStream baos, String uri,
			Object property, LinkedDataProperty propertyAnnotation)
			throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (property instanceof Collection) {
			@SuppressWarnings("unchecked")
			Collection<Object> properties = (Collection<Object>) property;
			for (Object obj : properties) {
				inversePropertyToRDF(baos, uri, obj, propertyAnnotation);
			}
		} else {
			for (Method m : property.getClass().getMethods()) {
				if (m.getAnnotation(LinkedDataUri.class) != null) {
					baos.write((getTabulations(1)
							+ "<rdf:Description rdf:about=\""
							+ m.invoke(property).toString() + "\">\n")
							.getBytes());
					baos.write((getTabulations(2) + "<"
							+ propertyAnnotation.predicate()
							+ " rdf:resource=\"" + uri + "\" />\n").getBytes());
					baos.write((getTabulations(1) + "</rdf:Description>\n")
							.getBytes());
				}
			}
		}
	}

	private void inversePropertyToRDF(ByteArrayOutputStream baos, String uri,
			String property, LinkedDataProperty propertyAnnotation)
			throws IOException {
		baos.write((getTabulations(1) + "<rdf:Description rdf:about=\""
				+ property + "\">\n").getBytes());
		baos.write((getTabulations(2) + "<" + propertyAnnotation.predicate()
				+ " rdf:resource=\"" + uri + "\" />\n").getBytes());
		baos.write((getTabulations(1) + "</rdf:Description>\n").getBytes());
	}

	private String getTabulations(int tabs) {
		String tabulations = "";
		for (int i = 0; i < tabs; i++) {
			tabulations += "\t";
		}
		return tabulations;
	}
}
