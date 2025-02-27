package com.baiyi.cratos.eds.ldap.client;

import com.baiyi.cratos.common.cred.Authorization;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.ldap.factory.LdapFactory;
import com.baiyi.cratos.eds.ldap.mapper.GroupAttributesMapper;
import com.baiyi.cratos.eds.ldap.mapper.PersonAttributesMapper;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.util.LdapUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;

import javax.naming.directory.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.ldap.client.LdapClient.SEARCH_KEY.OBJECTCLASS;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:21
 * @Version 1.0
 */
@Slf4j
@Component
public class LdapClient {

    public static final String GROUP_MEMBER = "groupMember";
    //public static final String GROUP_DN = "{}={},{}";

    public interface SEARCH_KEY {
        String OBJECTCLASS = "objectclass";
    }

    private LdapTemplate buildLdapTemplate(EdsLdapConfigModel.Ldap ldap) {
        return LdapFactory.buildLdapTemplate(ldap);
    }

    /**
     * 查询所有Person
     *
     * @return
     */
    public List<LdapPerson.Person> queryPersonList(EdsLdapConfigModel.Ldap ldap) {
        return buildLdapTemplate(ldap).search(query().where(OBJECTCLASS)
                .is(ldap.getUser()
                        .getObjectClass()), new PersonAttributesMapper());
    }

    /**
     * 查询所有Person username
     *
     * @return
     */
    public List<String> queryPersonNameList(EdsLdapConfigModel.Ldap ldap) {
        return buildLdapTemplate(ldap).search(query().where(OBJECTCLASS)
                .is(ldap.getUser()
                        .getObjectClass()), (AttributesMapper<String>) attrs -> (String) attrs.get(ldap.getUser()
                        .getId())
                .get());
    }

    /**
     * 通过dn查询Person
     *
     * @param dn
     * @return
     */
    public LdapPerson.Person findPersonByDn(EdsLdapConfigModel.Ldap ldap, String dn) {
        return buildLdapTemplate(ldap).lookup(dn, new PersonAttributesMapper());
    }

    /**
     * 通过dn查询Group
     *
     * @param dn
     * @return
     */
    public LdapGroup.Group findGroupByDn(EdsLdapConfigModel.Ldap ldap, String dn) {
        LdapGroup.Group group = buildLdapTemplate(ldap).lookup(dn, new GroupAttributesMapper());
        group.setGroupDn(Joiner.on(",")
                .join(dn, ldap.getBase()));
        return group;
    }

    /**
     * 校验登录
     *
     * @param credential
     * @return
     */
    public boolean verifyLogin(EdsLdapConfigModel.Ldap ldap, Authorization.Credential credential) {
        if (credential.isEmpty()) {
            return false;
        }
        final String username = credential.getUsername();
        final String password = credential.getPassword();
        log.info("Verify login content username={}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(OBJECTCLASS, "person"))
                .and(new EqualsFilter(ldap.getUser()
                        .getId(), username));
        try {
            return buildLdapTemplate(ldap).authenticate(ldap.getUser()
                    .getDn(), filter.toString(), password);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * z
     * 解除绑定
     *
     * @param dn
     */
    public void unbind(EdsLdapConfigModel.Ldap ldap, String dn) {
        buildLdapTemplate(ldap).unbind(dn);
    }

    private void bind(EdsLdapConfigModel.Ldap ldap, String dn, Object obj, Attributes attrs) {
        buildLdapTemplate(ldap).bind(dn, obj, attrs);
    }

    /**
     * 绑定用户
     *
     * @param person
     * @return
     */
    public void bindPerson(EdsLdapConfigModel.Ldap ldap, LdapPerson.Person person) {
        final String userId = ldap.getUser()
                .getId();
        final String userBaseDN = ldap.getUser()
                .getDn();
        final String userObjectClass = ldap.getUser()
                .getObjectClass();
        try {
            final String rdn = LdapUtils.toUserRDN(ldap, person);
            final String dn = Joiner.on(",")
                    .skipNulls()
                    .join(rdn, userBaseDN);
            // 基类设置
            BasicAttribute ocattr = new BasicAttribute("objectClass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            if (!userObjectClass.equalsIgnoreCase("person") && !userObjectClass.equalsIgnoreCase(
                    "organizationalPerson")) {
                ocattr.add(userObjectClass);
            }
            // 用户属性
            Attributes attrs = new BasicAttributes();
            attrs.put(ocattr);
            // cn={username}
            attrs.put(userId, person.getUsername());
            attrs.put("sn", person.getUsername());
            attrs.put("displayName", person.getDisplayName());
            attrs.put("mail", person.getEmail());
            attrs.put("userPassword", person.getUserPassword());
            attrs.put("mobile", (StringUtils.isEmpty(person.getMobile()) ? "0" : person.getMobile()));
            bind(ldap, dn, null, attrs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 绑定用户组
     *
     * @param group
     * @return
     */
    public void bindGroup(EdsLdapConfigModel.Ldap ldap, LdapGroup.Group group) {
        final String groupId = ldap.getGroup()
                .getId();
        final String groupBaseDN = ldap.getGroup()
                .getDn();
        final String groupObjectClass = ldap.getGroup()
                .getObjectClass();

        final String rdn = LdapUtils.toGroupRDN(ldap, group);
        final String dn = Joiner.on(",")
                .skipNulls()
                .join(rdn, groupBaseDN);
        // 基类设置
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("top");
        ocattr.add(groupObjectClass);
        // 用户属性
        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        // cn={groupName}
        attrs.put(groupId, group.getGroupName());
        // 添加一个空成员
        // attrs.put(GROUP_MEMBER, "");
        try {
            bind(ldap, dn, null, attrs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void updatePerson(EdsLdapConfigModel.Ldap ldap, LdapPerson.Person person) {
        String dn = LdapUtils.toUserDN(ldap, person);
        LdapPerson.Person checkPerson = findPersonByDn(ldap, dn);
        if (checkPerson == null) {
            return;
        }
        try {
            if (!StringUtils.isEmpty(person.getDisplayName()) && !person.getDisplayName()
                    .equals(checkPerson.getDisplayName())) {
                modifyAttributes(ldap, dn, "displayName", person.getDisplayName());
            }
            if (!StringUtils.isEmpty(person.getEmail()) && !person.getEmail()
                    .equals(checkPerson.getEmail())) {
                modifyAttributes(ldap, dn, "mail", person.getEmail());
            }
            if (!StringUtils.isEmpty(person.getMobile()) && !person.getMobile()
                    .equals(checkPerson.getMobile())) {
                modifyAttributes(ldap, dn, "mobile", person.getMobile());
            }
            if (!StringUtils.isEmpty(person.getUserPassword())) {
                modifyAttributes(ldap, dn, "userpassword", person.getUserPassword());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 查询所有Group
     *
     * @return
     */
    public List<LdapGroup.Group> queryGroupList(EdsLdapConfigModel.Ldap ldap) {
        return buildLdapTemplate(ldap).search(query().where(OBJECTCLASS)
                .is(ldap.getGroup()
                        .getObjectClass()), new GroupAttributesMapper());
    }

    public List<String> queryGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName) {
        try {
            DirContextAdapter adapter = (DirContextAdapter) buildLdapTemplate(ldap).lookup(
                    LdapUtils.toGroupDN(ldap, groupName));
            String[] members = adapter.getStringAttributes(ldap.getGroup()
                    .getMemberAttribute());
            return Arrays.stream(members)
                    .map(member -> member.split("[=,]"))
                    .filter(m -> m.length > 2 && !m[1].equals("admin"))
                    .map(m -> m[1])
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return List.of();
    }

    public void removeGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName, String username) {
        modificationGroupMember(ldap, groupName, username, DirContext.REMOVE_ATTRIBUTE);
    }

    public void addGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName, String username) {
        modificationGroupMember(ldap, groupName, username, DirContext.ADD_ATTRIBUTE);
    }

    private void modificationGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName, String username,
                                         int modificationType) {
        String userDn = LdapUtils.toUserDN(ldap, LdapPerson.Person.builder()
                .username(username)
                .build());
        String userFullDn = Joiner.on(",")
                .skipNulls()
                .join(userDn, ldap.getBase());
        try {
            buildLdapTemplate(ldap).modifyAttributes(LdapUtils.toGroupDN(ldap, groupName),
                    new ModificationItem[]{new ModificationItem(modificationType, new BasicAttribute(
                            ldap.getGroup()
                                    .getMemberAttribute(), userFullDn))});
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void modifyAttributes(EdsLdapConfigModel.Ldap ldap, String dn, String attrId, String value) {
        buildLdapTemplate(ldap).modifyAttributes(dn,
                new ModificationItem[]{new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute(attrId, value))});
    }

    public boolean hasPersonInLdap(EdsLdapConfigModel.Ldap ldap, String username) {
        String userDn = LdapUtils.toUserDN(ldap, LdapPerson.Person.builder()
                .username(username)
                .build());
        try {
            DirContextAdapter adapter = (DirContextAdapter) buildLdapTemplate(ldap).lookup(userDn);
            String cn = adapter.getStringAttribute(ldap.getUser()
                    .getId());
            if (username.equalsIgnoreCase(cn)) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public List<String> searchLdapGroup(EdsLdapConfigModel.Ldap ldap, String username) {
        List<String> groupList = Lists.newArrayList();
        try {
            String groupBaseDN = ldap.getGroup()
                    .getDn();
            String groupMember = ldap.getGroup()
                    .getMemberAttribute();
            String userId = ldap.getUser()
                    .getId();
            String userDn = LdapUtils.toUserDN(ldap, LdapPerson.Person.builder()
                    .username(username)
                    .build());
            String userFullDn = Joiner.on(",")
                    .skipNulls()
                    .join(userDn, ldap.getBase());
            groupList = buildLdapTemplate(ldap).search(LdapQueryBuilder.query()
                    .base(groupBaseDN)
                    .where(groupMember)
                    .is(userFullDn)
                    .and(userId)
                    .like("*"), (AttributesMapper<String>) attributes -> attributes.get(userId)
                    .get(0)
                    .toString());
        } catch (Exception e) {
            log.warn("Search ldap group error: username={}, {}", username, e.getMessage());
        }
        return groupList;
    }

}
