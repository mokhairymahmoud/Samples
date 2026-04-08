/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.samples.streaming;

import org.eclipse.edc.connector.controlplane.asset.spi.domain.Asset;
import org.eclipse.edc.connector.controlplane.transfer.spi.flow.DataFlowController;
import org.eclipse.edc.connector.controlplane.transfer.spi.types.DataFlowResponse;
import org.eclipse.edc.connector.controlplane.transfer.spi.types.TransferProcess;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.response.StatusResult;

import java.util.LinkedHashSet;
import java.util.Set;

class CompositeDataFlowController implements DataFlowController {
    private final DataFlowController delegate;
    private final DataFlowController kafkaController;

    CompositeDataFlowController(DataFlowController delegate, DataFlowController kafkaController) {
        this.delegate = delegate;
        this.kafkaController = kafkaController;
    }

    @Override
    public boolean canHandle(TransferProcess transferProcess) {
        return select(transferProcess).canHandle(transferProcess);
    }

    @Override
    public StatusResult<DataFlowResponse> prepare(TransferProcess transferProcess, Policy policy) {
        return select(transferProcess).prepare(transferProcess, policy);
    }

    @Override
    public StatusResult<DataFlowResponse> start(TransferProcess transferProcess, Policy policy) {
        return select(transferProcess).start(transferProcess, policy);
    }

    @Override
    public StatusResult<Void> suspend(TransferProcess transferProcess) {
        return select(transferProcess).suspend(transferProcess);
    }

    @Override
    public StatusResult<Void> terminate(TransferProcess transferProcess) {
        return select(transferProcess).terminate(transferProcess);
    }

    @Override
    public StatusResult<Void> started(TransferProcess transferProcess) {
        return select(transferProcess).started(transferProcess);
    }

    @Override
    public StatusResult<Void> completed(TransferProcess transferProcess) {
        return select(transferProcess).completed(transferProcess);
    }

    @Override
    public Set<String> transferTypesFor(Asset asset) {
        return merge(delegate.transferTypesFor(asset), kafkaController.transferTypesFor(asset));
    }

    @Override
    public Set<String> transferTypesFor(String value) {
        return merge(delegate.transferTypesFor(value), kafkaController.transferTypesFor(value));
    }

    private DataFlowController select(TransferProcess transferProcess) {
        return kafkaController.canHandle(transferProcess) ? kafkaController : delegate;
    }

    private Set<String> merge(Set<String> left, Set<String> right) {
        var result = new LinkedHashSet<String>();
        result.addAll(left);
        result.addAll(right);
        return result;
    }
}
