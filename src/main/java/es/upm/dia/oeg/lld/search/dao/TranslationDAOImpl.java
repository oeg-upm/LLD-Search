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

import es.upm.dia.oeg.lld.search.model.Translation;
import es.upm.dia.oeg.lld.search.utils.AppConstants;

@Component
public class TranslationDAOImpl implements TranslationDAO {

    @Override
    public List<String> getLanguages() {

        final String queryString = AppConstants.GET_ALL_LANGUAGES_QUERY;

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
            QuerySolution result, boolean indirect, boolean babelnet) {
        final Translation trans = new Translation();

        try {
            // trans.setTrans(result.get("trans").toString());
            trans.setLexiconSource(result.get("lexicon_source").toString());
            trans.setLexiconTarget(result.get("lexicon_target").toString());
            trans.setWrittenRepSource(label);
            trans.setWrittenRepTarget(result.get("written_rep_target").asLiteral().getLexicalForm());
            trans.setLangSource(lang);
            trans.setLangTarget(result.get("written_rep_target").asLiteral().getLanguage());
            trans.setPartOfSpeech(result.get("POS").toString());
            trans.setIndirect(indirect);

            if (babelnet && (result.get("babelnet") != null)) {
                trans.setBabelnetSynset(result.get("babelnet").toString());
            }

            if (indirect) {
                trans.setPivotLanguage(result.get("written_rep_pivot").asLiteral().getLanguage());

            }

        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
        return trans;
    }

    private final List<Translation> getTranslations(String label,
            String langSource, String queryString, boolean indirect,
            boolean babelnet) {

        final Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        final QueryExecution qe = QueryExecutionFactory.sparqlService(
                AppConstants.SPARQL_ENDPOINT, query);

        final ResultSet results = qe.execSelect();

        final List<Translation> translations = new ArrayList<Translation>();

        while (results.hasNext()) {
            final QuerySolution result = results.next();
            final Translation trans = createTranslation(label, langSource,
                    result, indirect, babelnet);
            translations.add(trans);
        }

        qe.close();

        return translations;
    }

    @Override
    public List<Translation> searchDirectTranslations(String label,
            String langSource, String langTarget, boolean babelnet) {

        // The written representation at SPARQL endpoint is encoded like:
        // "bank"@en
        final String writtenRep = "\"" + label + "\"@" + langSource;

        // Text in the query has to be between "", need to add & escape them.
        langSource = "\"" + langSource + "\"";

        String queryString;

        // SPARQL query differs if target language is a restriction
        if (langTarget == null) {
            if (babelnet) {
                queryString = String.format(
                        AppConstants.GET_DIRECT_TRANSLATIONS_ALL_LANGUAGES_BABELNET,
                        writtenRep, langSource, langSource);
            } else {
                queryString = String.format(
                        AppConstants.GET_DIRECT_TRANSLATIONS_ALL_LANGUAGES,
                        writtenRep, langSource, langSource);
            }
        } else {
            langTarget = "\"" + langTarget + "\"";

            if (babelnet) {
                queryString = String.format(
                        AppConstants.GET_DIRECT_TRANSLATIONS_ONE_LANGUAGE_BABELNET,
                        writtenRep, langSource, langTarget, langSource);
            } else {
                queryString = String.format(
                        AppConstants.GET_DIRECT_TRANSLATIONS_ONE_LANGUAGE,
                        writtenRep, langSource, langTarget, langSource);
            }
        }

        return getTranslations(label, langSource, queryString, false, babelnet);
    }

    @Override
    public List<Translation> searchIndirectTranslations(String label,
            String langSource, String langTarget, String langPivot,
            boolean babelnet) {

        // The written representation at SPARQL endpoint is encoded like:
        // "bank"@en
        final String writtenRep = "\"" + label + "\"@" + langSource;

        // Text in the query has to be between "", need to add & escape them.
        langSource = "\"" + langSource + "\"";
        langTarget = "\"" + langTarget + "\"";

        String queryString;

        // SPARQL query differs if target language is a restriction
        if (babelnet) {
            if (langPivot != null) {
                langPivot = "\"" + langPivot + "\"";
                queryString = String.format(
                        AppConstants.GET_INDIRECT_TRANSLATIONS_ONE_LANGUAGE_WITH_PIVOT_BABELNET,
                        writtenRep, langPivot, langTarget);
            } else {
                queryString = String.format(
                        AppConstants.GET_INDIRECT_TRANSLATIONS_ONE_LANGUAGE_BABELNET,
                        writtenRep, langTarget, langSource, langTarget);
            }
        } else {
            if (langPivot != null) {
                langPivot = "\"" + langPivot + "\"";
                queryString = String.format(
                        AppConstants.GET_INDIRECT_TRANSLATIONS_ONE_LANGUAGE_WITH_PIVOT,
                        writtenRep, langPivot, langTarget);
            } else {
                queryString = String.format(
                        AppConstants.GET_INDIRECT_TRANSLATIONS_ONE_LANGUAGE,
                        writtenRep, langTarget, langSource, langTarget);
            }
        }

        return getTranslations(label, langSource, queryString, true, babelnet);
    }

}
