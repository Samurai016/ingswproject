package it.unibs.ingswproject.platforms.cli.elements;

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
        System.out.printf(this.translator.translate("tree_renderer_child_pattern"), this.root.getNome(), nomeAttributo);
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
            System.out.printf("\t" + this.translator.translate("tree_renderer_selectable_pattern"), i+1, figlio.getNome());
            System.out.println();
        }
    }
}
