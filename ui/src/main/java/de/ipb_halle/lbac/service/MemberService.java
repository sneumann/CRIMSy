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
 * MemberService provides service to load, save, update users and groups.
 */
import de.ipb_halle.lbac.admission.AdmissionSubSystemType;
import de.ipb_halle.lbac.entity.Group;
import de.ipb_halle.lbac.entity.GroupEntity;
import de.ipb_halle.lbac.entity.Member;
import de.ipb_halle.lbac.entity.Node;
import de.ipb_halle.lbac.entity.User;
import de.ipb_halle.lbac.entity.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Stateless
public class MemberService implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static String PUBLIC_ACCOUNT_ID = "088e3bc0-7fb2-422e-b29a-71ca3ec907d2";
    private final static String OWNER_ACCOUNT_ID = "0a662938-e11e-4825-bd45-fa117963d12f";
    private final String SQL_GET_SIMILAR_NAMES = "SELECT name FROM usersgroups WHERE LOWER(name) LIKE LOWER(:name) AND membertype='U' AND name <> 'deactivated'";

    @PersistenceContext(name = "de.ipb_halle.lbac")
    private EntityManager em;

    @Inject
    private NodeService nodeService;

    private Logger logger;

    public MemberService() {
        this.logger = LogManager.getLogger(this.getClass().getName());
    }

    /**
     * load groups which match the definition given by the criteria map.
     *
     * @param cmap criteria map
     * @return list of groups matching the definition
     */
    @SuppressWarnings("unchecked")
    public List<Group> loadGroups(Map<String, Object> cmap) {

        CriteriaBuilder builder = this.em.getCriteriaBuilder();

        CriteriaQuery<GroupEntity> criteriaQuery = builder.createQuery(GroupEntity.class);
        Root<GroupEntity> groupRoot = criteriaQuery.from(GroupEntity.class);
        criteriaQuery.select(groupRoot);
        List<Predicate> predicates = new ArrayList<>();

        if (cmap.get("id") != null) {
            predicates.add(builder.equal(groupRoot.get("id"), cmap.get("id")));
        }
        if (cmap.get("node_id") != null) {
            predicates.add(builder.equal(groupRoot.get("node"), cmap.get("node_id")));
        }
        if (cmap.get("name") != null) {
            predicates.add(builder.equal(groupRoot.get("name"), cmap.get("name")));
        }
        if (cmap.get("subSystemType") != null) {
            if (cmap.get("subSystemType").getClass().equals(AdmissionSubSystemType.class)) {
                // single subsystem type
                predicates.add(builder.equal(groupRoot.get("subSystemType"), cmap.get("subSystemType")));
            } else {
                // list of subsystem types
                List<Predicate> pl = Arrays.stream((AdmissionSubSystemType[]) cmap.get("subSystemType"))
                        .map(t -> builder.equal(groupRoot.get("subSystemType"), t))
                        .collect(Collectors.toList());
                predicates.add(builder.or(pl.toArray(new Predicate[0])));
            }
        }
        if (cmap.get("subSystemData") != null) {
            predicates.add(builder.equal(groupRoot.get("subSystemData"), cmap.get("subSystemData")));
        }

        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));

        List<Group> result = new ArrayList<>();
        for (GroupEntity ge : this.em.createQuery(criteriaQuery).getResultList()) {
            Node node = this.nodeService.loadById(ge.getNode());
            result.add(new Group(ge, node));
        }
        return result;
    }

    /**
     * Gets all materialnames which matches the pattern %name%
     *
     * @param name name for searching
     * @return List of matching materialnames
     */
    @SuppressWarnings("unchecked")
    public Set<String> loadSimilarUserNames(String name) {
        return new HashSet<>(this.em.createNativeQuery(SQL_GET_SIMILAR_NAMES)
                .setParameter("name", "%" + name + "%")
                .getResultList());

    }

    /**
     * load members which match the definition given by the criteria map.
     *
     * @param cmap criteria map
     * @return list of Members matching the definition
     */
    @SuppressWarnings("unchecked")
    public List<User> loadUsers(Map<String, Object> cmap) {

        CriteriaBuilder builder = this.em.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteriaQuery = builder.createQuery(UserEntity.class);
        Root<UserEntity> userRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(userRoot);
        List<Predicate> predicates = new ArrayList<>();

        if (cmap.get("id") != null) {
            predicates.add(builder.equal(userRoot.get("id"), cmap.get("id")));
        }
        if (cmap.get("login") != null) {
            predicates.add(builder.equal(userRoot.get("login"), cmap.get("login")));
        }
        if (cmap.get("node_id") != null) {
            predicates.add(builder.equal(userRoot.get("node"), cmap.get("node_id")));
        }
        if (cmap.get("subSystemType") != null) {
            if (cmap.get("subSystemType").getClass().equals(AdmissionSubSystemType.class)) {
                // single subsystem type
                predicates.add(builder.equal(userRoot.get("subSystemType"), cmap.get("subSystemType")));
            } else {
                // list of subsystem types
                List<Predicate> pl = Arrays.stream((AdmissionSubSystemType[]) cmap.get("subSystemType"))
                        .map(t -> builder.equal(userRoot.get("subSystemType"), t))
                        .collect(Collectors.toList());
                predicates.add(builder.or(pl.toArray(new Predicate[0])));
            }
        }
        if (cmap.get("subSystemData") != null) {
            predicates.add(builder.equal(userRoot.get("subSystemData"), cmap.get("subSystemData")));
        }

        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[]{})));

        List<User> result = new ArrayList<User>();
        for (UserEntity ue : this.em.createQuery(criteriaQuery).getResultList()) {
            Node node = this.nodeService.loadById(ue.getNode());
            result.add(new User(ue, node));
        }
        return result;
    }

    /**
     * load a Group object by id
     *
     * @param id group id
     * @return the Group object
     */
    public Group loadGroupById(UUID id) {
        GroupEntity ge = this.em.find(GroupEntity.class, id);
        if (ge != null) {
            Node node = this.nodeService.loadById(ge.getNode());
            return new Group(ge, node);
        }
        return null;
    }

    /**
     * load a User object by id
     *
     * @param id user id
     * @return the User object
     */
    public User loadUserById(UUID id) {
        UserEntity ue = this.em.find(UserEntity.class, id);
        if (ue != null) {
            Node node = this.nodeService.loadById(ue.getNode());
            return new User(ue, node);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public Member loadMemberById(UUID id) {
        Member member = loadGroupById(id);
        if (member == null) {
            return loadUserById(id);
        }
        return member;
    }

    /**
     * save a single group
     *
     * @param g the Group to save
     * @return
     */
    public Group save(Group g) {
        GroupEntity ge = g.createEntity();
        ge = this.em.merge(ge);
        if (ge != null) {
            return new Group(ge, g.getNode());
        }
        return null;
    }

    /**
     * save a member; redirects to save(User) or save(Group)
     *
     * @param m
     * @return
     */
    public Member save(Member m) {
        if (m.isGroup()) {
            return save((Group) m);
        } else {
            return save((User) m);
        }
    }

    /**
     * save a single user
     *
     * @param u the User to save
     * @return
     */
    public User save(User u) {
        UserEntity ue = u.createEntity();
        ue = em.merge(ue);
        if (ue != null) {
            return new User(ue, u.getNode());
        }
        return null;
    }

}
