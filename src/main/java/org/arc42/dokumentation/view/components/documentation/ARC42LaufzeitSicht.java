package org.arc42.dokumentation.view.components.documentation;


import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOLaufzeit;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.UploadComponent;
import org.arc42.dokumentation.view.main.MainLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;


@Route(value = "laufzeitsicht/diagramm", layout = MainLayout.class)
public class ARC42LaufzeitSicht extends Arc42NewDocumentationView{

    @Override
    public void beforeEnter(BeforeEnterEvent event) {super.beforeEnter(event);}

    @Override
    public void init() {
        super.init();
        getLaufzeitsicht().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getLaufzeitsicht().setOpened(true);
        getLaufzeitsichtDiagramm().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");


        ARC42DAOAbstract dao = ImageDAOLaufzeit.getInstance();
        VerticalLayout verticalLayout = new VerticalLayout();
        H3 header  =new H3("Laufzeitsicht-Diagramm");

        UploadComponent uploadComponent =new UploadComponent(dao,url);

        verticalLayout.setSizeFull();
        verticalLayout.add(new BreadCrumbComponent(new String[]{"Bausteinsicht","Diagramm"}),header,uploadComponent);
        add(verticalLayout);
    }
}