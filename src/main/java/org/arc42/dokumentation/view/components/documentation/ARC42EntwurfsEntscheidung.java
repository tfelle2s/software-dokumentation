package org.arc42.dokumentation.view.components.documentation;


import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.DesignDecisionDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.EntwurfsEntscheidungDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOBaustein;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.components.customComponents.BreadCrumbComponent;
import org.arc42.dokumentation.view.main.MainLayout;
import org.arc42.dokumentation.view.util.GenHTMLForDD;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "architekturentscheidungen", layout = MainLayout.class)
public class ARC42EntwurfsEntscheidung extends Arc42NewDocumentationView{

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String arcId = event.getRouteParameters().get("arcId").get();
        super.beforeEnter(event);
    }

    @Override
    public void init() {
        super.init();
        getArchitekturentscheidungen().getSummary().getElement().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
        getArchitekturentscheidungen().setOpened(true);


        ARC42DAOAbstract dao = EntwurfsEntscheidungDAO.getInstance();
        VerticalLayout vl = new VerticalLayout();
        DesignDecisionDAO ddDao = DesignDecisionDAO.getInstance();
        ImageDAOBaustein imageDAOBaustein = ImageDAOBaustein.getInstance();
        List<ImageDTO> allImage = imageDAOBaustein.findAll(url);
        String content = "";
        if (allImage.size() >= 1){
            GenHTMLForDD htmlForDD = GenHTMLForDD.getInstance();
            List<String> ddIdsForImage = ddDao.findDDIdsForImage(allImage.get(0).getId());
            content = htmlForDD.getHtmlForDDs(ddIdsForImage);
        }
        String dbValue = (String) dao.findById(url);
        String value = "";

        if (dbValue != null && !dbValue.trim().isEmpty()) {
            value = value + dbValue;
        } else {
            String title = "<b><font size='5' face='Arial' style><b>Architekturentscheidungen</b></font></b>";
            value = value + title;
        }
        if (content != null && !content.trim().isEmpty()){
            String trennung = "<div><b>-----------------------------------------------------------------------------------------</b></div>";
            String leer = "</div>####Linie<div>";
            value = value + leer.replaceFirst("####Linie", trennung) + content;
        }


        IFrame iFrame = new IFrame();
        iFrame.setSrcdoc(value);
        iFrame.setSizeFull();

       // TextAreaComponent textAreaComponent = new TextAreaComponent("Entwurfsentscheidungen",null,ddDao);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();


        verticalLayout.add(new BreadCrumbComponent(new String[]{"Entwurfsentscheidungen"}),iFrame);



        add(verticalLayout);
    }
}

