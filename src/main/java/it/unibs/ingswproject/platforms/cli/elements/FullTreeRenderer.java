package it.unibs.ingswproject.platforms.cli.elements;

import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.translations.Translator;

import java.util.List;

/**
 * Classe per il rendering di un albero completo
 * Stampa tutti i nodi dell'albero, con la possibilità di selezionare i nodi del primo livello
 *
 * @see TreeRenderer
 */
public class FullTreeRenderer extends TreeRenderer {
    public static final int SELECTABLE_LEVEL = 1;
    private final Translator translator;

    public FullTreeRenderer(Translator translator) {
        this.translator = translator;
    }

    public FullTreeRenderer(Translator translator, Nodo root) {
        this(translator);
        this.setRoot(root);
    }

    public void render() {
        if (this.root == null) {
            throw new IllegalStateException("root_cannot_be_null");
        }

        this.renderNode(this.root, "", 0, 0);
    }

    private void renderNode(Nodo node, String prefix, int deepLevel, int index) {
        // Stampo elemento
        String nomeAttributo = this.root.getNomeAttributo() == null
                ? ""
                : String.format(this.translator.translate("tree_renderer_attribute_pattern"), this.root.getNomeAttributo());
        if (deepLevel == SELECTABLE_LEVEL) { // Se è il livello selezionabile, posso selezionare il nodo
            System.out.printf(prefix + this.translator.translate("tree_renderer_selectable_pattern"), index, node.getNome(), nomeAttributo);
        } else {
            System.out.printf(prefix + this.translator.translate("tree_renderer_child_pattern"), node.getNome(), nomeAttributo);
        }
        System.out.println();

        // No items
        List<Nodo> tree = node.getFigli();
        if (tree.isEmpty() && deepLevel == 0) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
            return;
        }

        for (int i = 0; i < tree.size(); i++) {
            Nodo figlio = tree.get(i);
            this.renderNode(figlio, prefix + "\t", deepLevel + 1, i + 1);
        }
    }
}
