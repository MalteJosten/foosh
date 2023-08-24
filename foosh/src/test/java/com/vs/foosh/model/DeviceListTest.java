package com.vs.foosh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vs.foosh.AbstractDeviceTest;
import com.vs.foosh.PredictionModelTest;
import com.vs.foosh.api.exceptions.device.CouldNotFindUniqueDeviceNameException;
import com.vs.foosh.api.model.device.DeviceNamePatchRequest;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;

public class DeviceListTest {

    @BeforeEach
    void setup() {
        ListService.getPredictionModelList().getList().clear();
        ListService.getDeviceList().getList().clear();
        ListService.getVariableList().getList().clear();
        PersistentDataService.deleteAll();
        ListService.getPredictionModelList().addThing(new PredictionModelTest());
    }

    ///
    /// isValidName()
    ///

    @Test
    void givenDevicesListWithOneDevice_whenIsValidWithRandomString_getTrue() {
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        assertEquals(true, ListService.getDeviceList().isValidName("test-other", device.getId()));
    }

    @Test
    void givenDevicesListWithOneDevice_whenIsValidWithUuid_getFalse() {
        AbstractDeviceTest device = new AbstractDeviceTest("test-device");
        ListService.getDeviceList().addThing(device);

        assertEquals(false, ListService.getDeviceList().isValidName(UUID.randomUUID().toString(), device.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithRandomString_getTrue() {
        AbstractDeviceTest device1 = new AbstractDeviceTest("test-device1");
        AbstractDeviceTest device2 = new AbstractDeviceTest("test-device2");
        ListService.getDeviceList().addThing(device1);
        ListService.getDeviceList().addThing(device2);

        assertEquals(true, ListService.getDeviceList().isValidName("test-other", device2.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithUuid_getFalse() {
        AbstractDeviceTest device1 = new AbstractDeviceTest("test-device1");
        AbstractDeviceTest device2 = new AbstractDeviceTest("test-device2");
        ListService.getDeviceList().addThing(device1);
        ListService.getDeviceList().addThing(device2);

        assertEquals(false, ListService.getDeviceList().isValidName(UUID.randomUUID().toString(), device2.getId()));
    }

    @Test
    void givenNonEmptyDevicesList_whenIsValidWithOwnDuplicate_getTrue() {
        AbstractDeviceTest device1 = new AbstractDeviceTest("test-device1");
        AbstractDeviceTest device2 = new AbstractDeviceTest("test-device2");
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

        AbstractDeviceTest device = new AbstractDeviceTest(sameName);
        ListService.getDeviceList().addThing(device);

        assertEquals(sameName, ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), sameName)));
    }

    @Test
    void givenDevicesListWithOneDevice_whenFindUniqueNameWithValidName_getSameString() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceTest device = new AbstractDeviceTest(oldName);
        ListService.getDeviceList().addThing(device);

        assertEquals(newName, ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName)));
    }

    @Test
    void givenDevicesListWithMultipleDevices_whenFindUniqueNameWithDuplicateNameNoTimeout_getIncrementedString() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceTest device   = new AbstractDeviceTest(oldName);
        AbstractDeviceTest device1  = new AbstractDeviceTest(newName);
        AbstractDeviceTest device2  = new AbstractDeviceTest(newName + "1");
        AbstractDeviceTest device3  = new AbstractDeviceTest(newName + "2");
        AbstractDeviceTest device4  = new AbstractDeviceTest(newName + "3");
        AbstractDeviceTest device5  = new AbstractDeviceTest(newName + "4");
        AbstractDeviceTest device6  = new AbstractDeviceTest(newName + "5");
        AbstractDeviceTest device7  = new AbstractDeviceTest(newName + "6");
        AbstractDeviceTest device8  = new AbstractDeviceTest(newName + "7");
        AbstractDeviceTest device9  = new AbstractDeviceTest(newName + "8");
        ListService.getDeviceList().setList(List.of(device, device1, device2, device3, device4, device5, device6, device7, device8, device9));

        assertEquals(newName + "9", ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName)));
    }

    @Test
    void givenDevicesListWithMultipleDevices_whenFindUniqueNameWithDuplicateNameWithTimeout_getCouldNotFindUniqueDeviceNameException() {
        String oldName = "test-device";
        String newName = "test-other";

        AbstractDeviceTest device    = new AbstractDeviceTest(oldName);
        AbstractDeviceTest device1   = new AbstractDeviceTest(newName);
        AbstractDeviceTest device2   = new AbstractDeviceTest(newName + "1");
        AbstractDeviceTest device3   = new AbstractDeviceTest(newName + "2");
        AbstractDeviceTest device4   = new AbstractDeviceTest(newName + "3");
        AbstractDeviceTest device5   = new AbstractDeviceTest(newName + "4");
        AbstractDeviceTest device6   = new AbstractDeviceTest(newName + "5");
        AbstractDeviceTest device7   = new AbstractDeviceTest(newName + "6");
        AbstractDeviceTest device8   = new AbstractDeviceTest(newName + "7");
        AbstractDeviceTest device9   = new AbstractDeviceTest(newName + "8");
        AbstractDeviceTest device10  = new AbstractDeviceTest(newName + "9");
        ListService.getDeviceList().setList(List.of(device, device1, device2, device3, device4, device5, device6, device7, device8, device9, device10));

        assertThrows(CouldNotFindUniqueDeviceNameException.class, () -> {
            ListService.getDeviceList().findUniqueName(new DeviceNamePatchRequest(device.getId(), newName));
        });
    }


}
