package es.weso.views;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

public class RDFView extends AbstractView {
	
	public static final String DEFAULT_CONTENT_TYPE = "application/rdf+xml";
	
	public RDFView() {
		setContentType(DEFAULT_CONTENT_TYPE);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		for(Object obj: model.values()){
			if(obj instanceof BindingResult) {
				System.out.println(((BindingResult) obj).getTarget().getClass());
			}
		}
	}

}
