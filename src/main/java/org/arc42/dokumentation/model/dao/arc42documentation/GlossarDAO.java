package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.List;

import static org.neo4j.driver.Values.parameters;


public class GlossarDAO extends ARC42DAOAbstract<ImageDTO, String> {

    private static GlossarDAO instance;

    public GlossarDAO() {
        super();
    }

    public static GlossarDAO getInstance() {
        if (instance == null) {
            instance = new GlossarDAO();
        }
        return instance;
    }


    @Override
    public ImageDTO save(ImageDTO imageDTO) {
        try (Session session = getDriver().session()) {
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    delete(null);
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " create (a)-[r:hatGlossar]->(s:Glossar {glossar:$glossar})" + System.lineSeparator() +
                                    "return Id(s), s.glossar",
                            parameters("id", arcId, "glossar", imageDTO.getDescription()));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = result.single();
                        dto = new ImageDTO();
                        if(!record.get("s.glossar").asString().equals("")) {
                            dto.setDescription(record.get("s.glossar").asString());
                        } else {
                            dto.setDescription("");
                        }
                    }
                    return dto;
                });
            }
        }
        return null;
    }

    @Override
    public Boolean delete(ImageDTO imageDTO) {
        try (Session session = getDriver().session()) {
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                session.writeTransaction(transaction -> {
                    transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    "match (a)-[r:hatGlossar]->(s:Glossar) detach delete s return count(*)",
                            parameters("id", arcId));
                    return 0;
                });
            }
        }
        return true;
    }

    @Override
    public void update(ImageDTO imageDTO) {
    }

    @Override
    public List<ImageDTO> findAll(String url) {
        return null;
    }

    @Override
    public ImageDTO findById(String id) {
        try (Session session = getDriver().session()) {
            Integer arcId = getActualArcId(id);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " match (a)-[r:hatGlossar]->(s:Glossar)" + System.lineSeparator() +
                                    "return Id(s), s.glossar",
                            parameters("id", arcId));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = (result.hasNext()) ? result.single() : null;
                        dto = new ImageDTO();
                        if (record != null) {
                            dto.setDescription(record.get("s.glossar").asString());
                        } else {
                            dto.setDescription("");
                        }
                    }
                    return dto;
                });
            }
        }
        return null;
    }
}
