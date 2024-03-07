package it.unibs.ingswproject.models;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.HashMap;

/**
 * Questa classe è un servizio singleton per lo storage.
 * Gestisce la connessione al database.
 *
 * @see Database
 * @see DB
 * @author Nicolò Rebaioli
 */
public class StorageService {
    private static StorageService instance;
    @SuppressWarnings("rawtypes")
    private final HashMap<Class, EntityRepository> repositories = new HashMap<>();

    private StorageService() {
        Database db = DB.getDefault();

        this.repositories.put(Comprensorio.class, new EntityRepository<>(Comprensorio.class, db));
        this.repositories.put(Nodo.class, new EntityRepository<>(Nodo.class, db));
    }

    /**
     * Get the instance of the storage service.
     * If the instance does not exist, it is created.
     * @return The instance of the storage service
     */
    public static StorageService getInstance() {
        if (instance == null) {
            instance = new StorageService();
        }
        return instance;
    }


    /**
     * Get the repository for an entity class.
     * @param entityClass The entity class
     * @param <T> The entity class
     * @return The repository for the entity class
     */
    @SuppressWarnings("unchecked")
    public <T> EntityRepository<T> getRepository(Class<T> entityClass) {
        return this.repositories.get(entityClass);
    }
}
