package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;

/**
 * This class is based on informations which can be extracted from an running
 * Jenkins instance via
 * <a href="http://jenkins-server/computer/api/schema">http://jenkins-server/
 * computer/api/schema</a>.
 * 
 * @author Karl Heinz Marbaise
 * 
 */
@Getter
public class Executor {

    private Job currentExecutable;
    // in XSD it's a reference to a class
    private Job currentWorkUnit;
    private Boolean idle;
    private Boolean likelyStuck;
    private int number;
    private int progress;

    public Executor setCurrentExecutable(Job currentExecutable) {
        this.currentExecutable = currentExecutable;
        return this;
    }

    public Executor setCurrentWorkUnit(Job currentWorkUnit) {
        this.currentWorkUnit = currentWorkUnit;
        return this;
    }

    public Executor setIdle(Boolean idle) {
        this.idle = idle;
        return this;
    }

    public Executor setLikelyStuck(Boolean likelyStuck) {
        this.likelyStuck = likelyStuck;
        return this;
    }

    public Executor setNumber(int number) {
        this.number = number;
        return this;
    }

    public Executor setProgress(int progress) {
        this.progress = progress;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentExecutable == null) ? 0 : currentExecutable.hashCode());
        result = prime * result + ((currentWorkUnit == null) ? 0 : currentWorkUnit.hashCode());
        result = prime * result + ((idle == null) ? 0 : idle.hashCode());
        result = prime * result + ((likelyStuck == null) ? 0 : likelyStuck.hashCode());
        result = prime * result + number;
        result = prime * result + progress;
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
        Executor other = (Executor) obj;
        if (currentExecutable == null) {
            if (other.currentExecutable != null)
                return false;
        } else if (!currentExecutable.equals(other.currentExecutable))
            return false;
        if (currentWorkUnit == null) {
            if (other.currentWorkUnit != null)
                return false;
        } else if (!currentWorkUnit.equals(other.currentWorkUnit))
            return false;
        if (idle == null) {
            if (other.idle != null)
                return false;
        } else if (!idle.equals(other.idle))
            return false;
        if (likelyStuck == null) {
            if (other.likelyStuck != null)
                return false;
        } else if (!likelyStuck.equals(other.likelyStuck))
            return false;
        if (number != other.number)
            return false;
        return progress == other.progress;
    }

}