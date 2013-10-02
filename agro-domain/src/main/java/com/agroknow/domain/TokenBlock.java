package com.agroknow.domain;

import java.io.Serializable;
import java.util.List;

public class TokenBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> learningResourceTypes;
    private List<String> endUserRoles;
    private List<String> contexts;
//    private Map<String, Object> taxonPaths;
    private String ageRange;

    /**
     * @return the learningResourceTypes
     */
    public List<String> getLearningResourceTypes() {
        return learningResourceTypes;
    }

    /**
     * @return the endUserRoles
     */
    public List<String> getEndUserRoles() {
        return endUserRoles;
    }

    /**
     * @return the contexts
     */
    public List<String> getContexts() {
        return contexts;
    }

    /**
     * @return the ageRange
     */
    public String getAgeRange() {
        return ageRange;
    }

    /**
     * @param learningResourceTypes
     *            the learningResourceTypes to set
     */
    public void setLearningResourceTypes(List<String> learningResourceTypes) {
        this.learningResourceTypes = learningResourceTypes;
    }

    /**
     * @param endUserRoles
     *            the endUserRoles to set
     */
    public void setEndUserRoles(List<String> endUserRoles) {
        this.endUserRoles = endUserRoles;
    }

    /**
     * @param contexts
     *            the contexts to set
     */
    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }

    /**
     * @param ageRange
     *            the ageRange to set
     */
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

//    public Map<String, Object> getTaxonPaths() {
//        return taxonPaths;
//    }
//
//    public void setTaxonPaths(Map<String, Object> taxonPaths) {
//        this.taxonPaths = taxonPaths;
//    }

}
