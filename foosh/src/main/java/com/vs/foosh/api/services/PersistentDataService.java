package com.vs.foosh.api.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;

public class PersistentDataService {
    public static void saveAll() {
        saveDeviceList();
        saveVariableList();
        savePredictionModelList();
    }
    
    public static void saveDeviceList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getDeviceSavePath().toFile()))) {
            oos.writeObject(ListService.getDeviceList());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException("devices");
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException("devices");
        }
    } 

    public static ReadSaveFileResult<DeviceList> hasSavedDeviceList() {
        ReadSaveFileResult<DeviceList> result = new ReadSaveFileResult<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getDeviceSavePath().toFile()))) {
            DeviceList list = (DeviceList) ois.readObject();
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
            oos.writeObject(ListService.getVariableList());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException("variables");
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException("variables");
        }
    } 

    public static ReadSaveFileResult<VariableList> hasSavedVariableList() {
        ReadSaveFileResult<VariableList> result = new ReadSaveFileResult<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getVariableSavePath().toFile()))) {
            VariableList list = (VariableList) ois.readObject();
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

    public static void savePredictionModelList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getPredictionModelSavePath().toFile()))) {
            oos.writeObject(ListService.getPredictionModelList());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException("predictionModels");
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException("predictionModels");
        }
    } 

    public static ReadSaveFileResult<PredictionModelList> hasSavedPredictionModelList() {
        ReadSaveFileResult<PredictionModelList> result = new ReadSaveFileResult<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getPredictionModelSavePath().toFile()))) {
            PredictionModelList list = (PredictionModelList) ois.readObject();
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

}