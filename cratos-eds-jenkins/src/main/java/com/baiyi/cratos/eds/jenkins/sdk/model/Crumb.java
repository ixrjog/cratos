package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;

/**
 * @author Adrien Lecharpentier
 *         <a href="mailto:adrien.lecharpentier@gmail.com">adrien.
 *         lecharpentier@gmail.com</a>
 */
@Getter
public class Crumb extends BaseModel {

    private String crumbRequestField;
    private String crumb;

    public Crumb() {
    }

    public Crumb(String crumbRequestField, String crumb) {
        this.crumbRequestField = crumbRequestField;
        this.crumb = crumb;
    }

}