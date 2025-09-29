package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/11 17:29
 * @Version 1.0
 */
@Getter
public enum CredentialTypeEnum {

    /**
     * 凭据类型
     */
    USERNAME_WITH_PASSWORD("Username with password"),
    SSH_USERNAME_WITH_PRIVATE_KEY("SSH Username with private key"),
    SSH_USERNAME_WITH_PUBLIC_KEY("SSH Username with public key"),
    SSH_USERNAME_WITH_KEY_PAIR("SSH Username with key pair"),
    TOKEN("Token"),
    ACCESS_KEY("Access key"),
    // Kubernetes Config 客户端配置文件
    KUBE_CONFIG("Kubeconfig"),
    // SSL 证书（Pem/Key）
    SSL_CERTIFICATES("SSL certificates"),
    OTP("OTP"),
    // 字典
    DICTIONARY("Dictionary"),
    // https://cloud.google.com/docs/authentication/application-default-credentials?hl=zh-cn
    GOOGLE_ADC("Google Application Default Credentials"),
    AZURE_SERVICE_PRINCIPAL("Azure Service Principal");

    private final String displayName;

    CredentialTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public static OptionsVO.Options toOptions() {
        return OptionsVO.Options.builder()
                .options(getOptions())
                .build();
    }

    private static List<OptionsVO.Option> getOptions() {
        return Arrays.stream(CredentialTypeEnum.values())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.getDisplayName())
                        .value(e.name())
                        .build())
                .collect(Collectors.toList());
    }

}
