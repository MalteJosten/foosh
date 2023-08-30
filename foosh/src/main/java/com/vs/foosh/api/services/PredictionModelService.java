package com.vs.foosh.api.services;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchIllegalPathException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchOperationException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsEmptyException;
import com.vs.foosh.api.exceptions.FooSHJsonPatch.FooSHJsonPatchValueIsNullException;
import com.vs.foosh.api.exceptions.predictionModel.PredictionModelPostMappingException;
import com.vs.foosh.api.model.misc.ThingType;
import com.vs.foosh.api.model.predictionModel.AbstractPredictionModel;
import com.vs.foosh.api.model.predictionModel.ParameterMapping;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPatchRequest;
import com.vs.foosh.api.model.predictionModel.PredictionModelMappingPostRequest;
import com.vs.foosh.api.model.predictionModel.VariableParameterMapping;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.web.FooSHJsonPatch;
import com.vs.foosh.api.model.web.FooSHPatchOperation;
import com.vs.foosh.api.model.web.LinkEntry;
import com.vs.foosh.api.services.helpers.IdService;
import com.vs.foosh.api.services.helpers.ListService;

@Service
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

    public static ResponseEntity<Object> patchModel(String id, List<Map<String, Object>> patchMappings) {
        ListService.getPredictionModelList().checkIfIdIsPresent(id);

        List<FooSHJsonPatch> patches = new ArrayList<>();
        for (Map<String, Object> patchMapping: patchMappings) {
            FooSHJsonPatch patch = new FooSHJsonPatch(patchMapping);
            patch.validateRequest(List.of(FooSHPatchOperation.REPLACE));
            patch.validateReplace(String.class);

            patches.add(patch);
        }    

        for (FooSHJsonPatch patch: patches) {
            List<String> pathSegments = List.of("/name");
            if (!patch.isValidPath(pathSegments, ThingType.PREDICTION_MODEL)) {
                throw new FooSHJsonPatchIllegalPathException("You can only edit the field 'name'!");
            }

            patchModelName(id, (String) patch.getValue());
        }

        PersistentDataService.savePredictionModelList();
        
        return respondWithModel(id);
    }

    private static boolean patchModelName(String id, String patchName) {
        UUID uuid = IdService.isUuid(id).get();

        if (patchName == null) {
            throw new FooSHJsonPatchValueIsNullException();
        }

        if (patchName.trim().isEmpty() || patchName.equals("")) {
            throw new FooSHJsonPatchValueIsEmptyException();
        }

        if (ListService.getPredictionModelList().isValidName(patchName, uuid)) {
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
        return respondWithMappings(id, HttpStatus.OK);
    }

    public static ResponseEntity<Object> postMappings(String id, PredictionModelMappingPostRequest request) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        setMappings(model, request);

        return respondWithMappings(id, HttpStatus.OK);
    }

    public static ResponseEntity<Object> patchMappings(String id, List<Map<String, Object>> patchMappings) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<FooSHJsonPatch> patches = convertPatchMappings(id, patchMappings);

        for (FooSHJsonPatch patch: patches) {
            checkForCorrectPatchPath(model, patch);
        }

        for (FooSHJsonPatch patch: patches) {
            patchMappingEntry(model, patch);
        }

        model.updateLinks();

        return respondWithMappings(id, HttpStatus.OK);
    }

    private static List<FooSHJsonPatch> convertPatchMappings(String id, List<Map<String, Object>> patchMappings) {
        List<FooSHJsonPatch> patches = new ArrayList<>();

        for (Map<String, Object> patchMapping: patchMappings) {
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
        
        return patches;
    }

    private static void checkForCorrectPatchPath(AbstractPredictionModel model, FooSHJsonPatch patch) {
        List<String> allowedPathSegments = List.of("uuid");
        if (!patch.isValidPath(allowedPathSegments, ThingType.PREDICTION_MODEL)) {
            throw new FooSHJsonPatchIllegalPathException("You can only edit an individual mapping entry (using '/{uuid}')!");
        }

        if (patch.getOperation() == FooSHPatchOperation.REPLACE) {
            Variable variable = ListService.getVariableList().getThing(patch.getDestination());

            if (!model.getVariableIds().contains(variable.getId())) {
                throw new FooSHJsonPatchOperationException(
                        model.getId(),
                        model.getMappingLinks(),
                        "You can only replace mappings which exist. Use the operation 'add' to add new mappings.",
                        HttpStatus.NOT_FOUND);

            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void patchMappingEntry(AbstractPredictionModel model, FooSHJsonPatch patch) {
        List<ParameterMapping> parameterMappings = new ArrayList<>();

        if (patch.getOperation() == FooSHPatchOperation.ADD || patch.getOperation() == FooSHPatchOperation.REPLACE) {
            for (PredictionModelMappingPatchRequest patchRequest: (List<PredictionModelMappingPatchRequest>) patch.getValue()) {
                parameterMappings.add(new ParameterMapping(patchRequest.getParameter(), patchRequest.getDeviceId().toString()));
            }
        }

        PredictionModelMappingPostRequest postRequest = new PredictionModelMappingPostRequest(UUID.fromString(patch.getDestination()), parameterMappings);

        switch (patch.getOperation()) {
            case ADD:
                setMappings(model, postRequest);
                break;
            case REPLACE:
                replaceMappings(model, postRequest);
                break;
            case REMOVE:
                deleteMapping(model, postRequest.getVariableId());
                break;
            default:
                break;
        }
    }

    private static void setMappings(AbstractPredictionModel model, PredictionModelMappingPostRequest request) {
        if (request.getVariableId() == null) {
            throw new PredictionModelPostMappingException(model.getId().toString(), "Please provide a field 'variableId' with a Variable ID!");
        }

        if (request.getMappings() == null) {
            throw new PredictionModelPostMappingException(model.getId().toString(), "Please provide a field 'mappings' with a list of parameter mappings!");
        }

        request.validate(model.getId().toString(), ListService.getVariableList().getThing(request.getVariableId().toString()).getDeviceIds());

        model.addMapping(request.getVariableId(), request.getMappings()); 
        model.updateLinks();

        PersistentDataService.saveAll();
    }

    private static void replaceMappings(AbstractPredictionModel model, PredictionModelMappingPostRequest request) {
        request.validate(model.getId().toString(), ListService.getVariableList().getThing(request.getVariableId().toString()).getDeviceIds());

        model.setMapping(request.getVariableId(), request.getMappings()); 
        model.updateLinks();

        PersistentDataService.saveAll();
    }

    public static void deleteMapping(AbstractPredictionModel model, UUID variableId) {
        model.deleteMapping(variableId);

        PersistentDataService.savePredictionModelList();
    }

    public static ResponseEntity<Object> deleteMappings(String id) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        model.deleteMappings();

        PersistentDataService.savePredictionModelList();

        return respondWithMappings(id, HttpStatus.OK);
    }

    private static ResponseEntity<Object> respondWithMappings(String id, HttpStatus status) {
        AbstractPredictionModel model = ListService.getPredictionModelList().getThing(id);

        List<VariableParameterMapping> mappings = model.getAllMappings();

        List<LinkEntry> links = new ArrayList<>();
        links.addAll(model.getSelfLinks());
        links.addAll(model.getMappingLinks());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("mappings", mappings);
        responseBody.put("_links", links);

        return new ResponseEntity<>(responseBody, status);
    }

}
