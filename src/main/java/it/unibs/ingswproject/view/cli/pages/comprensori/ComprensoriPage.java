package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.view.cli.App;
import it.unibs.ingswproject.view.cli.CliPage;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensoriPage extends CliPage {
    public ComprensoriPage(App app) {
        super(app);

        this.commands.put('1', "Aggiungi comprensorio");
    }

    @Override
    protected String getName() {
        return "Comprensori";
    }

    @Override
    protected boolean canView() {
        return AuthService.getInstance().isLoggedIn() && AuthService.getInstance().getCurrentUser().isConfiguratore();
    }

    @Override
    protected void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        if (input == '1') {
            this.app.navigateTo(new AddComprensorioPage(this.app));
        }
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show comprensori
        System.out.println("Comprensori:");
        List<Comprensorio> comprensori = StorageService.getInstance().getRepository(Comprensorio.class).findAll();

        if (comprensori.isEmpty()) {
            System.out.println("\tNessun comprensorio presente");
        } else {
            for (int i = 0; i < comprensori.size(); i++) {
                Comprensorio comprensorio = comprensori.get(i);
                System.out.println("\t" + (i + 1) + ". " + comprensorio.getNome());
            }
        }

        System.out.println();
    }
}