package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.models.AuthToken;

public interface PostAuth {
    AuthToken execute(PostAuthQuery postAuthQuery);
}
