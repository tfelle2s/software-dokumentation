package org.arc42.dokumentation.view.components.documentation;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.NotificationWindow;
import org.arc42.dokumentation.view.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "titel", layout = MainLayout.class)
public class ARC42Titel extends Arc42NewDocumentationView {
    private Arc42DokuNameDAO dao;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {super.beforeEnter(event);}

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        super.beforeLeave(event);
    }

    @Override
    public void init() {
        super.init();
        getEinfZiele().setOpened(true);
        getEinfZiele().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getTitel().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

        dao = new Arc42DokuNameDAO();
        DokuNameDTO dokuNameDTO = dao.findById(url);

        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        TextField titelTextField = new TextField("Titel");
        titelTextField.setValue(dokuNameDTO.getName());

        titelTextField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            speichern.setText("Ändern");
            hasChanges(true);
        });

        speichern.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            dokuNameDTO.setName(titelTextField.getValue());
            dao.update(dokuNameDTO);
            new NotificationWindow("Titel wurde erfolgreich gespeichert!", NotificationType.MEDIUM, NotificationType.SUCCESS);
            hasChanges(false);
            speichern.setText("Speichern");
        });

        add(new VerticalLayout(new BreadCrumbComponent(new String[]{"Einführung und Ziele", "Titel"}), titelTextField, speichern));
    }
}
