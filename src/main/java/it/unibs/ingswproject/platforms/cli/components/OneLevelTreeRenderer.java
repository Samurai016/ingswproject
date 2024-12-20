package it.unibs.ingswproject.platforms.cli.components;

import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.translations.Translator;

import java.util.List;

/**
 * Classe per il rendering di un albero con un solo livello di figli
 * Stampa solo i figli del nodo radice, che possono essere selezionati
 *
 * @author Nicolò Rebaioli
 */
public class OneLevelTreeRenderer extends TreeRenderer {
    private final Translator translator;

    public OneLevelTreeRenderer(Translator translator) {
        this.translator = translator;
    }

    public OneLevelTreeRenderer(Translator translator, Nodo root) {
        this(translator);
        this.setRoot(root);
    }

    @Override
    public void render() {
        if (this.root == null) {
            throw new IllegalStateException("root_cannot_be_null");
        }

        // Stampo header
        String nomeAttributo = this.root.getNomeAttributo() == null
                ? ""
                : String.format(this.translator.translate("tree_renderer_attribute_pattern"), this.root.getNomeAttributo());
        String descrizione = this.getDescrizione(this.root);
        System.out.printf(this.translator.translate("tree_renderer_child_pattern"), this.root.getNome(), descrizione, nomeAttributo);
        System.out.println();

        // No items
        List<Nodo> tree = this.root.getFigli();
        if (tree.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
            return;
        }

        // Stampo figli
        for (int i = 0; i < tree.size(); i++) {
            Nodo figlio = tree.get(i);
            String descrizioneFiglio = this.getDescrizione(figlio);
            String valoreAttributo = this.getValoreAttributo(figlio);

            System.out.print("\t");
            System.out.printf(
                    this.translator.translate("tree_renderer_selectable_pattern"),
                    i + 1,
                    figlio.getNome(),
                    valoreAttributo,
                    descrizioneFiglio
            );
            System.out.println();
        }
    }

    private String getDescrizione(Nodo nodo) {
        return (nodo.getDescrizione() == null || nodo.getDescrizione().isBlank()) ? "" : String.format(this.translator.translate("tree_renderer_description_pattern"), nodo.getDescrizione());
    }

    private String getValoreAttributo(Nodo nodo) {
        return (nodo.getValoreAttributo() == null || nodo.getValoreAttributo().isBlank()) ? "" : String.format(this.translator.translate("tree_renderer_attribute_value_pattern"), nodo.getValoreAttributo());
    }
}
