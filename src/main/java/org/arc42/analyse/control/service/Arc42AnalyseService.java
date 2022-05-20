package org.arc42.analyse.control.service;


import org.arc42.analyse.model.dto.DesignDecisionDTOForSA;
import org.arc42.analyse.model.dto.GitHubSecurityAdvisoryDTO;
import org.arc42.analyse.model.util.Credentials;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;

import java.util.List;

public class Arc42AnalyseService {
    private static Arc42AnalyseService instance;
    private final Neo4jConnectForNeo4j neo4j;

    private Arc42AnalyseService() {
        neo4j = new Neo4jConnectForNeo4j(Credentials.URL, Credentials.USER, Credentials.PASSWORD);
    }

    public static Arc42AnalyseService getInstance() {
        if (instance == null) {
            instance = new Arc42AnalyseService();
        }
        return instance;
    }

    public String getTitleByArcId(Integer arcId) {
        return neo4j.getTitleByArcId(arcId);
    }

    public Integer countDocumentedElemente(Integer arcId) {
        return neo4j.countDocumentedElemente(arcId);
    }

    public Integer countQualitaetsZiele(Integer arcId) {
        return neo4j.countQualitaetsZiele(arcId);
    }

    public Integer countStakeholder(Integer arcId) {
        return neo4j.countStakeholder(arcId);
    }

    public Integer countAufgabenstellung(Integer arcId) {
        return neo4j.countAufgabenstellung(arcId);
    }

    public int countTechnischRandbedingung(Integer arcId) {
        return neo4j.countTechnischRandbedingung(arcId);
    }

    public int countOrganisatorischRandbedingung(Integer arcId) {
        return neo4j.countOrganisatorischRandbedingung(arcId);
    }

    public int countKonventionRandbedingung(Integer arcId) {
        return neo4j.countKonventionRandbedingung(arcId);
    }

    public int countFachlicherKontext(Integer arcId) {
        return neo4j.countFachlicherKontext(arcId);
    }

    public String getFachlicherKontext(Integer arcId) {
        return neo4j.getFachlicherKontext(arcId);
    }

    public int countTechnischerKontext(Integer arcId) {
        return neo4j.countTechnischerKontext(arcId);
    }

    public String getTechnischerKontext(Integer arcId) {
        return neo4j.getTechnischerKontext(arcId);
    }

    public int countLoesungsStrategie(Integer arcId) {
        return neo4j.countLoesungsStrategie(arcId);
    }

    public String getLoesungsStrategie(Integer arcId) {
        return neo4j.getLoesungsStrategie(arcId);
    }

    public int countBausteinDiagram(Integer arcId) {
        return neo4j.countBausteinDiagram(arcId);
    }

    public String getBausteinBeschreibung(Integer arcId) {
        return neo4j.getBausteinBeschreibung(arcId);
    }

    public int countLaufzeitDiagram(Integer arcId) {
        return neo4j.countLaufzeitDiagram(arcId);
    }

    public String getLaufzeitBeschreibung(Integer arcId) {
        return neo4j.getLaufzeitBeschreibung(arcId);
    }

    public int countVerteilungDiagram(Integer arcId) {
        return neo4j.countVerteilungDiagram(arcId);
    }

    public String getVerteilungBeschreibung(Integer arcId) {
        return neo4j.getVerteilungBeschreibung(arcId);
    }

    public int countKonzepte(Integer arcId) {
        return neo4j.countKonzepte(arcId);
    }

    public String getKonzepte(Integer arcId) {
        return neo4j.getKonzepte(arcId);
    }

    public int countArchitekturEntscheidung(Integer arcId) {
        return neo4j.countArchitekturEntscheidung(arcId);
    }

    public int countDesignDecision(Integer arcId) {
        return neo4j.countDesignDecision(arcId);
    }

    public List<DesignDecisionDTOForSA> findDesignDecisionForArcId(Integer arcId) {
        return this.neo4j.findDesignDecisionForArcId(arcId);
    }

    public String getDecisionIdForRationaleId(Integer arcId, String rationaleId) {
        return this.neo4j.getDecisionIdForRationaleId(arcId, rationaleId);
    }

    public String getDesignDecisionTitle(String ddId) {
        return this.neo4j.getDesignDecisionTitle(ddId);
    }

    public String getReferencedAlternativeId(String ddId) {
        return neo4j.getReferencedAlternativeId(ddId);
    }

    public String getReferencedAlternativeTitle(String ddId) {
        return neo4j.getReferencedAlternativeTitle(ddId);
    }

    public List<String> getRationaleIdForArcId(Integer arcId) {
        return neo4j.getRationaleIdForArcId(arcId);
    }

    public String getRationaleBeschreibung(Integer arcId, String rationaleId) {
        return neo4j.getRationaleBeschreibung(arcId, rationaleId);
    }

    public String getRationaleReferenz(Integer arcId, String rationaleId) {
        return neo4j.getRationaleReferenz(arcId, rationaleId);
    }

    public String getArchitekturEntscheidung(Integer arcId) {
        return neo4j.getArchitekturEntscheidung(arcId);
    }

    public int countQualitaetSzenarien(Integer arcId) {
        return neo4j.countQualitaetSzenarien(arcId);
    }

// --Commented out by Inspection START (08.05.22, 20:37):
//    public String getQualitaetSzenarien(Integer arcId) {
//        return neo4j.getQualitaetSzenarien(arcId);
//    }
// --Commented out by Inspection STOP (08.05.22, 20:37)

    public int countRisiken(Integer arcId) {
        return neo4j.countRisiken(arcId);
    }

    public String getRisiken(Integer arcId) {
        return neo4j.getRisiken(arcId);
    }

    public int countGlossar(Integer arcId) {
        return neo4j.countGlossar(arcId);
    }

    public String getGlossar(Integer arcId) {
        return neo4j.getGlossar(arcId);
    }

    public void deleteAllGitHubSecurityAdvisory() {
        neo4j.deleteAllGitHubSecurityAdvisory();
    }

    public List<GitHubSecurityAdvisoryDTO> findAllGitHubSecurityAdvisory() {
        return neo4j.findAllGitHubSecurityAdvisory();
    }

    public void saveGitHubSecurityAdvisory(GitHubSecurityAdvisoryDTO gitHubSecurityAdvisoryDTO) {
        neo4j.saveGitHubSecurityAdvisory(gitHubSecurityAdvisoryDTO);
    }

    public Integer countAnalyseResult(Integer arcId) {
        return neo4j.countAnalyseResult(arcId);
    }

    public void deleteAnalyseResult(Integer arcId) {
        neo4j.deleteAnalyseResult(arcId);
    }

    public void saveAnalseResult(Integer arcId, String result) {
        neo4j.saveAnalseResult(arcId, result);
    }

    public String getAffectedArchitekturElement(String id) {
        return neo4j.getAffectedArchitekturElement(id);
    }

    public List<QualityDTO> getAllQualityGoals(String id) {
        List<QualityDTO> qualityDTOList = neo4j.findAllQualityGoalsByArcId(id);
        qualityDTOList.addAll(neo4j.findAllQualityScenariosByArcId(id));
        return qualityDTOList;
    }
}