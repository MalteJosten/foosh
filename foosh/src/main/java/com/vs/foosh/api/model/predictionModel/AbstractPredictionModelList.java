package com.vs.foosh.api.model.predictionModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.vs.foosh.api.model.misc.IThingList;
import com.vs.foosh.api.model.misc.Thing;
import com.vs.foosh.api.model.web.LinkEntry;

public class AbstractPredictionModelList implements Serializable, IThingList<AbstractPredictionModel, AbstractPredictionModelDisplayRepresentation>{

    @Override
    public void setList(List<AbstractPredictionModel> thingList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setList'");
    }

    @Override
    public List<AbstractPredictionModel> getList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getList'");
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
    public List<AbstractPredictionModelDisplayRepresentation> getDisplayListRepresentation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDisplayListRepresentation'");
    }

    @Override
    public AbstractPredictionModel getThing(String identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThing'");
    }

    @Override
    public void addThing(AbstractPredictionModel thing) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addThing'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLinks'");
    }

    @Override
    public void updateLinks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLinks'");
    }
    
}
