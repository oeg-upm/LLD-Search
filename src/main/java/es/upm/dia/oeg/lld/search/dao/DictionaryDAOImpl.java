package es.upm.dia.oeg.lld.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import es.upm.dia.oeg.lld.search.model.Dictionary;
import es.upm.dia.oeg.lld.search.utils.AppConstants;

@Component
public class DictionaryDAOImpl implements DictionaryDAO {

    @Override
    public List<Dictionary> getDictionaries() {
        final String queryString = AppConstants.GET_ALL_DICTIONARIES_QUERY;

        final Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        final QueryExecution qe = QueryExecutionFactory.sparqlService(
                AppConstants.SPARQL_ENDPOINT, query);

        final ResultSet results = qe.execSelect();

        final List<Dictionary> dictionaries = new ArrayList<Dictionary>();

        while (results.hasNext()) {
            final QuerySolution result = results.next();
            final Dictionary dict = new Dictionary();
            dict.setLangSource(result.get("lang_source").asLiteral().getString());
            dict.setLangTarget(result.get("lang_target").asLiteral().getString());
            dictionaries.add(dict);
        }

        qe.close();

        return dictionaries;
    }

}
