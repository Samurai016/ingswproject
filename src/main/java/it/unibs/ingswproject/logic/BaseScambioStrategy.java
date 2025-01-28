package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.Path;
import it.unibs.ingswproject.logic.graph.algorithms.DijkstraAlgorithm;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.models.repositories.ScambioRepository;
import org.h2.engine.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementazione della strategia di scambio base
 *
 * @author Nicolò Rebaioli
 */
public class BaseScambioStrategy implements ScambioStrategy {
    private final WeightComputationStrategy weightComputationStrategy = new WeightComputationStrategy() {
        @Override
        public double getMaxAcceptedWeight() {
            return 0;
        }

        @Override
        public double getMinAcceptedWeight() {
            return 0;
        }

        @Override
        public double getInitialWeight() {
            return 0;
        }

        @Override
        public double computeWeight(double currentWeight, double targetWeight) {
            return currentWeight + targetWeight;
        }
    };
    private final StorageService storageService;
    private final AuthService authService;
    private Graph graph = null;

    public BaseScambioStrategy(StorageService storageService, AuthService authService) {
        this.storageService = storageService;
        this.authService = authService;
    }

    @Override
    public List<LinkedList<Scambio>> chiudiScambi() {
        // Ottengo tutti gli scambi del comprensorio dell'utente, che sono aperti
        Utente utente = this.authService.getCurrentUser();
        ScambioRepository scambioRepository = (ScambioRepository) this.storageService.getRepository(Scambio.class);
        List<Scambio> scambi = scambioRepository
                .findByComprensorio(utente.getComprensorio())
                .stream()
                .filter(s -> s.getStato().equals(Scambio.Stato.APERTO))
                .toList();

        // Trovo gli scambi che si possono chiudere
        List<LinkedList<Scambio>> insiemiChiusi = this.findInsiemiChiusi(scambi);

        // Chiudo gli scambi
        for (LinkedList<Scambio> insiemeChiuso : insiemiChiusi) {
            Scambio nodoPartenza = insiemeChiuso.getFirst();
            for (int i = 0; i < insiemeChiuso.size(); i++) {
                Scambio chiusoDa = i == insiemeChiuso.size() - 1 ? nodoPartenza : insiemeChiuso.get(i + 1);
                scambioRepository.save(insiemeChiuso.get(i).chiudi(chiusoDa));
            }
        }

        return insiemiChiusi;
    }

    /**
     * Metodo che permette di trovare gli insiemi chiusi di scambi che si possono chiudere.
     * Un insieme chiuso è visto come una lista concatenata di Scambi che si chiudono.
     *
     * @param scambi Lista di tutti gli scambi da analizzare
     * @return Lista di insiemi chiusi
     */
    public List<LinkedList<Scambio>> findInsiemiChiusi(List<Scambio> scambi) {
        this.generateGraph(scambi);
        List<LinkedList<Integer>> insiemiChiusi = new ArrayList<>();

        for (int i = 0; i < scambi.size(); i++) {
            DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this.graph, this.weightComputationStrategy);

            // Calcolo tutti i cammini minimi tra i nodi
            List<Path> distanze = new ArrayList<>();
            for (int j = 0; j < scambi.size(); j++) {
                distanze.addAll(dijkstraAlgorithm.getPathsFrom(j));
            }

            // Rimuovo i cammini banali (quelli composti da un solo vertice)
            distanze.removeIf(p -> p.getVertices().size() == 1);

            // Per ogni vertice del grafo (scambio)
            // Cerco se c'è un cammino minimo che ritorna al vertice stesso
            if (distanze.isEmpty()) {
                break; // Non ho più cammini da analizzare
            }

            int uIndex = i;
            List<Path> camminiChePartonoDaU = distanze.stream()
                    .filter(p -> p.getVertices().getFirst() == uIndex)
                    .toList();
            List<Path> camminiCheArrivanoAU = distanze.stream()
                    .filter(p -> p.getVertices().getLast() == uIndex)
                    .toList();

            // Se non ci sono cammini che partono da u o che arrivano a u
            // allora non posso chiudere lo scambio
            if (camminiChePartonoDaU.isEmpty() || camminiCheArrivanoAU.isEmpty()) {
                continue;
            }

            // Altrimenti genero un insieme chiuso che chiude lo scambio
            LinkedList<Integer> camminoTotale = this.getCamminoTotale(camminiChePartonoDaU, camminiCheArrivanoAU);

            // Aggiungo il cammino totale all'insieme chiuso
            insiemiChiusi.add(camminoTotale);

            // Rimuovo gli archi usati dal grafo
            for (int j = 0; j < camminoTotale.size() - 1; j++) {
                this.graph.removeEdge(camminoTotale.get(j), camminoTotale.get(j + 1));
            }
        }

        // Converto gli insiemi chiusi in scambi
        List<LinkedList<Scambio>> insiemiChiusiScambi = new ArrayList<>();
        for (LinkedList<Integer> insiemeChiuso : insiemiChiusi) {
            LinkedList<Scambio> insiemeChiusoScambi = new LinkedList<>();
            insiemeChiuso.stream()
                    .map(scambi::get)
                    .forEach(insiemeChiusoScambi::add);
            insiemiChiusiScambi.add(insiemeChiusoScambi);
        }

        return insiemiChiusiScambi;
    }

    /**
     * Restituisce il cammino totale che parte da u e arriva a u
     * Il cammino totale non include come ultimo vertice il vertice u di partenza
     *
     * @param camminiChePartonoDaU Lista dei cammini che partono da u
     * @param camminiCheArrivanoAU Lista dei cammini che arrivano a u
     * @return Il cammino totale
     */
    private LinkedList<Integer> getCamminoTotale(List<Path> camminiChePartonoDaU, List<Path> camminiCheArrivanoAU) {
        LinkedList<Integer> camminoPartenza = camminiChePartonoDaU.getFirst().getVertices();
        LinkedList<Integer> camminoArrivo = camminiCheArrivanoAU.getFirst().getVertices();

        LinkedList<Integer> camminoArrivoSoloIntermedi = new LinkedList<>(camminoArrivo);
        camminoArrivoSoloIntermedi.removeFirst();
        camminoArrivoSoloIntermedi.removeLast();

        LinkedList<Integer> camminoTotale = new LinkedList<>();
        camminoTotale.addAll(camminoPartenza);
        camminoTotale.addAll(camminoArrivoSoloIntermedi);
        return camminoTotale;
    }

    /**
     * Genera il grafo a partire dagli scambi
     * Il grafo è composto dagli scambi come vertici e dagli archi che collegano due scambi se:
     * * il prodotto richiesto da uno è uguale a quello prodotto dall'altro
     * * la quantità richiesta è uguale alla quantità prodotta
     *
     * @param scambi La lista di scambi
     */
    public void generateGraph(List<Scambio> scambi) {
        this.graph = new Graph(scambi.size());

        for (int i = 0; i < scambi.size(); i++) {
            Scambio u = scambi.get(i);

            for (int j = 0; j < scambi.size(); j++) {
                Scambio v = scambi.get(j);

                // Se il prodotto richiesto da v è uguale a quello prodotto da u con quantità uguale
                // allora posso creare un arco tra u e v
                if (
                        u.getOfferta().equals(v.getRichiesta()) &&
                        v.getQuantitaRichiesta() == u.getQuantitaOfferta()
                ) {
                    this.graph.addEdge(i, j, 1);
                }
            }
        }
    }
}
