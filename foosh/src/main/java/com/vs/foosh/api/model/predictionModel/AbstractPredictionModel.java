package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingAlreadyPresentException;
import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.IThingListObserver;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.variable.VariableModification;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

public abstract class AbstractPredictionModel extends Thing implements IThingListObserver {
    private List<UUID> variableIds  = new ArrayList<>();
    private List<String> parameters = new ArrayList<>();

    private List<VariableParameterMapping> parameterMappings = new ArrayList<>();

    protected List<LinkEntry> links         = new ArrayList<>();
    protected List<LinkEntry> variableLinks = new ArrayList<>();
    protected List<LinkEntry> mappingLinks  = new ArrayList<>();
    protected List<LinkEntry> deviceLinks   = new ArrayList<>();

    protected Object valueBounds;

    /// This is a default implementation.
    /// Needs to be overwritten!
    public List<SmartHomeInstruction> makePrediction(UUID variableId, String value) {
        return new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
        updateLinks();
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
        
        ListService.getVariableList().getThing(variableId.toString()).attach(this);
        parameterMappings.add(new VariableParameterMapping(variableId, mappings));
        updateVariableIds();
    }

    public void addMapping(UUID variableId, List<ParameterMapping> mappings) {
        if (variableIds.contains(variableId)) {
            throw new ParameterMappingAlreadyPresentException(
                id,
                "You cannot add new parameter mappings to this variable/model. There are already variable parameter mappings. Please use PATCH (replace) instead.");
        }

        ListService.getVariableList().getThing(variableId.toString()).attach(this);
        parameterMappings.add(new VariableParameterMapping(variableId, mappings));
        updateVariableIds();
    }

    public void deleteMapping(UUID variableId) {
        if (!variableIds.contains(variableId)) {
            throw new ParameterMappingAlreadyPresentException(
                id,
                "You cannot remove parameter mappings which are not present in the list of parameter mappings!");
        }

        // Remove entry from variableIds
        for (Iterator<UUID> iterator = variableIds.iterator(); iterator.hasNext();) {
            UUID varId = iterator.next();
            if (varId.equals(variableId)) {
                ListService.getVariableList().getThing(variableId.toString()).detach(this); 
                iterator.remove();
                break;
            }
        }

        // Remove entry from parameterMappings
        for (Iterator<VariableParameterMapping> iterator = parameterMappings.iterator(); iterator.hasNext();) {
            VariableParameterMapping varParamMapping = iterator.next();
            if (varParamMapping.getVariableId().equals(variableId)) {
                iterator.remove();
                break;
            }
        }
    }

    public void deleteMappings() {
        for(UUID variableId: variableIds) {
            ListService.getVariableList().getThing(variableId.toString()).detach(this);    
        }

        parameterMappings.clear();

        updateVariableIds();
        updateVariableLinks();
        updateDeviceLinks();
    }

    protected void updateVariableIds() {
        // Check which variable IDs are not yet stored in the variableIds list
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            if (!variableIds.contains(varParamMapping.getVariableId())) {
                variableIds.add(varParamMapping.getVariableId());
            }
        }

        // Check which variable IDs got removed but still are stored in the variableIds list
        for (Iterator<UUID> iterator = variableIds.iterator(); iterator.hasNext();) {
            UUID variableId = iterator.next();
            boolean present = false;
            
            for (VariableParameterMapping varParamMapping: parameterMappings) {
                if (varParamMapping.getVariableId().equals(variableId)) {
                    present = true;
                    break;
                }
            }

            if (!present) {
                iterator.remove();
            }
        }
    }

    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
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
        LinkEntry patchId = new LinkEntry("selfStatic", LinkBuilder.getPredictionModelLink(this.id.toString()), HttpAction.PATCH, List.of("application/json"));

        LinkEntry getName   = new LinkEntry("selfName", LinkBuilder.getPredictionModelLink(this.name), HttpAction.GET, List.of());
        LinkEntry patchName = new LinkEntry("selfName", LinkBuilder.getPredictionModelLink(this.name), HttpAction.PATCH, List.of("application/json"));

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, patchId, getName, patchName));
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
        LinkEntry getMapping    = new LinkEntry("mappings", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry postMapping   = new LinkEntry("mappings", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patchMapping  = new LinkEntry("mappings", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.PATCH, List.of("application/json"));
        LinkEntry deleteMapping = new LinkEntry("mappings", LinkBuilder.getPredictionModelMappingLink(this.id.toString()), HttpAction.DELETE, List.of());

        if (mappingLinks != null || !mappingLinks.isEmpty()) {
            mappingLinks.clear();
        }

        if (getAllMappings().isEmpty()) {
            mappingLinks.addAll(List.of(getMapping, postMapping, patchMapping));
        } else {
            mappingLinks.addAll(List.of(getMapping, patchMapping, deleteMapping));
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

    @Override
    public void update(AbstractModification modification) {
        VariableModification variableModification = (VariableModification) modification;
        if (modification.getModificationType() == ModificationType.DELETION) {
            for (Iterator<VariableParameterMapping> iterator = parameterMappings.iterator(); iterator.hasNext();) {
                VariableParameterMapping varParamMapping = iterator.next();
                if (varParamMapping.getVariableId().equals(variableModification.getVariableId())) {
                    iterator.remove();
                }
            }

            updateVariableIds();
            updateVariableLinks();
            updateDeviceLinks();

            return;
        }

        if (modification.getModificationType() == ModificationType.DEPENDENCIES_CHANGED) {
            for (Iterator<VariableParameterMapping> iterator = parameterMappings.iterator(); iterator.hasNext();) {
                VariableParameterMapping varParamMapping = iterator.next();
                if (varParamMapping.getVariableId().equals(variableModification.getVariableId())) {
                    varParamMapping.clearMappings();
                }
            }

            updateDeviceLinks();

            return;
        }

        PersistentDataService.savePredictionModelList();
    }

    public Object getValueBounds() {
        return this.valueBounds;
    }

    public abstract void checkValueInBounds(String variableId, Object value);

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModel >>\n");
        builder.append("Variable-IDs:       " + variableIds + "\n");
        builder.append("Parameters:         " + parameters + "\n");
        builder.append("Parameter Mappings: " + parameterMappings + "\n");
        builder.append("Selflinks:          " + links + "\n");
        builder.append("Variable Links:     " + variableLinks + "\n");
        builder.append("Mapping Links:      " + mappingLinks + "\n");
        builder.append("Device Links:       " + deviceLinks);

        return builder.toString();
    }

}
