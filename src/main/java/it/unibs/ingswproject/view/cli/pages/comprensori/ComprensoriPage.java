package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.router.CliConstructor;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensoriPage extends CliPage {
    protected AuthService authService;
    protected StorageService storageService;
    protected CliPageFactory pageFactory;

    @CliConstructor
    public ComprensoriPage(CliApp app, AuthService authService, StorageService storageService, CliPageFactory pageFactory) {
        super(app);
        this.authService = authService;
        this.storageService = storageService;
        this.pageFactory = pageFactory;

        this.commands.put('1', "Aggiungi comprensorio");
    }

    @Override
    protected String getName() {
        return "Comprensori";
    }

    @Override
    protected boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    protected void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        try {
            if (input == '1') {
                this.app.navigateTo(this.pageFactory.generatePage(AddComprensorioPage.class));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show comprensori
        System.out.println("Comprensori:");
        List<Comprensorio> comprensori = this.storageService.getRepository(Comprensorio.class).findAll();

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