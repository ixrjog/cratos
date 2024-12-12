/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.cratos.eds.jenkins.model;

import lombok.Getter;

import java.util.List;

/**
 * This class is only needed to get all builds in
 * {@link JobWithDetails#getAllBuilds()}.
 * 
 * @author Karl Heinz Marbaise
 *
 *         NOTE: This class is not part of any public API
 */
@Getter
class AllBuilds extends BaseModel {
    private List<Build> allBuilds;

    public AllBuilds() {
    }

}