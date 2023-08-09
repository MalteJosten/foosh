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
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPatchRequest;
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

    // TODO: Also allow patching of mappings?
    public static ResponseEntity<Object> patchModel(String id, List<Map<String, Object>> patchMappings) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments)) {
                throw new FooSHJsonPatchIllegalArgumentException(model.getId().toString(), "You can only edit the field 'name'!");
            }

            patchModelName(id, (String) patch.getValue());
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

    public static ResponseEntity<Object> patchMappings(String id, List<Map<String, Object>> patchRequest) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchRequest) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.setParentId(id);

            patch.validateRequest(List.of(FooSHPatchOperation.ADD, FooSHPatchOperation.REPLACE, FooSHPatchOperation.REMOVE));
            
            switch (patch.getOperation()) {
                case ADD:
                    patch.validateAdd(PredictionModelMappingPatchRequest.class);
                    break;
                case REPLACE:
                    patch.validateReplace(PredictionModelMappingPatchRequest.class);
                    break;
                case REMOVE:
                    patch.validateRemove();
                    break;
                default:
                    break;
            }

            patches.add(patch);
        }

        for (FooSHJsonPatch patch: patches) {
            List<String> allowedPathSegments = List.of("/", "uuid");
            if(!patch.isValidPath(allowedPathSegments)) {
                throw new FooSHJsonPatchIllegalArgumentException(model.getId().toString(), "You can only edit the entire collection (using '/') or an individual mapping entry (using '/{uuid}')!");
            }

            if (patch.getPath().equals("/")) {
                patchAllMappings(id, patch);
            } else {
                patchMappingEntry(id, patch);
            }
        }

        return respondWithModel(id);
    }

    @SuppressWarnings("unchecked")
    private static void patchMappingEntry(String modelId, FooSHJsonPatch patch) {
        switch (patch.getOperation()) {
            case ADD:
                List<ParameterMapping> parameterMappings = new ArrayList<>();
                for (PredictionModelMappingPatchRequest patchRequest: (List<PredictionModelMappingPatchRequest>) patch.getValue()) {
                    parameterMappings.add(new ParameterMapping(patchRequest.getParameter(), patchRequest.getDeviceId().toString()));
                }

                PredictionModelMappingPostRequest postRequest = new PredictionModelMappingPostRequest(UUID.fromString(patch.getDestination()), parameterMappings);

                postMappings(modelId, postRequest);
                break;
            default:
                break;
        }
    }

    // TODO: Implement
    private static void patchAllMappings(String modelId, FooSHJsonPatch patch) {

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
