package org.arc42.dokumentation.view.components.customComponents;

import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.components.documentation.Arc42NewDocumentationView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.arc42.dokumentation.view.util.data.NotificationType;

public class TextAreaComponent extends VerticalLayout {

    final H3 header;
    private final TextArea textArea;

    public TextArea getTextArea() {
        return textArea;
    }

    public TextAreaComponent(String title, String body, ARC42DAOAbstract dao) {

        header = new H3(title);
        textArea =  new TextArea();
        if (body != null && !body.trim().isEmpty()){
            textArea.setValue(body);
        }
        textArea.setSizeFull();

        Button save = new Button("Speichern");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if(textArea.getValue().isEmpty() || textArea.getValue().equals("")) {
                new NotificationWindow("Sie müssen eine Beschreibung angeben und dann speichern!", NotificationType.SHORT,NotificationType.ERROR);
            } else {
                ImageDTO imageDTO = (ImageDTO) dao.findById(null);
                if(imageDTO==null) {
                    imageDTO = new ImageDTO();
                }

                imageDTO.setDescription(textArea.getValue());
                dao.save(imageDTO);
                new NotificationWindow("Erfolgreich gespeichert!",NotificationType.SHORT,NotificationType.SUCCESS);
            }
            new Arc42NewDocumentationView().hasChanges(false);
        });


        setSizeFull();
        add(header,textArea, save);
    }
}
