package org.arc42.dokumentation.model.dao.arc42documentation;


import org.neo4j.driver.*;
import org.neo4j.driver.Record;


import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class EntwurfsEntscheidungDAO extends ARC42DAOAbstract<String, String>{

    private static EntwurfsEntscheidungDAO instance;

    public EntwurfsEntscheidungDAO() {
        super();
        Driver driver = getDriver();
    }

    public static EntwurfsEntscheidungDAO getInstance() {
        if (instance == null) {
            instance = new EntwurfsEntscheidungDAO();
        }
        return instance;
    }


    @Override
    public String save(String s) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    delete(null);
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " create (a)-[r:hatEntscheidung]->(s:EntwurfsEntscheidung {entscheidung:$entscheidung})" + System.lineSeparator() +
                                    "return Id(s), s.entscheidung",
                            parameters("id", arcId, "entscheidung", s));
                    String fkontext = "";
                    if (result != null) {
                        Record record = result.single();
                        fkontext = record.get("s.entscheidung").asString();
                    }
                    return fkontext;
                });
            }
        }
        return null;
    }

    @Override
    public Boolean delete(String s) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                session.writeTransaction(transaction -> {
                    transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    "match (a)-[r:hatEntscheidung]->(s:EntwurfsEntscheidung) detach delete s return count(*)",
                            parameters("id", arcId));
                    return 0;
                });
            }
        }
        return true;
    }

    @Override
    public void update(String s) {
    }

    @Override
    public List<String> findAll(String url) {
        return null;
    }

    @Override
    public String findById(String id) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(id);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " match (a)-[r:hatEntscheidung]->(s:EntwurfsEntscheidung)" + System.lineSeparator() +
                                    "return Id(s), s.entscheidung",
                            parameters("id", arcId));
                    String fkontext = null;
                    if (result != null) {
                        Record record = (result.hasNext()) ? result.single() : null;
                        if (record != null) {
                            fkontext = record.get("s.entscheidung").asString();
                        }
                    }
                    return fkontext;
                });
            }
        }
        return "";
    }
}
