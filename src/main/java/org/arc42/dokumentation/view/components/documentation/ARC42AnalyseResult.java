package org.arc42.dokumentation.view.components.documentation;

import org.arc42.dokumentation.model.dao.arc42documentation.AnalyseResultDAO;
import org.arc42.dokumentation.view.main.MainLayout;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

@Route(value = "ergebnisse", layout = MainLayout.class)
public class ARC42AnalyseResult extends Arc42NewDocumentationView {

    private String arcId;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.arcId = event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        getResultsPage().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getResultsPage().setOpened(true);

        AnalyseResultDAO dao = AnalyseResultDAO.getInstance();
        String htmlContent = dao.findById(this.arcId);

        IFrame iFrame = new IFrame();
        if (htmlContent != null) {
            iFrame.setSrcdoc(htmlContent);
            iFrame.setSizeFull();
            add(iFrame);
        } else {
            add(new Label("Es wurde nichts dokumentiert."));
        }
    }

}