package com.central.tech.aiclientlib;

import android.support.annotation.NonNull;

import com.amazonaws.DefaultRequest;
import com.amazonaws.auth.AWS4Signer;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by mrdevxus on 2/13/18.
 */

public class AWSSignerHelpers {

    public static Request signV4(@NonNull Request request,
                                        @NonNull AiClientConfigs configs) throws IOException {

        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(configs.getServiceName());
        signer.setRegionName(configs.getRegion());

        BasicAWSCredentialsProvider credentialsProvider =
                new BasicAWSCredentialsProvider(configs.getAccessKey(), configs.getSecretKey());

        Request.Builder requestBuilder = request.newBuilder();
        DefaultRequest awsRequest = new DefaultRequest(configs.getServiceName());

        HttpUrl url = AWSHttpRequestHelpers.setEndpoint(requestBuilder, awsRequest, request.url());

        AWSHttpRequestHelpers.setQueryParams(awsRequest, url);
        AWSHttpRequestHelpers.setHttpMethod(awsRequest, request.method());
        AWSHttpRequestHelpers.setBody(awsRequest, request.body());

        signer.sign(awsRequest, credentialsProvider.getCredentials());
        awsRequest.addHeader("x-api-key", configs.getApiKey());

        AWSHttpRequestHelpers.applyAwsHeaders(requestBuilder, awsRequest.getHeaders());
        return requestBuilder.build();
    }
}
