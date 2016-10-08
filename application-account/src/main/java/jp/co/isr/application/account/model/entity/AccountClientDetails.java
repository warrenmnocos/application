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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This is the model used for OAuth2 {@link ClientDetails}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "account_client_details")
@EntityListeners(AuditingEntityListener.class)
public class AccountClientDetails
        implements Comparable<AccountClientDetails>, ClientDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false,
            name = "id")
    @TableGenerator(initialValue = 1,
            name = "account_client_details_id_generator",
            pkColumnName = "table_name",
            pkColumnValue = "account_client_details",
            table = "id_generator",
            valueColumnName = "available_id")
    @GeneratedValue(generator = "account_client_details_id_generator",
            strategy = GenerationType.TABLE)
    @NotNull
    protected long id;

    @Column(length = 1024,
            name = "client_id",
            nullable = false)
    @NotNull
    protected String clientId;

    @Column(length = 1024,
            name = "client_secret",
            nullable = false)
    @NotNull
    protected String clientSecret;

    @ElementCollection(fetch = FetchType.EAGER,
            targetClass = String.class)
    @CollectionTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_scope")
    @Column(length = 1024,
            name = "scope",
            nullable = false)
    @NotNull
    protected Set<String> scope;

    @ElementCollection(fetch = FetchType.EAGER,
            targetClass = String.class)
    @CollectionTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_resource_id")
    @Column(length = 1024,
            name = "resource_id",
            nullable = false)
    @NotNull
    protected Set<String> resourceIds;

    @ElementCollection(fetch = FetchType.EAGER,
            targetClass = String.class)
    @CollectionTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_authorized_grant_type")
    @Column(length = 1024,
            name = "authorized_grant_type",
            nullable = false)
    @NotNull
    protected Set<String> authorizedGrantTypes;

    @ElementCollection(fetch = FetchType.EAGER,
            targetClass = String.class)
    @CollectionTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_registered_redirect_uri")
    @Column(length = 1024,
            name = "registered_redirect_uri",
            nullable = false)
    @NotNull
    protected Set<String> registeredRedirectUri;

    @ElementCollection(fetch = FetchType.EAGER,
            targetClass = String.class)
    @CollectionTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_auto_approve_scope")
    @Column(length = 1024,
            name = "auto_approve_scope",
            nullable = false)
    @NotNull
    protected Set<String> autoApproveScopes;

    @ManyToMany(fetch = FetchType.EAGER,
            targetEntity = AccountClientGrantedAuthority.class)
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseJoinColumns = @JoinColumn(name = "account_client_granted_authority_id",
                    nullable = false,
                    referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "account_client_details_id",
                    nullable = false,
                    referencedColumnName = "id"),
            name = "account_client_details_authority")
    @NotNull
    protected List<GrantedAuthority> authorities;

    @Column(name = "access_token_validity_seconds")
    protected Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds")
    protected Integer refreshTokenValiditySeconds;

    @Transient
    protected Map<String, Object> additionalInformation;

    @Column(name = "auto_approve")
    protected boolean autoApprove;

    @Column(name = "scoped")
    protected boolean scoped;

    @Column(name = "secret_required")
    protected boolean secretRequired;

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

    public AccountClientDetails() {
        this.authorities = Collections.emptyList();
        this.authorizedGrantTypes = Collections.emptySet();
        this.resourceIds = Collections.emptySet();
        this.scope = Collections.emptySet();
        this.additionalInformation = Collections.emptyMap();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountClientDetails otherAccountClientDetails) {
        return clientId.compareTo(otherAccountClientDetails.clientId);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.clientId);
        hash = 61 * hash + Objects.hashCode(this.clientSecret);
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
        final AccountClientDetails other = (AccountClientDetails) obj;
        if (!Objects.equals(this.clientId, other.clientId)) {
            return false;
        }
        return Objects.equals(this.clientSecret, other.clientSecret);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUris) {
        this.registeredRedirectUri = registeredRedirectUris;
    }

    public Set<String> getAutoApproveScopes() {
        return autoApproveScopes;
    }

    public void setAutoApproveScopes(Set<String> autoApproveScopes) {
        this.autoApproveScopes = autoApproveScopes;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return autoApproveScopes.contains(scope);
    }

    @Override
    public boolean isScoped() {
        return scoped;
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }

    @Override
    public boolean isSecretRequired() {
        return secretRequired;
    }

    public void setSecretRequired(boolean secretRequired) {
        this.secretRequired = secretRequired;
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
        return "AccountClientDetails{" + "id=" + id + ", clientId=" + clientId + ", clientSecret=" + clientSecret + ", scope=" + scope + ", resourceIds=" + resourceIds + ", authorizedGrantTypes=" + authorizedGrantTypes + ", registeredRedirectUri=" + registeredRedirectUri + ", autoApproveScopes=" + autoApproveScopes + ", authorities=" + authorities + ", accessTokenValiditySeconds=" + accessTokenValiditySeconds + ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds + ", additionalInformation=" + additionalInformation + ", autoApprove=" + autoApprove + ", scoped=" + scoped + ", secretRequired=" + secretRequired + ", creator=" + creator + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + '}';
    }

}
