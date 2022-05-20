package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.data.Roles;
import com.vaadin.flow.server.VaadinSession;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class Arc42DokuNameDAO extends ARC42DAOAbstract<DokuNameDTO, String>{


    private static Arc42DokuNameDAO instance = null;

    public static Arc42DokuNameDAO getInstance() {
        if (instance == null) {
            instance = new Arc42DokuNameDAO();
        }
        return instance;
    }



    @Override
    public DokuNameDTO save(DokuNameDTO name) {
        String username = (String) VaadinSession.getCurrent().getAttribute(Roles.CURRENTUSER);


        try  (Session session = getDriver().session()) {
            return session.writeTransaction(tx -> {
                Result result = tx.run("match(u:Developer {devname:$username})" + System.lineSeparator() +
                        "create (d: Arc42 {name: $name}) " + System.lineSeparator() +
                        "create (u)-[r:create]->(d) return Id(d)", parameters("name", name.getName(), "username", username));
                Record record = result.single();
                return new DokuNameDTO(name.getName(), String.valueOf(record.get("Id(d)")));
            }
            );

        }
    }


    public Boolean delete1(DokuNameDTO s) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Arc42 {name:$name}) where Id(a)=$id" + System.lineSeparator() +
                        "detach delete a return count(*)", parameters("name", s.getName(), "id", Integer.parseInt(s.getId())));
                Record record = result.single();
                return record != null && !record.get("count(*)").isNull() && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }


    @Override
    public Boolean delete(DokuNameDTO s) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Arc42 {name:$name}) where Id(a)=$id match (a)-[r]->(c)" + System.lineSeparator() +
                        "detach delete r,a,c return count(*)", parameters("name", s.getName(), "id", Integer.parseInt(s.getId())));
                Record record = result.single();
                return record != null && !record.get("count(*)").isNull() && !String.valueOf(record.get("count(*)")).isEmpty();
            });
        }
    }

    @Override
    public void update(DokuNameDTO s) {
        try (Session session = getDriver().session()){
            session.writeTransaction(transaction -> {
                DokuNameDTO dto = null;
                if (s.getId() != null) {
                    Result result = transaction.run("match (a:Arc42) where Id(a)=$id" + System.lineSeparator() +
                            "set a.name=$name return Id(a), a.name", parameters("id", Integer.parseInt(s.getId()), "name", s.getName()));
                    if (result != null) {
                        Record record = result.single();
                        dto = new DokuNameDTO(record.get("a.name").asString(), String.valueOf(record.get("Id(a)")));
                    }
                }
                return dto;
            });
        }
    }

    @Override
    public List<DokuNameDTO> findAll(String url) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(a:Arc42) return a.name, Id(a)");
                return result.list(r -> new DokuNameDTO(r.get("a.name").asString(), String.valueOf(r.get("Id(a)"))));
            });
        }
    }

    @Override
    public DokuNameDTO findById(String arc42IdString) {
        if (arc42IdString != null && !arc42IdString.isEmpty()) {
            Integer arc42Id = Integer.parseInt(arc42IdString);
            try (Session session = getDriver().session()){
                return session.writeTransaction(transaction -> {
                    Result result = transaction.run("match(d:Arc42) where Id(d)=$arc return d.name, Id(d)", parameters("arc", arc42Id));
                    Record record = result.single();
                    if (record == null) {
                        return null;
                    }

                    return new DokuNameDTO(record.get("d.name").asString(), String.valueOf(record.get("Id(d)")));
                });
            }
        }
        return null;
    }
}
