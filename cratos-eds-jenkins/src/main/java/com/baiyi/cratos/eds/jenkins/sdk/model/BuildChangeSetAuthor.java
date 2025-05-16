package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;

/**
 * @author Karl Heinz Marbaise
 *
 */
@Getter
public class BuildChangeSetAuthor {

    private String absoluteUrl;
    private String fullName;

    public BuildChangeSetAuthor setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
        return this;
    }

    public BuildChangeSetAuthor setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((absoluteUrl == null) ? 0 : absoluteUrl.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildChangeSetAuthor other = (BuildChangeSetAuthor) obj;
        if (absoluteUrl == null) {
            if (other.absoluteUrl != null)
                return false;
        } else if (!absoluteUrl.equals(other.absoluteUrl))
            return false;
        if (fullName == null) {
            return other.fullName == null;
        } else return fullName.equals(other.fullName);
    }

}