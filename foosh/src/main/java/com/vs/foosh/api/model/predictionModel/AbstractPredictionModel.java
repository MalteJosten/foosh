package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public abstract class AbstractPredictionModel extends Thing {
    private List<UUID> variableIds = new ArrayList<>();
    private List<String> parameters = new ArrayList<>();

    private List<VariableParameterMapping> parameterMappings = new ArrayList<>();

    protected List<LinkEntry> links         = new ArrayList<>();
    protected List<LinkEntry> variableLinks = new ArrayList<>();
    protected List<LinkEntry> mappingLinks  = new ArrayList<>();
    protected List<LinkEntry> deviceLinks   = new ArrayList<>();

    /// This is a default implementation.
    /// Needs to be overwritten!
    public List<SmartHomeInstruction> makePrediction(String value) {
        return new ArrayList<>();
    }

    public List<UUID> getVariableIds() {
        return this.variableIds;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    protected void setParameters(List<String> parameters) {
        this.parameters = parameters;
        parameterMappings.clear();

        updateLinks();
    }

    public List<VariableParameterMapping> getAllMappings() {
        return this.parameterMappings;
    }

    public VariableParameterMapping getParameterMapping(UUID variableId) {
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            if (varParamMapping.getVariableId().equals(variableId)) {
                return varParamMapping;
            }
        }

        throw new CouldNotFindVariableParameterMappingException(this.id, variableId);
    }

    public List<ParameterMapping> getMappings(UUID variableId) {
        List<ParameterMapping> mappings = new ArrayList<>();

        for (VariableParameterMapping varParamMapping: parameterMappings) {
            mappings.addAll(varParamMapping.getMappings());
        }

        return mappings;
    }

    public void setMapping(UUID variableId, List<ParameterMapping> mappings) {
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            if (varParamMapping.getVariableId().equals(variableId)) {
                parameterMappings.remove(varParamMapping);
                break;
            }

        }
        
        parameterMappings.add(new VariableParameterMapping(variableId, mappings));
        udpateVariableIds();
    }

    public void patchMapping(FooSHJsonPatch patch) {
        // TODO: Implement
    }

    public void deleteMapping() {
        // TODO: Implement
    }

    protected void udpateVariableIds() {
        // Check which variable IDs are not yet stored in the variableIds list
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            if (!variableIds.contains(varParamMapping.getVariableId())) {
                variableIds.add(varParamMapping.getVariableId());
            }
        }

        // Check which variable IDs got removed but still are stored in the variableIds list
        for (UUID variableId: variableIds) {
            boolean present = false;
            for (VariableParameterMapping varParamMapping: parameterMappings) {
                if (varParamMapping.getVariableId().equals(variableId)) {
                    present = true;
                    break;
                }
            }

            if (!present) {
                variableIds.remove(variableId);
            }
        }
    }

    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    public List<LinkEntry> getVariableLinks() {
        return this.variableLinks;
    }

    public List<LinkEntry> getMappingLinks() {
        return this.mappingLinks;
    }

    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> allLinks = new ArrayList<>();
        allLinks.addAll(this.links);
        allLinks.addAll(this.variableLinks);
        allLinks.addAll(this.mappingLinks);
        allLinks.addAll(this.deviceLinks);

        return allLinks;
    }

    public void updateLinks() {
        updateSelfLinks();
        updateVariableLinks();
        updateMappingLinks();
        updateDeviceLinks();
    }

    protected void updateSelfLinks() {
        LinkEntry getId   = new LinkEntry("selfStatic", LinkBuilder.getPredictionModelLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry getName = new LinkEntry("selfName", LinkBuilder.getPredictionModelLink(this.name), HttpAction.GET, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, getName));
    }

    protected void updateVariableLinks() {
        if (variableLinks != null || !variableLinks.isEmpty()) {
            variableLinks.clear();
        }

        for (UUID variableId: variableIds) {
            variableLinks.addAll(ListService.getVariableList().getThing(variableId.toString()).getSelfStaticLinks("variable"));
        }
    }

    protected void updateMappingLinks() {
        LinkEntry getMapping    = new LinkEntry("mapping", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry postMapping   = new LinkEntry("mapping", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry deleteMapping = new LinkEntry("mapping", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.DELETE, List.of());

        if (mappingLinks != null || !mappingLinks.isEmpty()) {
            mappingLinks.clear();
        }

        if (getAllMappings().isEmpty()) {
            mappingLinks.addAll(List.of(getMapping, postMapping));
        } else {
            mappingLinks.addAll(List.of(getMapping, deleteMapping));
        }
    }

    protected void updateDeviceLinks() {
        if (deviceLinks != null || !deviceLinks.isEmpty()) {
            deviceLinks.clear();
        }

        Set<LinkEntry> linkSet = new HashSet<>();
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            for (ParameterMapping mapping: varParamMapping.getMappings()) {
                linkSet.addAll(ListService.getDeviceList().getThing(mapping.getDeviceId().toString()).getSelfStaticLinks("device"));
            }
        }

        deviceLinks.addAll(linkSet);
    }

    public PredictionModelDisplayRepresentation getDisplayRepresentation() {
        return new PredictionModelDisplayRepresentation(this);
    }
}
