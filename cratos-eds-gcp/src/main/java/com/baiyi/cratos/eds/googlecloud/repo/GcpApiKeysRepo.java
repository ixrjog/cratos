package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.builder.GcpApiKeysSettingsBuilder;
import com.google.api.apikeys.v2.ApiKeysClient;
import com.google.api.apikeys.v2.ApiKeysSettings;
import com.google.api.apikeys.v2.GetKeyStringResponse;
import com.google.api.apikeys.v2.Key;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 14:06
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GcpApiKeysRepo {

    private final GcpApiKeysSettingsBuilder gcpApiKeysSettingsBuilder;

    public List<Key> listApiKeys(EdsConfigs.Gcp googleCloud) throws IOException {
        ApiKeysSettings settings = gcpApiKeysSettingsBuilder.buildApiKeysSettings(googleCloud);
        try (ApiKeysClient client = ApiKeysClient.create(settings)) {
            String parent = googleCloud.getProject().toProjectName() + "/locations/global";
            List<Key> keys = Lists.newArrayList();
            for (Key key : client.listKeys(parent)
                    .iterateAll()) {
                keys.add(key);
            }
            return keys;
        }
    }

    public String getKeyString(EdsConfigs.Gcp googleCloud, String keyName) throws IOException {
        ApiKeysSettings settings = gcpApiKeysSettingsBuilder.buildApiKeysSettings(googleCloud);
        try (ApiKeysClient client = ApiKeysClient.create(settings)) {
            GetKeyStringResponse response = client.getKeyString(keyName);
            return response.getKeyString();
        }
    }

}
