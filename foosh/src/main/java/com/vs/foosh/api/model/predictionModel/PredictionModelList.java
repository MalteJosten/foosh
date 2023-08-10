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
import com.vs.foosh.api.services.LinkBuilder;
import com.vs.foosh.api.services.ListService;

public class PredictionModelList implements Serializable, IThingList<AbstractPredictionModel, PredictionModelDisplayRepresentation>{
    private List<AbstractPredictionModel> models = new ArrayList<>();

    @Override
    public void setList(List<AbstractPredictionModel> thingList) {
        List.copyOf(thingList);
        throw new UnsupportedOperationException("Unimplemented method 'setList'");
    }

    @Override
    public List<AbstractPredictionModel> getList() {
        return this.models;
    }

    @Override
    public List<Thing> getAsThings() {
        List<Thing> things = new ArrayList<>();
        for (AbstractPredictionModel model: models) {
            things.add(model);
        }

        return things;
    }

    @Override
    public void clearList() {
        models.clear();
    }

    @Override
    public List<PredictionModelDisplayRepresentation> getDisplayListRepresentation() {
        List<PredictionModelDisplayRepresentation> displayRepresentations = new ArrayList<>();
        for (AbstractPredictionModel model: ListService.getPredictionModelList().getList()) {
            displayRepresentations.add(new PredictionModelDisplayRepresentation(model));
        }

        return displayRepresentations;
    }

    @Override
    public AbstractPredictionModel getThing(String identifier) {
        for (AbstractPredictionModel model: getList()) {
            if (model.getId().toString().equals(identifier) || model.getName().toLowerCase().equals(identifier.toLowerCase().replace("%20", " "))) {
                return model;
            }
        }

        throw new PredictionModelNotFoundException(identifier);
    }

    @Override
    public void addThing(AbstractPredictionModel thing) {
        if (!models.contains(thing)) {
            models.add(thing);
        }
    }

    @Override
    public void deleteThing(String identifier) {
        return;
    }

    @Override
    public boolean isUniqueName(String name, UUID id) {
        try {
            // Check whether the provided 'name' could be an UUID.
            // Names in form of an UUID are disallowed.
            UUID.fromString(name);
            throw new PredictionModelNameMustNotBeAnUuidException(id);
        } catch (IllegalArgumentException e) {
            for (AbstractPredictionModel model : this.models) {
                // Check whether the name is already used
                if (model.getName().equalsIgnoreCase(name)) {
                    // If it's already used, check whether it's the same variable.
                    if (model.getId().equals(id)) {
                        return true;
                    }

                    throw new PredictionModelNameIsNotUniqueException(id, name);
                }

            }
        }

        return true;
    }

    @Override
    public void checkIfIdIsPresent(String identifier) {
        for (AbstractPredictionModel model: getList()) {
            if (model.getId().toString().equals(identifier) || model.getName().equals(identifier.toLowerCase())) {
                return;
            }
        }

        throw new PredictionModelNotFoundException(identifier);
    }

    @Override
    public List<LinkEntry> getLinks(String label) {
        LinkEntry get   = new LinkEntry(label, LinkBuilder.getPredictionModelListLink(), HttpAction.GET, List.of());
        LinkEntry patch = new LinkEntry(label, LinkBuilder.getPredictionModelListLink(), HttpAction.PATCH, List.of("application/json"));

        return new ArrayList<>(List.of(get, patch));
    }

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
