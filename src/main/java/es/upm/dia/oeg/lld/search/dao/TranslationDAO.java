package es.upm.dia.oeg.lld.search.dao;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationDAO {

    public List<String> getLanguages();
    
    public String getLanguageCode(String language);

    public List<Translation> searchDirectTranslations(Language lang, String label,
            String langSource, String langTarget, boolean babelnet);

    public List<Translation> searchIndirectTranslations(Language lang, String label,
            String langSource, String langTarget, String langPivot,
            boolean babelnet,double threshold);
}
