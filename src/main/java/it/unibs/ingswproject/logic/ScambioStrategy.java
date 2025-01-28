package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.entities.Scambio;

import java.util.LinkedList;
import java.util.List;

/**
 * Interfaccia per la strategia che permette di gestire le operazioni da effettuare tra gli scambi.
 *
 * @author Nicolò Rebaioli
 */
public interface ScambioStrategy {
    List<LinkedList<Scambio>> chiudiScambi();
}
