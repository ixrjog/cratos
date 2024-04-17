package com.baiyi.cratos.shell.auth.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Author baiyi
 * @Date 2024/4/17 上午11:42
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomPublicKeyConfiguration {

//    private final UserService userService;
//
//    private final UserCredentialService userCredentialService;
//
//    private final DsInstanceService dsInstanceService;
//
//    private final DsInstanceAssetService dsInstanceAssetService;
//
//    @Bean
//    @Primary
//    public PublickeyAuthenticatorProvider publickeyAuthenticatorProvider() {
//        return (username, publicKey, serverSession) -> {
//            User user = userService.getByUsername(username);
//            Map<String, String> userSshKeyDict = getUserSshKeyDict(user);
//            if (userSshKeyDict.isEmpty()) {
//                return false;
//            }
//            try {
//                File sshShellPubKeysTmpFile = Files.createTempFile("ssh-shell-pub-keys-", ".tmp").toFile();
//                try (FileWriter fw = new FileWriter(sshShellPubKeysTmpFile)) {
//                    for (String key : userSshKeyDict.keySet()) {
//                        fw.write(userSshKeyDict.get(key) + "\n");
//                    }
//                    fw.flush();
//                    return new SshShellPublicKeyAuthenticationProvider(sshShellPubKeysTmpFile).authenticate(username, publicKey, serverSession);
//                } catch (Exception e) {
//                    log.error("Error generating user {} public key", username);
//                } finally {
//                    if (!sshShellPubKeysTmpFile.delete()) {
//                        log.debug("Failed to delete temporary file {}", sshShellPubKeysTmpFile.getName());
//                    }
//                }
//            } catch (IOException ignored) {
//            }
//            return false;
//        };
//    }
//
//    private Map<String, String> getUserSshKeyDict(User user) {
//        Map<String, String> sshKeyDict = Maps.newHashMap();
//        List<UserCredential> userCredentialList = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.PUB_KEY.getType());
//        userCredentialList.forEach(c ->
//                sshKeyDict.put(c.getFingerprint(), c.getCredential())
//        );
//        List<DatasourceInstanceAsset> assets = querySshKeyAssets(user.getUsername());
//        assets.forEach(a -> sshKeyDict.put(a.getAssetKey(), a.getAssetKey2()));
//        return sshKeyDict;
//    }
//
//    public List<DatasourceInstanceAsset> querySshKeyAssets(String username) {
//        DsInstanceParam.DsInstanceQuery instanceQuery = DsInstanceParam.DsInstanceQuery.builder()
//                .instanceType(DsTypeEnum.GITLAB.getName())
//                .build();
//
//        List<DatasourceInstance> instances = dsInstanceService.queryByParam(instanceQuery);
//        List<DatasourceInstanceAsset> result = Lists.newArrayList();
//        instances.forEach(i -> {
//            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
//                    .assetType(DsAssetTypeConstants.GITLAB_SSHKEY.name())
//                    .instanceUuid(i.getUuid())
//                    .name(username)
//                    .build();
//            result.addAll(dsInstanceAssetService.queryAssetByAssetParam(asset));
//        });
//        return result;
//    }

}
