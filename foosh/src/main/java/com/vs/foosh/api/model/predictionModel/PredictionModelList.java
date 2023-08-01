package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.exceptions.variable.VariableNotFoundException;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setList'");
    }

    @Override
    public List<AbstractPredictionModel> getList() {
        return this.models;
    }

    @Override
    public List<Thing> getAsThings() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsThings'");
    }

    @Override
    public void clearList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearList'");
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

        throw new VariableNotFoundException(identifier);
    }

    @Override
    public void addThing(AbstractPredictionModel thing) {
        if (!models.contains(thing)) {
            models.add(thing);
        }
    }

    @Override
    public void deleteThing(String identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteThing'");
    }

    @Override
    public boolean isUniqueName(String name, UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUniqueName'");
    }

    @Override
    public void checkIfIdIsPresent(String identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkIfIdIsPresent'");
    }

    @Override
    public List<LinkEntry> getLinks(String label) {
        LinkEntry get = new LinkEntry(label, LinkBuilder.getPredictionModelListLink(), HttpAction.GET, List.of());

        return new ArrayList<>(List.of(get));
    }

    @Override
    public void updateLinks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLinks'");
    }
    
}
