package pl.cezaryregec.auth.service;

import pl.cezaryregec.auth.models.PostAuthQuery;
import pl.cezaryregec.auth.models.PostAuthResponse;
import pl.cezaryregec.exception.APIException;

public interface PostAuth {
    PostAuthResponse execute(PostAuthQuery postAuthQuery) throws APIException;
}
