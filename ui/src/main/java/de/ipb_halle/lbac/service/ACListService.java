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
package de.ipb_halle.lbac.service;

/**
 * ACListService provides service to load, save, update access control lists
 * (ACLs). The ACLs are considered to be immutable, i.e. if an ACL is changed, a
 * new ACList object should be created. If this new ACL already exists in the
 * database (as determined by the ACList.permEquals() method), this existing
 * ACList should replace the newly created ACL.
 *
 * This immutability is currently not enforced by implementation but it should
 * be part of convention.
 */
import de.ipb_halle.lbac.admission.GlobalAdmissionContext;
import de.ipb_halle.lbac.entity.ACEntry;
import de.ipb_halle.lbac.entity.ACEntryEntity;
import de.ipb_halle.lbac.entity.ACList;
import de.ipb_halle.lbac.entity.ACListEntity;

import de.ipb_halle.lbac.entity.ACObject;
import de.ipb_halle.lbac.entity.ACPermission;
import de.ipb_halle.lbac.entity.Membership;
import de.ipb_halle.lbac.entity.Node;
import de.ipb_halle.lbac.entity.User;
import de.ipb_halle.lbac.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;

@Stateless
public class ACListService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(name = "de.ipb_halle.lbac")
    private EntityManager em;

    @Inject
    private MembershipService membershipService;

    @Inject
    private MemberService memberService;

    @Inject
    private NodeService nodeService;

    private User ownerAccount;
    private transient Logger logger;

    /**
     * default constructor
     */
    public ACListService() {
        this.logger = LogManager.getLogger(this.getClass().getName());
    }

    /**
     * this method logs an error upon injection failure
     */
    @PostConstruct
    public void ACListServiceInit() {
        if (em == null) {
            logger.error("Injection failed for EntityManager. @PersistenceContext(name = \"de.ipb_halle.lbac\")");
        }
    }

    /**
     * Convenience Method to obtain the Owner user.
     *
     * @return the pseudo-account "ownerAcount"
     */
    public User getOwnerAccount() {
        if (this.ownerAccount == null) {
            UserEntity ue = this.em.find(UserEntity.class, UUID.fromString(GlobalAdmissionContext.OWNER_ACCOUNT_ID));
            if (ue != null) {
                Node node = this.nodeService.loadById(ue.getNode());
                this.ownerAccount = new User(ue, node);
            }
        }
        return this.ownerAccount;
    }

    /**
     * check if User u is permitted to perform action perm under ACList acl.
     *
     * @param perm
     * @param acl
     * @param u
     * @return true if action is permitted
     */
    public boolean isPermitted(ACPermission perm, ACList acl, User u) {
        Iterator<Membership> iter = this.membershipService.loadMemberOf(u).iterator();
        while (iter.hasNext()) {
            if (acl.getPerm(perm, iter.next().getGroup())) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if User u is permitted to perform action perm on ACObject aco. This
     * method also honours the owner of the field, which may have special
     * permissions to an object.
     *
     * @param perm the permission flag (READ, CREATE, EDIT, DELETE, ...)
     * @param aco the access controlled object
     * @param u the user for which access should be checked.
     * @return true if action is permitted
     */
    public boolean isPermitted(ACPermission perm, ACObject aco, User u) {
        ACList acl = aco.getACList();
        if (acl == null) {
            return false;
        }
        if (u.equals(aco.getOwner())) {
            if (acl.getPerm(perm, getOwnerAccount())) {
                return true;
            }
        }
        Iterator<Membership> iter = this.membershipService.loadMemberOf(u).iterator();
        while (iter.hasNext()) {
            if (acl.getPerm(perm, iter.next().getGroup())) {
                return true;
            }
        }
        return false;
    }

    /**
     * load the list of ACEntryEntries from the database
     * for a given ACList.
     * @param ae the id of the ACList
     */
    public List<ACEntry> loadACEntries(ACList acl) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();

        CriteriaQuery<ACEntryEntity> criteriaQuery = builder.createQuery(ACEntryEntity.class);
        Root<ACEntryEntity> acEntryRoot = criteriaQuery.from(ACEntryEntity.class);
        criteriaQuery.select(acEntryRoot);
        Predicate predicate = builder.equal(acEntryRoot.get("id").get("aclist_id"), acl.getId());
        criteriaQuery.where(predicate);
        List<ACEntry> l = new ArrayList<ACEntry> ();
        for(ACEntryEntity e : this.em.createQuery(criteriaQuery).getResultList()) {
            l.add(new ACEntry(e, acl, this.memberService.loadMemberById(e.getId().getMemberId())));
        }
        return l;
    }
    
    /**
     * load an already persisted ACList based on the permEquals() method or
     * return null
     */
    public ACList loadExisting(ACList ac) {
        ac.updatePermCode();

        CriteriaBuilder builder = this.em.getCriteriaBuilder();

        CriteriaQuery<ACListEntity> criteriaQuery = builder.createQuery(ACListEntity.class);
        Root<ACListEntity> acListRoot = criteriaQuery.from(ACListEntity.class);
        criteriaQuery.select(acListRoot);
        Predicate predicate = builder.equal(acListRoot.get("permCode"), ac.getPermCode());
        criteriaQuery.where(predicate);

        List<ACListEntity> l = this.em.createQuery(criteriaQuery).getResultList();
        if (l != null) {
            ListIterator<ACListEntity> li = l.listIterator();
            while (li.hasNext()) {
                ACListEntity entity = li.next();
                ACList acList = new ACList(entity);
                List<ACEntry> aceList = loadACEntries(acList);
                acList.setACEntries(aceList);
                if (ac.permEquals(acList)) {
                    return acList;
                }
            }
        }
        return null;
    }

    /**
     * load a ACList object by acl_id
     *
     * @param ai acl id
     * @return the ACList object
     */
    public ACList loadById(UUID ai) {
        ACListEntity entity = this.em.find(ACListEntity.class, ai);
        ACList acl = new ACList(entity);
        acl.setACEntries(loadACEntries(acl));
        return acl;
    }

    /**
     * save a single ACList object
     *
     * @param acl the ACList to save
     * @return the original ACList (persisted) or an already persisted ACList
     * with identical permissions.
     */
    public ACList save(ACList acl) {
        ACList r = loadExisting(acl);
        if (r == null) {
            ACListEntity entity = acl.createEntity();
            this.em.merge(entity);
            for(ACEntry ae: acl.getACEntries().values()) {
                this.em.merge(ae.createEntity());
            }
            return acl;
        }
        return r;
    }

}
