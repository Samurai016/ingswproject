package it.unibs.ingswproject.platforms.cli.controllers.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.SystemInfoPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class SystemInfoPageController extends CliPageController {
    protected final PageFactory pageFactory;

    @PageConstructor
    public SystemInfoPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService) {
        super(app, translator);
        this.pageFactory = pageFactory;
        this.view = new SystemInfoPageView(app, this, translator, cliUtils, projectUtils, authService, storageService);
    }

    @Override
    public String getName() {
        return this.translator.translate("system_info_page_title");
    }

    @Override
    public boolean canView() {
        return true;
    }
}