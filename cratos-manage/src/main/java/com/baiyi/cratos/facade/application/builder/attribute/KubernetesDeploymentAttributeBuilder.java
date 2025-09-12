package com.baiyi.cratos.facade.application.builder.attribute;

import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;

import java.util.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/6 13:57
 * &#064;Version 1.0
 */
public class KubernetesDeploymentAttributeBuilder {

    private Deployment deployment;
    private final Map<String, String> attributes;
    private Container appContainer;

    private static final String JAVA_OPTS = "JAVA_OPTS";
    private static final String ENV_JAVA_OPTS = "env.java.opts";
    private static final String ENV_JAVA_OPTS_TAG = "env.java.opts.tag";
    private static final String[] JVM_ARGS_PREFIX = {"-Xms", "-Xmx", "-Xmn"};
    private static final String SIDECAR_ISTIO_IO_INJECT = "sidecar.istio.io/inject";
    private static final String TERMINATION_GRACE_PERIOD_SECONDS = "terminationGracePeriodSeconds";

    private KubernetesDeploymentAttributeBuilder() {
        this.attributes = Maps.newHashMap();
    }

    public static KubernetesDeploymentAttributeBuilder newBuilder() {
        return new KubernetesDeploymentAttributeBuilder();
    }

    public KubernetesDeploymentAttributeBuilder withDeployment(Deployment deployment) {
        this.deployment = deployment;
        KubeUtils.findAppContainerOf(this.deployment)
                .ifPresent(container -> this.appContainer = container);
        return this;
    }

    private void makeEnv() {
        if (this.appContainer == null) {
            return;
        }
        this.appContainer.getEnv()
                .stream()
                .filter(e -> JAVA_OPTS.equals(e.getName()))
                .findFirst()
                .ifPresent(javaOptsEnvVar -> {
                    String javaOpts = javaOptsEnvVar.getValue();
                    List<String> javaOptsList = javaOpts != null ?
                            Splitter.onPattern(" |\\n")
                                    .omitEmptyStrings()
                                    .splitToList(javaOpts) :
                            Collections.emptyList();
                    // JAVA_OPTS
                    put(ENV_JAVA_OPTS, Joiner.on("\n")
                            .skipNulls()
                            .join(javaOptsList));
                    List<String> tags = javaOptsList.stream()
                            .filter(e -> Arrays.stream(JVM_ARGS_PREFIX)
                                    .anyMatch(e::startsWith))
                            .toList();
                    // JAVA_OPTS_TAG
                    put(ENV_JAVA_OPTS_TAG, Joiner.on(" ")
                            .skipNulls()
                            .join(tags));
                });
    }

    private void makeIstio() {
        Map<String, String> labels = this.deployment.getSpec()
                .getTemplate()
                .getMetadata()
                .getLabels();
        if (labels.containsKey(SIDECAR_ISTIO_IO_INJECT)) {
            put(SIDECAR_ISTIO_IO_INJECT, String.valueOf("true".equals(labels.get(SIDECAR_ISTIO_IO_INJECT))));
        }
    }

    private void makeTerminationGracePeriodSeconds() {
        long s = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getTerminationGracePeriodSeconds)
                .orElse(0L);
        put(TERMINATION_GRACE_PERIOD_SECONDS, s);
    }

    private void put(String name, String value) {
        this.attributes.put(name, value);
    }

    private void put(String name, Long value) {
        this.attributes.put(name, String.valueOf(value));
    }

    public Map<String, String> build() {
        makeEnv();
        makeIstio();
        return attributes;
    }

}
