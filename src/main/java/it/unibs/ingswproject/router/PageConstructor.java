package it.unibs.ingswproject.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione usata per identificare quale è il costruttore che PageFactory deve usare per creare una pagina
 * Ogni classe che estende PageController deve avere uno e un solo costruttore annotato con @PageConstructor
 *
 * @author Nicolò Rebaioli
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
public @interface PageConstructor {
}
