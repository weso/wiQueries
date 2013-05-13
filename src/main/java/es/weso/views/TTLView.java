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
 * A view that returns data in TTL
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 16/04/2013
 */
public class TTLView extends AbstractLinkedDataView {

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
		String uri = writeUri(TTLObject, baos);
		if (uri == null) {
			for (Method method : TTLObject.getClass().getMethods()) {
				LinkedDataCollection colAnnotation = method
						.getAnnotation(LinkedDataCollection.class);
				if (colAnnotation != null) {
					Collection<Object> objs = (Collection<Object>) method
							.invoke(TTLObject);
					for (Object obj : objs) {
						entityToTTL(obj, type, baos);
					}
				}
			}
		} else {
			writeProperties(baos, uri);
			baos.write(" .\n".getBytes());
			writeInverseProperties(baos, uri);
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
			baos.write(("\n<" + qs.getResource("s").toString()).getBytes());
			baos.write(("> " + getResource(qs, "p") + " <").getBytes());
			baos.write(uri.getBytes());
			baos.write("> .".getBytes());
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
		boolean first = true;
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			if (first) {
				first = false;
			} else {
				baos.write(";".getBytes());
			}
			baos.write(("\n\t" + getResource(qs, "p")).getBytes());
			baos.write((" " + getLiteral(qs, "l")).getBytes("UTF-8"));
			writeLangTag(qs, "lang", baos);
		}
	}

	@Override
	protected void writeLangTag(QuerySolution qs, String varName,
			ByteArrayOutputStream baos) throws IOException {
		Literal l = qs.getLiteral(varName);
		if (l != null) {
			String lang = l.getString();
			if (lang != null && !lang.trim().isEmpty()) {
				baos.write(("@" + lang).getBytes());
			}
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
	private String writeUri(Object TTLObject, ByteArrayOutputStream baos)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		String uri = null;
		for (Method method : TTLObject.getClass().getMethods()) {
			LinkedDataUri uriAnnotation = method
					.getAnnotation(LinkedDataUri.class);
			if (uriAnnotation != null) {
				uri = method.invoke(TTLObject).toString();
				baos.write(("<" + uri + ">").getBytes());
				break;
			}
		}
		return uri;
	}

	@Override
	protected String getLiteral(QuerySolution qs, String varName) {
		try {
			return "\"" + qs.getLiteral(varName).getString() + "\"";
		} catch (ClassCastException e) {
			return "<" + qs.getResource(varName).toString() + ">";
		}
	}
}