package org.arc42.dokumentation.view.components.documentation;


import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOBaustein;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.TextAreaComponent;
import org.arc42.dokumentation.view.main.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;


@Route(value = "bausteinsicht/beschreibung", layout = MainLayout.class)
public class ARC42BausteinsichtBeschreibung extends Arc42NewDocumentationView{

    ARC42DAOAbstract dao;


    @Override
    public void beforeEnter(BeforeEnterEvent event) {super.beforeEnter(event);}

    @Override
    public void init() {
        super.init();
        getBausteinsicht().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getBausteinsicht().setOpened(true);
        getBausteinsichtBeschreibung().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

        dao = ImageDAOBaustein.getInstance();
        ImageDTO dto = (ImageDTO) dao.findById(url);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(new BreadCrumbComponent(new String[]{"Bausteinsicht","Beschreibung"}));

        if(dto != null ) {
            TextAreaComponent textAreaComponent = new TextAreaComponent("Bausteinsicht-Beschreibung", dto.getDescription().equals("null") ? "" : dto.getDescription(), dao);
            textAreaComponent.getTextArea().addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                    event -> hasChanges(true));
            verticalLayout.add(textAreaComponent);
        } else {
            verticalLayout.add(new H3("Sie m??ssen vorher ein Bild und eine UFX-Datei hochladen, bevor sie hier die Bausteinsicht beschreiben!"));
        }
        add(verticalLayout);
    }
}
