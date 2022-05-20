package org.arc42.dokumentation.model.dao.arc42documentation;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.List;

import static java.lang.Integer.parseInt;
import static org.neo4j.driver.Values.parameters;

public class AnalyseResultDAO extends ARC42DAOAbstract<String, String> {

    private static AnalyseResultDAO instance;

    public AnalyseResultDAO() {
        super();
    }

    public static AnalyseResultDAO getInstance() {
        if (instance == null) {
            instance = new AnalyseResultDAO();
        }
        return instance;
    }

    @Override
    public String save(String s) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
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
        Integer arcId;
        try (Session session = getDriver().session()) {
            if (id != null) {
                arcId = parseInt(id);
            } else {
                arcId = getActualArcId(null);
            }
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " match (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis)" + System.lineSeparator() +
                                    "return Id(s), s.result",
                            parameters("id", arcId));
                    String aResult = null;
                    if (result != null) {
                        Record record = (result.hasNext()) ? result.single() : null;
                        if (record != null) {
                            aResult = record.get("s.result").asString();
                        }
                    }
                    return aResult;
                });
            }
        }
        return null;
    }

    @Override
    public void createRelationship(String dto) {
        super.createRelationship(dto);
    }
}
