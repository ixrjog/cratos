package com.baiyi.cratos.eds.jenkins.model;

import lombok.Getter;

@Getter
public class Executable {
    private Long number;

    private String url;

    public Executable setNumber(Long number) {
        this.number = number;
        return this;
    }

    public Executable setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        Executable other = (Executable) obj;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (url == null) {
            return other.url == null;
        } else return url.equals(other.url);
    }

}