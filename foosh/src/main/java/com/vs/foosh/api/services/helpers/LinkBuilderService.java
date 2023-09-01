package com.vs.foosh.api.services.helpers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;

/**
 * A {@link Service} to generate and construct {@link URI}s and {@link LinkEntry}s.
 */
@Service
public class LinkBuilderService {
    /**
     * The host part of the application's URL.
     */
    private static String host;

    /**
     * The port of the appliction.
     */
    private static int port;

    /**
     * Build a {@link URI} given a list of paths.
     * 
     * @param paths a {@link List} of path elements to construct the path with
     */
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

    /**
     * Return the URI of the device list route.
     * 
     * @return the {@link URI} of the list of devices
     */
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

    /**
     * Return the URI of an {@link AbstractDevice}.
     * 
     * @param identifier the identifier of the {@link AbstractDevice} to get the URI for
     * @return the {@link URI} of the the {@link AbstractDevice}
     */
    public static URI getDeviceLink(String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("devices/")
            .path(identifier)
            .build();

        return uri.toUri();
    }

    /**
     * Return the {@link LinkEntry}s of an {@link AbstractDevice} and of the devices list.
     * 
     * @param id the {@link AbstractDevice}'s identifier
     */
    public static List<LinkEntry> getDeviceLinkWithDevices(String id) {
        List<LinkEntry> links = new ArrayList<>();
        List<LinkEntry> deviceSelfLinks = ListService.getDeviceList().getThing(id).getSelfLinks();
        List<LinkEntry> devicesLinks    = ListService.getDeviceList().getLinks("devices");

        links.addAll(deviceSelfLinks);
        links.addAll(devicesLinks);

        return links;
    }

    /**
     * Return the URI of the variable list route.
     * 
     * @return the {@link URI} of the list of variables
     */
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

    /**
     * Return the {@code selfLinks} and {@code extLinks} of a {@link Variable}.
     * 
     * @param id the identifier of a {@link Variable}
     * @return a {@link List} with elements of type {@link LinkEntry}
     */
    public static List<LinkEntry> getVariableLinkBlock(String id) {
        Variable variable = ListService.getVariableList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(variable.getSelfLinks());
        links.addAll(variable.getExtLinks());

        return links;
    }

    /**
     * Return the URI of a {@link Variable}.
     * 
     * @param identifier the identifier of the {@link Variable} to get the URI for
     * @return the {@link URI} of the the {@link Variable}
     */
    public static URI getVariableLink(String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("vars/")
            .path(identifier)
            .build();

        return uri.toUri();
    }

    /**
     * Return the URI of a {@link Variable}'s devices list.
     * 
     * @param identifier the identifier of the {@link Variable} to get the device list URI for
     * @return the {@link URI} of the the {@link Variable}'s device list
     */
    public static URI getVariableDevicesLink(String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("vars/")
            .path(identifier + "/")
            .path("devices/")
            .build();

        return uri.toUri();
    }

    /**
     * Return the URI of a {@link Variable}'s models list.
     * 
     * @param identifier the identifier of the {@link Variable} to get the model list URI for
     * @return the {@link URI} of the the {@link Variable}'s model list
     */
    public static URI getVariableModelsLink(String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("vars/")
            .path(identifier + "/")
            .path("models/")
            .build();

        return uri.toUri();
    }

    /**
     * Return the URI of the prediction model list route.
     * 
     * @return the {@link URI} of the list of prediction models
     */
    public static URI getPredictionModelListLink() {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("models/")
            .build();

        return uri.toUri();
    }

    /**
     * Return the URI of an {@link AbstractPredictionModel}.
     * 
     * @param identifier the identifier of the {@link AbstractPredictionModel} to get the URI for
     * @return the {@link URI} of the the {@link AbstractPredicitonModel}
     */
    public static URI getPredictionModelLink(String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("models/")
            .path(identifier)
            .build();

        return uri.toUri();
    }

    /**
     * Return the URI of an {@link AbstractPredictionModel}'s mappings.
     * 
     * @param identifier the identifier of the {@link AbstractPredictionModel} to get the mappings' URI for
     * @return the {@link URI} of the the {@link AbstractPredictionModel}'s mappings
     */
    public static URI getPredictionModelMappingLink (String identifier) {
        UriComponents uri = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(host)
            .port(port)
            .path("api/")
            .path("models/")
            .path(identifier + "/")
            .path("mappings/")
            .build();

        return uri.toUri();
    }

    /**
     * Return the {@code selfLinks} of an {@link AbstractPredictionModel}.
     * 
     * @param id the identifier of a {@link Variable}
     * @return a {@link List} with elements of type {@link LinkEntry}
     */
    public static List<LinkEntry> getPredictionModelLinkBlock (String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());

        return links;
    }

    /**
     * Return the {@link LinkEntry} for the {@code /api/} (root) path.
     * 
     * @return a {@link LinkEntry}
     */
    public static LinkEntry getRootLinkEntry() {
        return new LinkEntry("root", buildPath(List.of("api")), HttpAction.GET, List.of());
    }

    /**
     * Return the {@link LinkEntry} for the {@code /api/} (root) path and the available "root" paths for {@code devices}, {@code variables}, and {@code prediction models}.
     * 
     * @return a {@link List} with elements of type {@link LinkEntry}
     */
    public static List<LinkEntry> getRootLinkEntries() {
        List<LinkEntry> links = new ArrayList<>();
        links.add(new LinkEntry("root", buildPath(List.of("api")), HttpAction.GET, List.of()));
        links.add(new LinkEntry("devices", LinkBuilderService.getDeviceListLink() , HttpAction.GET, List.of()));
        links.add(new LinkEntry("variables", LinkBuilderService.getVariableListLink() , HttpAction.GET, List.of()));
        links.add(new LinkEntry("models", LinkBuilderService.getPredictionModelListLink() , HttpAction.GET, List.of()));

        return links;
    }

    /**
     * Set the fields {@code host} and {@code port}.
     * 
     * @param pHost the new value for the field {@code host}
     * @param pPort the new value for the field {@code port}
     */
    public static void setServerVariables(String pHost, int pPort) {
        host = pHost;
        port = pPort;
    }
}
