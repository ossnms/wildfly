/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import org.jboss.ws.api.annotation.WebContext;

import org.jboss.as.ee.metadata.ClassAnnotationInformationFactory;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

/**
 * User: rsearls
 * Date: 7/17/14
 */
public class WebContextAnnotationInformationFactory extends
    ClassAnnotationInformationFactory<WebContext, WebContextAnnotationInfo> {

    protected WebContextAnnotationInformationFactory() {
        super(org.jboss.ws.api.annotation.WebContext.class, null);
    }

    @Override
    protected WebContextAnnotationInfo fromAnnotation(final AnnotationInstance annotationInstance, final boolean replacement) {
        String authMethodValue = asString(annotationInstance, "authMethod");
        String contextRootValue = asString(annotationInstance, "contextRoot");
        boolean secureWSDLAccessValue = asBoolean(annotationInstance, "secureWSDLAccessValue");
        String transportGuaranteeValue = asString(annotationInstance, "transportGuarantee");
        String urlPatternValue = asString(annotationInstance, "urlPattern");
        String virtualHostValue = asString(annotationInstance, "virtualHost");
        return new  WebContextAnnotationInfo(authMethodValue, contextRootValue,
            secureWSDLAccessValue, transportGuaranteeValue, urlPatternValue, virtualHostValue);
    }

    private String asString(final AnnotationInstance annotation, String property) {
        AnnotationValue value = annotation.value(property);
        return value == null ? "" : value.asString();
    }

    private boolean asBoolean(final AnnotationInstance annotation, String property) {
        AnnotationValue value = annotation.value(property);
        return value == null ? false : Boolean.getBoolean(value.asString());
    }
}
