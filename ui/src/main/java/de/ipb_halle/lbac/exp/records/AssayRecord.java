/*
 * Cloud Resource & Information Management System (CRIMSy)
 * Copyright 2020 Leibniz-Institut f. Pflanzenbiochemie
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
package de.ipb_halle.lbac.exp.records;

import de.ipb_halle.lbac.entity.DTO;
import de.ipb_halle.lbac.exp.ExpRecord;
import de.ipb_halle.lbac.exp.ExpRecordType;
import de.ipb_halle.lbac.exp.entity.AssayRecordEntity;
import de.ipb_halle.lbac.material.Material;

import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author fbroda
 */
public class AssayRecord implements DTO {

    private Logger logger = LogManager.getLogger(this.getClass().getName());
    private Long            recordid;
    private Assay           assay;
    private Material        material;
    private AssayOutcome    outcome;


    /**
     * constructor
     */
    public AssayRecord(AssayRecordEntity entity, Assay assay, Material material) {
        this.assay = assay;
        this.material = material;
        this.recordid = entity.getRecordId();
        // this.outcome = AssayOutcome.fromString(entity.getOutcome());
    }

    public AssayRecordEntity createEntity() {
        return new AssayRecordEntity()
            .setExpRecordId(this.assay.getExpRecordId())
            .setRecordId(this.recordid)
            .setMaterialId(this.material.getId())
            .setOutcome(this.outcome.toString());
    }

    public Assay getAssay() {
        return this.assay;
    }

    public Material getMaterial() {
        return this.material;
    }

    public AssayOutcome getOutcome() {
        return this.outcome;
    }

    public Long getRecordId() { 
        return this.recordid; 
    }

    public AssayRecord setAssay(Assay assay) {
        this.assay = assay;
        return this;
    }

    public AssayRecord setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public AssayRecord setOutcome(AssayOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    public AssayRecord setRecordId(Long recordid) { 
        this.recordid = recordid; 
        return this; 
    }
}
