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
package de.ipb_halle.lbac.material.difference;

import de.ipb_halle.lbac.entity.ACList;
import de.ipb_halle.lbac.material.bean.ModificationType;
import de.ipb_halle.lbac.material.entity.MaterialHistoryEntity;
import de.ipb_halle.lbac.service.ACListService;
import java.util.Date;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author fmauz
 */
public class MaterialOverviewDifference implements MaterialDifference {

    private Logger logger = LogManager.getLogger(this.getClass().getName());

    private int materialID;
    private UUID actorID;
    private Date mDate;
    private ModificationType action;
    private ACList acListOld;
    private ACList acListNew;
    private Integer projectIdOld;
    private Integer projectIdNew;
    private UUID ownerIdOld;
    private UUID ownerIdNew;
    private String digest;

    public MaterialOverviewDifference() {
    }

    public MaterialOverviewDifference(
            MaterialHistoryEntity dbentity,
            ACListService acService) {
        materialID = dbentity.getId().getMaterialid();
        actorID = dbentity.getActorid();
        mDate = dbentity.getId().getmDate();
        action = ModificationType.valueOf(dbentity.getAction());
        acListOld = acService.loadById(dbentity.getAclistid_old());
        acListNew = acService.loadById(dbentity.getAclistid_new());
        projectIdOld = dbentity.getProjectid_old();
        projectIdNew = dbentity.getProjectid_new();
        ownerIdOld = dbentity.getOwnerid_old();
        ownerIdNew = dbentity.getOwnerid_new();
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public UUID getActorID() {
        return actorID;
    }

    public void setActorID(UUID actorID) {
        this.actorID = actorID;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public ModificationType getAction() {
        return action;
    }

    public void setAction(ModificationType action) {
        this.action = action;
    }

    public ACList getAcListOld() {
        return acListOld;
    }

    public void setAcListOld(ACList acListOld) {
        this.acListOld = acListOld;
    }

    public ACList getAcListNew() {
        return acListNew;
    }

    public void setAcListNew(ACList acListNew) {
        this.acListNew = acListNew;
    }

    public Integer getProjectIdOld() {
        return projectIdOld;
    }

    public void setProjectIdOld(Integer projectIdOld) {
        this.projectIdOld = projectIdOld;
    }

    public Integer getProjectIdNew() {
        return projectIdNew;
    }

    public void setProjectIdNew(Integer projectIdNew) {
        this.projectIdNew = projectIdNew;
    }

    public UUID getOwnerIdOld() {
        return ownerIdOld;
    }

    public void setOwnerIdOld(UUID ownerIdOld) {
        this.ownerIdOld = ownerIdOld;
    }

    public UUID getOwnerIdNew() {
        return ownerIdNew;
    }

    public void setOwnerIdNew(UUID ownerIdNew) {
        this.ownerIdNew = ownerIdNew;
    }

    public boolean differenceFound() {

        boolean aclist = !(acListOld == null && acListNew == null);
        boolean project = projectIdNew != projectIdOld;
        boolean owner = !(ownerIdOld == null && ownerIdNew == null);

        return aclist || project || owner;

    }

    @Override
    public void initialise(int materialId, UUID actorID, Date mDate) {
        this.materialID = materialId;
        this.action = ModificationType.EDIT;
        this.actorID = actorID;
        this.mDate = mDate;
    }

    @Override
    public Date getModificationDate() {
        return mDate;
    }

    public void debug() {
        logger.info("--MaterialOverviewDifference--");
        logger.info("  materialID " + materialID);
        logger.info("  actorID " + actorID);
        logger.info("  date " + mDate);
        logger.info("  acListOld " + acListOld);
        logger.info("  acListNew " + acListNew);
        logger.info("  projectIdOld " + projectIdOld);
        logger.info("  projectIdNew " + projectIdNew);
        logger.info("  ownerIdOld " + ownerIdOld);
        logger.info("  ownerIdNew " + ownerIdNew);
        logger.info("                              ");

    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

}
