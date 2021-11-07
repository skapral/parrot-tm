package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.json.AssertJsonArraySize;
import com.skapral.parrot.itests.utils.authentication.Authentication;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpRequest;

public class AssertTasksListSize extends AssertHttp {
    public AssertTasksListSize(Authentication<?> auth, URI tasksUri, int size) {
        super(
            cli -> auth.authenticate(HttpRequest.newBuilder())
                .uri(tasksUri)
                .GET()
                .build(),
            resp -> {
                var array = new JSONArray(resp.body());
                return new AssertCombined(
                    new StatusCode2XX(resp),
                    new AssertJsonArraySize(array, size)
                );
            }
        );
    }
}
