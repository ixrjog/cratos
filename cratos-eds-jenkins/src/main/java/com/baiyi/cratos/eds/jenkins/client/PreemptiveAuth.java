/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.cratos.eds.jenkins.client;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class PreemptiveAuth implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);
        if (authState.getAuthScheme() == null) {
            AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
            CredentialsProvider credentialsProvider = (CredentialsProvider) context
                    .getAttribute(HttpClientContext.CREDS_PROVIDER);
            HttpHost targetHost = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
            if (authScheme != null) {
                Credentials credentials = credentialsProvider
                        .getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                if (credentials == null) {
                    throw new HttpException("No credentials for preemptive authentication");
                }
                authState.update(authScheme, credentials);
            }
        }
    }

}