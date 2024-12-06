package com.baiyi.cratos.facade.application.builder.attribute;

import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/6 13:57
 * &#064;Version 1.0
 */
public class KubernetesDeploymentAttributeBuilder {

    private Deployment deployment;
    private Map<String, String> attributes;

    private static final String JAVA_OPTS = "JAVA_OPTS";

    private static final String ENV_JAVA_OPTS = "env.java.opts";
    private static final String ENV_JAVA_OPTS_TAG = "env.java.opts.tag";
    private static final String[] JVM_ARGS_PREFIX = {"-Xms", "-Xmx", "-Xmn"};
    public static final String SIDECAR_ISTIO_IO_INJECT = "sidecar.istio.io/inject";

    private KubernetesDeploymentAttributeBuilder(){
        this.attributes = Maps.newHashMap();
    }

    public static KubernetesDeploymentAttributeBuilder newBuilder() {
        return new KubernetesDeploymentAttributeBuilder();
    }

    public KubernetesDeploymentAttributeBuilder withDeployment(Deployment deployment) {
        this.deployment = deployment;
        return this;
    }

    private void makeEnv() {
        Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(this.deployment);
        optionalContainer.flatMap(container -> container.getEnv()
                        .stream()
                        .filter(e -> JAVA_OPTS.equals(e.getName()))
                        .findFirst())
                .ifPresent(javaOptsEnvVar -> {
                    List<String> javaOptsList = Splitter.onPattern(" |\\n")
                            .omitEmptyStrings()
                            .splitToList(javaOptsEnvVar.getValue());
                    // JAVA_OPTS
                    this.attributes.put(ENV_JAVA_OPTS, Joiner.on("\n")
                            .skipNulls()
                            .join(javaOptsList));
                    List<String> tags = javaOptsList.stream()
                            .filter(e -> Arrays.stream(JVM_ARGS_PREFIX)
                                    .anyMatch(e::startsWith))
                            .toList();
                    // JAVA_OPTS_TAG
                    this.attributes.put(ENV_JAVA_OPTS_TAG, Joiner.on(" ")
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
            this.attributes.put(SIDECAR_ISTIO_IO_INJECT, String.valueOf("true".equals(labels.get(SIDECAR_ISTIO_IO_INJECT))));
        }
    }

    public Map<String, String> build() {
        makeEnv();
        // Istio Envoy
        makeIstio();
        return attributes;
    }

}
