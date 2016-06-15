/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.webservices.deployers;

/**
 * User: rsearls
 * Date: 7/17/14
 */
public class WebContextAnnotationInfo {
    private final String authMethod;
    private final String contextRoot;
    private final boolean secureWSDLAccess;
    private final String transportGuarantee;
    private final String urlPattern;
    private final String virtualHost;

    public WebContextAnnotationInfo(final String authMethod, final String contextRoot, final boolean secureWSDLAccess, final String transportGuarantee, final String urlPattern, final String virtualHost) {

        this.authMethod = authMethod;
        this.contextRoot = contextRoot;
        this.secureWSDLAccess = secureWSDLAccess;
        this.transportGuarantee = transportGuarantee;
        this.urlPattern = urlPattern;
        this.virtualHost = virtualHost;

    }

    public String getAuthMethod() {
        return authMethod;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public boolean isSecureWSDLAccess() {
        return secureWSDLAccess;
    }

    public String getTransportGuarantee() {
        return transportGuarantee;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

}
