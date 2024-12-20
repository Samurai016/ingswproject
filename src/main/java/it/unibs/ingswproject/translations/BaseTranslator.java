package it.unibs.ingswproject.translations;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe base per la traduzione di stringhe
 * Sfrutta il meccanismo delle ResourceBundle per la traduzione
 * Le risorse vengono caricate da un URLClassLoader
 * È possibile cambiare la lingua in qualsiasi momento
 *
 * @author Nicolò Rebaioli
 */
public class BaseTranslator implements Translator {
    protected final ArrayList<ResourceBundle> resourceBundles = new ArrayList<>();
    protected ClassLoader classLoader;
    protected Locale locale;

    /**
     * Costruttore che crea un'istanza del traduttore
     *
     * @param folder Cartella in cui sono presenti i file delle risorse
     * @throws MalformedURLException Se l'URL della cartella non è valido
     */
    public BaseTranslator(String folder) throws MalformedURLException {
        this(folder, Locale.getDefault());
    }

    /**
     * Costruttore che crea un'istanza del traduttore
     *
     * @param folder Cartella in cui sono presenti i file delle risorse
     * @param locale Lingua da utilizzare
     * @throws MalformedURLException Se l'URL della cartella non è valido
     */
    public BaseTranslator(String folder, Locale locale) throws MalformedURLException {
        this(locale);
        this.classLoader = new URLClassLoader(new URL[]{
                URI.create(folder).toURL()
        });
    }

    /**
     * Costruttore che crea un'istanza del traduttore
     *
     * @param classLoader ClassLoader da utilizzare per caricare le risorse
     */
    public BaseTranslator(ClassLoader classLoader) {
        this(classLoader, Locale.getDefault());
    }

    /**
     * Costruttore che crea un'istanza del traduttore
     *
     * @param classLoader ClassLoader da utilizzare per caricare le risorse
     * @param locale Lingua da utilizzare
     */
    public BaseTranslator(ClassLoader classLoader, Locale locale) {
        this(locale);
        this.classLoader = classLoader;
    }

    /**
     * Costruttore che crea un'istanza del traduttore
     *
     * @param locale Lingua da utilizzare
     */
    public BaseTranslator(Locale locale) {
        this.locale = locale;
    }

    /**
     * Aggiunge un resource bundle al traduttore
     *
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     * @param name Il nome del resource bundle
     * @return Il traduttore stesso (builder pattern)
     */
    public BaseTranslator addResourceBundle(String name) {
        this.resourceBundles.add(ResourceBundle.getBundle(name, this.locale, this.classLoader));
        return this;
    }

    /**
     * Aggiunge un resource bundle al traduttore
     *
     * @param resourceBundle Il resource bundle da aggiungere
     * @return Il traduttore stesso (builder pattern)
     */
    public BaseTranslator addResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundles.add(resourceBundle);
        return this;
    }

    /**
     * Imposta la lingua del traduttore
     *
     * @param locale La lingua da impostare
     * @return Il traduttore stesso (builder pattern)
     */
    public BaseTranslator setLocale(Locale locale) {
        this.locale = locale;
        // Reload all resource bundles
        this.resourceBundles.replaceAll(resourceBundle -> ResourceBundle.getBundle(resourceBundle.getBaseBundleName(), this.locale, this.classLoader));
        return this;
    }

    /**
     * Ottiene la lingua del traduttore
     *
     * @return La lingua del traduttore
     */
    public Locale getLocale() {
        return this.locale;
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

    @Override
    public String translate(String key, Object... args) {
        return String.format(this.translate(key), args);
    }
}
