package it.unibs.ingswproject.router;

import it.unibs.ingswproject.controllers.cli.CliPageController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe factory per la creazione di pagine
 * Si occupa di gestire l'inversione di controllo per la creazione di pagine
 */
public class PageFactory {
    protected final Map<Class<?>, Object> dependencies;

    /**
     * Costruttore di default
     */
    public PageFactory() {
        this(new HashMap<>());
    }

    /**
     * Costruttore con dipendenze
     *
     * @param dependencies Mappa delle dipendenze
     */
    public PageFactory(Map<Class<?>, Object> dependencies) {
        this.dependencies = dependencies;
    }


    /**
     * Registra una dipendenza
     *
     * @param type     Tipo della dipendenza
     * @param instance Istanza della dipendenza
     * @return PageFactory La factory stessa (builder pattern)
     */
    public PageFactory registerDependency(Class<?> type, Object instance) {
        this.dependencies.put(type, instance);
        return this;
    }

    /**
     * Genera un'istanza di una pagina
     *
     * @param pageClass Classe della pagina
     * @return T Il controller della pagina generata
     * @author Nicolò Rebaioli
     */
    public <T extends CliPageController> T generatePage(Class<T> pageClass) {
        Constructor<?> constructor = this.findCliConstructor(pageClass);
        if (constructor == null) {
            throw new IllegalArgumentException("No CliConstructor found for class " + pageClass.getName());
        }

        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            args[i] = this.dependencies.get(type);
        }

        try {
            //noinspection unchecked
            return (T) constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cerca un costruttore annotato con @CliConstructor
     *
     * @param pageClass Classe della pagina
     * @return Constructor Il costruttore trovato o null se non è stato trovato
     */
    protected Constructor<?> findCliConstructor(Class<?> pageClass) {
        for (Constructor<?> constructor : pageClass.getConstructors()) {
            if (constructor.isAnnotationPresent(PageConstructor.class)) {
                return constructor;
            }
        }
        return null;
    }
}
