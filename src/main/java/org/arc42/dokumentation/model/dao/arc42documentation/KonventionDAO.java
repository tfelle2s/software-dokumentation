package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.KonventionDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class KonventionDAO extends ARC42DAOAbstract<KonventionDTO, String> {
    private static KonventionDAO instance;

    private KonventionDAO() {
        super();
        getDriver();
    }

    public static KonventionDAO getInstance() {
        if (instance == null) {
            instance = new KonventionDAO();
        }
        return instance;
    }


    public KonventionDTO save(KonventionDTO konventionDTO) {
        Integer arcId = getActualArcId(null);
        if (arcId != null) {
            try (Session session = getDriver().session()){
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match (d:Arc42) where Id(d)=$arc" + System.lineSeparator() +
                            "create (d)-[r:hatKonvention]->(a:Konvention {konvention:$konvention, erlaeuterung:$erlaeuterung}) return a.konvention, a.erlaeuterung, Id(a)", parameters("arc", arcId, "konvention", konventionDTO.getKonvention(), "erlaeuterung", konventionDTO.getErlaeuterung()));
                    Record record = result.next();
                    return new KonventionDTO(String.valueOf(record.get("Id(a)")), record.get("a.konvention").toString(), record.get("a.erlaeuterung").toString());
                });
            }
        }
        return null;
    }

    public void update(KonventionDTO konventionDTO) {
        try (Session session = getDriver().session()){
            session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Konvention) where Id(a)=$id  set a = {konvention:$konvention, erlaeuterung:$erlaeuterung} return a.konvention, a.erlaeuterung, Id(a)", parameters("id", Integer.parseInt(konventionDTO.getId()), "konvention", konventionDTO.getKonvention(), "erlaeuterung", konventionDTO.getErlaeuterung()));
                KonventionDTO dto = null;
                if (result != null) {
                    Record record = result.single();
                    dto = new KonventionDTO(String.valueOf(record.get("Id(a)")), record.get("a.konvention").toString(), record.get("a.erlaeuterung").toString());
                }
                return dto;
            });
        }
    }

    public Boolean delete(KonventionDTO konventionDTO) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Konvention)where Id(a)=$id" + System.lineSeparator() +
                        "match(d:Arc42) match (a)<-[r:hatKonvention]-(d) delete r,a return count(*)", parameters("id", Integer.parseInt(konventionDTO.getId())));
                Record record = result.single();
                return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }

    public List<KonventionDTO> findAll(String url) {
        Integer arcId = getActualArcId(url);
        if (arcId == null) {
            return new ArrayList<>();
        }
        return findAllByArcId(String.valueOf(arcId));
    }

    public List<KonventionDTO> findAllByArcId(String id) {
        try(Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(d:Arc42) where Id(d)=$id match(a:Konvention) match (d)-[r:hatKonvention]->(a) return a.konvention, a.erlaeuterung, Id(a)", parameters("id", Integer.parseInt(id)));
                return result.list(record -> {
                    KonventionDTO dto = new KonventionDTO(record.get("a.konvention").asString(), record.get("a.erlaeuterung").asString());
                    dto.setId(String.valueOf(record.get("Id(a)")));
                    return dto;
                });
            });
        }
    }

    public KonventionDTO findById(String id) {
        if (id != null && !id.isEmpty()) {
            Integer arcId = Integer.parseInt(id);
            try (Session session = getDriver().session()) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Konvention) where Id(a)=$id return a.konvention, a.erlaeuterung, Id(a)", parameters("id", arcId));
                    Record record = result.single();
                    if (record == null) {
                        return null;
                    }
                    return new KonventionDTO(String.valueOf(record.get("Id(a)")), record.get("a.konvention").toString(), record.get("a.erlaeuterung").toString());
                });
            }

        }
        return null;
    }

}
