/**
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.extension.recorder.video.impl;

import org.arquillian.extension.recorder.When;
import org.arquillian.extension.recorder.video.Video;
import org.arquillian.recorder.reporter.PropertyEntry;
import org.arquillian.recorder.reporter.event.InTestResourceReport;
import org.arquillian.recorder.reporter.event.PropertyReportEvent;
import org.arquillian.recorder.reporter.impl.TakenResourceRegister;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class InTestVideoResourceReportObserver {

    @Inject
    private Instance<TakenResourceRegister> takenResourceRegister;

    @Inject
    private Event<PropertyReportEvent> reportEvent;

    public void onInTestResourceReport(@Observes InTestResourceReport event) {

        TakenResourceRegister register = takenResourceRegister.get();

        if (register == null) {
            return; // no video implementation on the class path
        }

        for (Video video : register.getTakenVideos()) {
            if (!register.getReportedVideos().contains(video)) {

                PropertyEntry propertyEntry = new VideoReportEntryBuilder()
                    .withWhen(When.IN_TEST)
                    .withMetadata(video.getResourceMetaData())
                    .withVideo(video)
                    .build();

                reportEvent.fire(new PropertyReportEvent(propertyEntry));
            }
        }

        register.invalidateVideos();
    }
}
