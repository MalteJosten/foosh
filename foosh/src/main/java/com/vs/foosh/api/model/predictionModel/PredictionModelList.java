package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNotFoundException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameMustNotBeAnUuidException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelNameIsNotUniqueException;
import com.vs.foosh.api.model.misc.IThingList;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.HttpAction;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.LinkBuilderService;
import com.vs.foosh.api.services.ListService;

/**
 * A container holding all {@link AbstractPredictionModel}s with a bunch of functions allowing setting, retrieval, and modification of parameter mappings
 * and making predictions.
 * 
 * It implements {@link IThingListSubject} as it can be observed by objects implementing the {@link IThingListSubscriber}.
 * @see <a href="https://refactoring.guru/design-patterns/observer">Observer Pattern</a>
 * 
 * It implements {@link IThingList}<{@link AbstractPredictionModel}, {@link PredicitonModelDisplayRepresentation}> to give the class capabilities of a list of {@link Thing}s.
 * It implements {@link Serializable} so that it can be (de)serialized for saving and loading into and from persistent storage.
 * 
 * @see com.vs.foosh.api.services.PersistentDataService#savePredictionModelList()
 * @see com.vs.foosh.api.services.PersistentDataService#hasSavedPredictionModelList()
 */
public class PredictionModelList implements Serializable, IThingList<AbstractPredictionModel, PredictionModelDisplayRepresentation> {
    /**
     * The list containing all {@link AbstractPredictionModel}s.
     */
    private List<AbstractPredictionModel> models = new ArrayList<>();

    /**
     * set the list of prediction models.
     * 
     * @param modelList the {@link List} with elements of type {@link AbstractPredictionModel}
     */
    @Override
    public void setList(List<AbstractPredictionModel> modelList) {
        List.copyOf(modelList);
    }

    /**
     * Return the list of prediction models.
     * 
     * @return a {@link List} with elements of type {@link AbstractPredictionModel}
     */
    @Override
    public List<AbstractPredictionModel> getList() {
        return this.models;
    }

    /**
     * Return a list of prediction models as things.
     * 
     * @return a {@link List} with elements of type {@link AbstractPredictionModel} as a {@link Thing}
     */
    @Override
    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();
        for (AbstractPredictionModel model: models) {
            things.add(model);
        }

        return things;
    }

    /**
     * Clear the list of prediction models.
     */
    @Override
    public void clearList() {
        models.clear();
    }

    /**
     * Build an retun the display representation of every prediction model in {@code models}.
     */
    @Override
    public List<PredictionModelDisplayRepresentation> getDisplayListRepresentation() {
        List<PredictionModelDisplayRepresentation> displayRepresentations = new ArrayList<>();
        for (AbstractPredictionModel model: ListService.getPredictionModelList().getList()) {
            displayRepresentations.add(new PredictionModelDisplayRepresentation(model));
        }

        return displayRepresentations;
    }

    /**
     * Search for a predicitonModel in {@code models} based on its {@code id} or {@code name}.
     * 
     * @param identifier the identifier to match each {@link AbstractPredictionModel}'s {@code id} and {@code name} against
     * @return the first matched {@link AbstractPredictionModel} or throw a {@link PredictionModelNotFoundException} if no {@link AbstractPredictionModel}
     * with matching fields was found.
     */
    @Override
    public AbstractPredictionModel getThing(String identifier) {
        for (AbstractPredictionModel model: getList()) {
            if (model.getId().toString().equals(identifier) || model.getName().toLowerCase().equals(identifier.toLowerCase().replace("%20", " "))) {
                return model;
            }
        }

        throw new PredictionModelNotFoundException(identifier);
    }

    /**
     * Add a prediction model to the list of models.
     * The prediction model is only added if it is not yet present in {@code models}.
     * 
     * @param model the {@link AbstractPredictionModel} to add to {@code models} 
     */
    @Override
    public void addThing(AbstractPredictionModel model) {
        if (!models.contains(model)) {
            models.add(model);
        }
    }

    /**
     * Given a {@link String} identifier, delete a prediction model from the list of models.
     * 
     * @param identifier either the name or the uuid of the {@link AbstractPredictionModel} that should be removed from {@code models}
     */
    @Override
    public void deleteThing(String identifier) {
        return;
    }

    /**
     * Check whether the provided {@code name} is valid.
     * A name is valid if
     *     - it is not a {@link UUID}
     *     - it is not used by any other {@link AbstractPredictionModel} in the list of prediction models
     * 
     * If the name is an {@link UUID}, a {@link PredictionModelNameMustNotBeAnUuidException} is thrown.
     * If the name is is not unique, a {@link PredictionModelNameIsNotUniqueException} is thrown.
     * 
     * @param name the {@code name} of the {@link AbstractPredictionModel} under test
     * @param uuid the {@code id} of the {@link AbstractPredictionModel} under test
     * @return {@code true} if the name is unique
     */
    @Override
    public boolean isValidName(String name, UUID id) {
        try {
            // Check whether the provided 'name' could be an UUID.
            // Names in form of an UUID are disallowed.
            UUID.fromString(name);
            throw new PredictionModelNameMustNotBeAnUuidException();
        } catch (IllegalArgumentException e) {
            for (AbstractPredictionModel model : this.models) {
                // Check whether the name is already used
                if (model.getName().equalsIgnoreCase(name)) {
                    // If it's already used, check whether it's the same variable.
                    if (model.getId().equals(id)) {
                        return true;
                    }

                    throw new PredictionModelNameIsNotUniqueException(name);
                }

            }
        }

        return true;
    }

    /**
     * Check whether the a prediction model with the provided identifier is present in the list of models.
     * 
     * A {@link PredictionModelIdNotFoundException} is thrown if no {@link AbstractPredictionModel} with matching {@code name} or {@code id} is found.
     * 
     * @param identifier the {@code name} or {@code id} of the {@link AbstractPredictionModel} in question
     */
    @Override
    public void checkIfIdIsPresent(String identifier) {
        for (AbstractPredictionModel model: getList()) {
            if (model.getId().toString().equals(identifier) || model.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new PredictionModelNotFoundException(identifier);
    }

    /**
     * Return the list of self links.
     * These include all currently available HTTP endpoints for {@code /api/models/}.
     * 
     * @param label the label which is used to construct each {@link LinkEntry}
     * @return the list with elements of type {@link LinkEntry} of currently available HTTP endpoints for {@code /api/models/}
     */
    @Override
    public List<LinkEntry> getLinks(String label) {
        LinkEntry get   = new LinkEntry(label, LinkBuilderService.getPredictionModelListLink(), HttpAction.GET, List.of());

        return new ArrayList<>(List.of(get));
    }

    /**
     * Update the links of all prediction models.
     */
    @Override
    public void updateLinks() {
        for (AbstractPredictionModel model: getList()) {
            model.updateLinks();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< PredictionModelList >>\n");
        builder.append("Models: " + models);

        return builder.toString();
    }
    
}
