package es.weso.model;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import es.weso.annotations.LinkedDataCollection;
import es.weso.annotations.LinkedDataEntity;

@JsonAutoDetect
@XmlRootElement
@LinkedDataEntity(type = "qb:Observation")
/**
 * Class that automatically maps a {@link Collection} of {@link Observation}s
 * 
 * @author <a href="http://alejandro-montes.appspot.com">Alejandro Montes
 *         García</a>
 * @version 1.0
 * @since 10/04/2013
 */
public class ObservationCollection {

	private Collection<Observation> observations;

	public ObservationCollection() {
		observations = new HashSet<Observation>();
	}

	@LinkedDataCollection
	public Collection<Observation> getObservations() {
		return observations;
	}

	public void setObservations(Collection<Observation> observations) {
		this.observations = observations;
	}

}