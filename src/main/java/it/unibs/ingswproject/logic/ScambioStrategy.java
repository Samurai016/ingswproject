package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.entities.Scambio;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public interface ScambioStrategy {
    HashMap<Scambio, Collection<Scambio>> findScambiChiudibili(List<Scambio> scambi);
}
