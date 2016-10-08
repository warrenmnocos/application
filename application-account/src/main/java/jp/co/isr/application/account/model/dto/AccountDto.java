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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is the template for an account model DTO.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountDto implements Comparable<AccountDto>, Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute
    protected long id;

    @NotNull
    @XmlAttribute
    protected String email;

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

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AccountDto otherAccountDto) {
        return email.compareTo(otherAccountDto.email);
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

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "AccountDto{" + "id=" + id + ", email=" + email + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + '}';
    }

}
