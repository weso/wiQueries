package es.weso.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;

/**
 * Utility class to deal with SPARQL queries and memcached
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 05/04/2013
 */
public class JenaMemcachedClient {

	private static Logger log = Logger.getLogger(JenaMemcachedClient.class);
	private int expireTime;

	public JenaMemcachedClient() {
		try {
			String time = Conf.getConfig("cache.expire.time");
			expireTime = Integer.parseInt(time);
		} catch (NullPointerException e) {
			log.info("cache.expire.time property not found, "
					+ "initialising it as default");
			expireTime = 2592000;
		} catch (NumberFormatException e) {
			log.info("Invalid cache.expire.time property, "
					+ "initialising it as default");
			expireTime = 2592000;
		}
	}

	/**
	 * Executes a query
	 * 
	 * @param queryStr
	 *            The string of the query to be executed
	 * @return The result of the execution of a query
	 */
	public ResultSet executeQuery(String queryStr) {
		String xmlResult;
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(Conf.getConfig("cache.server")));
		try {
			MemcachedClient memcachedClient = builder.build();
			xmlResult = getResultFromCache(queryStr, memcachedClient);
			memcachedClient.shutdown();
		} catch (IOException e) {
			xmlResult = treatCacheException(queryStr, e);
		} catch (TimeoutException e) {
			xmlResult = treatCacheException(queryStr, e);
		} catch (InterruptedException e) {
			xmlResult = treatCacheException(queryStr, e);
		} catch (MemcachedException e) {
			xmlResult = treatCacheException(queryStr, e);
		}
		return ResultSetFactory.fromXML(xmlResult);
	}

	/**
	 * Tries to get a result from the cache and stores it if it was not present
	 * 
	 * @param queryStr
	 *            The query to be performed
	 * @param memcachedClient
	 *            The cache client
	 * @return The XML representation of the {@link ResultSet}
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	private String getResultFromCache(String queryStr,
			MemcachedClient memcachedClient) throws TimeoutException,
			InterruptedException, MemcachedException {
		String xmlResult;
		String key = "Query" + queryStr.hashCode();
		log.info("Trying to get " + key + " from cache");
		xmlResult = memcachedClient.get(key);
		if (xmlResult == null) {
			log.info(key
					+ " was not stored in the cache, querying SPARQL endpoint");
			xmlResult = queryEndpointAndStore(queryStr, memcachedClient, key);
		}
		return xmlResult;
	}

	/**
	 * Treats a cache exception
	 * 
	 * @param queryStr
	 *            The query to be performed if the cache is not working
	 * @param e
	 *            The exception to be treated
	 * @return The XML representation of the {@link ResultSet}
	 */
	private String treatCacheException(String queryStr, Exception e) {
		log.error("An error occured querying the cache. "
				+ "Querying SPARQL endpoint", e);
		return queryEndpoint(queryStr);
	}

	/**
	 * Queries the sparql endpoint and stores the result in the cache
	 * 
	 * @param queryStr
	 *            The query to be performed
	 * @param memcachedClient
	 *            The cache client
	 * @param key
	 *            The key used in the cache to store the results
	 * @return The XML representation of the {@link ResultSet}
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 */
	private String queryEndpointAndStore(String queryStr,
			MemcachedClient memcachedClient, String key)
			throws TimeoutException, InterruptedException, MemcachedException {
		String xmlResult = "";
		try {
			xmlResult = queryEndpoint(queryStr);
			memcachedClient.set(key, expireTime, xmlResult);
		} catch (QueryException e) {
			log.error("An error ocurred querying the endpoint. "
					+ "The results will not be stored in the cache.", e);
			throw new QueryException("An error ocurred querying the endpoint. "
					+ "The results will not be stored in the cache.", e);
		}
		return xmlResult;
	}

	/**
	 * Queries the SPARQL endpoint
	 * 
	 * @param queryStr
	 *            The query to be performed
	 * @return The XML representation of the {@link ResultSet}
	 */
	private String queryEndpoint(String queryStr) {
		Query query = QueryFactory.create(queryStr, Syntax.syntaxARQ);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(
				Conf.getConfig("sparql.endpoint"), query);
		ResultSet rs = qexec.execSelect();
		if (rs == null) {
			throw new QueryException("Invalid ResultSet");
		}
		if (!rs.hasNext()) {
			throw new IllegalArgumentException(
					"ResultSet was empty. Check your query:\n" + queryStr);
		}
		return ResultSetFormatter.asXMLString(rs);
	}

}
