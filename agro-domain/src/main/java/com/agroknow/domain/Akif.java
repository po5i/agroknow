package com.agroknow.domain;

import com.agroknow.domain.parser.json.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class Akif extends SimpleMetadata {

    private static final long serialVersionUID = 1L;
    private Date creationDate;
    private Date lastUpdateDate;
    private boolean generateThumbnail;
    private String status;
    private String set;
    private String identifier;
    private List<Contributor> contributors;
    private TokenBlock tokenBlock;
    private Map<String, LanguageBlock> languageBlocks;
    private Rights rights;
    private List<Expression> expressions;
    private Map<String, List<String>> learningObjectives;

    public Akif() {

    }

    public Akif(Set<String> locations, Set<String> identifiers) {
        super(locations, identifiers);
    }

    /**
     * @return the rights
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * @param rights
     *            the rights to set
     */
    public void setRights(Rights rights) {
        this.rights = rights;
    }

    /**
     * @return the learningObjectives
     */
    public Map<String, List<String>> getLearningObjectives() {
        return learningObjectives;
    }

    /**
     * @param learningObjectives
     *            the learningObjectives to set
     */
    public void setLearningObjectives(Map<String, List<String>> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    /*
     * (non-Javadoc)
     * @see com.agroknow.domain.SimpleMetadata#getLocations()
     */
    @Override
    public Set<String> getLocations() {
        if (CollectionUtils.isEmpty(this.expressions))
            return null;

        Set<String> locations = new HashSet<String>();
        for (Expression e : expressions) {
            if (CollectionUtils.isEmpty(e.getManifestations())) {
                continue;
            }
            for (Manifestation mf : e.getManifestations()) {
                if (CollectionUtils.isEmpty(mf.getItems())) {
                    continue;
                }
                for (Manifestation.Item i : mf.getItems()) {
                    if (i.getUrl() != null) {
                        locations.add(i.getUrl());
                    }
                }
            }
        }
        return locations;
    }

    /*
     * (non-Javadoc)
     * @see com.agroknow.domain.SimpleMetadata#getIdentifiers()
     */
    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<String>(1);
        result.add(this.getIdentifier());
        return result;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the lastUpdateDate
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @return the generateThumbnail
     */
    public boolean isGenerateThumbnail() {
        return generateThumbnail;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the set
     */
    public String getSet() {
        return set;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the contributors
     */
    public List<Contributor> getContributors() {
        return contributors;
    }

    /**
     * @return the tokenBlock
     */
    public TokenBlock getTokenBlock() {
        return tokenBlock;
    }

    /**
     * @return the languageBlocks
     */
    public Map<String, LanguageBlock> getLanguageBlocks() {
        return languageBlocks;
    }

    /**
     * @return the expressions
     */
    public List<Expression> getExpressions() {
        return expressions;
    }

    /**
     * @param creationDate
     *            the creationDate to set
     */
    @JsonDeserialize(using=CustomJsonDateDeserializer.class)
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @param lastUpdateDate
     *            the lastUpdateDate to set
     */
    @JsonDeserialize(using=CustomJsonDateDeserializer.class)
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @param generateThumbnail
     *            the generateThumbnail to set
     */
    public void setGenerateThumbnail(boolean generateThumbnail) {
        this.generateThumbnail = generateThumbnail;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param set
     *            the set to set
     */
    public void setSet(String set) {
        this.set = set;
    }

    /**
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @param contributors
     *            the contributors to set
     */
    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    /**
     * @param tokenBlock
     *            the tokenBlock to set
     */
    public void setTokenBlock(TokenBlock tokenBlock) {
        this.tokenBlock = tokenBlock;
    }

    /**
     * @param languageBlocks
     *            the languageBlocks to set
     */
    public void setLanguageBlocks(Map<String, LanguageBlock> languageBlocks) {
        this.languageBlocks = languageBlocks;
    }

    /**
     * @param expressions
     *            the expressions to set
     */
    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }
}