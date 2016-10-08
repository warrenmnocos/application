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
package jp.co.isr.application.account.service;

import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountClientDetails;
import jp.co.isr.application.account.model.entity.AccountClientGrantedAuthority;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.model.entity.AccountUserGrantedAuthority;

/**
 * This is a service used to populate data stores, used for development
 * purposes.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
public interface DataPopulatorService {

    /**
     * Populates {@link AccountUserGrantedAuthority}.
     */
    void populateAccountUserGrantedAuthority();

    /**
     * Populate
     * {@link Account}, {@link AccountUserDetails}, {@link AccountLoginAudit}.
     */
    void populateAccountUserDetails();

    /**
     * Populates {@link AccountClientGrantedAuthority}.
     */
    void populateAccountClientGrantedAuthority();

    /**
     * Populates {@link AccountClientDetails}.
     */
    void populateAccountClientDetailsService();

}
