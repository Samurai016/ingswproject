package it.unibs.ingswproject.router;

import it.unibs.ingswproject.controllers.cli.CliPageController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class PageFactory {
    protected Map<Class<?>, Object> dependencies;

    public PageFactory() {
        this(new HashMap<>());
    }
    public PageFactory(Map<Class<?>, Object> dependencies) {
        this.dependencies = dependencies;
    }

    public void registerDependency(Class<?> type, Object instance) {
        this.dependencies.put(type, instance);
    }

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

    protected Constructor<?> findCliConstructor(Class<?> pageClass) {
        for (Constructor<?> constructor : pageClass.getConstructors()) {
            if (constructor.isAnnotationPresent(PageConstructor.class)) {
                return constructor;
            }
        }
        return null;
    }
}
