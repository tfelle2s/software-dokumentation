package org.arc42.dokumentation.control.logic;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.arc42.dokumentation.model.dao.general.UserDAO;
import org.arc42.dokumentation.model.dto.general.DB_UserDTO;
import org.arc42.dokumentation.model.dto.general.FE_LoginDTO;
import org.arc42.dokumentation.view.util.data.Roles;


public class Login {

    public static void login(FE_LoginDTO logindto) {
        String useremail = logindto.getUsername();
        String userpassword = logindto.getPassword();

        UserDAO userdao = UserDAO.getInstance();
        DB_UserDTO userdto = userdao.getUser(useremail, userpassword);
        String username = userdto.getUsername();
        VaadinSession.getCurrent().setAttribute("username", username);
        UI.getCurrent().getSession().setAttribute(Roles.CURRENTUSER, username);
        UI.getCurrent().navigate("arc42View");
    }

    public static void logout() {
        UI.getCurrent().getSession().setAttribute(Roles.CURRENTUSER, null);
        UI.getCurrent().navigate("login");
    }

    public static boolean userExist(FE_LoginDTO logindto) {
        UserDAO userdao = UserDAO.getInstance();
        return userdao.existUser(logindto.username);
    }

    public static void register(FE_LoginDTO logindto) {
        UserDAO userdao = UserDAO.getInstance();
        userdao.setUser(logindto.username, logindto.password);
    }


}