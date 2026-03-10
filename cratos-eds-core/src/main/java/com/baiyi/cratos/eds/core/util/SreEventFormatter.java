package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.RegexSensitiveDataMasker;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Getter;

import java.util.Map;

import static com.baiyi.cratos.eds.core.util.SreEventFormatter.Action.EXECUTE_COMMAND;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 17:17
 * &#064;Version 1.0
 */

public class SreEventFormatter {

    public static final String EVENT_ID = "eventId";

    @Getter
    public enum Action {
        LOGIN_SERVER("loginServer"),
        LOGOUT_SERVER("logoutServer"),
        LOGIN_CONTAINER("loginContainer"),
        EXECUTE_COMMAND("executeCommand");

        private final String value;

        Action(String value) {
            this.value = value;
        }
    }

    /**
     * Login & Logout Server
     *
     * @param user
     * @param action
     * @param target
     * @return
     */
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event format(User user, Action action, String target) {
        Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(action.getValue())
                .description(
                        StringFormatter.arrayFormat(
                                "SSH {} to server {} via Cratos SSH-Server",
                                action.equals(Action.LOGIN_SERVER) ? "login" : "logout", target
                        ))
                .target(target)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    /**
     * Login Container
     *
     * @param user
     * @param action
     * @param cluster
     * @param namespace
     * @param deployment
     * @param pod
     * @return
     */
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event format(User user, Action action, String cluster,
                                                                            String namespace, String deployment,
                                                                            String pod) {
        Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(action.value)
                .description(StringFormatter.arrayFormat(
                        "Login to kubernetes pod {} (cluster:{} namespace:{} deployment:{}) via Cratos Kubernetes", pod,
                        cluster, namespace, deployment
                ))
                .target(StringFormatter.arrayFormat(
                        "{} (cluster:{} namespace:{} deployment:{})", pod, cluster,
                        namespace, deployment
                ))
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    /**
     * Execute Command
     *
     * @param user
     * @param cluster
     * @param namespace
     * @param command
     * @return
     */
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event format(User user, String cluster, String namespace,
                                                                            String command) {
        String desensitizedCommand = RegexSensitiveDataMasker.maskSensitiveData(command);
        Map<String, String> ext = Map.of(
                EVENT_ID, PasswordGenerator.generateNo(), "masked", "true", "phase", "started", "command",
                desensitizedCommand
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(EXECUTE_COMMAND.value)
                .description(StringFormatter.arrayFormat(
                        "Execute command in Kubernetes container (cluster:{} namespace:{}) via Cratos CommandExec",
                        cluster, namespace
                ))
                .target(StringFormatter.arrayFormat("cluster:{} namespace:{}", cluster, namespace))
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event format(User user, String cluster, String namespace,
                                                                            String command, Boolean success,
                                                                            String outMsg, String errorMsg) {
        String desensitizedCommand = RegexSensitiveDataMasker.maskSensitiveData(command);
        String desensitizedOutMsg = RegexSensitiveDataMasker.maskSensitiveData(outMsg);
        String desensitizedErrorMsg = RegexSensitiveDataMasker.maskSensitiveData(errorMsg);
        Map<String, String> ext = Map.of(
                EVENT_ID, PasswordGenerator.generateNo(), "masked", "true", "phase", "success", success.toString(),
                "completed", "command", desensitizedCommand, "outMsg", desensitizedOutMsg, "errorMsg",
                desensitizedErrorMsg
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(EXECUTE_COMMAND.value)
                .description(StringFormatter.arrayFormat(
                        "Execute command in Kubernetes container (cluster:{} namespace:{}) via Cratos CommandExec",
                        cluster, namespace
                ))
                .target(StringFormatter.arrayFormat("cluster:{} namespace:{}", cluster, namespace))
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

}
