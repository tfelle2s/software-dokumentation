package org.arc42.dokumentation.model.dao.arc42documentation;


import org.arc42.analyse.model.dto.GitHubSecurityAdvisoryDTO;
import org.arc42.dokumentation.view.util.DateFormater;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;



public class GithubSecurityAdvisoryDAO extends ARC42DAOAbstract<GitHubSecurityAdvisoryDTO, String>{

    private static GithubSecurityAdvisoryDAO instance;

    public static GithubSecurityAdvisoryDAO getInstance() {
        if (instance == null) {
            instance = new GithubSecurityAdvisoryDAO();
        }
        return instance;
    }


    private GithubSecurityAdvisoryDAO() {
        super();
    }

    @Override
    public GitHubSecurityAdvisoryDTO save(GitHubSecurityAdvisoryDTO gitHubSecurityAdvisoryDTO) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("merge(s:SecurityAdvisory {id:$id, published:$published, updated:$updated, title:$title, category:$category, content:$content}) return s.id, s.published, s.updated, s.title, s.category, s.content",
                        parameters("id", gitHubSecurityAdvisoryDTO.getId(), "published", DateFormater.format(gitHubSecurityAdvisoryDTO.getPublished()), "updated", DateFormater.format(gitHubSecurityAdvisoryDTO.getUpdated()), "title", gitHubSecurityAdvisoryDTO.getTitle(), "category", gitHubSecurityAdvisoryDTO.getCategory(), "content", gitHubSecurityAdvisoryDTO.getContent()));
                GitHubSecurityAdvisoryDTO dto = null;
                if (result != null) {
                    Record record = result.single();
                    if (record != null) {
                        Date published = null;
                        Date updated = null;
                        try {
                            String publishedString = record.get(1).asString();
                            String updatedString = record.get(2).asString();
                            published = DateFormater.parse(publishedString);
                            updated = DateFormater.parse(updatedString);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dto = new GitHubSecurityAdvisoryDTO(record.get(0).asString(), published, updated, record.get(3).asString(), record.get(4).asString(), record.get(5).asString());
                    }
                }
                return dto;
            });
        }
    }

    @Override
    public Boolean delete(GitHubSecurityAdvisoryDTO gitHubSecurityAdvisoryDTO) {
        return false;
    }

    @Override
    public void update(GitHubSecurityAdvisoryDTO gitHubSecurityAdvisoryDTO) {
    }

    @Override
    public List<GitHubSecurityAdvisoryDTO> findAll(String url) {
        try (Session session = getDriver().session()){
            return session.writeTransaction(transaction -> {
                Result result = transaction.run("match(s:SecurityAdvisory) return s.id, s.published, s.updated, s.title, s.category, s.content");
                List<GitHubSecurityAdvisoryDTO> dtos = null;
                if (result != null) {
                    dtos = result.list(record -> {
                        GitHubSecurityAdvisoryDTO dto = null;
                        Date published;
                        Date updated;
                        try {
                            String publishedString = record.get(1).asString();
                            String updatedString = record.get(2).asString();
                            published = DateFormater.parse(publishedString);
                            updated = DateFormater.parse(updatedString);
                            dto = new GitHubSecurityAdvisoryDTO(record.get(0).asString(), published, updated, record.get(3).asString(), record.get(4).asString(), record.get(5).asString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return dto;
                    });

                }
                return dtos;
            });
        }
    }

    @Override
    public GitHubSecurityAdvisoryDTO findById(String id) {
        return null;
    }


}

