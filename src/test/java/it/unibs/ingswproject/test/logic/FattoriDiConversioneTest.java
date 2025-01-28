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
    private static final StorageService storageService = new StorageService(DB.getDefault());
    private static final FattoreDiConversioneStrategy fattoreDiConversioneStrategy = new BaseFattoreDiConversioneStrategy(storageService);
    private static Nodo root;

    @BeforeAll
    static void setUpAll() {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        root = Nodo.createRoot("Frutta", null, "colore");
        root.getFigli().addAll(List.of(new Nodo[]{
                Nodo.createFoglia("Mela", null, "rosso", root),
                Nodo.createFoglia("Banana", null, "giallo", root)
        }));

        storageService.getRepository(Nodo.class).save(root);
    }

    @Test
    @Order(1)
    @DisplayName("Test only child")
        // Frutta
        //  - Mela
        //  - Banana
        //  - PERA
    void TestOnlyChild() {
        try (Transaction innerTransaction = storageService.getDatabase().beginTransaction()) {
            EntityRepository<Nodo> repo = storageService.getRepository(Nodo.class);
            Nodo pera = Nodo.createFoglia("Pera", null, "verde", root);
            repo.save(pera);
            root = repo.find(root.getId()); // Refresh root

            List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(root);
            Assertions.assertEquals(2, FDCs.size());
            innerTransaction.rollback();
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test child with 1 root arch")
        // Frutta
        //  - Mela
        //  - Banana
        //      - Banana Matura
        //      - Banana Acerba
    void TestChildOneRootArch() {
        try (Transaction innerTransaction = storageService.getDatabase().beginTransaction()) {
            EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
            EntityRepository<FattoreDiConversione> repoFDC = storageService.getRepository(FattoreDiConversione.class);

            // Mela e Banana
            List<Nodo> children = root.getFigli();
            Nodo mela = children.stream().filter(n -> n.getNome().equals("Mela")).findFirst().orElse(null);
            Nodo banana = children.stream().filter(n -> n.getNome().equals("Banana")).findFirst().orElse(null);

            // Add FDCs
            repoFDC.save(new FattoreDiConversione(mela, banana, 1.0));

            // Banana Matura e Banana Acerba
            assert banana != null;
            repoNodi.save(banana.setNomeAttributo("maturazione"));
            Nodo bananaMatura = Nodo.createFoglia("Banana Matura", null, "giallo", banana);
            Nodo bananaAcerba = Nodo.createFoglia("Banana Acerba", null, "verde", banana);
            banana.getFigli().addAll(List.of(new Nodo[]{bananaMatura, bananaAcerba}));
            repoNodi.save(bananaMatura);
            repoNodi.save(bananaAcerba);
            root = repoNodi.find(root.getId()); // Refresh root

            List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(banana);
            Assertions.assertEquals(2, FDCs.size());
            innerTransaction.rollback();
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test child with 2 root arch")
        // Frutta
        //  - Mela
        //  - Banana
        //      - Banana Matura
        //      - Banana Acerba
        //  - Pera
    void TestChildTwoRootArch() {
        try (Transaction innerTransaction = storageService.getDatabase().beginTransaction()) {
            EntityRepository<Nodo> repoNodi = storageService.getRepository(Nodo.class);
            EntityRepository<FattoreDiConversione> repoFDC = storageService.getRepository(FattoreDiConversione.class);

            // Mela e Banana
            List<Nodo> children = root.getFigli();
            Nodo mela = children.stream().filter(n -> n.getNome().equals("Mela")).findFirst().orElse(null);
            Nodo banana = children.stream().filter(n -> n.getNome().equals("Banana")).findFirst().orElse(null);

            // Pera
            Nodo pera = Nodo.createFoglia("Pera", null, "verde", root);
            root.getFigli().add(pera);
            repoNodi.save(pera);
            root = repoNodi.find(root.getId()); // Refresh root

            // Add FDCs
            repoFDC.save(new FattoreDiConversione(mela, banana, 1.0));
            repoFDC.save(new FattoreDiConversione(banana, pera, 1.0));

            // Banana Matura e Banana Acerba
            assert banana != null;
            repoNodi.save(banana.setNomeAttributo("maturazione"));
            Nodo bananaMatura = Nodo.createFoglia("Banana Matura", null, "giallo", banana);
            Nodo bananaAcerba = Nodo.createFoglia("Banana Acerba", null, "verde", banana);
            banana.getFigli().addAll(List.of(new Nodo[]{bananaMatura, bananaAcerba}));
            repoNodi.save(bananaMatura);
            repoNodi.save(bananaAcerba);

            List<FattoreDiConversione> FDCs = fattoreDiConversioneStrategy.getFattoriDiConversionToSet(banana);
            Assertions.assertEquals(3, FDCs.size());
            innerTransaction.rollback();
        }
    }

    @AfterAll
    static void tearDown() {
    }
}
