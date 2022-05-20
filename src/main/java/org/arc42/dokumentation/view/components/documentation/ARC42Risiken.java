package org.arc42.dokumentation.view.components.documentation;


import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikenDAO;
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

@Route(value = "risiken", layout = MainLayout.class)
public class ARC42Risiken extends Arc42NewDocumentationView{

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String arcId = event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        getRisiken().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getRisiken().setOpened(true);

        ARC42DAOAbstract dao = RisikenDAO.getInstance();
        ImageDTO imageDTO = (ImageDTO) dao.findById(url);


        TextAreaComponent textAreaComponent = new TextAreaComponent("Risiken", imageDTO.getDescription(), dao);
        textAreaComponent.getTextArea().addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                event -> hasChanges(true));
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();



        verticalLayout.add(new BreadCrumbComponent(new String[]{"Risiken"}),textAreaComponent);
        add(verticalLayout);
    }
}