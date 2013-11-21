
package com.agroknow.domain;

import com.agroknow.domain.parser.json.CustomJsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author aggelos
 */
public class InternalFormat extends SimpleMetadata {

    protected Date creationDate;
    protected Date lastUpdateDate;
    protected String status;
    protected String set;
    protected String identifier;

    public InternalFormat() {}

    public InternalFormat(Set<String> locations, Set<String> identifiers) {
        super(locations, identifiers);
    }

    /*
     * (non-Javadoc)
     * @see com.agroknow.domain.SimpleMetadata#getIdentifiers()
     */
    @Override
    @JsonIgnore
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<String>(1);
        result.add(this.getIdentifier());
        return result;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @JsonDeserialize(using=CustomJsonDateDeserializer.class)
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonDeserialize(using=CustomJsonDateDeserializer.class)
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


}
