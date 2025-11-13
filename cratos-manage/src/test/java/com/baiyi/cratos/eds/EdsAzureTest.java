package com.baiyi.cratos.eds;


import com.baiyi.cratos.eds.azure.graph.model.GraphUserModel;
import com.baiyi.cratos.eds.azure.repo.GraphUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAzureConfigModel;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/10 10:35
 * &#064;Version 1.0
 */
public class EdsAzureTest extends BaseEdsTest<EdsAzureConfigModel.Azure> {

    @Test
    public void test1() throws Exception {
        // ClientCredentialGrant.doTest();
        EdsAzureConfigModel.Azure azure = getConfig(51);
        List<GraphUserModel.User> users = GraphUserRepo.listUsers(azure);
        System.out.println(users);

//        for (GraphUserModel.User user : users) {
//
//            InMemoryBackingStore backingStore = (InMemoryBackingStore) user.getBackingStore();
//            Map<String, Object> map = backingStore.enumerate();
//            GraphUserModel.User u =
//            GraphUserModel.User.of(map);
//
//            System.out.println(u);
//
//        }

    }

    @Test
    public void test2() throws Exception {
        EdsAzureConfigModel.Azure azure = getConfig(51);
        GraphUserModel.User u = GraphUserRepo.getUserById(azure, "102e560b-9741-42b4-9324-4428ad0c70ae");
        System.out.println(u);
    }

}
