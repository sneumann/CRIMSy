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
package de.ipb_halle.lbac.material.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author fmauz
 */
@Entity
@Table(name = "storageconditions_storages")
public class StorageConditionStorageEntity implements Serializable {

    @EmbeddedId
    private StorageConditionStorageId id;

    public StorageConditionStorageEntity(StorageConditionStorageId id) {
        this.id = id;
    }

    public StorageConditionStorageEntity() {
    }

    public StorageConditionStorageId getId() {
        return id;
    }

    public void setId(StorageConditionStorageId id) {
        this.id = id;
    }

}