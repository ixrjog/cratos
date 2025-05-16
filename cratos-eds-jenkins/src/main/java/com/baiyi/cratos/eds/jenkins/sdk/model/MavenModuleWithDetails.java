/*
 * Copyright (c) 2018 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.baiyi.cratos.eds.jenkins.sdk.model;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.eds.jenkins.helper.FunctionalHelper.SET_CLIENT;
import static java.util.stream.Collectors.toList;

/**
 * Model Class for Maven Modules
 * 
 * @author Jakub Zacek
 */
@Setter
public class MavenModuleWithDetails extends BaseModel {

    private List<Build> builds = Collections.emptyList();
    @Getter
    private List actions = Collections.emptyList();
    @Getter
    private String displayName;
    @Getter
    private BuildResult result;
    @Getter
    private String url;
    @Getter
    private long duration;
    @Getter
    private long timestamp;

    public List<Build> getBuilds() {
        return builds.stream()
            .map(SET_CLIENT(this.getClient()))
            .collect(toList());
    }
    
    public Build getBuildByNumber(final int buildNumber) {
        return builds.stream()
            .filter(isBuildNumberEqualTo(buildNumber))
            .map(SET_CLIENT(this.getClient()))
            .findFirst()
            .orElse(Build.BUILD_HAS_NEVER_RUN);
    }

    public String getConsoleOutputText() throws IOException {
        return client.get(getUrl() + "/logText/progressiveText");
    }

    public TestReport getTestReport() throws IOException {
        return client.get(this.getUrl() + "/testReport/?depth=1", TestReport.class);
    }

}