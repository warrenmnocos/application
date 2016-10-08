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
import java.time.LocalDate;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is the template for an account request model.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountRequestDto
        implements Comparable<AccountRequestDto>, Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    protected Set<LocalDate> starts;

    @XmlElement
    protected Set<LocalDate> ends;

    @XmlElement
    protected Set<String> emails;

    @XmlElement
    protected Set<String> firstNames;

    @XmlElement
    protected Set<String> middleNames;

    @XmlElement
    protected Set<String> lastNames;

    @Override
    public int compareTo(AccountRequestDto otherAccountRequestDto) {
        return Integer.compare(
                emails.parallelStream()
                        .map(String::hashCode)
                        .reduce(0, (previousValue, newValue) -> previousValue + newValue),
                otherAccountRequestDto.emails.parallelStream()
                        .map(String::hashCode)
                        .reduce(0, (previousValue, newValue) -> previousValue + newValue));

    }

    public Set<LocalDate> getStarts() {
        return starts;
    }

    public void setStarts(Set<LocalDate> starts) {
        this.starts = starts;
    }

    public Set<LocalDate> getEnds() {
        return ends;
    }

    public void setEnds(Set<LocalDate> ends) {
        this.ends = ends;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public Set<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(Set<String> firstNames) {
        this.firstNames = firstNames;
    }

    public Set<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(Set<String> middleNames) {
        this.middleNames = middleNames;
    }

    public Set<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(Set<String> lastNames) {
        this.lastNames = lastNames;
    }

    @Override
    public String toString() {
        return "AccountRequestDto{" + "starts=" + starts + ", ends=" + ends + ", emails=" + emails + ", firstNames=" + firstNames + ", middleNames=" + middleNames + ", lastNames=" + lastNames + '}';
    }

}
