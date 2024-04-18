package it.unibs.ingswproject.translations;

import java.net.MalformedURLException;

public class TranslatorService {
    protected static final String DEFAULT_FOLDER = "file:i18n/";
    protected static Translator instance;

    public static Translator getInstance() {
        if (instance == null) {
            try {
                instance = new BaseTranslator(DEFAULT_FOLDER);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Error while creating translator instance", e);
            }
        }
        return instance;
    }
}
