package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;


public class QualityGoalDAO extends ARC42DAOAbstract<QualityGoalDTO, String>{

    private static QualityGoalDAO instance;

    public static QualityGoalDAO getInstance() {
        if (instance == null) {
            instance = new QualityGoalDAO();
        }
        return instance;
    }

    private QualityGoalDAO() {
        super();
    }

    @Override
    public QualityGoalDTO save(QualityGoalDTO qualityGoalDTO) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
               return session.writeTransaction(transaction -> {
                   Result result = transaction.run("match (d:Arc42) where Id(d)=$id" + System.lineSeparator() +
                           "create (d)-[r:hasQualityGoal]->(a:Qualitaetsziel {qualitaetsziel:$quality, motivation: $motivation})" + System.lineSeparator() +
                           "return a.qualitaetsziel, a.motivation, Id(a)", parameters("id", arcId, "quality", qualityGoalDTO.getQualitaetsziel(), "motivation", qualityGoalDTO.getMotivation()));
                   QualityGoalDTO dto = null;
                   if (result != null) {
                       Record record = result.single();
                       dto = new QualityGoalDTO(record.get("a.qualitaetsziel").asString(), record.get("a.motivation").asString(), qualityGoalDTO.getQualityCriteria());
                       dto.setId(String.valueOf(record.get("Id(a)")));
                   }

                   return dto;
               });
            }
        }
        return null;
    }

    public void upadateRelationship(QualityGoalDTO qualityGoalDTO) {
        createQualityCriteria(qualityGoalDTO);

        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                for (int i = 0; i < qualityGoalDTO.getQualityCriteria().size(); i++) {
                    String query = "MATCH (a:Qualitaetsziel), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"+qualityGoalDTO.getQualityCriteria().get(i)+"'  AND id(a)=" + qualityGoalDTO.getId()+" CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
                    session.run(query);

                }
            }
        }
    }

    public void createRelationship(QualityGoalDTO qualityGoalDTO) {
        createQualityCriteria(qualityGoalDTO);
        QualityGoalDTO qualityGoalDTO1 = save(qualityGoalDTO);

        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                for (int i = 0; i < qualityGoalDTO1.getQualityCriteria().size(); i++) {
                    String query = "MATCH (a:Qualitaetsziel), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"+qualityGoalDTO1.getQualityCriteria().get(i)+"'  AND id(a)=" + qualityGoalDTO1.getId()+" CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
                    session.run(query);

                }
                }
            }
    }

    public void deleteRelationship(QualityGoalDTO qualityGoalDTO) {
        try (Session session = getDriver().session()){
            session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Qualitaetsziel) where Id(a)=$id" + System.lineSeparator() +
                        "match(d:Arc42) match (a)<-[r:hasQualityGoal]-(d)" + System.lineSeparator() +
                        "match (e)<-[n:hasQualityCriteria]-(a)" + System.lineSeparator() +
                        "delete n" + System.lineSeparator() +
                        "return count(n)", parameters("id", Integer.parseInt(qualityGoalDTO.getId())));
                Record record = result.single();
                return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }

    @Override
    public Boolean delete(QualityGoalDTO qualityGoalDTO) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Qualitaetsziel) where Id(a)=$id" + System.lineSeparator() +
                        "match(d:Arc42) match (a)<-[r:hasQualityGoal]-(d)" + System.lineSeparator() +
                        "match (e)<-[n:hasQualityCriteria]-(a)" + System.lineSeparator() +
                        "delete n,r,a" + System.lineSeparator() +
                        "return count(n)", parameters("id", Integer.parseInt(qualityGoalDTO.getId())));
                Record record = result.single();
                return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }


    public void createQualityCriteria(QualityGoalDTO qualityGoalDTO) {
        Boolean success = true;

            try (Session session = getDriver().session()){
                session.writeTransaction(transaction -> {
                    for (int i = 0; i < qualityGoalDTO.getQualityCriteria().size(); i++) {
                        transaction.run("MERGE (n:Qualitaetskriterium {qualitaetskriterium: $criteria}) RETURN id(n)", parameters("criteria", qualityGoalDTO.getQualityCriteria().get(i)));
                    }
                    return true;
                });
            }
    }

    @Override
    public void update(QualityGoalDTO qualityGoalDTO) {
        deleteRelationship(qualityGoalDTO);
        upadateRelationship(qualityGoalDTO);
        try (Session session = getDriver().session()){
            session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Qualitaetsziel) where Id(a)=$id " + System.lineSeparator() +
                        "match(c:Qualitaetskriterium)" + System.lineSeparator() +
                        "match (a)-[r:hasQualityCriteria]->(c)" + System.lineSeparator() +
                        "set a = {qualitaetsziel:$quality, motivation:$motif} " + System.lineSeparator() +
                        "return a.qualitaetsziel, a.motivation, Id(a),collect(c.qualitaetskriterium) AS criteria", parameters("id", Integer.parseInt(qualityGoalDTO.getId()), "quality", qualityGoalDTO.getQualitaetsziel(), "motif", qualityGoalDTO.getMotivation()));
                QualityGoalDTO dto = null;
                if (result != null) {
                    Record record = result.single();
                    dto = new QualityGoalDTO(record.get("a.qualitaetsziel").asString(), record.get("a.motivation").asString(), record.get("criteria").asList());
                    dto.setId(String.valueOf(record.get("Id(a)")));
                }
                return dto;
            });
        }
    }

    @Override
    public List<QualityGoalDTO> findAll(String url) {
        Integer arcId = getActualArcId(url);
        if (arcId == null) {
            return new ArrayList<>();
        }
        return findAllByArcId(arcId.toString());
    }

    public List<QualityGoalDTO> findAllByArcId(String arcIdString) {
        if (arcIdString != null && !arcIdString.isEmpty()){
            Integer arcId = Integer.parseInt(arcIdString);

            try (Session session = getDriver().session()){
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Qualitaetsziel)" + System.lineSeparator() +
                            "match(c:Qualitaetskriterium)" + System.lineSeparator() +
                            "match(d:Arc42) where Id(d)=$id AND (d)-[:hasQualityGoal]->(a)" + System.lineSeparator() +
                            "match (a)-[r:hasQualityCriteria]->(c) " + System.lineSeparator() +
                            "return a.qualitaetsziel, a.motivation, Id(a),collect(c.qualitaetskriterium) AS criteria", parameters("id", arcId));
                    return result.list(r -> {
                        QualityGoalDTO qualityGoalDTO = new QualityGoalDTO(r.get("a.qualitaetsziel").asString(), r.get("a.motivation").asString(), r.get("criteria").asList());
                        qualityGoalDTO.setId(String.valueOf(r.get("Id(a)")));
                        return qualityGoalDTO;
                    });
                });
            }
        }
        return new ArrayList<>();
    }

    @Override
    public QualityGoalDTO findById(String id) {
        if (id != null && !id.isEmpty()) {
            Integer idI = Integer.parseInt(id);
            try (Session session = getDriver().session()){
                session.writeTransaction(transaction -> {
                    Result result = transaction.run("match (a:Qualitaetsziel) where Id(a)=$id return a.qualitaetsziel, a.motivation, Id(a)", parameters("id", idI));
                    QualityGoalDTO dto = null;
                    if (result!=null) {
                        Record record = result.single();
                        dto = new QualityGoalDTO(record.get("a.qualitaetsziel").asString(), record.get("a.motivation").asString(),record.get("a.categories").asList());
                        dto.setId(String.valueOf(record.get("Id(a)")));
                    }
                    return dto;
                });
            }
        }
        return null;
    }
}
