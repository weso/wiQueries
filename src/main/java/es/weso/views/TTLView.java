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
 * A view that returns data in TTL
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 16/04/2013
 */
public class TTLView extends AbstractView {

	public static final String DEFAULT_CONTENT_TYPE = "text/turtle";

	public TTLView() {
		setContentType(DEFAULT_CONTENT_TYPE);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Object ttlObject = getTTLObject(model);
		LinkedDataEntity entity = ttlObject.getClass().getAnnotation(
				LinkedDataEntity.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writePrefixes(baos);
		if (entity != null) {
			entityToTTL(ttlObject, entity.type(), baos);
		}
		writeToResponse(response, baos);
	}

	/**
	 * Writes the prefixes to be used in the TTL
	 * 
	 * @param baos
	 *            The output stream
	 * @throws IOException
	 */
	private void writePrefixes(ByteArrayOutputStream baos) throws IOException {
		for (Map.Entry<String, String> entry : Conf.getPrefixes().entrySet()) {
			baos.write(("@prefix " + entry.getKey() + ":\t<" + entry.getValue() + "> .\n")
					.getBytes());
		}
		baos.write("\n".getBytes());
	}

	/**
	 * Gets the object to be written from the model
	 * 
	 * @param model
	 *            Combined output {@link Map} (never null), with dynamic values
	 *            taking precedence over static attributes
	 * @return The object to be written
	 */
	private Object getTTLObject(Map<String, Object> model) {
		Object TTLObject = null;
		for (Object obj : model.values()) {
			if (obj instanceof BindingResult) {
				TTLObject = ((BindingResult) obj).getTarget();
				break;
			}
		}
		return TTLObject;
	}

	/**
	 * Writes the main entity to TTL
	 * 
	 * @param TTLObject
	 *            The object to be written
	 * @param type
	 *            The type of the object to be written
	 * @param baos
	 *            The output stream
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private void entityToTTL(Object TTLObject, String type,
			ByteArrayOutputStream baos) throws IllegalAccessException,
			InvocationTargetException, IOException {
		String uri = writeUri(TTLObject, type, baos);
		for (Method method : TTLObject.getClass().getMethods()) {
			LinkedDataProperty propertyAnnotation = method
					.getAnnotation(LinkedDataProperty.class);
			writeProperty(TTLObject, baos, method, propertyAnnotation);
		}
		baos.write(" .\n".getBytes());
		writeInverseProperties(TTLObject, baos, uri);
	}

	/**
	 * Writes a property of the main object
	 * 
	 * @param TTLObject
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
	private void writeProperty(Object TTLObject, ByteArrayOutputStream baos,
			Method method, LinkedDataProperty propertyAnnotation)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		if (propertyAnnotation != null && !propertyAnnotation.inverse()) {
			String property = method.invoke(TTLObject).toString();
			baos.write((" ;\n\t" + propertyAnnotation.predicate() + " \""
					+ property + "\"").getBytes());
		}
	}

	/**
	 * Writes the URI of an object
	 * 
	 * @param TTLObject
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
	private String writeUri(Object TTLObject, String type,
			ByteArrayOutputStream baos) throws IllegalAccessException,
			InvocationTargetException, IOException {
		String uri = null;
		for (Method method : TTLObject.getClass().getMethods()) {
			LinkedDataUri uriAnnotation = method
					.getAnnotation(LinkedDataUri.class);
			if (uriAnnotation != null) {
				uri = method.invoke(TTLObject).toString();
				baos.write(("<" + uri + "> rdf:type " + type).getBytes());
				break;
			}
		}
		return uri;
	}

	/**
	 * Writes the inverse properties of the object
	 * 
	 * @param TTLObject
	 *            The object to be written
	 * @param baos
	 *            The output stream
	 * @param uri
	 *            The uri of the object
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private void writeInverseProperties(Object TTLObject,
			ByteArrayOutputStream baos, String uri)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		for (Method method : TTLObject.getClass().getMethods()) {
			LinkedDataProperty propertyAnnotation = method
					.getAnnotation(LinkedDataProperty.class);
			if (propertyAnnotation != null && propertyAnnotation.inverse()) {
				Object prop = method.invoke(TTLObject);
				if (prop instanceof String || prop.getClass().isPrimitive()) {
					inversePropertyToTTL(baos, uri, prop.toString(),
							propertyAnnotation.predicate());
				} else {
					inversePropertyToTTL(baos, uri, prop,
							propertyAnnotation.predicate());
				}

			}
		}
	}

	/**
	 * Writes an inverse property when this is not a primitive type or a string
	 * 
	 * @param baos
	 *            The output stream
	 * @param uri
	 *            The URI of the father object
	 * @param property
	 *            The property to be written
	 * @param predicate
	 *            The predicate to link the property with the object
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private void inversePropertyToTTL(ByteArrayOutputStream baos, String uri,
			Object property, String predicate) throws IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (property instanceof Collection) {
			@SuppressWarnings("unchecked")
			Collection<Object> properties = (Collection<Object>) property;
			for (Object obj : properties) {
				inversePropertyToTTL(baos, uri, obj, predicate);
			}
		} else {
			for (Method m : property.getClass().getMethods()) {
				if (m.getAnnotation(LinkedDataUri.class) != null) {
					inversePropertyToTTL(baos, uri, m.invoke(property)
							.toString(), predicate);
				}
			}
		}
	}

	/**
	 * Writes an inverse property when this is can be represented as a string
	 * 
	 * @param baos
	 *            The output stream
	 * @param uri
	 *            The URI of the father object
	 * @param property
	 *            The property to be written
	 * @param predicate
	 *            The predicate to link the property with the object
	 */
	private void inversePropertyToTTL(ByteArrayOutputStream baos, String uri,
			String property, String predicate) throws IOException {
		baos.write(("\n<" + property + "> " + predicate + "<" + uri + "> .")
				.getBytes());
	}
}