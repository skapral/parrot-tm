package com.skapral.parrot.itests.utils.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class JwtAuthentication implements Authentication<DecodedJWT> {
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
    public final Optional<DecodedJWT> authenticate() {
        return authorizationHeader();
    }

    @Override
    public final HttpRequest.Builder authenticate(HttpRequest.Builder originalRequest) {
        var jwtTokenOpt = authorizationHeader();
        if(jwtTokenOpt.isPresent()) {
            return originalRequest.header("Authorization", "Bearer " + jwtTokenOpt.get().getToken());
        } else {
            return originalRequest;
        }
    }

    private final Optional<DecodedJWT> authorizationHeader() {
        return memory.memoized(
                new MemoizedCallable<>() {
                    @Override
                    public final Optional<DecodedJWT> call() {
                        try {
                            var req = HttpRequest.newBuilder()
                                    .POST(HttpRequest.BodyPublishers.noBody())
                                    .uri(authUri.resolve("/login?login=" + login))
                                    .build();
                            var resp = CLIENT.send(req, HttpResponse.BodyHandlers.discarding());
                            var auth=  resp.headers().firstValue("Authorization")
                                    .map(s -> s.replace("Bearer ", ""))
                                    .map(JWT::decode);
                            return auth;
                        } catch(Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
    }
}
