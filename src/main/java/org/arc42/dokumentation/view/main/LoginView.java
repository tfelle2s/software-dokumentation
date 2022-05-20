package org.arc42.dokumentation.view.main;

import org.arc42.dokumentation.control.logic.Login;
import org.arc42.dokumentation.model.dto.general.FE_LoginDTO;
import org.arc42.dokumentation.view.components.documentation.RegisterComponent;
import org.arc42.dokumentation.view.components.customComponents.NotificationWindow;
import org.arc42.dokumentation.view.util.data.Roles;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.neo4j.driver.exceptions.NoSuchRecordException;


@PageTitle("Login")
@Route("login")
@RouteAlias(value = "")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(UI.getCurrent().getSession().getAttribute(Roles.CURRENTUSER)!=null){
            beforeEnterEvent.rerouteTo(LoginView.class);
        } else {
            this.setUp();
        }
    }

    private void setUp() {

        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Header i18nHeader = new LoginI18n.Header();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nHeader.setTitle("Arc42 Dokumentation");
        i18nHeader.setDescription("");
        i18n.setHeader(i18nHeader);
        i18nForm.setTitle("Login");
        i18nForm.setUsername("Benutzername");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Einloggen");
        i18nForm.setForgotPassword("Noch kein Konto? Hier registrieren!");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Authentifizierungsfehler!");
        i18nErrorMessage.setMessage("Ihr Benutzername oder Passwort ist falsch!");
        i18n.setErrorMessage(i18nErrorMessage);

        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);
        loginOverlay.setOpened(true);

        loginOverlay.addLoginListener((ComponentEventListener<AbstractLogin.LoginEvent>) loginEvent -> {
            FE_LoginDTO logindto = new FE_LoginDTO(loginEvent.getUsername(), loginEvent.getPassword());
            try {
                Login.login(logindto);
                loginOverlay.close();
            } catch (NoSuchRecordException e) {
                loginOverlay.setError(true);
                loginOverlay.setOpened(true);
            } catch (Exception e) {
                new NotificationWindow(e.getMessage(),5000,"error");
                e.printStackTrace();
            }
        });

        loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> new RegisterComponent().getDialog().open());
        getStyle().set("background-color", "var(--lumo-contrast-5pct)");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        add(loginOverlay);
    }
}