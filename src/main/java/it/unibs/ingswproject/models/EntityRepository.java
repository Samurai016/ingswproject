package it.unibs.ingswproject.models;

import io.ebean.DB;
import io.ebean.Database;

import java.util.List;

/**
 * Questa classe è un repository generico per le entità.
 * Fornisce un'interfaccia base per le operazioni CRUD sulle entità.
 * In caso di necessità, è possibile estendere questa classe per aggiungere logiche specifiche.
 *
 * @param <T> La classe dell'entità
 * @author Nicolò Rebaioli
 */
public class EntityRepository<T> {
    protected final Class<T> entityClass;
    protected final Database database;

    public EntityRepository(Class<T> entityClass, Database database) {
        this.entityClass = entityClass;
        this.database = database;
    }
    public EntityRepository(Class<T> entityClass) {
        this(entityClass, DB.getDefault());
    }

    /**
     * Salva un'entità verificandone la validità.
     * @param entity L'entità da salvare
     */
    public void save(T entity) {
        this.validate(entity);
        this.database.save(entity);
    }

    /**
     * Elimina un'entità.
     * @param entity The entity to delete
     */
    public void delete(T entity) {
        this.database.delete(entity);
    }

    /**
     * Cerca un'entità in base all'id.
     * @param id L'id dell'entità
     * @return L'entità trovata
     */
    public T find(Object id) {
        return this.database.find(this.entityClass, id);
    }

    /**
     * Trova tutte le entità.
     * @return La lista di entità trovate
     */
    public List<T> findAll() {
        return this.database.find(this.entityClass).findList();
    }

    /**
     * Verifica la validità di un'entità.
     * Questo metodo viene chiamato prima di creare o aggiornare un'entità.
     * Se l'entità non è valida, dovrebbe lanciare un'eccezione.
     * In caso di logiche specifiche, è possibile sovrascrivere questo metodo.
     * @param entity The entity to validate
     */
    protected void validate(T entity) {
        // Do nothing by default
    }
}
