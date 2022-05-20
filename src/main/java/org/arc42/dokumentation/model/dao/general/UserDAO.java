package org.arc42.dokumentation.model.dao.general;

import org.arc42.dokumentation.control.service.Neo4jconnection;
import org.arc42.dokumentation.model.dto.general.DB_UserDTO;
import org.arc42.dokumentation.view.util.data.Credentials;

public class UserDAO {
    private static UserDAO instance = null;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public void setUser(String username, String password) {
        String uri = Credentials.DATABASEURL;
        try (Neo4jconnection connection = new Neo4jconnection(uri, "neo4j", "stones-principal-roars")) {
            connection.setUserData(username, password);
        }
    }

    public boolean existUser(String username) {
        String uri = Credentials.DATABASEURL;
        try (Neo4jconnection connection = new Neo4jconnection(uri, "neo4j", "stones-principal-roars")) {
            return connection.userExist(username);
        }
    }

    public DB_UserDTO getUser(String username, String password) {
        String uri = Credentials.DATABASEURL;
        try (Neo4jconnection connection = new Neo4jconnection(uri, "neo4j", "stones-principal-roars")) {
            return connection.getUserData(username, password);
        }
    }


}
