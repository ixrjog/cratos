package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.workorder.enums.DeploymentJvmSpecTypes;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/5 15:08
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JvmSpecUtils {

    private static final String[] JVM_ARGS = {"-Xms", "-Xmx", "-Xmn", "-XX:MetaspaceSize", "-XX:MaxMetaspaceSize", "-XX:MaxDirectMemorySize", "-XX:ReservedCodeCacheSize"};

    static final String SMALL_ARGS = "-Xms1G -Xmx1G -Xmn500M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:MaxDirectMemorySize=256M -XX:ReservedCodeCacheSize=128M";
    static final String LARGE_ARGS = "-Xms2560M -Xmx2560M -Xmn1200M -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M -XX:MaxDirectMemorySize=256M -XX:ReservedCodeCacheSize=128M";
    static final String XLARGE_ARGS = "-Xms4G -Xmx4G -Xmn2G -XX:MetaspaceSize=384M -XX:MaxMetaspaceSize=384M -XX:MaxDirectMemorySize=1G -XX:ReservedCodeCacheSize=256M";
    static final String XLARGE2_ARGS = "-Xms10G -Xmx10G -Xmn5G -XX:MetaspaceSize=512M -XX:MaxMetaspaceSize=512M -XX:MaxDirectMemorySize=1G -XX:ReservedCodeCacheSize=256M";

    private static final Map<DeploymentJvmSpecTypes, String> SPEC_JVM_ARGS_MAP = initSpecJvmArgsMap();
    private static final Map<DeploymentJvmSpecTypes, ApplicationDeploymentModel.ResourceRequirements> SPEC_RESOURCE_REQUIREMENTS_MAP = initSpecResourceRequirementsMap();

    public static ApplicationDeploymentModel.ResourceRequirements getResourceRequirements(DeploymentJvmSpecTypes jvmSpecType) {
        return SPEC_RESOURCE_REQUIREMENTS_MAP.get(jvmSpecType);
    }

    private static Map<DeploymentJvmSpecTypes, String> initSpecJvmArgsMap() {
        return Map.ofEntries(
                entry(DeploymentJvmSpecTypes.SMALL, SMALL_ARGS), entry(DeploymentJvmSpecTypes.LARGE, LARGE_ARGS),
                entry(DeploymentJvmSpecTypes.XLARGE, XLARGE_ARGS), entry(DeploymentJvmSpecTypes.XLARGE2, XLARGE2_ARGS)
        );
    }

    private static Map<DeploymentJvmSpecTypes, ApplicationDeploymentModel.ResourceRequirements> initSpecResourceRequirementsMap() {
        return Map.ofEntries(
                entry(DeploymentJvmSpecTypes.SMALL, buildResourceRequirements(DeploymentJvmSpecTypes.SMALL)),
                entry(DeploymentJvmSpecTypes.LARGE, buildResourceRequirements(DeploymentJvmSpecTypes.LARGE)),
                entry(DeploymentJvmSpecTypes.XLARGE, buildResourceRequirements(DeploymentJvmSpecTypes.XLARGE)),
                entry(DeploymentJvmSpecTypes.XLARGE2, buildResourceRequirements(DeploymentJvmSpecTypes.XLARGE2))
        );
    }

    private static ApplicationDeploymentModel.ResourceRequirements buildResourceRequirements(
            DeploymentJvmSpecTypes jvmSpecType) {
        switch (jvmSpecType) {
            case DeploymentJvmSpecTypes.SMALL -> {
                return buildResourceRequirements("500m", "1Gi", "1", "2Gi");
            }
            case DeploymentJvmSpecTypes.LARGE -> {
                return buildResourceRequirements("500m", "3Gi", "2", "4Gi");
            }
            case DeploymentJvmSpecTypes.XLARGE -> {
                return buildResourceRequirements("2", "4Gi", "4", "8Gi");
            }
            case DeploymentJvmSpecTypes.XLARGE2 -> {
                return buildResourceRequirements("4", "10Gi", "8", "16Gi");
            }
            default -> throw new WorkOrderTicketException("Invalid jvm specification: {}", jvmSpecType.name());
        }
    }

    public static ApplicationDeploymentModel.ResourceRequirements buildResourceRequirements(String requestCpu,
                                                                                            String requestMemory,
                                                                                            String limitCpu,
                                                                                            String limitMemory) {
        return ApplicationDeploymentModel.ResourceRequirements.builder()
                .limits(Map.ofEntries(entry("cpu", limitCpu), entry("memory", limitMemory)))
                .requests(Map.ofEntries(entry("cpu", requestCpu), entry("memory", requestMemory)))
                .build();
    }

    public static String getSpecJvmArgs(DeploymentJvmSpecTypes deploymentJvmSpecTypes) {
        return SPEC_JVM_ARGS_MAP.get(deploymentJvmSpecTypes);
    }

    public static List<String> parse(DeploymentJvmSpecTypes spec, String javaOpts) {
        // 当前JAVA_OPTS
        List<String> originalArgs = Splitter.onPattern(" |\\n")
                .omitEmptyStrings()
                .splitToList(javaOpts);
        // 新Args结果集
        List<String> result = Lists.newArrayList(SPEC_JVM_ARGS_MAP.get(spec));
        // 查找没有的参数附加到新Args结果集
        originalArgs.stream()
                .filter(originalArg -> Arrays.stream(JVM_ARGS)
                        .noneMatch(originalArg::startsWith))
                .forEach(result::add);
        return result;
    }

    public static String toCommandLine(List<String> jvmArgs) {
        return Joiner.on(" ")
                .skipNulls()
                .join(jvmArgs);
    }

}
