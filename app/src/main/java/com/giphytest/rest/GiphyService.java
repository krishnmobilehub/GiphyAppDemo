package com.giphytest.rest;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class GiphyService {


    private static final String BASE_URL = "http://api.giphy.com/";

    public static final int DEFAULT_RESULTS_COUNT = 24;

    public static final String PUBLIC_API_KEY = "8516ae5930e2408f8dd8849ccb63835d";

    private static GiphyApi api;

    private static volatile GiphyService instance;

    private GiphyService(final String endPoint) {
        api = ServiceUtil.INSTANCE.createService(GiphyApi.class, endPoint);
    }

    public static GiphyService getInstance() {
        return getInstance(BASE_URL);
    }

    static GiphyService getInstance(final String endPoint) {
        if (instance == null) {
            synchronized (GiphyService.class) {
                if (instance == null) {
                    instance = new GiphyService(endPoint);
                }
            }
        }

        return instance;
    }

    public Observable<GiphyResponse> getTrendingResults() {
        return getTrendingResults(PUBLIC_API_KEY);
    }

    Observable<GiphyResponse> getTrendingResults(final String apiKey) {
        return getTrendingResults(DEFAULT_RESULTS_COUNT, apiKey);
    }

    Observable<GiphyResponse> getTrendingResults(final int limit, final String apiKey) {
        return api.getTrendingResults(limit, apiKey);
    }

    public Observable<GiphyResponse> getSearchResults(final String searchString) {
        return getSearchResults(searchString, PUBLIC_API_KEY);
    }

    Observable<GiphyResponse> getSearchResults(final String searchString, final String apiKey) {
        return getSearchResults(searchString, DEFAULT_RESULTS_COUNT, apiKey);
    }

    Observable<GiphyResponse> getSearchResults(final String searchString, final int limit, final String apiKey) {
        return api.getSearchResults(searchString, limit, apiKey);
    }

    public interface GiphyApi {

        @GET("/v1/gifs/trending") Observable<GiphyResponse> getTrendingResults(@Query("limit") final int limit,
                                                                               @Query("api_key") final String apiKey);

        @GET("/v1/gifs/search") Observable<GiphyResponse> getSearchResults(@Query("q") final String searchString,
                                                                           @Query("limit") final int limit,
                                                                           @Query("api_key") final String apiKey);
    }
}
