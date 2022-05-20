package org.arc42.dokumentation.view.main;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.arc42.dokumentation.control.logic.Login;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3("Arc42 Dokumentation");
        Button logout = new Button("Logout", e -> Login.logout());

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(), logo, logout, new Div());

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink arc42Documentation = new RouterLink("Arc42 Dokumentation", Arc42DocumentationView.class);
        arc42Documentation.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(arc42Documentation));
    }
}