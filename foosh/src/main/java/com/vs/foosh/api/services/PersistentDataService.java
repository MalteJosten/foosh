package com.vs.foosh.api.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.model.device.AbstractDevice;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.variable.Variable;
import com.vs.foosh.api.model.variable.VariableList;

public class PersistentDataService {
    public static void saveDeviceList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getDeviceSavePath().toFile()))) {
            oos.writeObject(DeviceList.getInstance());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException("devices");
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException("devices");
        }
    } 

    @SuppressWarnings("unchecked")
    public static ReadSaveFileResult<AbstractDevice> hasSavedDeviceList() {
        ReadSaveFileResult<AbstractDevice> result = new ReadSaveFileResult<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getDeviceSavePath().toFile()))) {
            List<AbstractDevice> list = (List<AbstractDevice>) ois.readObject();
            result.setData(list);
            result.setSuccess(true);
        } catch (IOException e) {
            result.setSuccess(false);
        } catch (ClassNotFoundException e) {
            result.setSuccess(false);
        } catch (ClassCastException e) {
            result.setSuccess(false);
        }

        return result;
    }

    public static void deleteDeviceListSave() {
        try {
            Files.copy(ApplicationConfig.getDeviceSavePath(), ApplicationConfig.getDeleteDevicePath(), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(ApplicationConfig.getDeviceSavePath());
        } catch (IOException e) {
            throw new CouldNotDeleteCollectionException();
        }
    }

    public static void saveVariableList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getVariableSavePath().toFile()))) {
            oos.writeObject(VariableList.getInstance());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException("variables");
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException("variables");
        }
    } 

    @SuppressWarnings("unchecked")
    public static ReadSaveFileResult<Variable> hasSavedVariableList() {
        ReadSaveFileResult<Variable> result = new ReadSaveFileResult<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getVariableSavePath().toFile()))) {
            List<Variable> list = (List<Variable>) ois.readObject();
            result.setData(list);
            result.setSuccess(true);
        } catch (IOException e) {
            result.setSuccess(false);
        } catch (ClassNotFoundException e) {
            result.setSuccess(false);
        } catch (ClassCastException e) {
            result.setSuccess(false);
        }

        return result;
    }

    public static void deleteVariableListSave() {
        try {
            Files.copy(ApplicationConfig.getVariableSavePath(), ApplicationConfig.getDeleteVariablePath(), StandardCopyOption.REPLACE_EXISTING); //todo
            Files.delete(ApplicationConfig.getVariableSavePath());
        } catch (IOException e) {
            throw new CouldNotDeleteCollectionException();
        }
    }
}