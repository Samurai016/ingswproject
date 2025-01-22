package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.entities.Scambio;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public interface ScambioStrategy {
    List<LinkedList<Scambio>> findScambiChiudibili(List<Scambio> scambi);
}
