package org.arc42.dokumentation.view.components.documentation;

import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.TechnischKontextDAO;
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
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;


@Route(value = "kontextabgrenzung/technischer-kontext", layout = MainLayout.class)
public class ARC42TechnischKontext extends Arc42NewDocumentationView{

    @Override
    public void beforeEnter(BeforeEnterEvent event) {super.beforeEnter(event);}

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        super.beforeLeave(event);
    }

    @Override
    public void init() {
        super.init();
        //Brotkrümelnavigation
        getKontextabgrenzung().setOpened(true);
        getKontextabgrenzung().getSummary().getElement().getStyle()
                .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getTechnischerKontext().getStyle()
                .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

        //View-Inhalt
        ARC42DAOAbstract<ImageDTO, String> dao = TechnischKontextDAO.getInstance();
        ImageDTO imageDTO = (ImageDTO) dao.findById(url);

        TextAreaComponent textAreaComponent =
                new TextAreaComponent("Technischer Kontext", imageDTO.getDescription(), dao);
        textAreaComponent.getTextArea()
                .addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                event -> hasChanges(true));

        //Hinzufügen der Komponenten
        VerticalLayout verticalLayout =
                new VerticalLayout(new BreadCrumbComponent(new String[]{"Kontextabgrenzung","Technisch"}),textAreaComponent);
        verticalLayout.setSizeFull();
        add(verticalLayout);
    }
}
