package it.unibs.ingswproject.platforms.cli.views.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.SystemInfoPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class SystemInfoPageView extends CliPageView {
    protected final StorageService storageService;

    public SystemInfoPageView(CliApp app, SystemInfoPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService, StorageService storageService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        System.out.printf(this.translator.translate("system_info_page_version"), this.projectUtils.getProjectVersion());
        System.out.println();
        System.out.printf(this.translator.translate("system_info_page_database"), this.storageService.getDatabase().name());
        System.out.println();
        System.out.printf(this.translator.translate("system_info_page_authors"), this.projectUtils.get("authors"));
        System.out.println();
        System.out.printf(this.translator.translate("system_info_page_copyright"), this.projectUtils.get("copyright"));
        System.out.println();

        System.out.println();
    }
}