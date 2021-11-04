package com.skapral.parrot.itests.utils.authentication;

import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;
import com.pragmaticobjects.oo.tests.AssertCombined;
import com.skapral.parrot.itests.assertions.http.ResponseHasHeader;
import com.skapral.parrot.itests.assertions.http.StatusCodeRedirect;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JwtAuthentication implements Authentication {
    private final HttpClient CLIENT = HttpClient.newHttpClient();
    private final Memory memory;
    private final URI authUri;
    private final String login;

    public JwtAuthentication(Memory memory, URI authUri, String login) {
        this.memory = memory;
        this.authUri = authUri;
        this.login = login;
    }

    @Override
    public final void authenticate() {
        authorizationHeader();
    }

    @Override
    public final HttpRequest.Builder authenticate(HttpRequest.Builder originalRequest) {
        var authorizationHeaderValue = authorizationHeader();
        return originalRequest.header("Authorization", authorizationHeaderValue);
    }

    private final String authorizationHeader() {
        return memory.memoized(
                new MemoizedCallable<String>() {
                    @Override
                    public final String call() {
                        try {
                            var req = HttpRequest.newBuilder()
                                    .POST(HttpRequest.BodyPublishers.noBody())
                                    .uri(authUri.resolve("/auth/login?login=" + login))
                                    .build();
                            var resp = CLIENT.send(req, HttpResponse.BodyHandlers.discarding());
                            new AssertCombined(
                                    new StatusCodeRedirect(resp),
                                    new ResponseHasHeader(resp, "Authorization")
                            ).check();
                            return resp.headers().firstValue("Authorization").get();
                        } catch(Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
    }
}
