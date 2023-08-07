package com.vs.foosh.api.model.device;

import java.util.UUID;

public record PatchDeviceNameValidationData(String identifier, UUID uuid, String name) {}