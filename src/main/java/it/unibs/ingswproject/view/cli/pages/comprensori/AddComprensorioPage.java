package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;

import java.util.Scanner;

/**
 * @author Nicolò Rebaioli
 */
public class AddComprensorioPage extends CliPage {
    public AddComprensorioPage(CliApp app) {
        super(app);
    }

    @Override
    protected String getName() {
        return "Aggiungi comprensorio";
    }

    @Override
    protected boolean canView() {
        return AuthService.getInstance().isLoggedIn() && AuthService.getInstance().getCurrentUser().isConfiguratore();
    }

    @Override
    public void render() {
        System.out.println();

        try {
            Scanner scanner = new Scanner(System.in);
            Comprensorio comprensorio = new Comprensorio();

            System.out.print("Inserisci il nome del comprensorio (END per annullare): ");
            String nome = scanner.nextLine();
            if (nome.equals("END")) {
                this.app.goBack();
                return;
            }

            comprensorio.setNome(nome);

            System.out.println();
            System.out.println("Inserimento nel database...");
            StorageService.getInstance().getRepository(Comprensorio.class).create(comprensorio);

            System.out.println("Comprensorio inserito con successo, premi invio per tornare indietro");
            scanner.nextLine();
        } catch (Throwable e) {
            System.out.println("Errore: " + e.getMessage());
            CliApp.waitForInput();
        }

        this.app.goBack();
    }
}