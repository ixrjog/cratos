package com.baiyi.cratos.eds.ldap.mapper;

import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:16
 * @Version 1.0
 */
public class GroupAttributesMapper implements AttributesMapper<LdapGroup.Group> {

    /**
     * Map Attributes to an object. The supplied attributes are the attributes
     * from a single SearchResult.
     *
     * @param attrs attributes from a SearchResult.
     * @return an object built from the attributes.
     * @throws NamingException if any error occurs mapping the attributes
     */
    @Override
    public LdapGroup.Group mapFromAttributes(Attributes attrs) throws NamingException {
        return LdapGroup.Group.builder()
                .groupName((String) attrs.get("cn")
                        .get())
                .build();
    }

}