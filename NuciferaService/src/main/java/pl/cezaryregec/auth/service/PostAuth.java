package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.models.AuthResponse;

public interface PostAuth {
    AuthResponse execute(PostAuthQuery postAuthQuery);
}
