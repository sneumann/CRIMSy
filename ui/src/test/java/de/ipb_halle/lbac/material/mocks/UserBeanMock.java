/*
 * Leibniz Bioactives Cloud
 * Copyright 2017 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.ipb_halle.lbac.material.mocks;

import de.ipb_halle.lbac.admission.UserBean;
import de.ipb_halle.lbac.entity.User;

/**
 *
 * @author fmauz
 */
public class UserBeanMock extends UserBean {

    private User currentAccount;

    public void setCurrentAccount(User u) {
        this.currentAccount = u;
    }

    @Override
    public User getCurrentAccount() {
        return currentAccount;
    }
}
