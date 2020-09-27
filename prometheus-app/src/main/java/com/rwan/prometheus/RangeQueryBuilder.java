package com.rwan.prometheus;

/**
 * @Author: apple
 * @Date: 2020/8/21
 * @Version 2.0
 */
public class RangeQueryBuilder implements QueryBuilder {


//        private static final String TARGET_URL_PATH="api/v1/query_range?query=${query}&dedup=${dedup}&partial_response=${partial_response}&start=${start}&end=${end}&step=${step}&max_source_resolution=${max_source_resolution}&_=${timestamp}";
        private static final String TARGET_URL_PATH="api/v1/query_range?query=${query}";


        private String targetUrl ;

        private boolean dedup = true;

        private boolean partial_response = true;


        public RangeQueryBuilder(String serverUrl){

                targetUrl = serverUrl + TARGET_URL_PATH;
        }

}
