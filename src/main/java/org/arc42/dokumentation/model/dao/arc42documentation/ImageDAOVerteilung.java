package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;

import static org.neo4j.driver.Values.parameters;


public class ImageDAOVerteilung extends ARC42DAOAbstract<ImageDTO, String> {
    private static ImageDAOVerteilung instance;

    public ImageDAOVerteilung() {
        super();
    }

    public static ImageDAOVerteilung getInstance() {
        if (instance == null) {
            instance = new ImageDAOVerteilung();
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
                                    " create (a)-[r:verteilungssicht]->(s:Image:Verteilung {bildName:$bildName, bildMimeType:$bildMimeType, bildPath:$bildPath, uxfName:$uxfName, uxfMimeType:$uxfMimeType, uxfPath:$uxfPath, description:$description})" + System.lineSeparator() +
                                    "return Id(s), s.bildName, s.bildMimeType, s.bildPath, s.uxfName, s.uxfMimeType, s.uxfPath, s.description",
                            parameters("id", arcId, "bildName", imageDTO.getBildName(), "bildMimeType", imageDTO.getBildMimeType(), "bildPath", imageDTO.getBildStream(), "uxfName", imageDTO.getUxfName(), "uxfMimeType", imageDTO.getUxfMimeType(), "uxfPath", imageDTO.getUxfStream(), "description", imageDTO.getDescription()));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = result.single();
                        dto = new ImageDTO(String.valueOf(record.get("Id(s)")), record.get("s.bildName").asString(), record.get("s.bildMimeType").asString(), record.get("s.bildPath").asByteArray(), record.get("s.uxfName").asString(), record.get("s.uxfMimeType").asString(), record.get("s.uxfPath").asByteArray(), record.get("s.description").asString());
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
                                    "match (a)-[r:verteilungssicht]->(s:Image:Verteilung) detach delete s return count(*)",
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
                                    " match (a)-[r:verteilungssicht]->(s:Image:Verteilung)" + System.lineSeparator() +
                                    "return Id(s), s.bildName, s.bildMimeType, s.bildPath, s.uxfName, s.uxfMimeType, s.uxfPath, s.description",
                            parameters("id", arcId));
                    ImageDTO dto = null;
                    if (result != null) {
                        Record record = (result.hasNext()) ? result.single() : null;
                        if (record != null) {
                            dto = new ImageDTO(String.valueOf(record.get("Id(s)")), record.get("s.bildName").asString(), record.get("s.bildMimeType").asString(), record.get("s.bildPath").asByteArray(), record.get("s.uxfName").asString(), record.get("s.uxfMimeType").asString(), record.get("s.uxfPath").asByteArray(), record.get("s.description").asString());
                        }
                    }
                    return dto;
                });
            }
        }
        return null;
    }
}

