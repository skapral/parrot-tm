package com.skapral.parrot.itests.assertions.business;

import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.http.endpoints.CreateNewTask;
import com.skapral.parrot.itests.utils.authentication.Authentication;

import java.net.URI;
import java.net.http.HttpRequest;

public class TaskCreation extends AssertHttp {
    public TaskCreation(URI tasksUri, Authentication<?> auth, String description) {
        super(
            new CreateNewTask(auth, tasksUri, description),
            resp -> new StatusCode2XX(resp)
        );
    }
}
