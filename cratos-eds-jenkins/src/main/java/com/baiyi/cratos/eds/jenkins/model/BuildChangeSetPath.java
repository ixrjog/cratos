package com.baiyi.cratos.eds.jenkins.model;

import lombok.Getter;

/**
 * @author Karl Heinz Marbaise
 *
 */
@Getter
public class BuildChangeSetPath {

    /**
     * The SCM operation, <code>add</code> or <code>edit</code> or <code>delete</code>
     *
     * -- GETTER --
     *  Return the SCM operation.
     *
     @see <a href="http://javadoc.jenkins.io/hudson/scm/EditType.html">EditType</a>
      * @return the SCM operation, <code>add</code> or <code>edit</code> or <code>delete</code>
      *
     */
    private String editType;
    private String file;

    /**
     * Sets the SCM operation.
     * @param editType the SCM operation, <code>add</code> or <code>edit</code> or <code>delete</code>
     * @see <a href="http://javadoc.jenkins.io/hudson/scm/EditType.html">EditType</a>
     */
    public BuildChangeSetPath setEditType(String editType) {
        this.editType = editType;
        return this;
    }

    public BuildChangeSetPath setFile(String file) {
        this.file = file;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((editType == null) ? 0 : editType.hashCode());
        result = prime * result + ((file == null) ? 0 : file.hashCode());
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
        BuildChangeSetPath other = (BuildChangeSetPath) obj;
        if (editType == null) {
            if (other.editType != null)
                return false;
        } else if (!editType.equals(other.editType))
            return false;
        if (file == null) {
            return other.file == null;
        } else return file.equals(other.file);
    }

}