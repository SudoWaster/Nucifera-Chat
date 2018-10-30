package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.exception.APIException;

public interface PostAuth {
    AuthToken execute(PostAuthQuery postAuthQuery) throws APIException;
}
