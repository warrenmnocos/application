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
package jp.co.isr.application.account.model.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is a DTO consisting of account model and account user details model.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountWithUserDetailsDto
        implements Comparable<AccountWithUserDetailsDto>, Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute
    protected long id;

    @NotNull
    @XmlAttribute
    protected String email;

    @NotNull
    @XmlAttribute
    protected String password;

    @NotNull
    @XmlElement
    protected String firstName;

    @XmlElement
    protected String middleName;

    @NotNull
    @XmlElement
    protected String lastName;

    @XmlElement
    protected Map<String, AddressDto> addresses;

    @XmlElement
    protected Map<String, String> contacts;

    @XmlElement
    protected boolean accountNonExpired;

    @XmlElement
    protected boolean accountNonLocked;

    @XmlElement
    protected boolean credentialsNonExpired;

    @XmlElement
    protected boolean enabled;

    @NotNull
    @XmlElement
    protected Set<String> grantedAuthorities;

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountWithUserDetailsDto otherAccountWithUserDetailsDto) {
        return email.compareTo(otherAccountWithUserDetailsDto.email);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.email);
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
        final AccountDto other = (AccountDto) obj;
        return Objects.equals(this.email, other.email);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, AddressDto> addresses) {
        this.addresses = addresses;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(Set<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public String toString() {
        return "AccountWithUserDetailsDto{" + "id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", addresses=" + addresses + ", contacts=" + contacts + ", accountNonExpired=" + accountNonExpired + ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + ", grantedAuthorities=" + grantedAuthorities + '}';
    }

}
