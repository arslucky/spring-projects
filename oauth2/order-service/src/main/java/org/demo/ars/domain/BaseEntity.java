package org.demo.ars.domain;

import java.util.Date;
/**
 * @author arsen.ibragimov
 *
 */
public abstract class BaseEntity {

    private Date createdAt;

    private Date lastModified;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt( Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified( Date lastModified) {
        this.lastModified = lastModified;
    }

}
