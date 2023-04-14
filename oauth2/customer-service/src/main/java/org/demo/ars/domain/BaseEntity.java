package org.demo.ars.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

/**
 * @author arsen.ibragimov
 *
 */
@MappedSuperclass
@EntityListeners( AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt( Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified( Long lastModified) {
        this.lastModified = lastModified;
    }

}
