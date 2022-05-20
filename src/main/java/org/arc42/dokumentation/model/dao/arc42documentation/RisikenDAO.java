package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;


import java.util.List;

import static org.neo4j.driver.Values.parameters;


public class RisikenDAO extends ARC42DAOAbstract<ImageDTO, String>{

    private static RisikenDAO instance;

    public RisikenDAO() {
        super();
    }

    public static RisikenDAO getInstance() {
        if (instance == null) {
            instance = new RisikenDAO();
        }
        return instance;
    }


    @Override
    public ImageDTO save(ImageDTO imageDTO) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    delete(null);
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " create (a)-[r:hatRisiken]->(s:Risiken {risiken:$risiken})" + System.lineSeparator() +
                                    "return Id(s), s.risiken",
                            parameters("id", arcId, "risiken", imageDTO.getDescription()));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = result.single();
                        dto = new ImageDTO();
                        if(!record.get("s.risiken").asString().equals("")) {
                            dto.setDescription(record.get("s.risiken").asString());
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
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                session.writeTransaction(transaction -> {
                    transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    "match (a)-[r:hatRisiken]->(s:Risiken) detach delete s return count(*)",
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
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(id);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                                    " match (a)-[r:hatRisiken]->(s:Risiken)" + System.lineSeparator() +
                                    "return Id(s), s.risiken",
                            parameters("id", arcId));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = (result.hasNext()) ? result.single() : null;
                        dto = new ImageDTO();
                        if (record != null) {
                            dto.setDescription(record.get("s.risiken").asString());
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
