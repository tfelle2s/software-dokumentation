package org.arc42.dokumentation.model.dao.arc42documentation;


import Model.Class;
import Model.*;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.neo4j.driver.Values.parameters;

public class DesignDecisionDAO extends ARC42DAOAbstract<DesignDecision, String> {

    private static DesignDecisionDAO instance;

    private DesignDecisionDAO() {
        super();
        Driver driver = getDriver();
    }

    public static DesignDecisionDAO getInstance() {
        if (instance == null) {
            instance = new DesignDecisionDAO();
        }
        return instance;
    }

    @Override
    public DesignDecision save(DesignDecision designDecision) {
        return save(null, designDecision);
    }

    public DesignDecision save(ImageDTO imageDTO, DesignDecision designDecision) {
        Integer arcId = getActualArcId(null);
        try (Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("merge (d:DesignDecision {id:$id, title:$title, beschreibung:$beschreibung}) return Id(d)", parameters("imageId", imageDTO.getId(), "id", designDecision.getId(), "title", designDecision.getTitle(), "beschreibung", designDecision.getBeschreibung()));
                Record single = result.single();
                if (single != null) {
                    String nodeId = String.valueOf(single.get("Id(d)"));
                    ExistenceDecision existenceDecision;
                    existenceDecision = (ExistenceDecision) designDecision;

                    if (imageDTO != null && imageDTO.getUxfStream() != null) {
                        transaction.run("match(d:DesignDecision) match(i:Image) where Id(d)=$nodeId and Id(i)=$id create (i)-[r:hatDecision]->(d) return Id(i)", parameters("nodeId", Integer.valueOf(nodeId), "id", Integer.valueOf(imageDTO.getId())));
                    }
                    transaction.run("match(d:DesignDecision) where Id(d)=$nodeId create (d)-[r:wirdBegründetDurch]->(b:Rationale {id:$id, beschreibung:$beschreibung, referenz:$referenz})", parameters("nodeId", Integer.valueOf(nodeId), "id", designDecision.getRationale().getId(), "beschreibung", designDecision.getRationale().getBeschreibung(), "referenz", designDecision.getRationale().getReferenz()));
                    transaction.run("match(d:DesignDecision) match(a:Arc42) where Id(d)=$nodeId and Id(a)=$docuId merge (d)<-[r:trifft]-(s:Stakeholder {id:$id, contact:$email, expectation:$role, roleOrName:$name})<-[h:hasStakeholder]-(a)", parameters("nodeId", Integer.valueOf(nodeId), "id", designDecision.getStakeholder().getId(), "name", designDecision.getStakeholder().getName(), "email", designDecision.getStakeholder().getEmail(), "role", designDecision.getStakeholder().getRole(), "docuId", arcId));
                    transaction.run("match(d:DesignDecision) where Id(d)=$nodeId merge (d)-[l:ausgewaehlt]->(p:DDAlternative {id:$id, name:$name,  beschreibung:$beschreibung, vorteile:$vorteile, nachteile:$nachteile})", parameters("nodeId", Integer.valueOf(nodeId), "id", designDecision.getDecision().getId(), "name", designDecision.getDecision().getTitle(), "beschreibung", designDecision.getDecision().getBeschreibung(), "vorteile", listAsString(designDecision.getDecision().getVorteile()), "nachteile", listAsString(designDecision.getDecision().getNachteile())));
                    existenceDecision.getArchitekturElements().forEach(element -> transaction.run("match(d:DesignDecision) where Id(d)=$nodeId create (d)-[r:beeinflusst]->(a:ArchitekturElement {name:$name})", parameters("nodeId", Integer.valueOf(nodeId), "name", ((Class) element).getName())));
                    existenceDecision.getIssues().forEach(ddIssue -> transaction.run("match(d:DesignDecision) where Id(d)=$nodeId merge (d)-[l:löst]->(p:DDIssue {id:$id, beschreibung:$beschreibung})", parameters("nodeId", Integer.valueOf(nodeId), "id", ddIssue.getId(), "beschreibung", ddIssue.getBeschreibung())));
                    existenceDecision.getAlternatives().forEach(ddAlternative -> transaction.run("match(d:DesignDecision) where Id(d)=$nodeId merge (d)-[l:hatAlternative]->(p:DDAlternative {id:$id, name:$name,  beschreibung:$beschreibung, vorteile:$vorteile, nachteile:$nachteile})", parameters("nodeId", Integer.valueOf(nodeId), "id", ddAlternative.getId(), "name", ddAlternative.getTitle(), "beschreibung", ddAlternative.getBeschreibung(), "vorteile", listAsString(ddAlternative.getVorteile()), "nachteile", listAsString(ddAlternative.getNachteile()))));
                }
                return designDecision;
            });
        }
    }

    @Override
    public Boolean delete(DesignDecision designDecision) {
        return null;
    }

    @Override
    public void update(DesignDecision designDecision) {
    }

    @Override
    public List<DesignDecision> findAll(String url) {
        return null;
    }

    @Override
    public DesignDecision findById(String id) {

        try (Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                ExistenceDecision decision = new ExistenceDecision();
                Result result = transaction.run("match(d:DesignDecision) where d.id=$id return d.id, d.title, d.beschreibung", parameters("id", id));
                if (result != null) {
                    Record single = result.single();
                    if (single != null) {
                        decision.setBeschreibung(single.get("d.beschreibung").asString());
                        decision.setTitle(single.get("d.title").asString());
                        decision.setId(single.get("d.id").asString());
                    }
                }
                Result aElementsResult = transaction.run("match(d:DesignDecision) match(a:ArchitekturElement)<-[r:beeinflusst]-(d) where d.id=$id return a.name", parameters("id", id));
                if (aElementsResult != null) {
                    Set<UMLComponent> aElement = new HashSet<>(aElementsResult.list(record -> new Class(record.get("a.name").asString(), null)));
                    decision.setArchitekturElements(aElement);
                }
                Result rationaleResult = transaction.run("match(d:DesignDecision)  match (d)-[r:wirdBegründetDurch]->(b:Rationale) where d.id=$id return b.id, b.beschreibung, b.referenz", parameters("id", id));
                if (rationaleResult != null) {
                    Record single = rationaleResult.single();
                    if (single != null) {
                        DDRationale rationale = new DDRationale(single.get("b.id").asString(), single.get("b.beschreibung").asString(), single.get("b.referenz").asString());
                        decision.setRationale(rationale);
                    }
                }
                Result stakeholderResult = transaction.run("match(d:DesignDecision) match (d)<-[r:trifft]-(s:Stakeholder) where d.id=$nodeId return s.id, s.contact, s.expectation, s.roleOrName", parameters("nodeId", id));
                if (stakeholderResult != null) {
                    Record record = stakeholderResult.single();
                    DDStakeholder stakeholder = new DDStakeholder(record.get("s.id").asString(), record.get("s.roleOrName").asString(), record.get("s.contact").asString(), record.get("s.expectation").asString());
                    decision.setStakeholder(stakeholder);
                }
                Result issueResult = transaction.run("match(d:DesignDecision) match (d)-[l:löst]->(p:DDIssue) where d.id=$nodeId return p.id, p.beschreibung", parameters("nodeId", id));
                if (issueResult != null) {
                    Set<DDIssue> issues = new HashSet<>(issueResult.list(record -> new DDIssue(record.get("p.id").asString(), record.get("p.beschreibung").asString())));
                    decision.setIssues(issues);
                }
                Result alternativeResult = transaction.run("match(d:DesignDecision) match (d)-[l:hatAlternative]->(p:DDAlternative) where d.id=$nodeId return p.id, p.name, p.beschreibung, p.vorteile, p.nachteile", parameters("nodeId", id));
                if (alternativeResult != null) {
                    Set<DDAlternative> alternatives = new HashSet<>(alternativeResult.list(record -> new DDAlternative(record.get("p.id").asString(), record.get("p.name").asString(), record.get("p.beschreibung").asString())));
                    decision.setAlternatives(alternatives);
                }

                Result decisionResult = transaction.run("match(d:DesignDecision) match (d)-[l:ausgewaehlt]->(p:DDAlternative) where d.id=$nodeId return p.id, p.name, p.beschreibung, p.vorteile, p.nachteile", parameters("nodeId", id));
                if (decisionResult != null) {
                    Record record = decisionResult.single();
                    DDAlternative alternative = new DDAlternative(record.get("p.id").asString(), record.get("p.name").asString(), record.get("p.beschreibung").asString());
                    decision.setDecision(alternative);
                }
                return decision;
            });
        }
    }

    public List<String> findDDIdsForImage(String imageId) {
        try (Session session = getDriver().session()) {
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(i:Image) match(d:DesignDecision) where Id(i)=$id  match (i)-[r:hatDecision]->(d) return d.id", parameters("id", Integer.valueOf(imageId)));
                return new ArrayList<>(result.list(record -> record.get("d.id").asString()));
            });
        }
    }

    private String listAsString(Set<String> strings) {
        StringBuilder result = new StringBuilder();
        Object[] array = strings.toArray();
        if (!strings.isEmpty()) {
            for (int i = 0; i < strings.size(); i++) {
                if (i == 0) {
                    result.append(array[i]);
                } else {
                    result.append(",").append(array[i]);
                }
            }
        }
        return result.toString();
    }

// --Commented out by Inspection START (17.04.22, 17:49):
//    private Set<String> stringAsSet(String strings) {
//        return  new HashSet<String>(Arrays.asList(strings.split(",")));
//    }
// --Commented out by Inspection STOP (17.04.22, 17:49)
}
