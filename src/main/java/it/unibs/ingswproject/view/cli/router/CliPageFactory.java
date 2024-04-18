package it.unibs.ingswproject.view.cli.router;

import it.unibs.ingswproject.view.cli.CliPage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class CliPageFactory {
    protected Map<Class<?>, Object> dependencies;

    public CliPageFactory() {
        this(new HashMap<>());
    }
    public CliPageFactory(Map<Class<?>, Object> dependencies) {
        this.dependencies = dependencies;
    }

    public void registerDependency(Class<?> type, Object instance) {
        this.dependencies.put(type, instance);
    }

    public <T extends CliPage> T generatePage(Class<T> pageClass) throws ReflectiveOperationException {
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

        //noinspection unchecked
        return (T) constructor.newInstance(args);
    }

    protected Constructor<?> findCliConstructor(Class<?> pageClass) {
        for (Constructor<?> constructor : pageClass.getConstructors()) {
            if (constructor.isAnnotationPresent(CliConstructor.class)) {
                return constructor;
            }
        }
        return null;
    }
}
