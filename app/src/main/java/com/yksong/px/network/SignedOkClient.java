package com.yksong.px.network;

import com.fivehundredpx.api.auth.AccessToken;

import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by esong on 2015-06-02.
 */
public class SignedOkClient extends OkClient {
    private final OAuthConsumer mOAuthConsumer;

    public SignedOkClient(String consumerKey, String consumerSecret, AccessToken token) {
        super();
        mOAuthConsumer = new RetrofitHttpOAuthConsumer(consumerKey, consumerSecret);
        mOAuthConsumer.setTokenWithSecret(token.getToken(), token.getTokenSecret());
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request requestToSend = request;
        try {
            HttpRequestAdapter signedAdapter = (HttpRequestAdapter) mOAuthConsumer.sign(request);
            requestToSend = (Request) signedAdapter.unwrap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return super.execute(requestToSend);
    }
}
