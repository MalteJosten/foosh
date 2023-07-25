package com.vs.foosh.api.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;


public class LinkBuilder {
    private static String host;
    private static int port;

    public static URI buildPath(List<String> paths) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port);

        for (int i = 0; i < paths.size(); i++) {
            StringBuilder toAppend = new StringBuilder(paths.get(i));

            if (i != (paths.size() - 1)) {
                toAppend.append('/');
            }

            uriBuilder.path(toAppend.toString());
        }

        return uriBuilder.build().toUri();
    }

    public static URI getDeviceListLink() {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("devices/")
            .build();

        return uri.toUri();
    }


    public static URI getDeviceLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
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
            .path("api/")
            .path("vars/")
            .build();

        return uri.toUri();
    }

    public static List<LinkEntry> getVariableLinkBlock (String id) {
        Variable variable = VariableList.getVariable(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getExtLinks());

        return links;
    }

    public static URI getVariableLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("var/")
            .path(id.toString())
            .build();

        return uri.toUri();
    }

    public static URI getModelLink(UUID id) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
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

    public static LinkEntry getRootLinkEntry() {
        return new LinkEntry("root", buildPath(List.of("api")), HttpAction.GET, List.of());
    }

    public static List<LinkEntry> getDeviceLinkWithDevices(String id) {
        List<LinkEntry> links = new ArrayList<>();
        List<LinkEntry> deviceSelfLinks = DeviceList.getDevice(id).getSelfLinks();
        List<LinkEntry> devicesLinks    = DeviceList.getLinks("devices");

        links.addAll(deviceSelfLinks);
        links.addAll(devicesLinks);

        return links;
    }

    public static void setServerVariables(String pHost, int pPort) {
        host = pHost;
        port = pPort;
    }
}
