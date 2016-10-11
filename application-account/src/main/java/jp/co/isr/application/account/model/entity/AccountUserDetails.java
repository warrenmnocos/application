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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedBy;

/**
 * This is the model for {@link UserDetails} associated to an {@link Account},
 * used for security authentication.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Entity
@Cacheable
@Table(name = "account_user_details")
@EntityListeners(AuditingEntityListener.class)
public class AccountUserDetails implements Comparable<AccountUserDetails>, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false,
            name = "id")
    @TableGenerator(initialValue = 1,
            name = "account_user_details_id_generator",
            pkColumnName = "table_name",
            pkColumnValue = "account_user_details",
            table = "id_generator",
            valueColumnName = "available_id")
    @GeneratedValue(generator = "account_user_details_id_generator",
            strategy = GenerationType.TABLE)
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER,
            targetEntity = AccountUserGrantedAuthority.class)
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseJoinColumns = @JoinColumn(name = "jan_user_granted_authority_id",
                    nullable = false,
                    referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "account_user_details_id_",
                    nullable = false,
                    referencedColumnName = "id"),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            name = "account_user_details_granted_authority")
    @NotNull
    protected Collection<AccountUserGrantedAuthority> grantedAuthorities;

    @Column(length = 512,
            nullable = false,
            name = "password")
    @NotNull
    protected String password;

    @Column(nullable = false,
            name = "username",
            unique = true)
    @NotNull
    protected String username;

    @Column(nullable = false,
            name = "account_non_expired")
    protected boolean accountNonExpired;

    @Column(nullable = false,
            name = "account_non_locked")
    protected boolean accountNonLocked;

    @Column(nullable = false,
            name = "credentials_non_expired")
    protected boolean credentialsNonExpired;

    @Column(nullable = false,
            name = "enabled")
    protected boolean enabled;

    @CreatedBy
    @Column(name = "creator",
            nullable = false)
    @NotNull
    protected String creator;

    @CreatedDate
    @Column(nullable = false,
            name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date createdDate;

    @LastModifiedDate
    @Column(nullable = false,
            name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date lastModifiedDate;

    public AccountUserDetails() {
    }

    public AccountUserDetails(String username, String password,
            Collection<AccountUserGrantedAuthority> grantedAuthorities,
            boolean accountNonExpired, boolean accountNonLocked,
            boolean credentialsNonExpired, boolean enabled) {
        this();
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public AccountUserDetails(String username, String password,
            Collection<AccountUserGrantedAuthority> grantedAuthorities, boolean allowed) {
        this(username, password, grantedAuthorities, allowed, allowed, allowed, allowed);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountUserDetails otherAccountUserDetails) {
        return username.compareTo(otherAccountUserDetails.username);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.username);
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
        final AccountUserDetails other = (AccountUserDetails) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<AccountUserGrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(Collection<AccountUserGrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "AccountUserDetails{" + "id=" + id + ", grantedAuthorities=" + grantedAuthorities + ", password=" + password + ", username=" + username + ", accountNonExpired=" + accountNonExpired + ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + ", creator=" + creator + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + '}';
    }

}
