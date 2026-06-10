package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.view.security.ApiScanVO;
import com.baiyi.cratos.facade.security.ApiScanFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/5 16:04
 * &#064;Version 1.0
 */
public class ApiScanFacadeTest extends BaseUnit {

    @Resource
    private ApiScanFacade apiScanFacade;

    private static final String config = """
            scanConfig:
              - name: Swagger UI Exposure
                severity: HIGH
                category: INFO_LEAK
                rules:
                  - path: /swagger-ui.html
                    methods: [GET]
                  - path: /swagger-ui
                    methods: [GET]
                  - path: /swagger-ui/
                    methods: [GET]
                  - path: /swagger-ui/index.html
                    methods: [GET]
                  - path: /doc.html
                    methods: [GET]
                  - path: /doc.html#/home
                    methods: [GET]
                  - path: /api.html
                    methods: [GET]
                  - path: /docs
                    methods: [GET]
                  - path: /docs/
                    methods: [GET]
                  - path: /swagger
                    methods: [GET]
                  - path: /swagger/
                    methods: [GET]
                  - path: /swagger/index.html
                    methods: [GET]
                  - path: /swaggerui
                    methods: [GET]
                  - path: /swaggerui/
                    methods: [GET]
                  - path: /swagger-editor
                    methods: [GET]
                  - path: /explorer
                    methods: [GET]
                  - path: /redoc
                    methods: [GET]
                  - path: /redoc.html
                    methods: [GET]
                  - path: /swagger/ui
                    methods: [GET]
                  - path: /swagger/ui/index
                    methods: [GET]
                  - path: /Swagger-ui.html
                    methods: [GET]
                  - path: /Swagger/
                    methods: [GET]
                  - path: /Swagger/index.html
                    methods: [GET]
                  - path: /Swagger/ui/index
                    methods: [GET]
                  - path: /Doc.html
                    methods: [GET]
                  - path: /swagger-bootstrap-ui/index.html
                    methods: [GET]
                  - path: /swagger-ui-bootstrap/index.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: API Docs JSON/YAML Exposure
                severity: HIGH
                category: INFO_LEAK
                rules:
                  - path: /v2/api-docs
                    methods: [GET]
                  - path: /v2/api-docs?group=default
                    methods: [GET]
                  - path: /v2/api-docs?group=all
                    methods: [GET]
                  - path: /v2/api-docs-ext
                    methods: [GET]
                  - path: /v2/api-docs.yaml
                    methods: [GET]
                  - path: /v2/api-docs.yml
                    methods: [GET]
                  - path: /v2/api-docs.json
                    methods: [GET]
                  - path: /v3/api-docs
                    methods: [GET]
                  - path: /v3/api-docs-ext
                    methods: [GET]
                  - path: /v3/api-docs.yaml
                    methods: [GET]
                  - path: /v3/api-docs.yml
                    methods: [GET]
                  - path: /v3/api-docs.json
                    methods: [GET]
                  - path: /v3/api-docs/swagger-config
                    methods: [GET]
                  - path: /v1/api-docs
                    methods: [GET]
                  - path: /v1/api-docs.yaml
                    methods: [GET]
                  - path: /v1/api-docs.yml
                    methods: [GET]
                  - path: /v1/api-docs.json
                    methods: [GET]
                  - path: /api-docs
                    methods: [GET]
                  - path: /api-docs/
                    methods: [GET]
                  - path: /api-docs/swagger-config
                    methods: [GET]
                  - path: /api-docs.yaml
                    methods: [GET]
                  - path: /api-docs.yml
                    methods: [GET]
                  - path: /api-docs.json
                    methods: [GET]
                  - path: /apidocs
                    methods: [GET]
                  - path: /apidocs/
                    methods: [GET]
                  - path: /swagger.json
                    methods: [GET]
                  - path: /swagger.yaml
                    methods: [GET]
                  - path: /swagger.yml
                    methods: [GET]
                  - path: /openapi.json
                    methods: [GET]
                  - path: /openapi.yaml
                    methods: [GET]
                  - path: /openapi.yml
                    methods: [GET]
                  - path: /spec.yaml
                    methods: [GET]
                  - path: /spec.yml
                    methods: [GET]
                  - path: /swagger/v1/swagger.json
                    methods: [GET]
                  - path: /swagger/v2/swagger.json
                    methods: [GET]
                  - path: /swagger/v3/swagger.json
                    methods: [GET]
                  - path: /docs/swagger.json
                    methods: [GET]
                  - path: /swagger/doc.json
                    methods: [GET]
                  - path: /static/swagger.json
                    methods: [GET]
                  - path: /resources/swagger.yaml
                    methods: [GET]
                  - path: /V2/api-docs
                    methods: [GET]
                  - path: /V3/api-docs
                    methods: [GET]
                  - path: /Swagger.json
                    methods: [GET]
                  - path: /Swagger.yaml
                    methods: [GET]
                  - path: /API-Docs
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: API Prefixed Swagger Exposure
                severity: HIGH
                category: INFO_LEAK
                rules:
                  - path: /api/docs
                    methods: [GET]
                  - path: /api/doc
                    methods: [GET]
                  - path: /api/redoc
                    methods: [GET]
                  - path: /api/redoc.html
                    methods: [GET]
                  - path: /api/swaggerui
                    methods: [GET]
                  - path: /api/swaggerui/
                    methods: [GET]
                  - path: /api/swagger
                    methods: [GET]
                  - path: /api/swagger/
                    methods: [GET]
                  - path: /api/swagger/index.html
                    methods: [GET]
                  - path: /api/swagger-ui.html
                    methods: [GET]
                  - path: /api/swagger-ui/
                    methods: [GET]
                  - path: /api/swagger-ui/index.html
                    methods: [GET]
                  - path: /api/swagger/ui
                    methods: [GET]
                  - path: /api/swagger/ui/index
                    methods: [GET]
                  - path: /api/doc.html
                    methods: [GET]
                  - path: /api/api-docs
                    methods: [GET]
                  - path: /api/v2/api-docs
                    methods: [GET]
                  - path: /api/v3/api-docs
                    methods: [GET]
                  - path: /api/swagger.json
                    methods: [GET]
                  - path: /api/swagger.yaml
                    methods: [GET]
                  - path: /api/swagger.yml
                    methods: [GET]
                  - path: /api/swagger-bootstrap-ui/index.html
                    methods: [GET]
                  - path: /api/swagger-ui-bootstrap/index.html
                    methods: [GET]
                  - path: /API/swagger.json
                    methods: [GET]
                  - path: /API/swagger.yaml
                    methods: [GET]
                  - path: /api/v1
                    methods: [GET]
                  - path: /api/v2
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Versioned Swagger Exposure
                severity: MEDIUM
                category: INFO_LEAK
                rules:
                  - path: /v1/swagger-ui.html
                    methods: [GET]
                  - path: /v1/swagger/
                    methods: [GET]
                  - path: /v1/swagger.json
                    methods: [GET]
                  - path: /v2/swagger-ui.html
                    methods: [GET]
                  - path: /v2/swagger/
                    methods: [GET]
                  - path: /v2/swagger.json
                    methods: [GET]
                  - path: /v3/swagger-ui.html
                    methods: [GET]
                  - path: /v3/swagger/
                    methods: [GET]
                  - path: /v3/swagger.json
                    methods: [GET]
                  - path: /open-api/v2/api-docs
                    methods: [GET]
                  - path: /open-api/swagger-ui.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Swagger Resources & Webjars Exposure
                severity: MEDIUM
                category: INFO_LEAK
                rules:
                  - path: /swagger-resources
                    methods: [GET]
                  - path: /swagger-resources/
                    methods: [GET]
                  - path: /swagger-resources/swagger-resource
                    methods: [GET]
                  - path: /swagger-resources/configuration/ui
                    methods: [GET]
                  - path: /swagger-resources/configuration/security
                    methods: [GET]
                  - path: /webjars/swagger-ui/
                    methods: [GET]
                  - path: /webjars/swagger-ui/index.html
                    methods: [GET]
                  - path: /webjars/springfox-swagger-ui/
                    methods: [GET]
                  - path: /webjars/springfox-swagger-ui/springfox.css
                    methods: [GET]
                  - path: /webjars/springfox-swagger-ui/swagger-ui.js
                    methods: [GET]
                  - path: /META-INF/resources/swagger-ui.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Path Traversal Bypass Attempts
                severity: CRITICAL
                category: PATH_TRAVERSAL
                rules:
                  - path: //swagger-ui.html
                    methods: [GET]
                  - path: /%2e%2e/swagger-ui.html
                    methods: [GET]
                  - path: /..;/swagger-ui.html
                    methods: [GET]
                  - path: /api-docs/..;/swagger-ui.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Other API Documentation Exposure
                severity: MEDIUM
                category: INFO_LEAK
                rules:
                  - path: /rest-api-docs
                    methods: [GET]
                  - path: /api-specification
                    methods: [GET]
                  - path: /api-documentation
                    methods: [GET]
                  - path: /api-reference
                    methods: [GET]
                  - path: /public-api
                    methods: [GET]
                  - path: /api_doc/
                    methods: [GET]
                  - path: /kong-docs
                    methods: [GET]
                  - path: /kong/spec
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Framework Specific Exposure
                severity: MEDIUM
                category: INFO_LEAK
                rules:
                  - path: /spring-security-rest/api/swagger-ui.html
                    methods: [GET]
                  - path: /spring-security-oauth-resource/swagger-ui.html
                    methods: [GET]
                  - path: /actuator/swagger
                    methods: [GET]
                  - path: /actuator/swagger-ui
                    methods: [GET]
                  - path: /user/swagger-ui.html
                    methods: [GET]
                  - path: /libs/swaggerui
                    methods: [GET]
                  - path: /sw/swagger-ui.html
                    methods: [GET]
                  - path: /template/swagger-ui.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: Druid Monitor Exposure
                severity: CRITICAL
                category: UNAUTH_ACCESS
                rules:
                  - path: /druid/index.html
                    methods: [GET]
                expect:
                  statusNot: [200]
            
              - name: GraphQL & AI Plugin Exposure
                severity: HIGH
                category: INFO_LEAK
                rules:
                  - path: /graphql
                    methods: [GET, POST]
                  - path: /.well-known/ai-plugin.json
                    methods: [GET]
                expect:
                  statusNot: [200]
            
            """;

    @Autowired
    private Environment environment;

    @Test
    void test1() {
        ApiScanVO.ScanConfig scanConfig = ApiScanVO.ScanConfig.loadAs(config);
        String port = environment.getProperty("local.server.port");
        System.out.println(port);
        // apiScanFacade.scanTarget("cratos", "cratos-1", "127.0.0.1", "", port , scanConfig ,"test");
    }

}
