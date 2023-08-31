package com.vs.foosh.api.model.predictionModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.CouldNotFindVariableParameterMappingException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingAlreadyPresentException;
import com.vs.foosh.api.exceptions.predictionModel.ParameterMappingNotFoundException;
import com.vs.foosh.api.model.misc.AbstractModification;
import com.vs.foosh.api.model.misc.IThingListSubscriber;
import com.vs.foosh.api.model.misc.ModificationType;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.variable.VariableModification;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.LinkBuilderService;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A subclass of {@link Thing} that adds support for prediction models.
 * It adds the additional fields:
 *      * variableIds
 *      * parameters
 *      * paratemerMappings
 *      * valueBounds
 *      * various link lists
 * These add prediction model specific data and enables linking to {@link Variable}s.o
 * 
 * It implements the interface {@link IThingListSubscriber} to act as a subscriber of the observer pattern.
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 * 
 * It implements the interface {@link IPredictionMaker} to make predictions based on a given value and provide
 * the user with corresponding smart home api instructions.
 */
public abstract class AbstractPredictionModel extends Thing implements IThingListSubscriber, IPredictionMaker {
    /**
     * The {@link List} with elements of type {@link UUID} containing IDs of variables which are linked to this model.
     */
    protected List<UUID> variableIds  = new ArrayList<>();

    /**
     * The {@link List} with elements of type {@link String} containing all parameter names relevant for this prediction model.
     */
    protected List<String> parameters = new ArrayList<>();


    /**
     * The {@link List} with elements of type {@link VariableParameterMapping} containing all mappings for every variable mapping.
     */
    protected List<VariableParameterMapping> parameterMappings = new ArrayList<>();

    /**
     *  The {@link List} with elements of type {@link LinkEntry} which point to the model's own API URIs.
     */
    protected List<LinkEntry> links         = new ArrayList<>();

    /**
     * The {@link List} with elements of type {@link LinkEntry} which point to all variable API URIs which are linked to this model.
     */
    protected List<LinkEntry> variableLinks = new ArrayList<>();

    /**
     * The {@link List> with elements of type {@link LinkEntry} wihch point to the current mapping API URIs.
     */
    protected List<LinkEntry> mappingLinks  = new ArrayList<>();

    /**
     * The {@link List} with elements of type {@link LinkEntry} which point to all device API URIs which are part of a mapping.
     */
    protected List<LinkEntry> deviceLinks   = new ArrayList<>();

    /**
     * The interval/bounds in which the value of a prediction needs to be part of.
     * 
     * E.g., {@code valueBounds} could be [4.0,30.0] for the use case of 'temperature' in degrees celsius.
     */
    protected Object valueBounds;

    /**
     * Make a prediction with this prediction model} based on the provided {@code value}.
     * 
     * @apiNote This is a default implementation! needs to be overwritten when extending {@code AbstractPredictionModel}.
     * 
     * @param variableId the {@link UUID} of the variable for which we perform the prediction
     * @param value the value for which we make the prediction
     */
    @Override
    public List<SmartHomeInstruction> makePrediction(UUID variableId, String value) {
        return new ArrayList<>();
    }

    /**
     * Set the field {@code name} and update all link lists.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
        updateLinks();
    }

    /**
     * Return the field {@code variableIds}.
     * 
     * @return the field {@code variableIds}
     */
    public List<UUID> getVariableIds() {
        return this.variableIds;
    }

    /**
     * Return the field {@code parameters}.
     * 
     * @return the field {@code parameters}
     */
    public List<String> getParameters() {
        return this.parameters;
    }

    /**
     * Set the field {@code parameters}, clear the list of parameter mappings and update all links.
     * 
     * @param parameters the {@link List} with elements of type {@link String} containing parameters
     */
    protected void setParameters(List<String> parameters) {
        this.parameters = parameters;
        parameterMappings.clear();

        updateLinks();
    }

    /**
     * Return the field {@code parameterMappings}.
     * 
     * @return the field {@code parameterMappings}
     */
    public List<VariableParameterMapping> getAllMappings() {
        return this.parameterMappings;
    }

    /**
     * Return a variable parameter mapping, given a variable ID.
     * 
     * If there is no {@link VariableParameterMapping} which is associated with this model and the {@code variableId}
     * a {@link CouldNotFindVariableParameterMappingException} is thrown.
     * 
     * @param variableId the {@link UUID} of the {@link Variable} we want to retrieve the variable parameter mapping for
     * @return the {@link VariableParameterMapping} if it is present in {@code parameterMappings}
     */
    public VariableParameterMapping getParameterMapping(UUID variableId) {
        for (VariableParameterMapping varParamMapping: parameterMappings) {
            if (varParamMapping.getVariableId().equals(variableId)) {
                return varParamMapping;
            }
        }

        throw new CouldNotFindVariableParameterMappingException(this.id, variableId);
    }

    /**
     * Return a list of parameter mappings, given a variable ID.
     * 
     * @return the {@link List} with elements of type {@link ParameterMapping} which all mappings which are part of
     * a {@link VariableParameterMapping} in {@code parameterMappings}
     */
    public List<ParameterMapping> getMappings() {
        List<ParameterMapping> mappings = new ArrayList<>();

        for (VariableParameterMapping varParamMapping: parameterMappings) {
            mappings.addAll(varParamMapping.getMappings());
        }

        return mappings;
    }

    /**
     * Set a list with elements of type {@link ParameterMapping} for this model and the variable with the provided {@code variableId} as its
     * corresponding mappings.
     * If there already is an entry in {@code parameterMapping} for the {@link Variable}, it is overwritten.
     * 
     * @param variableId the {@link UUID} of a {@link Variable} to set the mapping for
     * @param mappings the {@link List} with elements of type {@link ParameterMapping}
     */
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

    /**
     * Add a a list {@link ParameterMapping}s to the mappings for this model and the variable with the provided {@code variableId}.
     * If there already is an entry in {@code variableIds} for the {@code variableId} a {@link ParameterMappingAlreadyPresentException}
     * is thrown.
     * 
     * This model is also registered as a subscriber at the {@link Variable} in question.
     * @see com.vs.foosh.api.model.misc.IThingListPublisher#attach(IThingListSubscriber)
     * 
     * The field {@code variableIds} is updated.
     * 
     * @param variableId the {@link UUID} of a {@link Variable} to set the mapping for
     * @param mappings the {@link List} with elements of type {@link ParameterMapping}
     */
    public void addMapping(UUID variableId, List<ParameterMapping> mappings) {
        if (variableIds.contains(variableId)) {
            throw new ParameterMappingAlreadyPresentException("You cannot add new parameter mappings to this variable/model. "
                + "There are already variable parameter mappings. "
                + "Please use PATCH (replace) instead.");
        }

        ListService.getVariableList().getThing(variableId.toString()).attach(this);
        parameterMappings.add(new VariableParameterMapping(variableId, mappings));
        updateVariableIds();
    }

    /**
     * Delete a variable mapping from this model, given a variable ID.
     * 
     * If {@code variableIds} does not contain the {@code variableId}, a ParameterMappingAlready
     * 
     * @param variableID the {@link UUID} of the {@link Variable} in question
     */
    public void deleteMapping(UUID variableId) {
        if (!variableIds.contains(variableId)) {
            throw new ParameterMappingNotFoundException("You cannot remove parameter mappings which are not present in the list of parameter mappings!");
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

    /**
     * Delete all parameter mappings.
     * 
     *    * The model unsubscribes from all previously subscribed {@link Variable}s.
     *    * @see com.vs.foosh.api.model.misc.IThingListPublisher#detach(IThingListSubscriber)
     * 
     *    * Clear the list {@code parameterMappings}.
     *
     *    * Update various link lists.
     */
    public void deleteMappings() {
        for(UUID variableId: variableIds) {
            ListService.getVariableList().getThing(variableId.toString()).detach(this);    
        }

        parameterMappings.clear();

        updateVariableIds();
        updateVariableLinks();
        updateDeviceLinks();
    }

    /**
     * Update the {@link List} of {@code variableIds} based on the entries of {@code parameterMappings}.
     */
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

    /**
     * Return the field {@code links}.
     * 
     * @return the field {@code links}
     */
    public List<LinkEntry> getSelfLinks() {
        return this.links;
    }

    /**
     * Return all entries of {@code links} which have the label "selfStatic".
     * 
     * @return a {@link List} with elements of type {@link LinkEntry}
     */
    public List<LinkEntry> getSelfStaticLinks(String label) {
        List<LinkEntry> links = new ArrayList<>();
       
        for(LinkEntry link: getSelfLinks()) {
            if (link.getRelation().equals("selfStatic")) {
                links.add(new LinkEntry(label, link.getLink(), link.getAction(), link.getTypes()));
            }
        }

        return links;
    }

    /**
     * Return the field {@code variableLinks}.
     * 
     * @return the field {@code variableLinks}
     */
    public List<LinkEntry> getVariableLinks() {
        return this.variableLinks;
    }

    /**
     * Return the field {@code mappingLinks}.
     * 
     * @return the field {@code mappingLinks}
     */
    public List<LinkEntry> getMappingLinks() {
        return this.mappingLinks;
    }

    /**
     * Return the field {@code deviceLinks}.
     * 
     * @return the field {@code deviceLinks}
     */
    public List<LinkEntry> getDeviceLinks() {
        return this.deviceLinks;
    }

    /**
     * Return the a list containig all elements of {@code links}, {@code variableLinks}, {@code mappingLinks}, {@code deviceLinks}.
     * 
     * @return a {@link List} with elements of type {@code LinkEntry} containing all prediction model links
     */
    public List<LinkEntry> getAllLinks() {
        List<LinkEntry> allLinks = new ArrayList<>();
        allLinks.addAll(this.links);
        allLinks.addAll(this.variableLinks);
        allLinks.addAll(this.mappingLinks);
        allLinks.addAll(this.deviceLinks);

        return allLinks;
    }

    /**
     * Update all link lists.
     */
    public void updateLinks() {
        updateSelfLinks();
        updateVariableLinks();
        updateMappingLinks();
        updateDeviceLinks();
    }

    /**
     * Update the entries of {@code links}.
     */
    protected void updateSelfLinks() {
        LinkEntry getId   = new LinkEntry("selfStatic", LinkBuilderService.getPredictionModelLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry patchId = new LinkEntry("selfStatic", LinkBuilderService.getPredictionModelLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));

        LinkEntry getName   = new LinkEntry("selfName", LinkBuilderService.getPredictionModelLink(this.name), HttpAction.GET, List.of());

        if (links != null || !links.isEmpty()) {
            links.clear();
        }

        links.addAll(List.of(getId, patchId, getName));
    }

    /**
     * Update the entries of {@code variableLinks}.
     */
    protected void updateVariableLinks() {
        if (variableLinks != null || !variableLinks.isEmpty()) {
            variableLinks.clear();
        }

        for (UUID variableId: variableIds) {
            variableLinks.addAll(ListService.getVariableList().getThing(variableId.toString()).getSelfStaticLinks("variable"));
        }
    }

    /**
     * Update the entries of {@code mappingLinks}.
     */
    protected void updateMappingLinks() {
        LinkEntry getMapping    = new LinkEntry("mappings", LinkBuilderService.getPredictionModelMappingLink(this.id.toString()), HttpAction.GET, List.of());
        LinkEntry postMapping   = new LinkEntry("mappings", LinkBuilderService.getPredictionModelMappingLink(this.id.toString()), HttpAction.POST, List.of("application/json"));
        LinkEntry patchMapping  = new LinkEntry("mappings", LinkBuilderService.getPredictionModelMappingLink(this.id.toString()), HttpAction.PATCH, List.of("application/json-patch+json"));
        LinkEntry deleteMapping = new LinkEntry("mappings", LinkBuilderService.getPredictionModelMappingLink(this.id.toString()), HttpAction.DELETE, List.of());

        if (mappingLinks != null || !mappingLinks.isEmpty()) {
            mappingLinks.clear();
        }

        if (getAllMappings().isEmpty()) {
            mappingLinks.addAll(List.of(getMapping, postMapping, patchMapping));
        } else {
            mappingLinks.addAll(List.of(getMapping, patchMapping, deleteMapping));
        }
    }

    /**
     * Update the entries of {@code deviceLinks}.
     */
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

    /**
     * Return the display representation of this prediction model.
     * 
     * @return the {@link PredictionModelDisplayRepresentation} of this prediction model
     */
    public PredictionModelDisplayRepresentation getDisplayRepresentation() {
        return new PredictionModelDisplayRepresentation(this);
    }

    /**
     * The implementation of {@link com.vs.foosh.api.model.misc.IThingListSubscriber#update() update()}.
     * 
     * If the publisher ({@link Variable}) was deleted, the corresponding parameterMapping is removed.
     * If some of the publisher's ({@link Variable}) dependencies have changed, the corresponding parameterMapping is cleared.
     * 
     * After any changes, we save any changes into persistent storage.
     */
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

    /**
     * Return the field {@code valueBounds}.
     * 
     * @return the field {@code valueBounds}
     */
    public Object getValueBounds() {
        return this.valueBounds;
    }

    /**
     * Check whether the given {@code value} is inside the {@code valueBounds}.
     * 
     * @param variableId the {@link UUID} of the {@link Variable} for which we want to make a prediction
     * @param value the {@code value} we want to make a prediction for
     */
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
