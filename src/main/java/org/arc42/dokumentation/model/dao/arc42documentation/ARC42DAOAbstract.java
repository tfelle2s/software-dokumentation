package org.arc42.dokumentation.model.dao.arc42documentation;


import com.vaadin.flow.component.UI;
import org.arc42.dokumentation.control.service.Neo4jconnection;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Driver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class ARC42DAOAbstract<T, I> implements ARC42DAOI<T, I> {

    private Driver driver;
    private Matcher m;

    public ARC42DAOAbstract() {
        String uri = Credentials.DATABASEURL;
        Neo4jconnection connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
        this.driver = connection.getDriver();
    }

    public Driver getDriver() {
        if (driver == null) {
            String uri = Credentials.DATABASEURL;
            Neo4jconnection connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
            this.driver = connection.getDriver();
        }
        return driver;
    }

    public Integer getActualArcId(String id) {
        if(id==null) {
            Pattern MY_PATTERN = Pattern.compile("(?<=\\/)\\d+(?=\\/)");
            UI.getCurrent().getPage().executeJs("return window.location.pathname;").then(String.class,
                    location -> m = MY_PATTERN.matcher(location));
            if (m == null) {
                m = MY_PATTERN.matcher(UI.getCurrent().getInternals().getActiveViewLocation().getPath());
            }
            String actualArcId = null;


            //Matcher
            while (m.find()) {
                actualArcId = m.group(0);
            }

            m = null;
            return Integer.parseInt(actualArcId);
        }
        return Integer.parseInt(id);
    }
}