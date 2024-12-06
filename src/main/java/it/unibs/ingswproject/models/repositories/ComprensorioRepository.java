package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.Comprensorio;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensorioRepository extends EntityRepository<Comprensorio> {
    public ComprensorioRepository(Database db) {
        super(Comprensorio.class, db);
    }

    @Override
    protected void validate(Comprensorio entity) {
        super.validate(entity);

        // Verifico che non ci siano comprensori con lo stesso nome
        boolean existsComprensorio = this.database
                .find(Comprensorio.class)
                .where().ieq("nome", entity.getNome())
                .exists();
        if (existsComprensorio) {
            throw new IllegalArgumentException("comprensorio_already_exists");
        }
    }
}
