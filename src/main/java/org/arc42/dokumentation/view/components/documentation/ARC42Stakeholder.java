package org.arc42.dokumentation.view.components.documentation;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.StakeholderDAO;
import org.arc42.dokumentation.model.dto.documentation.StakeholderDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.NotificationWindow;
import org.arc42.dokumentation.view.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

import java.util.List;
import java.util.Optional;

@Route(value = "stakeholder", layout = MainLayout.class)
public class ARC42Stakeholder extends Arc42NewDocumentationView {

    TextField rolle;
    TextField erwartung;
    TextField kontakt;
    private Grid<StakeholderDTO> stakeholderGrid;
    private StakeholderDTO stakeholderDTO;
    private StakeholderDAO dao;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        getEinfZiele().setOpened(true);
        getEinfZiele().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getStakeholder().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

        dao = StakeholderDAO.getInstance();
        List<StakeholderDTO> stakeholders = this.dao.findAll(url);

        this.stakeholderGrid = new Grid(StakeholderDTO.class, false);
        stakeholderGrid.addColumn(StakeholderDTO::getRoleORName).setHeader("Rolle");
        stakeholderGrid.addColumn(StakeholderDTO::getContact).setHeader("Kontakt");
        stakeholderGrid.addColumn(StakeholderDTO::getExpectation).setHeader("Erwartung");
        stakeholderGrid.setItems(stakeholders);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        rolle = new TextField("Rolle");
        rolle.addFocusShortcut(Key.ENTER);
        rolle.focus();
        kontakt = new TextField("Kontakt");
        erwartung = new TextField("Erwartung");
        Button addStakeholder = new Button("Hinzuf??gen");
        addStakeholder.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addStakeholder.addClickShortcut(Key.ENTER);
        horizontalLayout.add(rolle, kontakt, erwartung);

        Button edit = new Button("??ndern");
        Button delete = new Button("L??schen");
        edit.setVisible(false);
        delete.setVisible(false);
        addStakeholder.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addStakeholder.addClickShortcut(Key.ENTER);

        HorizontalLayout buttonLayout = new HorizontalLayout(addStakeholder, edit, delete);
        HorizontalLayout gridLayout = new HorizontalLayout(stakeholderGrid);

        addStakeholder.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if(!(rolle.getValue().isEmpty() || kontakt.getValue().isEmpty() || erwartung.getValue().isEmpty())) {
                StakeholderDTO stakeholderDTO = new StakeholderDTO(rolle.getValue(), kontakt.getValue(), erwartung.getValue());
                dao.save(stakeholderDTO);
                clearTextFieldValue();
                stakeholderGrid.deselectAll();
                updateList();
            } else {
                new NotificationWindow("Es m??ssen alle Felder ausgef??llt werden!", NotificationType.SHORT, NotificationType.NEUTRAL);
            }
        });

        stakeholderGrid.addSelectionListener((SelectionListener<Grid<StakeholderDTO>, StakeholderDTO>) selection -> {
            Optional<StakeholderDTO> optionalDoku = selection.getFirstSelectedItem();
            if (optionalDoku.isPresent()) {
                stakeholderDTO = optionalDoku.get();
                kontakt.setValue(stakeholderDTO.getContact());
                erwartung.setValue(stakeholderDTO.getExpectation());
                rolle.setValue(stakeholderDTO.getRoleORName());
                addStakeholder.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
            } else {
                addStakeholder.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
            }
        });

        edit.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            stakeholderDTO.setContact(kontakt.getValue());
            stakeholderDTO.setExpectation(erwartung.getValue());
            stakeholderDTO.setRoleORName(rolle.getValue());
            dao.update(stakeholderDTO);
            clearTextFieldValue();
            updateList();
            new NotificationWindow("Stakeholder erfolgreich ge??ndert!", NotificationType.SHORT, NotificationType.SUCCESS);
        });

        delete.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            dao.delete(stakeholderDTO);
            clearTextFieldValue();
            updateList();
            new NotificationWindow("Stakeholder erfolgreich gel??scht!", NotificationType.SHORT, NotificationType.NEUTRAL);

        });

        VerticalLayout main = new VerticalLayout();
        main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
        main.add(new BreadCrumbComponent(new String[]{"Einf??hrung und Ziele", "Stakeholder"}), gridLayout, horizontalLayout, buttonLayout);
        add(main);
    }

    public void updateList() {
        List<StakeholderDTO> stakeholders = this.dao.findAll(url);
        this.stakeholderGrid.setItems(stakeholders);
    }

    private void clearTextFieldValue() {
        rolle.clear();
        kontakt.clear();
        erwartung.clear();
    }
}

