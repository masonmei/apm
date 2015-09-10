package com.baidu.oped.apm.testapp.service.remote;

import org.springframework.util.MultiValueMap;

/**
 * class RemoteService 
 *
 * @author meidongxu@baidu.com
 */



public interface RemoteService {

    <R> R get(String url, Class<R> responseType) throws Exception;
    <R> R get(String url, MultiValueMap<String, String> params, Class<R> responseType) throws Exception;;

    <R> R post(String url, Class<R> responseType) throws Exception;;
    <R> R post(String url, MultiValueMap<String, String> params, Class<R> responseType) throws Exception;;

}
