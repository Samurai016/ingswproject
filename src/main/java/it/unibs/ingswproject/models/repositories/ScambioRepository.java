package it.unibs.ingswproject.models.repositories;

import io.ebean.Database;
import io.ebean.Transaction;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.entities.*;
import it.unibs.ingswproject.models.entities.query.QScambio;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ScambioRepository extends EntityRepository<Scambio> {
    public ScambioRepository(Database db) {
        super(Scambio.class, db);
    }

    /**
     * Trova gli scambi inseriti da un determinato utente, in ordine di data di creazione
     *
     * @param autore Utente autore
     * @return Lista di scambi
     */
    public List<Scambio> findByAutore(Utente autore) {
        return new QScambio()
                .autore.eq(autore)
                .findList()
                .stream()
                .sorted((a, b) -> {
                    // Metto prima gli scambi aperti, ordinandoli per data di creazione
                    boolean isAOpen = a.getStato().equals(Scambio.Stato.APERTO);
                    boolean isBOpen = b.getStato().equals(Scambio.Stato.APERTO);
                    if (isAOpen && !isBOpen) {
                        return -1;
                    }
                    if (!isAOpen && isBOpen) {
                        return 1;
                    }
                    return a.getDataCreazione().compareTo(b.getDataCreazione());
                })
                .toList();
    }

    /**
     * Trova gli scambi di un determinato comprensorio
     *
     * @param comprensorio Comprensorio di appartenenza
     * @return Lista di scambi
     */
    public List<Scambio> findByComprensorio(Comprensorio comprensorio) {
        return new QScambio()
                .autore.comprensorio.eq(comprensorio)
                .findList();
    }

    /**
     * Trova gli scambi in un cui un determinato nodo è richiesto o offerto
     *
     * @param nodo Nodo richiesto o offerto
     * @return Lista di scambi
     */
    public List<Scambio> findByNodo(Nodo nodo) {
        return new QScambio()
                .or()
                    .richiesta.eq(nodo)
                    .offerta.eq(nodo)
                .endOr()
                .findList();
    }

    /**
     * Trova gli scambi da notificare
     *
     * @return Lista di scambi
     */
    public List<Scambio> findDaNotificare() {
        return new QScambio()
                .stato.eq(Scambio.Stato.CHIUSO)
                .hasBeenNotified.eq(false)
                .findList();
    }

    /**
     * Notifica un insieme chiuso di scambi, salvando lo stato
     *
     * @param insiemeChiuso Insieme chiuso di scambi
     */
    public void notificaInsiemeChiuso(LinkedList<Scambio> insiemeChiuso) {
        try (var transaction = this.database.beginTransaction()) {
            for (Scambio scambio : insiemeChiuso) {
                this.save(scambio.notifica());
                this.database.refresh(scambio);
            }
            transaction.commit();
        }
    }

    @Override
    public void save(Scambio entity) {
        try (Transaction transaction = this.database.beginTransaction()) {
            // Aggiorno la versione
            super.save(entity);

            // Aggiorno lo storico se lo stato è cambiato
            List<StoricoScambio> storico = entity.getStorico();
            Scambio.Stato lastStato = storico.isEmpty() ? null : storico.getLast().getStato();
            if (lastStato == null || !lastStato.equals(entity.getStato())) {
                this.database.save(new StoricoScambio(entity));
            }

            transaction.commit();
        }
        this.database.refresh(entity);
    }
}
