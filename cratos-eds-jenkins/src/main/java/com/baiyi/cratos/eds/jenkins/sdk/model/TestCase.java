package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;

/**
 * @author Karl Heinz Marbaise
 */

@Getter
public class TestCase {

    private int age;
    private String className;
    private double duration;
    private int failedSince;
    private String name;
    private boolean skipped;
    private String status;
    private String errorDetails;
    private String errorStackTrace;

    public TestCase setAge(int age) {
        this.age = age;
        return this;
    }

    public TestCase setClassName(String className) {
        this.className = className;
        return this;
    }

    public TestCase setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public TestCase setFailedSince(int failedSince) {
        this.failedSince = failedSince;
        return this;
    }

    public TestCase setName(String name) {
        this.name = name;
        return this;
    }

    public TestCase setSkipped(boolean skipped) {
        this.skipped = skipped;
        return this;
    }

    public TestCase setStatus(String status) {
        this.status = status;
        return this;
    }

    public TestCase setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
        return this;
    }

    public TestCase setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + age;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((errorDetails == null) ? 0 : errorDetails.hashCode());
        result = prime * result + ((errorStackTrace == null) ? 0 : errorStackTrace.hashCode());
        result = prime * result + failedSince;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (skipped ? 1231 : 1237);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        TestCase other = (TestCase) obj;
        if (age != other.age)
            return false;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        if (errorDetails == null) {
            if (other.errorDetails != null)
                return false;
        } else if (!errorDetails.equals(other.errorDetails))
            return false;
        if (errorStackTrace == null) {
            if (other.errorStackTrace != null)
                return false;
        } else if (!errorStackTrace.equals(other.errorStackTrace))
            return false;
        if (failedSince != other.failedSince)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (skipped != other.skipped)
            return false;
        if (status == null) {
            return other.status == null;
        } else return status.equals(other.status);
    }

}