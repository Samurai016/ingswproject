package it.unibs.ingswproject.platforms.cli.errors.exceptions;

/**
 * Eccezione lanciata quando l'utente annullare l'operazione in corso
 * es. Durante l'inserimento di un valore digita la chiave per uscire
 *
 * @see it.unibs.ingswproject.platforms.cli.utils.CliUtils#readFromConsoleQuittable(String)
 * @see it.unibs.ingswproject.platforms.cli.utils.CliUtils#readFromConsoleQuittable(String, boolean) 
 * @author Nicolò Rebaioli
 */
public class CliQuitException extends Exception {
}
