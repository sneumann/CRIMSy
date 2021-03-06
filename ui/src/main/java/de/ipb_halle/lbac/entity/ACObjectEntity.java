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
package de.ipb_halle.lbac.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base class for an access controlled objects.
 */
@MappedSuperclass
public class ACObjectEntity {

    @Column(name = "aclist_id")
    private UUID aclist;

    @Column(name = "owner_id")
    private UUID owner;

    /**
     * Gets the owner
     *
     * @return owner
     */
    public UUID getOwner() {
        return this.owner;
    }

    /**
     * Gets the ACList
     *
     * @return ACList
     */
    public UUID getACList() {
        return this.aclist;
    }

    /**
     * Sets the ACList
     *
     * @param acl ACList
     * @return ACObject for chaining
     */
    public ACObjectEntity setACList(UUID acl) {
        this.aclist = acl;
        return this;
    }

    /**
     * Sets the owner of the ACObject
     *
     * @param u User who will be owner
     * @return ACObject for chaining
     */
    public ACObjectEntity setOwner(UUID u) {
        this.owner = u;
        return this;
    }

}
