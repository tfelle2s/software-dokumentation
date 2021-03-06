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
import org.arc42.dokumentation.model.dao.arc42documentation.TechnischDAO;
import org.arc42.dokumentation.model.dto.documentation.TechnischDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.NotificationWindow;
import org.arc42.dokumentation.view.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

import java.util.List;
import java.util.Optional;

@Route(value = "randbedingungen/technisch", layout = MainLayout.class)
public class ARC42Technisch extends Arc42NewDocumentationView {

    private TechnischDAO dao;
    private Grid<TechnischDTO> techG;
    private TechnischDTO technischDTO;
    private TextField randbedingung;
    private TextField hintergrund;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        this.dao = TechnischDAO.getInstance();
        getRandbedingunen().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getRandbedingunen().setOpened(true);
        getTechnisch().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");


        List<TechnischDTO> technischDTOS = dao.findAll(url);
        this.techG = new Grid<>(TechnischDTO.class, false);
        techG.addColumn(TechnischDTO::getRandbedingung).setHeader("Technische Randbedingung");
        techG.addColumn(TechnischDTO::getHintergrund).setHeader("Hintergrund");
        techG.setItems(technischDTOS);


        HorizontalLayout horizontalLayout = new HorizontalLayout();
        randbedingung = new TextField("Randbedingung");
        hintergrund = new TextField("Hintergrund");
        Button addRandbedingung = new Button("Hinzuf??gen");
        addRandbedingung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button edit = new Button("??ndern");
        Button delete = new Button("L??schen");
        edit.setVisible(false);
        delete.setVisible(false);

        HorizontalLayout buttonLayout = new HorizontalLayout(addRandbedingung, edit, delete);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addRandbedingung.addClickShortcut(Key.ENTER);
        horizontalLayout.add(randbedingung, hintergrund);

        addRandbedingung.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if (!(randbedingung.getValue().isEmpty() || hintergrund.getValue().isEmpty())) {
                TechnischDTO technischDTO = new TechnischDTO(randbedingung.getValue(), hintergrund.getValue());
                dao.save(technischDTO);
                clearTextFieldValue();
                updateList();
            } else {
                new NotificationWindow("Es m??ssen alle Eingabefelder ausgef??llt werden!", NotificationType.SHORT, NotificationType.NEUTRAL);
            }
        });

        HorizontalLayout gridLayout = new HorizontalLayout(techG);

        techG.addSelectionListener((SelectionListener<Grid<TechnischDTO>, TechnischDTO>) selection -> {
            Optional<TechnischDTO> optionalDoku = selection.getFirstSelectedItem();
            if (optionalDoku.isPresent()) {
                technischDTO = optionalDoku.get();
                randbedingung.setValue(technischDTO.getRandbedingung());
                hintergrund.setValue(technischDTO.getHintergrund());
                addRandbedingung.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);

            } else {
                addRandbedingung.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
            }
        });

        edit.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            technischDTO.setRandbedingung(randbedingung.getValue());
            technischDTO.setHintergrund(hintergrund.getValue());
            dao.update(technischDTO);
            clearTextFieldValue();
            updateList();
            new NotificationWindow("Technische Randbedingung erfolgreich ge??ndert!", NotificationType.SHORT, NotificationType.SUCCESS);
        });

        delete.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            dao.delete(technischDTO);
            clearTextFieldValue();
            updateList();
            new NotificationWindow("Technische Randbedingung erfolgreich gel??scht!", NotificationType.SHORT, NotificationType.NEUTRAL);
        });
        VerticalLayout main = new VerticalLayout();
        main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
        main.add(new BreadCrumbComponent(new String[]{"Randbedingungen", "Technisch"}), gridLayout, horizontalLayout, buttonLayout);
        add(main);
    }

    public void updateList() {
        List<TechnischDTO> technischDTOS = dao.findAll(url);
        this.techG.setItems(technischDTOS);
    }

    private void clearTextFieldValue() {
        randbedingung.clear();
        hintergrund.clear();
    }


}

