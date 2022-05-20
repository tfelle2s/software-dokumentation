package org.arc42.dokumentation.model.dao.arc42documentation;

import org.arc42.dokumentation.model.dto.documentation.TechnischDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class TechnischDAO extends ARC42DAOAbstract<TechnischDTO, String> {

    private static TechnischDAO instance;

    private TechnischDAO() {
        super();
    }

    public static TechnischDAO getInstance() {
        if (instance == null) {
            instance = new TechnischDAO();
        }
        return instance;
    }


    public TechnischDTO save(TechnischDTO technischDTO) {
        Integer arcId = getActualArcId(null);
        if (arcId != null) {
            try (Session session = getDriver().session()) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match (d:Arc42) where Id(d)=$arc" + System.lineSeparator() +
                            "create (d)-[r:hatTechnischRandbedingung]->(a:TechnischeRandbedingung {randbedingung:$randbedingung, hintergrund:$hintergrund}) return a.randbedingung, a.hintergrund, Id(a)", parameters("arc", arcId, "randbedingung", technischDTO.getRandbedingung(), "hintergrund", technischDTO.getHintergrund()));
                    Record record = result.next();
                    return new TechnischDTO(String.valueOf(record.get("Id(a)")), record.get("a.randbedingung").toString(), record.get("a.erlaeuterung").toString());
                });
            }
        }
        return null;
    }

    public void update(TechnischDTO technischDTO) {
        try (Session session = getDriver().session()) {
            session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:TechnischeRandbedingung) where Id(a)=$id  set a = {randbedingung:$randbedingung, hintergrund:$hintergrund} return a.randbedingung, a.hintergrund, Id(a)", parameters("id", Integer.parseInt(technischDTO.getId()), "randbedingung", technischDTO.getRandbedingung(), "hintergrund", technischDTO.getHintergrund()));
                TechnischDTO dto = null;
                if (result != null) {
                    Record record = result.single();
                    dto = new TechnischDTO(String.valueOf(record.get("Id(a)")), record.get("a.randbedingung").toString(), record.get("a.hintergrund").toString());
                }
                return dto;
            });
        }
    }

    public Boolean delete(TechnischDTO technischDTO) {
        try (Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:TechnischeRandbedingung)where Id(a)=$id" + System.lineSeparator() +
                        "match(d:Arc42) match (a)<-[r:hatTechnischRandbedingung]-(d) delete r,a return count(*)", parameters("id", Integer.parseInt(technischDTO.getId())));
                Record record = result.single();
                return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }

    public List<TechnischDTO> findAll(String url) {
        Integer arcId = getActualArcId(url);
        if (arcId == null) {
            return new ArrayList<>();
        }
        return findAllByArcId(String.valueOf(arcId));
    }

    public List<TechnischDTO> findAllByArcId(String id) {
        try (Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(d:Arc42) where Id(d)=$id match(a:TechnischeRandbedingung) match (d)-[r:hatTechnischRandbedingung]->(a) return a.randbedingung, a.hintergrund, Id(a)", parameters("id", Integer.parseInt(id)));
                return result.list(record -> {
                    TechnischDTO dto = new TechnischDTO(record.get("a.randbedingung").asString(), record.get("a.hintergrund").asString());
                    dto.setId(String.valueOf(record.get("Id(a)")));
                    return dto;
                });
            });
        }
    }

    public TechnischDTO findById(String id) {
        if (id != null && !id.isEmpty()) {
            Integer arcId = Integer.parseInt(id);
            try (Session session = getDriver().session()) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:TechnischeRandbedingung) where Id(a)=$id return a.randbedingung, a.hintergrund, Id(a)", parameters("id", arcId));
                    Record record = result.single();
                    if (record == null) {
                        return null;
                    }
                    return new TechnischDTO(String.valueOf(record.get("Id(a)")), record.get("a.randbedingung").toString(), record.get("a.hintergrund").toString());
                });
            }

        }
        return null;
    }
}
