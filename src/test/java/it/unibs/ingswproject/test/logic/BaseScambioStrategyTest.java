package it.unibs.ingswproject.test.logic;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.Transaction;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.logic.BaseScambioStrategy;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.entities.Utente;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class BaseScambioStrategyTest {
    private static final StorageService storageService = new StorageService(DB.getDefault());
    private static BaseScambioStrategy baseScambioStrategy;

    private static final Comprensorio comprensorio = new Comprensorio("Bergamo");

    private static final Utente utente1 = (new Utente("utente1", "password1", Utente.Ruolo.FRUITORE)).setComprensorio(comprensorio);
    private static final Utente utente2 = (new Utente("utente2", "password2", Utente.Ruolo.FRUITORE)).setComprensorio(comprensorio);

    private static final Nodo matematica = createFoglia("Matematica");
    private static final Nodo pianoforte = createFoglia("Pianoforte");
    private static final Nodo stiratura = createFoglia("Stiratura");
    private static final List<Nodo> nodi = List.of(matematica, pianoforte, stiratura);

    private static Nodo createFoglia(String nome) {
        return Nodo.createFoglia(nome, "", "", null);
    }

    private static Scambio createScambio(Utente autore, Nodo richiesta, Nodo offerta, int quantitaRichiesta, RoutingComputationStrategy routingComputationStrategy) {
        return new Scambio(
                autore,
                richiesta,
                offerta,
                quantitaRichiesta,
                routingComputationStrategy
        );
    }
    private static Scambio createScambio(Utente autore, Nodo richiesta, Nodo offerta, int quantitaRichiesta) {
        return createScambio(
                autore,
                richiesta,
                offerta,
                quantitaRichiesta,
                new AlwaysOneRoutingStrategy(nodi)
        );
    }
    private static void saveScambi(List<Scambio> scambi) {
        Database db = storageService.getDatabase();
        try (Transaction transaction = db.beginTransaction()) {
            db.deleteAll(db.find(Scambio.class).findList());
            db.saveAll(scambi);
            transaction.commit();
        }
    }
    private static void refreshScambi(List<Scambio> scambi) {
        Database db = storageService.getDatabase();
        for (Scambio scambio : scambi) {
            db.refresh(scambio);
        }
    }

    @BeforeAll
    static void setUpAll() {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        EntityRepository<Comprensorio> comprensorioRepository = storageService.getRepository(Comprensorio.class);
        EntityRepository<Utente> utenteRepository = storageService.getRepository(Utente.class);
        EntityRepository<Nodo> nodoRepository = storageService.getRepository(Nodo.class);

        // Creo comprensori o
        comprensorioRepository.save(comprensorio);

        // Creo utenti
        utenteRepository.save(utente1);
        utenteRepository.save(utente2);

        // Creo foglie
        nodoRepository.save(matematica);
        nodoRepository.save(pianoforte);
        nodoRepository.save(stiratura);

        AuthService authService = new AuthService(storageService);
        authService.login("utente1", "password1");
        baseScambioStrategy = new BaseScambioStrategy(storageService, authService);
    }

    @Test
    @Description("Chiusura di 2 scambi diretti, come slide 13 della documentazione")
    void testChiusuraDiretta() {
        List<Scambio> scambi = List.of(
                createScambio(utente1, pianoforte, matematica, 10),
                createScambio(utente2, matematica, pianoforte, 10),
                createScambio(utente2, stiratura, matematica, 15)
        );
        List<LinkedList<Scambio>> expected = new ArrayList<>();
        LinkedList<Scambio> expected1 = new LinkedList<>();
        expected1.add(scambi.get(0));
        expected1.add(scambi.get(1));
        expected.add(expected1);

        // Aggiungo scambi al db
        saveScambi(scambi);

        List<LinkedList<Scambio>> result = baseScambioStrategy.chiudiScambi();

        // Refresh scambi
        refreshScambi(scambi);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, result), // Insiemi chiusi son corretti
                () -> Assertions.assertEquals(scambi.get(0).getChiusoDa(), scambi.get(1)), // Scambio 1 chiuso da scambio 2
                () -> Assertions.assertEquals(scambi.get(1).getChiusoDa(), scambi.get(0)) // Scambio 2 chiuso da scambio 1
        );
    }

    @Test
    @Description("Chiusura di 3 scambi indiretti, come slide 16 della documentazione")
    void testChiusuraCombinata() {
        CustomCostRoutingStrategy strategy = new CustomCostRoutingStrategy(nodi);
        CustomCostRoutingStrategy.costs.put(matematica, new HashMap<>(Map.of(
                pianoforte, 1.0,  // Matematica -> Pianoforte
                stiratura, 1.5      // Matematica -> Stiratura
        )));
        CustomCostRoutingStrategy.costs.put(pianoforte, new HashMap<>(Map.of(
                matematica, 1.0,  // Pianoforte -> Matematica
                stiratura, 1.5      // Pianoforte -> Stiratura
        )));
        CustomCostRoutingStrategy.costs.put(stiratura, new HashMap<>(Map.of(
                matematica, 1/1.5,  // Stiratura -> Matematica
                pianoforte, 1/1.5      // Stiratura -> Pianoforte
        )));

        List<Scambio> scambi = List.of(
                // r: Stiratura [15] -> o: Matematica
                createScambio(utente1, stiratura, matematica, 15, strategy),
                // r: Matematica [10] -> o: Pianoforte
                createScambio(utente2, matematica, pianoforte, 10, strategy),
                // r: Pianoforte [10] -> o: Stiratura
                createScambio(utente1, pianoforte, stiratura, 10, strategy)
        );
        List<LinkedList<Scambio>> expected = new ArrayList<>();
        LinkedList<Scambio> expected1 = new LinkedList<>();
        expected1.add(scambi.get(0));
        expected1.add(scambi.get(1));
        expected1.add(scambi.get(2));
        expected.add(expected1);

        // Aggiungo scambi al db
        saveScambi(scambi);

        List<LinkedList<Scambio>> result = baseScambioStrategy.chiudiScambi();

        // Refresh scambi
        refreshScambi(scambi);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expected, result), // Insiemi chiusi son corretti
                () -> Assertions.assertEquals(scambi.get(0).getChiusoDa(), scambi.get(1)), // Scambio 1 chiuso da scambio 2
                () -> Assertions.assertEquals(scambi.get(1).getChiusoDa(), scambi.get(2)), // Scambio 2 chiuso da scambio 3
                () -> Assertions.assertEquals(scambi.get(2).getChiusoDa(), scambi.get(0)) // Scambio 3 chiuso da scambio 1
        );
    }

    @Test
    @Description("Nessuna chiusura possibile")
    void testNessunaChiusura() {
        CustomCostRoutingStrategy strategy = new CustomCostRoutingStrategy(nodi);
        CustomCostRoutingStrategy.costs.put(matematica, new HashMap<>(Map.of(
                pianoforte, 1.0,  // Matematica -> Pianoforte
                stiratura, 1.5      // Matematica -> Stiratura
        )));
        CustomCostRoutingStrategy.costs.put(pianoforte, new HashMap<>(Map.of(
                matematica, 1.0,  // Pianoforte -> Matematica
                stiratura, 1.5      // Pianoforte -> Stiratura
        )));
        CustomCostRoutingStrategy.costs.put(stiratura, new HashMap<>(Map.of(
                matematica, 1/1.5,  // Stiratura -> Matematica
                pianoforte, 1/1.5      // Stiratura -> Pianoforte
        )));

        List<Scambio> scambi = List.of(
                // NON CHIUDUBILE (quantita diverse)
                // r: Matematica [1] -> o: Pianoforte
                createScambio(utente1, matematica, pianoforte, 1, strategy),
                // r: Pianoforte [3] -> o: Matematica
                createScambio(utente2, pianoforte, matematica, 3, strategy),

                // NON CHIUDIBILE (nessun percorso)
                // r: Stiratura [15] -> o: Matematica
                createScambio(utente1, stiratura, matematica, 15, strategy),
                // r: Matematica [15] -> o: Pianoforte
                createScambio(utente2, matematica, pianoforte, 15, strategy),
                // r: Pianoforte [10] -> o: Stiratura
                createScambio(utente1, pianoforte, stiratura, 10, strategy)
        );
        List<LinkedList<Scambio>> expected = new ArrayList<>();

        // Aggiungo scambi al db
        saveScambi(scambi);

        List<LinkedList<Scambio>> result = baseScambioStrategy.chiudiScambi();

        // Refresh scambi
        refreshScambi(scambi);

        Assertions.assertEquals(expected, result);
    }

    private record AlwaysOneRoutingStrategy(Collection<Nodo> nodi) implements RoutingComputationStrategy {
        @Override
        public double getRoutingCost(Nodo nodo1, Nodo nodo2) {
            return 1;
        }

        @Override
        public HashMap<Nodo, Double> getRoutingCostsFrom(Nodo nodo1) {
            HashMap<Nodo, Double> result = new HashMap<>();
            for (Nodo nodo2 : this.nodi) {
                if (!nodo1.equals(nodo2)) {
                    result.put(nodo2, this.getRoutingCost(nodo1, nodo2));
                }
            }
            return result;
        }
    }
    private record CustomCostRoutingStrategy(Collection<Nodo> nodi) implements RoutingComputationStrategy {
        public static final HashMap<Nodo, HashMap<Nodo, Double>> costs = new HashMap<>();

        @Override
        public double getRoutingCost(Nodo nodo1, Nodo nodo2) {
            return costs.get(nodo1).get(nodo2);
        }

        @Override
        public HashMap<Nodo, Double> getRoutingCostsFrom(Nodo nodo1) {
            HashMap<Nodo, Double> result = new HashMap<>();
            for (Nodo nodo2 : this.nodi) {
                if (!nodo1.equals(nodo2)) {
                    result.put(nodo2, this.getRoutingCost(nodo1, nodo2));
                }
            }
            return result;
        }
    }
}
