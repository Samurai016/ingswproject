package it.unibs.ingswproject.test.logic;

import io.ebean.DB;
import io.ebean.Transaction;
import it.unibs.ingswproject.logic.BaseFattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FattoriDiConversioneTest {
    private static final StorageService storageService = new StorageService(DB.getDefault()); // Il database presente qui è specificatamente configurato per i test nel file application-test.conf
    private static final FattoreDiConversioneStrategy fattoreDiConversioneStrategy = new BaseFattoreDiConversioneStrategy(storageService);
    private static Nodo root;
    private Transaction transaction;

    @BeforeAll
    static void setUpAll() {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        EntityRepository<Nodo> repo = storageService.getRepository(Nodo.class);
        root = Nodo.createRoot("Gerarchia 1", null, "attributo_ger1");
        repo.save(root);
    }

    @BeforeEach
    void setUp() {
        this.transaction = DB.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        if (this.transaction != null && this.transaction.isActive()) {
            this.transaction.rollback();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Cammino P1: Una sola gerarchia senza foglie")
    void testCamminoP1() {
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(root);
        Assertions.assertTrue(FDCs.isEmpty(), "La lista dei fattori di conversione dovrebbe essere vuota");
    }

    @Test
    @Order(2)
    @DisplayName("Cammino P2: Una sola gerarchia con una sola foglia")
    void testCamminoP2() {
        EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
        Nodo foglia = Nodo.createFoglia("NonRadice", null, "attributo", root);
        repoNodi.save(foglia);

        root = repoNodi.find(root.getId()); // Ricarico il nodo root per assicurarmi che le modifiche siano visibili
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(foglia);
        Assertions.assertTrue(FDCs.isEmpty(), "La lista dei fattori di conversione dovrebbe essere vuota");
    }

    @Test
    @Order(3)
    @DisplayName("Cammino P3: Una sola gerarchia, 2 foglie ma FDC tra esse già esiste")
    void testCamminoP3() {
        EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
        EntityRepository<FattoreDiConversione> repoFDC = storageService.getRepository(FattoreDiConversione.class);

        Nodo foglia1 = Nodo.createFoglia("Foglia1", null, "attributo", root);
        Nodo foglia2 = Nodo.createFoglia("Foglia2", null, "attributo2", root);
        repoNodi.save(foglia1);
        repoNodi.save(foglia2);

        FattoreDiConversione fdc = new FattoreDiConversione(foglia1, foglia2, 1.0); // Fattore è un valore non rilevante
        repoFDC.save(fdc);

        root = repoNodi.find(root.getId()); // Ricarico il nodo root per assicurarmi che le modifiche siano visibili
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(root);
        Assertions.assertTrue(FDCs.isEmpty(), "La lista dei fattori di conversione dovrebbe essere vuota");
    }

    @Test
    @Order(4)
    @DisplayName("Cammino P4: Una sola gerarchia, 2 foglie, 1 FDC tra le 2")
    void testCamminoP4() {
        EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);

        Nodo foglia1 = Nodo.createFoglia("Foglia1", null, "attributo", root);
        Nodo foglia2 = Nodo.createFoglia("Foglia2", null, "attributo2", root);
        repoNodi.save(foglia1);
        repoNodi.save(foglia2);

        FattoreDiConversione expectedFDC = new FattoreDiConversione(foglia1, foglia2, 1.0); // Fattore è un valore non rilevante

        root = repoNodi.find(root.getId()); // Ricarico il nodo root per assicurarmi che le modifiche siano visibili
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(root);
        List<FattoreDiConversione> expectedFDCs = List.of(expectedFDC);
        Assertions.assertEquals(expectedFDCs.size(), FDCs.size(), "La lista dei fattori di conversione dovrebbe contenere il FDC tra le due foglie");
    }

    @Test
    @Order(5)
    @DisplayName("Cammino P5: Ex-foglia diventa categoria (1 FDC) con una sola foglia, 1 FDC tra nuova foglia ed ex-connesso")
    void testCamminoP5() {
        EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
        EntityRepository<FattoreDiConversione> repoFDC = storageService.getRepository(FattoreDiConversione.class);

        Nodo foglia1 = Nodo.createFoglia("N1", null, "attributo", root);
        Nodo exFoglia2 = Nodo.createFoglia("N2", null, "attributo2", root);
        repoNodi.save(foglia1);
        repoNodi.save(exFoglia2);

        FattoreDiConversione fdc = new FattoreDiConversione(foglia1, exFoglia2, 1.0); // Fattore è un valore non rilevante
        repoFDC.save(fdc);

        Nodo nuovaFoglia = Nodo.createFoglia("N1.1", null, "attributo3", exFoglia2);
        repoNodi.save(nuovaFoglia);

        FattoreDiConversione expectedFDC = new FattoreDiConversione(foglia1, nuovaFoglia, 1.0); // Fattore è un valore non rilevante

        exFoglia2 = repoNodi.find(exFoglia2.getId()); // Ricarico l'ex-foglia per assicurarmi che le modifiche siano visibili
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(exFoglia2);
        List<FattoreDiConversione> expectedFDCs = List.of(expectedFDC);
        Assertions.assertEquals(expectedFDCs.size(), FDCs.size(), "La lista dei fattori di conversione dovrebbe contenere il FDC tra la nuova foglia e l'ex-foglia");
    }

    @Test
    @Order(6)
    @DisplayName("Cammino P6: Nuova gerarchia aggiunta e altra gerarchia con foglie")
    void testCamminoP8() {
        EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
        EntityRepository<FattoreDiConversione> repoFDC = storageService.getRepository(FattoreDiConversione.class);

        Nodo foglia1 = Nodo.createFoglia("Foglia1", null, "attributo", root);
        repoNodi.save(foglia1);

        Nodo nuovaGerarchia = Nodo.createRoot("Gerarchia 2", null, "attributo3");
        repoNodi.save(nuovaGerarchia);

        FattoreDiConversione fdc = new FattoreDiConversione(foglia1, nuovaGerarchia, 1.0); // Fattore è un valore non rilevante
        repoFDC.save(fdc);

        nuovaGerarchia = repoNodi.find(nuovaGerarchia.getId()); // Ricarico il nodo root per assicurarmi che le modifiche siano visibili
        List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(nuovaGerarchia);
        List<FattoreDiConversione> expectedFDCs = List.of(fdc);
        Assertions.assertEquals(expectedFDCs.size(), FDCs.size(), "La lista dei fattori di conversione dovrebbe contenere il FDC tra la foglia della prima gerarchia e la nuova gerarchia");
    }
}
