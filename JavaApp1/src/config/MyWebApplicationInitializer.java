package config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}
	
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {RESTConfig.class };
	}

	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

}
