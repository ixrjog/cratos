package com.baiyi.cratos.eds.ldap.mapper;

import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:19
 * @Version 1.0
 */
public class PersonAttributesMapper implements AttributesMapper<LdapPerson.Person> {

    /**
     * Map Attributes to an object. The supplied attributes are the attributes
     * from a single SearchResult.
     *
     * @param attrs attributes from a SearchResult.
     * @return an object built from the attributes.
     * @throws NamingException if any error occurs mapping the attributes
     */
    @Override
    public LdapPerson.Person mapFromAttributes(Attributes attrs) throws NamingException {
        LdapPerson.Person person = new LdapPerson.Person();
        person.setUsername(getAttribute(attrs, "cn"));
        person.setDisplayName(getAttribute(attrs, "displayName", person.getUsername()));
        person.setMobile(getAttribute(attrs, "mobile"));
        person.setEmail(getAttribute(attrs, "mail"));
        person.setJobNo(getAttribute(attrs, "jobNo"));
        return person;
    }

    private String getAttribute(Attributes attrs, String attributeName) throws NamingException {
        return getAttribute(attrs, attributeName, null);
    }

    private String getAttribute(Attributes attrs, String attributeName, String defaultValue) throws NamingException {
        try {
            return (String) attrs.get(attributeName)
                    .get();
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

}
