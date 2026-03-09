package com.ts4.customer.data.utils.logs;

import feign.Client;
import feign.Request;
import feign.Response;
import java.io.IOException;

public class FeignResponseInterceptor implements Client {
    private final Client delegate;

    public FeignResponseInterceptor(Client delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        return delegate.execute(request, options);
    }
}
