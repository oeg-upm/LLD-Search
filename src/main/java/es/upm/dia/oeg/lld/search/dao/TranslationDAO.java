package es.upm.dia.oeg.lld.search.dao;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationDAO {

    public List<String> getLanguages();

    public List<Translation> searchAllTranslations(String label,
            String langSource, String langTarget);
}
