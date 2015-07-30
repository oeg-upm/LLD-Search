package es.upm.dia.oeg.lld.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import es.upm.dia.oeg.lld.search.dao.ElasticsSearchAccess;

@SpringBootApplication
public class LldMultilingualSearchApplication extends
        SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(LldMultilingualSearchApplication.class);
    }

    public static void main(String[] args) {
    	
        SpringApplication.run(LldMultilingualSearchApplication.class, args);
        ElasticsSearchAccess.closeClient();
    }
}
