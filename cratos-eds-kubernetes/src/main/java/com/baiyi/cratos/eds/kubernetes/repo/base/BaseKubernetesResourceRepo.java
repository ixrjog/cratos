package com.baiyi.cratos.eds.kubernetes.repo.base;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 14:12
 * &#064;Version 1.0
 */
@Slf4j
public abstract class BaseKubernetesResourceRepo<C extends io.fabric8.kubernetes.client.Client, T extends HasMetadata> implements IKubernetesResourceRepo<C, T> {

    abstract protected C buildClient(EdsConfigs.Kubernetes kubernetes);

    abstract protected T create(C client, T resource);

    abstract protected T get(C client, String namespace, String name);

    abstract protected T find(C client, T resource);

    abstract protected void delete(C client, T resource);

    abstract protected List<T> list(C client, String namespace);

    @Override
    public T create(EdsConfigs.Kubernetes kubernetes, String content) {
        try (final C client = buildClient(kubernetes)) {
            T resource = loadAs(client, content);
            return create(client, resource);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public T get(EdsConfigs.Kubernetes kubernetes, String namespace, String name) {
        try (final C client = buildClient(kubernetes)) {
            return get(client, namespace, name);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public T find(EdsConfigs.Kubernetes kubernetes, String content) {
        try (final C client = buildClient(kubernetes)) {
            T resource = loadAs(client, content);
            return find(client, resource);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(EdsConfigs.Kubernetes kubernetes, T resource) {
        try (final C client = buildClient(kubernetes)) {
            delete(client, resource);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<T> list(EdsConfigs.Kubernetes kubernetes, String namespace) {
        try (final C client = buildClient(kubernetes)) {
            return list(client, namespace);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
