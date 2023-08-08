package com.vs.foosh.api.services;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalArgumentException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.predictionModel.VariableParameterMapping;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;

public class PredictionModelService {
    public static ResponseEntity<Object> getModels() {
        AbstractMap.SimpleEntry<String, Object> result = new AbstractMap.SimpleEntry<String, Object>("predictionModels", ListService.getPredictionModelList().getDisplayListRepresentation());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(result.getKey(), result.getValue());
        responseBody.put("_links", ListService.getPredictionModelList().getLinks("self"));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public static ResponseEntity<Object> getModel(String id) {
        return respondWithModel(id);
    }

    public static ResponseEntity<Object> patchModel(String id, List<Map<String, String>> patchMappings) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, String> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.ADD));

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments)) {
                throw new FooSHJsonPatchIllegalArgumentException(model.getId().toString(), "You can only edit the field 'name'!");
            }

            patchModelName(id, patch.getValue());
        }

        PersistentDataService.savePredictionModelList();
        
        return respondWithModel(id);
    }

    private static boolean patchModelName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        if (patchName == null) {
            throw new FooSHJsonPatchValueIsNullException(uuid);
        }

        if (patchName.trim().isEmpty() || patchName.equals("")) {
            throw new FooSHJsonPatchValueIsEmptyException(uuid.toString());
        }

        if (ListService.getPredictionModelList().isUniqueName(patchName, uuid)) {
            ListService.getPredictionModelList().getThing(id).setName(patchName);
            return true;
        }
        return false;
    }

    private static ResponseEntity<Object> respondWithModel(String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("predictionModel", model.getDisplayRepresentation().getModel());
        responseBody.put("_links", model.getAllLinks());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public static ResponseEntity<Object> getMappings(String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<VariableParameterMapping> mappings = model.getAllMappings();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(model.getVariableLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mappings", mappings);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public static ResponseEntity<Object> postMappings(String id, PredictionModelMappingPostRequest request) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        request.validate(id, ListService.getVariableList().getThing(request.getVariableId().toString()).getDeviceIds());

        model.setMapping(request.getVariableId(), request.getMappings()); 
        model.updateLinks();

        PersistentDataService.saveAll();

        VariableParameterMapping mapping = model.getParameterMapping(request.getVariableId());

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(ListService.getVariableList().getThing(request.getVariableId().toString()).getSelfStaticLinks("variable"));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mapping", mapping);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public static ResponseEntity<Object> deleteMappings(String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        model.deleteMapping();

        PersistentDataService.savePredictionModelList();

        List<VariableParameterMapping> mappings = model.getAllMappings();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getDeviceLinks());
        links.addAll(model.getVariableLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mappings", mappings);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
