package es.upm.dia.oeg.lld.search.dao;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationDAO {

    public List<Translation> getTranslations();

    public Translation getTranslationByID(String id);

}
