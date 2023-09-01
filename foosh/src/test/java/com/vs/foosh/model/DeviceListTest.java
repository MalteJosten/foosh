package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.model.device.DeviceNamePatchRequest;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.api.services.helpers.ListService;
import com.vs.foosh.helper.AbstractDeviceMock;

public class DeviceListTest {

    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
    }

    ///
    /// isValidName()
    ///

    @Test
    void givenDevicesListWithOneDevice_whenIsValidWithRandomString_getTrue() {
        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        assertEquals(true, ListService.getDeviceList().isValidName("test-other", device.getId()));
    }

    @Test
    void givenDevicesListWithOneDevice_whenIsValidWithUuid_getFalse() {
        AbstractDeviceMock device = new AbstractDeviceMock("test-device");
        ListService.getDeviceList().addThing(device);

        assertEquals(false, ListService.getDeviceList().isValidName(UUID.randomUUID().toString(), device.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithRandomString_getTrue() {
        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        AbstractDeviceMock device2 = new AbstractDeviceMock("test-device2");
        ListService.getDeviceList().addThing(device1);
        ListService.getDeviceList().addThing(device2);

        assertEquals(true, ListService.getDeviceList().isValidName("test-other", device2.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithUuid_getFalse() {
        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        AbstractDeviceMock device2 = new AbstractDeviceMock("test-device2");
        ListService.getDeviceList().addThing(device1);
        ListService.getDeviceList().addThing(device2);

        assertEquals(false, ListService.getDeviceList().isValidName(UUID.randomUUID().toString(), device2.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithOwnDuplicate_getTrue() {
        AbstractDeviceMock device1 = new AbstractDeviceMock("test-device1");
        AbstractDeviceMock device2 = new AbstractDeviceMock("test-device2");
        ListService.getDeviceList().addThing(device1);
        ListService.getDeviceList().addThing(device2);

        assertEquals(true, ListService.getDeviceList().isValidName("test-device2", device2.getId()));
    }

    ///
    /// findUniqueName()
    ///

    @Test
    void givenDevicesListWithOneDevice_whenFindUniqueNameWithSameName_getSameString() {
        String sameName = "test-device";

        AbstractDeviceMock device = new AbstractDeviceMock(sameName);
        ListService.getDeviceList().addThing(device);

        assertEquals(sameName, ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), sameName)));
    }

    @Test
    void givenDevicesListWithOneDevice_whenFindUniqueNameWithValidName_getSameString() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceMock device = new AbstractDeviceMock(oldName);
        ListService.getDeviceList().addThing(device);

        assertEquals(newName, ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName)));
    }

    @Test
    void givenDevicesListWithMultipleDevices_whenFindUniqueNameWithDuplicateNameNoTimeout_getIncrementedString() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceMock device   = new AbstractDeviceMock(oldName);
        AbstractDeviceMock device1  = new AbstractDeviceMock(newName);
        AbstractDeviceMock device2  = new AbstractDeviceMock(newName + "1");
        AbstractDeviceMock device3  = new AbstractDeviceMock(newName + "2");
        AbstractDeviceMock device4  = new AbstractDeviceMock(newName + "3");
        AbstractDeviceMock device5  = new AbstractDeviceMock(newName + "4");
        AbstractDeviceMock device6  = new AbstractDeviceMock(newName + "5");
        AbstractDeviceMock device7  = new AbstractDeviceMock(newName + "6");
        AbstractDeviceMock device8  = new AbstractDeviceMock(newName + "7");
        AbstractDeviceMock device9  = new AbstractDeviceMock(newName + "8");
        ListService.getDeviceList().setList(List.of(device, device1, device2, device3, device4, device5, device6, device7, device8, device9));

        assertEquals(newName + "9", ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName)));
    }

    @Test
    void givenDevicesListWithMultipleDevices_whenFindUniqueNameWithDuplicateNameWithTimeout_getCouldNotFindUniqueDeviceNameException() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceMock device    = new AbstractDeviceMock(oldName);
        AbstractDeviceMock device1   = new AbstractDeviceMock(newName);
        AbstractDeviceMock device2   = new AbstractDeviceMock(newName + "1");
        AbstractDeviceMock device3   = new AbstractDeviceMock(newName + "2");
        AbstractDeviceMock device4   = new AbstractDeviceMock(newName + "3");
        AbstractDeviceMock device5   = new AbstractDeviceMock(newName + "4");
        AbstractDeviceMock device6   = new AbstractDeviceMock(newName + "5");
        AbstractDeviceMock device7   = new AbstractDeviceMock(newName + "6");
        AbstractDeviceMock device8   = new AbstractDeviceMock(newName + "7");
        AbstractDeviceMock device9   = new AbstractDeviceMock(newName + "8");
        AbstractDeviceMock device10  = new AbstractDeviceMock(newName + "9");
        ListService.getDeviceList().setList(List.of(device, device1, device2, device3, device4, device5, device6, device7, device8, device9, device10));

        assertThrows(CouldNotFindUniqueDeviceNameException.class, () -> {
            ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName));
        });
    }


}
