package es.upm.dia.oeg.lld.search.dao;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import es.upm.dia.oeg.lld.search.model.Translation;
import es.upm.dia.oeg.lld.search.utils.AppConstants;

@Component
public class TranslationDAOImpl implements TranslationDAO {

    @Override
    public List<String> getLanguages() {

        final String queryString = "PREFIX lemon: <http://www.lemon-model.net/lemon#> "
                + "PREFIX tr: <http://purl.org/net/translation#> "
                + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "SELECT distinct ?lexicon_a ?lang "
                + "WHERE {"
                + "  ?lexicon_a a lemon:Lexicon ; "
                + "    lemon:language ?lang ; "
                + "    rdfs:label ?label . "
                + "} ORDER BY ?lang ";

        final Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        final QueryExecution qe = QueryExecutionFactory.sparqlService(
                AppConstants.SPARQL_ENDPOINT, query);

        final ResultSet results = qe.execSelect();

        final List<String> languages = new ArrayList<String>();

        while (results.hasNext()) {
            final QuerySolution result = results.next();
            languages.add(result.get("lang").asLiteral().getString());
        }

        qe.close();

        return languages;
    }

    private final Translation createTranslation(String label, String lang,
            QuerySolution result) {
        final Translation trans = new Translation();

        try {
            trans.setTrans(result.get("trans").toString());
            trans.setLexiconSource(result.get("lexicon_a").toString());
            trans.setLexiconTarget(result.get("lexicon_b").toString());
            trans.setSenseSource(result.get("sense_a").toString());
            trans.setSenseTarget(result.get("sense_b").toString());
            trans.setWrittenRepSource(label);
            trans.setWrittenRepTarget(result.get("written_rep_b").asLiteral().getLexicalForm());
            trans.setLangSource(lang);
            trans.setLangTarget(result.get("written_rep_b").asLiteral().getLanguage());
            trans.setPartOfSpeech(result.get("POS").toString());

            final RDFNode bn = result.get("babelnet");
            if (bn != null) {
                trans.setBabelnetSynset(bn.toString());
            }
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
        return trans;
    }

    @Override
    public List<Translation> searchAllTranslations(String label,
            String langSource, String langTarget) {

        // The written representation at SPARQL endpoint is encoded like:
        // "bank"@en
        final String writtenRep = "\"" + label + "\"@" + langSource;

        // Text in the query has to be between "", need to add & escape them.
        langSource = "\"" + langSource + "\"";

        // If all languages required, use a variable. Otherwise, add & escape ""
        if (langTarget.equalsIgnoreCase("All")) {
            langTarget = "?lang_b";
        } else {
            langTarget = "\"" + langTarget + "\"";
        }

        final String queryString = "PREFIX lemon: <http://www.lemon-model.net/lemon#> "
                + "PREFIX tr: <http://purl.org/net/translation#> "
                + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> "
                + "SELECT DISTINCT ?written_rep_b ?POS ?sense_a ?sense_b ?trans ?lexicon_a ?lexicon_b ?babelnet "
                + "WHERE { " + "?form_a lemon:writtenRep "
                + writtenRep
                + " . "
                + "?lex_entry_a lemon:lexicalForm ?form_a . "
                + "?sense_a lemon:isSenseOf  ?lex_entry_a .  "
                + "?lexicon_a lemon:entry ?lex_entry_a . "
                + "?lexicon_a lemon:language "
                + langSource
                + " .  "
                + "?trans  tr:translationSense  ?sense_a .  "
                + "?trans  tr:translationSense  ?sense_b .  "
                + "?sense_b lemon:isSenseOf  ?lex_entry_b .   "
                + "?lex_entry_b lemon:lexicalForm ?form_b . "
                + "?form_b lemon:writtenRep ?written_rep_b . "
                + "?lex_entry_b  lexinfo:partOfSpeech ?POS .   "
                + "?lexicon_b lemon:entry ?lex_entry_b . "
                + "?lexicon_b lemon:language "
                + langTarget
                + " .  "
                + "minus { ?lexicon_b lemon:language "
                + langSource
                + "  } "
                + "optional {?sense_a lemon:reference ?babelnet "
                + "}} ORDER BY ?lexicon_b ";

        final Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        final QueryExecution qe = QueryExecutionFactory.sparqlService(
                AppConstants.SPARQL_ENDPOINT, query);

        final ResultSet results = qe.execSelect();

        final List<Translation> translations = new ArrayList<Translation>();

        while (results.hasNext()) {
            final QuerySolution result = results.next();
            final Translation trans = createTranslation(label, langSource,
                    result);
            translations.add(trans);
        }

        qe.close();

        return translations;
    }
}
