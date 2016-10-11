/*
 * Copyright 2016 International Systems Research Co. (ISR).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.isr.application.account.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

/**
 * This is the model for {@link GrantedAuthority}, used for security
 * authentication.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Entity
@Cacheable
@Table(name = "account_user_granted_authority")
@EntityListeners(AuditingEntityListener.class)
public class AccountUserGrantedAuthority
        implements Comparable<AccountUserGrantedAuthority>, GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false,
            name = "id")
    @TableGenerator(initialValue = 1,
            name = "account_user_granted_authority_id_generator",
            pkColumnName = "table_name",
            pkColumnValue = "user_granted_authority",
            table = "id_generator",
            valueColumnName = "available_id")
    @GeneratedValue(generator = "account_user_granted_authority_id_generator",
            strategy = GenerationType.TABLE)
    protected long id;

    @Column(name = "authority",
            nullable = false,
            unique = true)
    protected String authority;

    @CreatedBy
    @Column(name = "creator",
            nullable = false)
    protected String creator;

    @CreatedDate
    @Column(nullable = false,
            name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @LastModifiedDate
    @Column(nullable = false,
            name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    public AccountUserGrantedAuthority() {
        createdDate = lastModifiedDate = new Date();
    }

    public AccountUserGrantedAuthority(String authority) {
        this();
        this.authority = authority;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountUserGrantedAuthority otherAccountUserGrantedAuthority) {
        return authority.compareTo(otherAccountUserGrantedAuthority.authority);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.authority);
        return hash;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountUserGrantedAuthority other = (AccountUserGrantedAuthority) obj;
        return Objects.equals(this.authority, other.authority);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "AccountUserGrantedAuthority{" + "id=" + id + ", authority=" + authority + ", creator=" + creator + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + '}';
    }

}
