/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.deployment;

import org.jboss.as.deployment.chain.DeploymentChain;
import org.jboss.as.deployment.module.DeploymentModuleLoader;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Service that represents a deployment.  Should be used as a dependency for all services registered for the deployment.
 * The life-cycle of this service should be used to control the life-cycle of the deployment. 
 *
 * @author John E. Bailey
 */
public class DeploymentService implements Service<Void> {
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("deployment");
    private static Logger logger = Logger.getLogger("org.jboss.as.deployment");

    private final String deploymentName;
    private InjectedValue<DeploymentChain> deploymentChain = new InjectedValue<DeploymentChain>();
    private InjectedValue<DeploymentModuleLoader> moduleLoader = new InjectedValue<DeploymentModuleLoader>();

    public DeploymentService(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    @Override
    public void start(StartContext context) throws StartException {
    }

    @Override
    public void stop(StopContext context) {
    }

    @Override
    public Void getValue() throws IllegalStateException {
        return null;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public DeploymentChain getDeploymentChain() {
        return deploymentChain.getValue();
    }

    public Injector<DeploymentChain> getDeploymentChainInjector() {
        return deploymentChain;
    }

    public DeploymentModuleLoader getModuleLoader() {
        return moduleLoader.getValue();
    }

    public Injector<DeploymentModuleLoader> getModuleLoaderInjector() {
        return moduleLoader;
    }
}
