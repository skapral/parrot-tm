package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.http.endpoints.GetListOfTasks;
import com.skapral.parrot.itests.assertions.json.AssertAssumingJsonArrayResponse;
import com.skapral.parrot.itests.assertions.json.AssertJsonArraySize;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;

public class AssertTasksListSize extends AssertHttp {
    public AssertTasksListSize(Authentication<?> auth, URI tasksUri, int size) {
        super(
            new GetListOfTasks(auth, tasksUri),
            resp -> new AssertAssumingJsonArrayResponse(
                resp,
                array -> new AssertCombined(
                    new StatusCode2XX(resp),
                    new AssertJsonArraySize(array, size)
                )
            )
        );
    }
}
