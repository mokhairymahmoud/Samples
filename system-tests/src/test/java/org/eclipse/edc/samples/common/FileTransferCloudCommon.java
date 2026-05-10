/*
 *  Copyright (c) 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Materna Information & Communications SE - initial test implementation for sample
 *
 */

package org.eclipse.edc.samples.common;

import static org.eclipse.edc.samples.common.NegotiationCommon.fetchDatasetFromCatalog;
import static org.eclipse.edc.samples.common.NegotiationCommon.getContractAgreementId;
import static org.eclipse.edc.samples.common.NegotiationCommon.negotiateContract;

public class FileTransferCloudCommon {

    private static final String FETCH_DATASET_FROM_CATALOG_FILE_PATH = "transfer/transfer-05-file-transfer-cloud/resources/get-dataset.json";
    private static final String NEGOTIATE_CONTRACT_FILE_PATH = "transfer/transfer-05-file-transfer-cloud/resources/negotiate-contract.json";

    public static String runNegotiation() {
        var catalogDatasetId = fetchDatasetFromCatalog(FETCH_DATASET_FROM_CATALOG_FILE_PATH);
        var contractNegotiationId = negotiateContract(NEGOTIATE_CONTRACT_FILE_PATH, catalogDatasetId);
        return getContractAgreementId(contractNegotiationId);
    }

}
