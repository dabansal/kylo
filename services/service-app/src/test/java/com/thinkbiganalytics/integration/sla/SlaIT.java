package com.thinkbiganalytics.integration.sla;

/*-
 * #%L
 * kylo-service-app
 * %%
 * Copyright (C) 2017 ThinkBig Analytics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.thinkbiganalytics.feedmgr.rest.model.FeedMetadata;
import com.thinkbiganalytics.feedmgr.sla.ServiceLevelAgreementGroup;
import com.thinkbiganalytics.integration.IntegrationTestBase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Creates a feed, creates two SLAs for the feed, which are expected to succeed and to fail,
 * triggers SLA assessments, asserts SLA assessment results
 */
public class SlaIT extends IntegrationTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(SlaIT.class);

    private static final String TEST_FILE = "sla-assessment.txt";

    @Test
    public void testSla() throws IOException {
        copyDataToDropzone(TEST_FILE);

        LocalDateTime now = LocalDateTime.now();
        String systemName = now.format(DateTimeFormatter.ofPattern("HH_mm_ss_SSS"));
        FeedMetadata response = createSimpleFeed("sla_" + systemName, TEST_FILE);

        waitForFeedToComplete();

        ServiceLevelAgreementGroup sla = createOneHourAgoFeedProcessingDeadlineSla(response.getCategoryAndFeedName(), response.getFeedId());
        triggerSla(sla.getName());
        assertSLAs(response);
    }

    @Override
    public void startClean() {
        super.startClean();
    }

//    @Test
    public void temp() {
        triggerSla("Before 12 with cron expression 0 0 12 1/1 * ? *");
    }


    private void assertSLAs(FeedMetadata response) {

    }


}