package es.weso.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.view.AbstractView;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.binding.Binding;

import es.weso.util.Conf;
import es.weso.util.JenaMemcachedClient;

public abstract class AbstractLinkedDataView extends AbstractView {

	/**
	 * Writes the lang tag for literals that need it
	 * 
	 * @param qs
	 *            The {@link QuerySolution} where the lang tag is stored
	 * @param varName
	 *            The name of the {@link Literal} that has the lang tag stored
	 * @param baos
	 *            The output streamo
	 * @throws IOException
	 */
	protected abstract void writeLangTag(QuerySolution qs, String varName,
			ByteArrayOutputStream baos) throws IOException;

	/**
	 * Gets a literal string from a {@link QuerySolution}
	 * 
	 * @param qs
	 *            The {@link QuerySolution}
	 * @param varName
	 *            The name of the {@link Literal}
	 * @return The String representing a {@link Literal}
	 */
	protected String getLiteral(QuerySolution qs, String varName) {
		try {
			return qs.getLiteral(varName).getString();
		} catch (ClassCastException e) {
			return qs.getResource(varName).toString();
		}
	}

	/**
	 * Gets a resource from a {@link QuerySolution} with prefix substitution
	 * 
	 * @param qs
	 *            The {@link QuerySolution}
	 * @param varName
	 *            The name of the resource
	 * @return The resource with prefix substitution
	 */
	protected String getResource(QuerySolution qs, String varName) {
		String resource = qs.getResource("p").toString();
		Map<String, String> prefixes = Conf.getPrefixes();
		for (Map.Entry<String, String> entry : prefixes.entrySet()) {
			if (resource.startsWith(entry.getValue())) {
				resource = resource.replace(entry.getValue(), entry.getKey()
						+ ":");
				break;
			}
		}
		return resource;
	}

	/**
	 * Gets the direct properties of a entity with the given uri
	 * 
	 * @param uri
	 *            The uri representing the entity
	 * @return The direct properties of the entity
	 * @throws IOException
	 */
	protected ResultSet getDirectProperties(String uri) throws IOException {
		JenaMemcachedClient client = JenaMemcachedClient.create();
		try {
			return client.executeQuery(Conf.getQuery("all.about.entity", uri));
		} catch (IllegalArgumentException e) {
			return getEmptyResultSet();
		}
	}

	/**
	 * Gets the inverse properties of a entity with the given uri
	 * 
	 * @param uri
	 *            The uri representing the entity
	 * @return The inverse properties of the entity
	 * @throws IOException
	 */
	protected ResultSet getInverseProperties(String uri) throws IOException {
		JenaMemcachedClient client = JenaMemcachedClient.create();
		try {
			return client.executeQuery(Conf.getQuery(
					"all.about.entity.inverse", uri));
		} catch (IllegalArgumentException e) {
			return getEmptyResultSet();
		}
	}

	/**
	 * Gets a number of tabulations
	 * 
	 * @param n
	 *            The amount of tabulations
	 * @return N tabulations
	 */
	protected String getTabs(int n) {
		String tabulations = "";
		for (int i = 0; i < n; i++) {
			tabulations += "\t";
		}
		return tabulations;
	}

	private ResultSet getEmptyResultSet() {
		return new ResultSet() {

			public void remove() {
			}

			public Model getResourceModel() {
				return null;
			}

			public List<String> getResultVars() {
				return Collections.emptyList();
			}

			public int getRowNumber() {
				return 0;
			}

			public boolean hasNext() {
				return false;
			}

			public QuerySolution next() {
				return null;
			}

			public Binding nextBinding() {
				return null;
			}

			public QuerySolution nextSolution() {
				return null;
			}
		};
	}
}
