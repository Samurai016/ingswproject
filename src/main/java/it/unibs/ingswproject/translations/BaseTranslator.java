package it.unibs.ingswproject.translations;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class BaseTranslator implements Translator {
    protected ArrayList<ResourceBundle> resourceBundles = new ArrayList<>();
    protected ClassLoader classLoader;
    protected Locale locale;

    public BaseTranslator(String folder) throws MalformedURLException {
        this(folder, Locale.getDefault());
    }

    public BaseTranslator(String folder, Locale locale) throws MalformedURLException {
        this(locale);
        this.classLoader = new URLClassLoader(new URL[]{
                URI.create(folder).toURL()
        });
    }

    public BaseTranslator(Locale locale) {
        this.locale = locale;
    }

    public void addResourceBundle(String name) {
        this.resourceBundles.add(ResourceBundle.getBundle(name, this.locale, this.classLoader));
    }

    public void addResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundles.add(resourceBundle);
    }

    @Override
    public String translate(String key) {
        for (ResourceBundle resourceBundle : this.resourceBundles) {
            if (resourceBundle.containsKey(key)) {
                return resourceBundle.getString(key);
            }
        }
        return key;
    }
}
