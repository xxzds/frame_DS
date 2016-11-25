package com.anjz.http.core;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.result.CommonResultCode;
import com.anjz.result.PlainResult;

/**
 * post请求
 * @author ding.shuai
 * @date 2016年7月19日下午3:24:52
 */
public class HttpPostUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpPostUtil.class);

    /**
     * @param requestUri
     * @param params
     * @param responseHandler
     * @return PlainResult<String>
     */
    public static PlainResult<String> execute(final String requestUri, final Map<String, String> params,
                                              final ResponseHandler<PlainResult<String>> responseHandler) {
        if (StringUtils.isBlank(requestUri)) {
            return null;
        }

        CloseableHttpClient httpclient = HttpClientFactory.getCloseableHttpClient();

        try {
            HttpUriRequest request = buildUriPostRequest(requestUri, params);
            return httpclient.execute(request, responseHandler);
        } catch (Exception e) {
            String msg = e.getCause() == null ? e.toString() : e.getCause().getMessage();
            return new PlainResult<String>().setErrorMessage(CommonResultCode.EXCEPITON_HTTP_CALL, msg);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("error message", e);
            }
        }

    }


    private static HttpUriRequest buildUriPostRequest(final String requestUri, final Map<String, String> params)
            throws URISyntaxException {
        RequestBuilder builder = RequestBuilder.post().setUri(new URI(requestUri));
        if (params != null) {
            for (Entry<String, String> param : params.entrySet()) {
                builder.addParameter(param.getKey(), param.getValue());
            }
        }

        return builder.build();
    }
}
