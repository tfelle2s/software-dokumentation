package org.arc42.dokumentation.view.components.documentation;

import org.arc42.dokumentation.control.logic.GoogleBigQueryController;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.main.Arc42DocumentationView;
import org.arc42.dokumentation.view.util.data.Roles;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveObserver;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class DocumentationAbstractView extends HorizontalLayout implements BeforeEnterObserver, BeforeLeaveObserver {

    // --Commented out by Inspection (17.05.22, 00:41):protected Goog// --Commented out by Inspection (17.05.22, 00:41):leBigQueryController googleBigQueryController;
// --Commented out by Inspection START (17.05.22, 00:41):
//    protected final Logger logger;
//    // --Commented out by Inspection (17.// --Commented out by Inspection (17.05.22, 00:41):05.22, 00:41):protected HorizontalLayout body;
// --Commented out by Inspection STOP (17.05.22, 00:41)
    protected Arc42DokuNameDAO dao;
    protected DokuNameDTO result;
    String url;
/*
    protected DocumentationAbstractView() {
        this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        setSizeFull();
        try {
            googleBigQueryController = GoogleBigQueryController.getInstance();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

 */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getUI().getSession().getAttribute(Roles.CURRENTUSER) == null) {
            UI.getCurrent().navigate("login");
        }
    }

    abstract void init();
}