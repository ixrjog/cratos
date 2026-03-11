package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.RegexSensitiveDataMasker;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Getter;

import java.util.Map;

import static com.baiyi.cratos.eds.core.util.SreEventFormatter.Action.EXECUTE_COMMAND;
import static java.util.Map.entry;

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
        EXECUTE_COMMAND("executeCommand"),
        CHANGE_INGRESS("changeIngress"),
        REDEPLOY_DEPLOYMENT("redeployDeployment"),
        DELETE_POD("deletePod"),
        ;

        private final String value;

        Action(String value) {
            this.value = value;
        }
    }

    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event deletePod(User user, String ticketNo,
                                                                               String ticketId, String cluster,
                                                                               String namespace, String deploymentName,
                                                                               String podName) {
        Map<String, String> ext = Map.ofEntries(entry("ticketNo", ticketNo), entry("ticketId", ticketId));
        Map<String, String> targetContent = Map.ofEntries(
                entry("cluster", cluster), entry("namespace", namespace),
                entry("deployment", deploymentName), entry("pod", podName)
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(Action.DELETE_POD.value)
                .description(StringFormatter.format(
                        "Delete Kubernetes pod {} via Cratos Kubernetes Resources",
                        deploymentName
                ))
                .target(podName)
                .targetContent(targetContent)
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    /**
     *
     * @param user
     * @param ticketNo
     * @param ticketId
     * @param cluster
     * @param namespace
     * @param deploymentName
     * @return
     */
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event redeployDeployment(User user, String ticketNo,
                                                                                        String ticketId, String cluster,
                                                                                        String namespace,
                                                                                        String deploymentName) {
        Map<String, String> ext = Map.ofEntries(entry("ticketNo", ticketNo), entry("ticketId", ticketId));
        Map<String, String> targetContent = Map.ofEntries(
                entry("cluster", cluster), entry("namespace", namespace),
                entry("deployment", deploymentName)
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(Action.REDEPLOY_DEPLOYMENT.value)
                .description(StringFormatter.format(
                        "Redeploy Kubernetes deployment {} via Cratos Kubernetes Resources",
                        deploymentName
                ))
                .target(deploymentName)
                .targetContent(targetContent)
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event changeIngress(User user, String ingressName,
                                                                                   String namespace,
                                                                                   String loadBalancer, String rules,
                                                                                   String sourceTrafficLimitQps,
                                                                                   String targetTrafficLimitQps,
                                                                                   String changeMessage) {
        Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
        Map<String, String> sourceContent = Map.ofEntries(
                entry("ingressName", ingressName),
                entry("loadBalancer", loadBalancer), entry("rules", rules),
                entry("trafficLimitQps", sourceTrafficLimitQps)
        );
        Map<String, String> targetContent = Map.ofEntries(
                entry("ingressName", ingressName),
                entry("loadBalancer", loadBalancer), entry("rules", rules),
                entry("trafficLimitQps", targetTrafficLimitQps)
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(Action.CHANGE_INGRESS.value)
                .description(
                        StringFormatter.format("Change Kubernetes Ingress rate limit configuration: {}", changeMessage))
                .target(ingressName)
                .sourceContent(sourceContent)
                .targetContent(targetContent)
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    /**
     * Login & Logout Server
     *
     * @param user
     * @param action
     * @param serverName
     * @param loginAccount
     * @param serverIp
     * @return
     */
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event loginServer(User user, Action action,
                                                                                 String serverName, String loginAccount,
                                                                                 String serverIp) {
        Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
        Map<String, String> targetContent = Map.ofEntries(
                entry("serverName", serverName),
                entry("loginAccount", loginAccount),
                entry("serverIp", serverIp)
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(action.getValue())
                .description(
                        StringFormatter.arrayFormat(
                                "SSH {} to server {}({}@{}) via Cratos SSH-Server",
                                action.equals(Action.LOGIN_SERVER) ? "login" : "logout", serverName, loginAccount,
                                serverIp
                        ))
                .target(serverName)
                .targetContent(targetContent)
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
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event loginContainer(User user, Action action,
                                                                                    String cluster, String namespace,
                                                                                    String deployment, String pod) {
        Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
        Map<String, String> targetContent = Map.ofEntries(
                entry("cluster", cluster), entry("namespace", namespace),
                entry("deployment", deployment), entry("pod", pod)
        );
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(action.value)
                .description(StringFormatter.arrayFormat(
                        "Login to kubernetes pod {} (cluster:{} namespace:{} deployment:{}) via Cratos Kubernetes", pod,
                        cluster, namespace, deployment
                ))
                .target(pod)
                .targetContent(targetContent)
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
    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event executeCommand(User user, String cluster,
                                                                                    String namespace, String command) {
        String desensitizedCommand = RegexSensitiveDataMasker.maskSensitiveData(command);
        Map<String, String> ext = Map.ofEntries(
                entry(EVENT_ID, PasswordGenerator.generateNo()), entry("masked", "true"), entry("phase", "started"),
                entry("command", desensitizedCommand)
        );
        Map<String, String> targetContent = Map.ofEntries(entry("cluster", cluster), entry("namespace", namespace));
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(EXECUTE_COMMAND.value)
                .description(StringFormatter.arrayFormat(
                        "Execute command in Kubernetes container (cluster:{} namespace:{}) via Cratos CommandExec",
                        cluster, namespace
                ))
                .target(cluster)
                .targetContent(targetContent)
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

    public static com.baiyi.cratos.domain.model.SreBridgeModel.Event executeCommand(User user, String cluster,
                                                                                    String namespace, String command,
                                                                                    Boolean success, String outMsg,
                                                                                    String errorMsg) {
        String desensitizedCommand = RegexSensitiveDataMasker.maskSensitiveData(command);
        String desensitizedOutMsg = RegexSensitiveDataMasker.maskSensitiveData(outMsg);
        String desensitizedErrorMsg = RegexSensitiveDataMasker.maskSensitiveData(errorMsg);
        Map<String, String> ext = Map.ofEntries(
                entry(EVENT_ID, PasswordGenerator.generateNo()), entry("masked", "true"), entry("phase", "completed"),
                entry("success", success.toString()), entry("command", desensitizedCommand),
                entry("outMsg", desensitizedOutMsg), entry("errorMsg", desensitizedErrorMsg)
        );
        Map<String, String> targetContent = Map.ofEntries(entry("cluster", cluster), entry("namespace", namespace));
        return com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                .operator(user.getEmail())
                .action(EXECUTE_COMMAND.value)
                .description(StringFormatter.arrayFormat(
                        "Execute command in Kubernetes container (cluster:{} namespace:{}) via Cratos CommandExec",
                        cluster, namespace
                ))
                .target(cluster)
                .targetContent(targetContent)
                .env(namespace)
                .affection("")
                .severity("low")
                .status("executed")
                .ext(ext)
                .build();
    }

}
