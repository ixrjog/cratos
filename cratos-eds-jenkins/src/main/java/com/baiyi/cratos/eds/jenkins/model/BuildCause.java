/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.cratos.eds.jenkins.model;

import lombok.Getter;

import java.util.Optional;

@Getter
public class BuildCause {
    private String shortDescription;

    // For upstreams
    private Integer upstreamBuild;
    private String upstreamProject;
    private String upstreamUrl;

    // For manual kickoffs
    private String userId;
    private String userName;

    public BuildCause() {
        this.upstreamBuild = 0;
        //TODO: Think about initialization of the other
        // attributes as well.
        // userId = StringConstant.EMPTY;
    }

    public BuildCause setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public BuildCause setUpstreamBuild(Integer upstreamBuild) {
        this.upstreamBuild = upstreamBuild;
        return this;
    }

    public BuildCause setUpstreamProject(String upstreamProject) {
        this.upstreamProject = upstreamProject;
        return this;
    }

    public BuildCause setUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
        return this;
    }

    public BuildCause setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public BuildCause setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BuildCause that = (BuildCause) o;

        if (Optional.ofNullable(shortDescription).map(description -> !description.equals(that.shortDescription)).orElseGet(() -> that.shortDescription != null))
            return false;
        if (Optional.ofNullable(upstreamBuild).map(build -> !build.equals(that.upstreamBuild)).orElseGet(() -> that.upstreamBuild != null))
            return false;
        if (Optional.ofNullable(upstreamProject).map(project -> !project.equals(that.upstreamProject)).orElseGet(() -> that.upstreamProject != null))
            return false;
        if (Optional.ofNullable(upstreamUrl).map(url -> !url.equals(that.upstreamUrl)).orElseGet(() -> that.upstreamUrl != null))
            return false;
        if (Optional.ofNullable(userId).map(id -> !id.equals(that.userId)).orElseGet(() -> that.userId != null))
            return false;
        if (Optional.ofNullable(userName).map(name -> !name.equals(that.userName)).orElseGet(() -> that.userName != null))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shortDescription != null ? shortDescription.hashCode() : 0;
        result = 31 * result + (upstreamBuild != null ? upstreamBuild.hashCode() : 0);
        result = 31 * result + (upstreamProject != null ? upstreamProject.hashCode() : 0);
        result = 31 * result + (upstreamUrl != null ? upstreamUrl.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

}