package com.agroknow.domain.agrif;

import java.io.Serializable;
import java.util.List;

public class Expression implements Serializable {

    private static final long serialVersionUID = 1L;

    private String language;
    private List<Manifestation> manifestations;
    private List<Citation> citations;
    private List<Publisher> publishers;
    private List<String> descriptionEdition;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Manifestation> getManifestations() {
        return manifestations;
    }

    public void setManifestations(List<Manifestation> manifestations) {
        this.manifestations = manifestations;
    }

    public List<Citation> getCitations() {
        return citations;
    }

    public void setCitations(List<Citation> citations) {
        this.citations = citations;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    public List<String> getDescriptionEdition() {
        return descriptionEdition;
    }

    public void setDescriptionEdition(List<String> descriptionEdition) {
        this.descriptionEdition = descriptionEdition;
    }

    
}