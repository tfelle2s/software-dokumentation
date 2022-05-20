package org.arc42.dokumentation.control.service;


import org.arc42.dokumentation.model.dto.general.DB_UserDTO;
import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Neo4jconnection implements AutoCloseable {
    private final Driver driver;

    public Neo4jconnection(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void close() {
        driver.close();
    }

    public org.neo4j.driver.Driver getDriver() {
        return driver;
    }


    public boolean userExist(final String username) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                Result result1 = tx.run("MATCH(a:Developer {devname:$username}) RETURN count(a)>0 AS result;", parameters("username", username));
                return result1.single().get(0).asBoolean();
            });
        }
    }


    public DB_UserDTO getUserData(final String username, final String password) {
        try (Session session = driver.session()) {
            String result = session.writeTransaction(tx -> {
                Result result1 = tx.run("MATCH(a:Developer {devname:$username, password:$password}) RETURN a.devname", parameters("username", username, "password", password));
                return result1.single().get(0).asString();
            });
            DB_UserDTO userdto = DB_UserDTO.getInstance();
            userdto.setUsername(result);
            return userdto;
        }
    }

    public void setUserData(final String username, final String password) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                Result result1 = tx.run("CREATE(d:Developer {devname:$username, password:$password}) return d.name", parameters("username", username, "password", password));
                return result1.single().get(0).asString();
            });
        }
    }
}
