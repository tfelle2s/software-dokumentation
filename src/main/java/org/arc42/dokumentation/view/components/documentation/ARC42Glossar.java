package org.arc42.dokumentation.view.components.documentation;


import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.GlossarDAO;
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

@Route(value = "glossar", layout = MainLayout.class)
public class ARC42Glossar extends Arc42NewDocumentationView{

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String arcId = event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        getGlossar().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getGlossar().setOpened(true);
        ARC42DAOAbstract dao = GlossarDAO.getInstance();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        ImageDTO imageDTO = (ImageDTO) dao.findById(url);

        TextAreaComponent textAreaComponent = new TextAreaComponent("Glossar", imageDTO.getDescription(), dao);
        textAreaComponent.getTextArea().addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                event -> hasChanges(true));
        verticalLayout.add(new BreadCrumbComponent(new String[]{"Glossar"}),textAreaComponent);
        add(verticalLayout);
    }
}

