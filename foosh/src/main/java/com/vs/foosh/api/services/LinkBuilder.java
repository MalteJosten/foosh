package com.vs.foosh.api.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


public class LinkBuilder {
    private static String host;
    private static int port;

    public static URI buildPath(List<String> paths) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port);

        paths.forEach(path -> {
            uriBuilder
                .path(path + '/');
        });

        return uriBuilder.build().toUri();
    }

    public static URI getDeviceListLink() {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("devices/")
            .build();

        return uri.toUri();
    }

    public static List<URI> getDeviceBatchLink(List<UUID> deviceIDs) {
        List<URI> uris = new ArrayList<>();

        for(UUID deviceID: deviceIDs) {
           uris.add(getDeviceLink(deviceID));
        }

        return uris;
    }

    public static URI getDeviceLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("device/")
            .path(id.toString())
            .build();

        return uri.toUri();
    }

    public static URI getVariableListLink() {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("vars/")
            .build();

        return uri.toUri();
    }

    public static URI getVariableLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("var/")
            .path(id.toString())
            .build();

        return uri.toUri();
    }

    public static List<URI> getModelBatchLink(List<UUID> modelIDs) {
        List<URI> uris = new ArrayList<>();

        for(UUID modelID: modelIDs) {
           uris.add(getModelLink(modelID));
        }

        return uris;
    }

    public static URI getModelLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("model/")
            .path(id.toString())
            .build();

        return uri.toUri();
    }

    public static Map<String, Object> getJSONLinkBlock(Map<String, String> linkMapping) {
        Map<String, Object> linkBlock = new HashMap<>();

        for (String key: linkMapping.keySet()) {
            linkBlock.put(key, linkMapping.get(key));
        }

        return linkBlock;
    }

    public static Map<String, URI> getRootLinkEntry() {
        Map<String, URI> rootLink = new HashMap<>();
        rootLink.put("root", buildPath(List.of("api")));

        return rootLink;
    }

    public static void setServerVariables(String pHost, int pPort) {
        host = pHost;
        port = pPort;
    }
}
