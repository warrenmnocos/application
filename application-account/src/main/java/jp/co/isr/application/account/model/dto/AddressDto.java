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
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is the template for an address model.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressDto implements Comparable<AddressDto>, Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    protected String town;

    @XmlElement
    protected String city;

    @XmlElement
    protected String country;

    @XmlElement
    protected String zipCode;

    /**
     * {@inheritDoc }
     */
    @Override
    public int compareTo(AddressDto otherAddress) {
        return country.compareTo(otherAddress.country)
                + zipCode.compareTo(otherAddress.zipCode);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.country);
        hash = 23 * hash + Objects.hashCode(this.zipCode);
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
        final AddressDto other = (AddressDto) obj;
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        return Objects.equals(this.zipCode, other.zipCode);
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "Address{" + "town=" + town + ", city=" + city + ", country=" + country + ", zipCode=" + zipCode + '}';
    }

}
