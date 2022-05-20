package org.arc42.dokumentation.model.dao.arc42documentation;

import org.arc42.dokumentation.model.dto.documentation.StakeholderDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;


import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class StakeholderDAO extends ARC42DAOAbstract<StakeholderDTO,String>{

    private static StakeholderDAO instance;

    private StakeholderDAO() {
        super();
    }

    public static StakeholderDAO getInstance() {
        if (instance == null) {
            instance = new StakeholderDAO();
        }
        return instance;
    }


    public StakeholderDTO save(StakeholderDTO stakeholderDTO) {
        try (Session session = getDriver().session()){
            Integer arcId = getActualArcId(null);
            if (arcId != null) {
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                            " create (a)-[r:hasStakeholder]->(s:Stakeholder {roleOrName:$roleName, contact:$contact, expectation:$expectation})" + System.lineSeparator() +
                            "return Id(s), s.roleOrName, s.contact, s.expectation", parameters("id", arcId, "roleName", stakeholderDTO.getRoleORName(), "contact", stakeholderDTO.getContact(), "expectation", stakeholderDTO.getExpectation()));
                    StakeholderDTO dto = null;
                    if (result != null) {
                        Record record = result.single();
                        dto = new StakeholderDTO(record.get("s.roleOrName").asString(), record.get("s.contact").asString(), record.get("s.expectation").asString());
                        dto.setId(String.valueOf(record.get("Id(s)")));
                    }
                    return dto;
                });
            }
        }
        return null;
    }

    public void update(StakeholderDTO stakeholderDTO) {
        try (Session session = getDriver().session()){
            session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Stakeholder) where Id(a)=$id set a = {roleOrName:$roleName, contact:$contact, expectation:$expectation}" + System.lineSeparator() +
                        "return Id(a), a.roleOrName, a.contact, a.expectation", parameters("id", Integer.parseInt(stakeholderDTO.getId()), "roleName", stakeholderDTO.getRoleORName(), "contact", stakeholderDTO.getContact(), "expectation", stakeholderDTO.getExpectation()));
                StakeholderDTO dto = null;
                if (result != null) {
                    Record record = result.single();
                    dto = new StakeholderDTO(record.get("a.roleOrName").asString(), record.get("a.contact").asString(), record.get("a.expectation").asString());
                    dto.setId(String.valueOf(record.get("Id(a)")));
                }
                return dto;
            });
        }
    }

    public Boolean delete(StakeholderDTO stakeholderDTO) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Stakeholder) where Id(a)=$id" + System.lineSeparator() +
                        "detach delete a return count(*)", parameters("id", Integer.parseInt(stakeholderDTO.getId())));
                Record record = result.single();
                return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }

    public List<StakeholderDTO> findAll(String url) {
        Integer arcId = getActualArcId(url);
        if (arcId == null) {
            return new ArrayList<>();
        }
        return findAllByArcId(arcId.toString());
    }


    public List<StakeholderDTO> findAllByArcId(String arcIdString) {
        if (arcIdString != null && !arcIdString.isEmpty()){
            Integer arcId = Integer.parseInt(arcIdString);

            try (Session session = getDriver().session()){
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(a:Stakeholder)" + System.lineSeparator() +
                            "match(d:Arc42) where Id(d)=$id" + System.lineSeparator() +
                            "match (d)-[r:hasStakeholder]->(a) return Id(a), a.roleOrName, a.contact, a.expectation", parameters("id", arcId));
                    return result.list(r -> {
                        StakeholderDTO stakeholderDTO = new StakeholderDTO(r.get("a.roleOrName").asString(), r.get("a.contact").asString(), r.get("a.expectation").asString());
                        stakeholderDTO.setId(String.valueOf(r.get("Id(a)")));
                        return stakeholderDTO;
                    });
                });
            }
        }
        return new ArrayList<>();
    }

    public StakeholderDTO findById(String id) {

        if (id != null && !id.isEmpty()) {
            Integer idI = Integer.parseInt(id);
            try (Session session = getDriver().session()){
                session.writeTransaction(transaction -> {
                    Result result = transaction.run("match (a:Stakeholder) where Id(a)=$id return return Id(a), a.roleOrName, a.contact, a.expectation", parameters("id", idI));
                    StakeholderDTO dto = null;
                    if (result != null) {
                        Record record = result.single();
                        dto = new StakeholderDTO(record.get("a.roleOrName").asString(), record.get("a.contact").asString(), record.get("a.expectation").asString());
                        dto.setId(String.valueOf(record.get("Id(a)")));
                    }
                    return dto;
                });
            }
        }
        return null;
    }
}
