package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/19 下午5:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class SshFingerprintUtils {

    public static String calcFingerprint(String privateKey, String publicKey) {
        String fingerprint = "-";
        
        if (!StringUtils.hasText(publicKey)) {
            return fingerprint;
        }
        
        // 提取公钥部分（去除前面的注释）
        publicKey = extractPublicKey(publicKey);
        
        try {
            KeyPair keyPair = KeyPair.load(
                new JSch(), 
                StringUtils.hasText(privateKey) ? privateKey.getBytes() : null, 
                publicKey.getBytes()
            );
            
            if (keyPair == null) {
                throw new InvalidCredentialException("The SSH key(pair) is incorrect.");
            }
            
            String fp = keyPair.getFingerPrint();
            if (fp != null) {
                fingerprint = fp;
            }
        } catch (JSchException e) {
            throw new InvalidCredentialException(
                "The SSH key(pair) is incorrect: " + e.getMessage()
            );
        }
        
        return fingerprint;
    }
    
    /**
     * 提取公钥内容，去除前面的注释或其他文本
     */
    private static String extractPublicKey(String publicKey) {
        // 支持的公钥类型前缀
        String[] keyPrefixes = {
            "ssh-rsa",
            "ssh-dss",
            "ssh-ed25519",
            "ecdsa-sha2-nistp256",
            "ecdsa-sha2-nistp384",
            "ecdsa-sha2-nistp521"
        };
        
        for (String prefix : keyPrefixes) {
            int index = publicKey.indexOf(prefix);
            if (index >= 0) {
                return publicKey.substring(index);
            }
        }
        
        // 如果没有找到已知前缀，返回原始内容
        return publicKey;
    }

}
