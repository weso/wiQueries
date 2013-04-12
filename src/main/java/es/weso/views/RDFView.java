package es.weso.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import es.weso.annotations.LinkedDataEntity;
import es.weso.annotations.LinkedDataProperty;
import es.weso.annotations.LinkedDataUri;

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
		Object rdfObject = null;
		for (Object obj : model.values()) {
			if (obj instanceof BindingResult) {
				rdfObject = ((BindingResult) obj).getTarget();
				break;
			}
		}
		LinkedDataEntity entity = rdfObject.getClass().getAnnotation(
				LinkedDataEntity.class);
		String type = entity.type();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("<?xml version=\"1.0\"?>\n".getBytes());
		baos.write("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n"
				.getBytes());
		entityToRdf(rdfObject, entity, type, baos, 1);
		baos.write("</rdf:RDF>".getBytes());
		writeToResponse(response, baos);
	}

	private void entityToRdf(Object rdfObject, LinkedDataEntity entity,
			String type, ByteArrayOutputStream baos, int tabs)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		String uri;
		String property;
		if (entity != null) {
			for (Method method : rdfObject.getClass().getMethods()) {
				LinkedDataUri uriAnnotation = method
						.getAnnotation(LinkedDataUri.class);
				if (uriAnnotation != null) {
					uri = method.invoke(rdfObject).toString();
					baos.write((getTabulations(tabs) + "<" + type + " rdf:about=\"" + uri + "\">\n")
							.getBytes());
					break;
				}
			}
			for (Method method : rdfObject.getClass().getMethods()) {
				LinkedDataProperty propertyAnnotation = method
						.getAnnotation(LinkedDataProperty.class);
				if (propertyAnnotation != null && !propertyAnnotation.inverse()) {
					property = method.invoke(rdfObject).toString();
					baos.write((getTabulations(tabs + 1) + "<" + propertyAnnotation.predicate() + ">"
							+ property + "</" + propertyAnnotation.predicate() + ">\n")
							.getBytes());
				}
			}
			baos.write((getTabulations(tabs) + "</" + type + ">\n").getBytes());
		}
	}

	private String getTabulations(int tabs) {
		String tabulations = "";
		for(int i = 0; i < tabs; i++) {
			tabulations += "\t";
		}
		return tabulations;
	}
}
