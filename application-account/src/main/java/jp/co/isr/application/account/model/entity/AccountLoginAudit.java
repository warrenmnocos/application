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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This is an entity model for auditing {@link AccountUserDetails} login
 * activity.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "account_login_audit")
@EntityListeners(AuditingEntityListener.class)
public class AccountLoginAudit implements Comparable<AccountLoginAudit>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false,
            name = "id")
    @TableGenerator(initialValue = 1,
            name = "account_login_audit_id_generator",
            pkColumnName = "table_name",
            pkColumnValue = "account_login_audit",
            table = "id_generator",
            valueColumnName = "available_id")
    @GeneratedValue(generator = "account_login_audit_id_generator",
            strategy = GenerationType.TABLE)
    @NotNull
    protected long id;

    @ManyToOne(fetch = FetchType.EAGER,
            optional = false,
            targetEntity = Account.class)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            name = "account_id",
            nullable = false,
            referencedColumnName = "id")
    @NotNull
    protected Account account;

    @CreatedDate
    @Column(nullable = false,
            name = "login_time")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date loginTime;

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountLoginAudit otherAccountLoginAudit) {
        return account.compareTo(otherAccountLoginAudit.account)
                + loginTime.compareTo(otherAccountLoginAudit.loginTime);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.account);
        hash = 97 * hash + Objects.hashCode(this.loginTime);
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
        final AccountLoginAudit other = (AccountLoginAudit) obj;
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        return Objects.equals(this.loginTime, other.loginTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "AccountLoginAudit{" + "id=" + id + ", account=" + account + ", createdDate=" + loginTime + '}';
    }

}
