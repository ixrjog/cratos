package com.baiyi.cratos.shell.context;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午5:36
 * &#064;Version 1.0
 */
public class ComputerAssetContext {

    private static final ThreadLocal<Map<Integer, EdsAsset>> COMPUTER_CONTEXT = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, ServerAccount>> ACCOUNT_CONTEXT = new ThreadLocal<>();


    // List<ServerAccount>

    public static void setContext(Map<Integer, EdsAsset> computerContext, List<ServerAccount> serverAccounts) {
        COMPUTER_CONTEXT.set(computerContext);
        if (!CollectionUtils.isEmpty(serverAccounts)) {
            ACCOUNT_CONTEXT.set(serverAccounts.stream()
                    .collect(Collectors.toMap(ServerAccount::getName, a -> a, (k1, k2) -> k1)));
        }
    }

    @Deprecated
    public static void setComputerContext(Map<Integer, EdsAsset> computerContext) {
        COMPUTER_CONTEXT.set(computerContext);
    }

    public static Map<Integer, EdsAsset> getComputerContext() {
        return COMPUTER_CONTEXT.get();
    }

    public static Map<String, ServerAccount> getAccountContext() {
        return ACCOUNT_CONTEXT.get();
    }

    public static void remove() {
        COMPUTER_CONTEXT.remove();
        ACCOUNT_CONTEXT.remove();
    }

}
