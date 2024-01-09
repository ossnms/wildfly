/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.wildfly.extension.microprofile.health.deployment;

import static org.jboss.as.weld.Capabilities.WELD_CAPABILITY_NAME;

import jakarta.enterprise.inject.spi.BeanManager;
import org.jboss.as.controller.capability.CapabilityServiceSupport;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.weld.WeldCapability;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.wildfly.extension.microprofile.health.MicroProfileHealthReporter;
import org.wildfly.extension.microprofile.health.MicroProfileHealthSubsystemDefinition;
import org.wildfly.extension.microprofile.health._private.MicroProfileHealthLogger;

import java.util.function.Supplier;

/**
 */
public class DeploymentProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        Module module = deploymentUnit.getAttachment(Attachments.MODULE);

        final CapabilityServiceSupport support = deploymentUnit.getAttachment(Attachments.CAPABILITY_SERVICE_SUPPORT);

        final WeldCapability weldCapability;
        try {
            weldCapability = support.getCapabilityRuntimeAPI(WELD_CAPABILITY_NAME, WeldCapability.class);
        } catch (CapabilityServiceSupport.NoSuchCapabilityException e) {
            //We should not be here since the subsystem depends on weld capability. Just in case ...
            throw MicroProfileHealthLogger.LOGGER.deploymentRequiresCapability(
                    deploymentUnit.getName(),
                    WELD_CAPABILITY_NAME);
        }
        if (weldCapability.isPartOfWeldDeployment(deploymentUnit) && deploymentUnit.getParent() == null) {
            final MicroProfileHealthReporter healthReporter = (MicroProfileHealthReporter) phaseContext.getServiceRegistry().getService(MicroProfileHealthSubsystemDefinition.HEALTH_REPORTER_SERVICE).getValue();

            ServiceBuilder<?> serviceBuilder = phaseContext.getServiceTarget().addService(phaseContext.getPhaseServiceName().append("beanMangerSupplier"));
            Supplier<BeanManager> beanMangerSupplier = weldCapability.addBeanManagerService(deploymentUnit, serviceBuilder);
            serviceBuilder.install();

            weldCapability.registerExtensionInstance(new CDIExtension(healthReporter, module, beanMangerSupplier), deploymentUnit);
        }

    }
}