package org.arc42.dokumentation.view.main;


import org.arc42.analyse.control.ARC42AnalyseResultService;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.AnalyseResultDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.components.documentation.DialogComponent;
import org.arc42.dokumentation.view.components.customComponents.NotificationWindow;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.neo4j.driver.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "arc42View", layout = MainLayout.class)
public class Arc42DocumentationView  extends ReadDocumentionAbstract {

    private final List<DokuNameDTO> dokuNameDTOList = new ArrayList<>();
    private final Arc42DokuNameDAO dao;
    ARC42DAOAbstract dao1;
    private String arcId;
    private DokuNameDTO dokuNameDTO;

    public Arc42DocumentationView() {
        //Load Documentations
        dao = new Arc42DokuNameDAO();
        setSizeFull();
        fillPossibleArc42Doku();

        //Grid creation
        Grid<DokuNameDTO> gridDocumentation = new Grid<>(DokuNameDTO.class, false);
        gridDocumentation.addColumn(DokuNameDTO::getId).setHeader("ID").setSortable(true);
        gridDocumentation.addColumn(DokuNameDTO::getName).setHeader("Title").setSortable(true);
        gridDocumentation.setAllRowsVisible(true);
        gridDocumentation.setMaxHeight("50%");


        //Empty check
        if (this.dokuNameDTOList != null) gridDocumentation.setItems(this.dokuNameDTOList);

        //Header
        H3 headline = new H3("Arc42 Dokumentation");
        headline.addClassNames("text-l", "m-m");
        Button create = new Button("Neue Dokumentation erstellen");
        HorizontalLayout header = new HorizontalLayout(headline,create);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(headline);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        //Button-Layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button update = new Button("Bearbeiten");
        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button delete = new Button("L??schen");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-inline-end", "auto");
        Button analyze = new Button("Analyse starten");
        buttonLayout.add(update,delete,analyze);
        buttonLayout.setVisible(false);


        add(header,gridDocumentation,buttonLayout);

        create.addClickListener(buttonClickEvent -> {
            DialogComponent dialogComponent = new DialogComponent("createDoku");
            dialogComponent.getCreateButton().addClickShortcut(Key.ENTER);
            dialogComponent.getCreateButton().addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {

                String arcId = null;

                if (arcId == null) {
                    DokuNameDTO result = dao.save(new DokuNameDTO(dialogComponent.getTitel().getValue()));
                    if (result != null && !result.getName().isEmpty()) {
                        dialogComponent.getDialog().close();
                        arcId = result.getId();
                        UI.getCurrent().navigate("arc42/" + arcId + "/titel");
                    }
                }
            });

        });
        update.addClickListener(buttonClickEvent -> UI.getCurrent().navigate("arc42/"+dokuNameDTO.getId()+"/titel"));

        delete.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            DialogComponent dialogComponent = new DialogComponent("delete");
            dialogComponent.getDeleteButton().addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent1 -> {
                if (arcId != null) {
                    Boolean result = dao.delete1(dokuNameDTO);
                    if (result != null && result) {
                        dialogComponent.getDialog().close();
                        buttonLayout.setVisible(false);
                        new NotificationWindow("Die ARC42 Dokumentation wurde erfolgreich gel??scht!", 4000,"success");
                        fillPossibleArc42Doku();
                        gridDocumentation.getDataProvider().refreshAll();
                    } else {
                        new NotificationWindow("Die ARC42 Dokumentation konnte nicht gel??scht werden!", 4000,"error");

                    }
                }
            });
        });

        gridDocumentation.addSelectionListener(selection -> {
            Optional<DokuNameDTO> optionalDoku = selection.getFirstSelectedItem();
            if (optionalDoku.isPresent()) {
                dokuNameDTO= optionalDoku.get();
                arcId=optionalDoku.get().getId();
                buttonLayout.setVisible(true);
            } else {
                buttonLayout.setVisible(false);
            }
        });

        analyze.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            dao1 = AnalyseResultDAO.getInstance();
            ARC42AnalyseResultService arc42AnalyseResultService = new ARC42AnalyseResultService();
            arc42AnalyseResultService.starteAnalyse(Integer.parseInt(dokuNameDTO.getId()));
            UI.getCurrent().navigate("arc42/"+dokuNameDTO.getId()+"/ergebnisse");

        });
    }

    public void init() {
       new Arc42DocumentationView();
        }

    private void fillPossibleArc42Doku() {
        Arc42DokuNameDAO dao = new Arc42DokuNameDAO();
        dokuNameDTOList.clear();
        List<DokuNameDTO> arc42Dokus = null;
        try {
            arc42Dokus = dao.findAll(null);
        } catch (DatabaseException dbe) {
            //Notification.show("DB-Exception", dbe.getMessage(), Notification.Type.ERROR_MESSAGE);
        } catch (Exception e) {
            //Notification.show("Exception", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

        if (arc42Dokus == null) {
            //Notification.show("Exception", "No Arc42 Documentation available!", Notification.Type.ERROR_MESSAGE);
        } else {
            for (DokuNameDTO name : arc42Dokus) {
                this.dokuNameDTOList.add(name);
            }
        }
    }

}
