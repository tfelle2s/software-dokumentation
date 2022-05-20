package org.arc42.dokumentation.view.components.documentation;


import com.vaadin.flow.component.html.H3;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOLaufzeit;
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

@Route(value = "laufzeitsicht/beschreibung", layout = MainLayout.class)
public class ARC42LaufzeitBeschreibung extends Arc42NewDocumentationView{


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {

        super.init();

        getLaufzeitsicht().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getLaufzeitsicht().setOpened(true);
        getLaufzeitsichtBeschreibung().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

        dao = ImageDAOLaufzeit.getInstance();
        ImageDTO dto = (ImageDTO) dao.findById(url);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(new BreadCrumbComponent(new String[]{"Bausteinsicht","Beschreibung"}));

        if(dto != null ) {
            TextAreaComponent textAreaComponent = new TextAreaComponent("Laufzeitsicht-Beschreibung", dto.getDescription().equals("null") ? "" : dto.getDescription(), dao);
            textAreaComponent.getTextArea().addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                    event -> hasChanges(true));
        } else {
            verticalLayout.add(new H3("Sie müssen vorher ein Bild und eine UFX-Datei hochladen, bevor sie hier die Laufzeitsicht beschreiben!"));
        }
        add(verticalLayout);
    }
}