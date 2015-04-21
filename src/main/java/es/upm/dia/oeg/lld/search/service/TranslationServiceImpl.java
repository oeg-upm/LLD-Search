package es.upm.dia.oeg.lld.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
import es.upm.dia.oeg.lld.search.model.Translation;

@Service
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    TranslationDAO translationDAO;

    @Override
    public Translation getTranslationByID(String id) {
        Translation translation = translationDAO.getTranslationByID(id);

        if (translation == null) {
            translation = new Translation();
            translation.setTrans("null translation");
        }

        return translation;
    }

}
