package com.touchlink.medespoir.WebServices.apollo;


import com.apollographql.apollo.ApolloClient;

public class WebServicesFactory {

    private static WebServicesFactory instance;
    private static ApolloClient apolloClient;

    public static WebServicesFactory getInstance(){
        if (instance == null)
            instance = new WebServicesFactory();
        return instance ;

    }


    public ApolloClient getApolloClient()
    {
        if (apolloClient == null)
            apolloClient =ApolloClient.builder()
                    .serverUrl(Urls.MAIN_URL)
                    .build();;
        return apolloClient ;
    }
}
