package com.vs.foosh.api.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.ApplicationConfig;
import com.vs.foosh.api.exceptions.misc.CouldNotDeleteCollectionException;
import com.vs.foosh.api.exceptions.misc.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.misc.SavingDirectoryException;
import com.vs.foosh.api.exceptions.misc.SavingToFileIOException;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.services.helpers.ListService;

/**
 * A {@link Service} that provides functionalities for saving and loading {@link DeviceList}, {@link VariableList}, and {@link PredictionModelList} to and from local persistent storage.
 */
@Service
public class PersistentDataService {

    /**
     * Setup service by creating directory.
     */
    public static void setup() {
        try {
            Files.createDirectories(ApplicationConfig.getSaveDirPath());
        } catch (IOException e) {
            throw new SavingDirectoryException(ApplicationConfig.getSaveDirPath());
        }
    }

    /**
     * Save all lists to local persistent storage.
     */
    public static void saveAll() {
        saveDeviceList();
        saveVariableList();
        savePredictionModelList();
    }

    /**
     * Delete all save files.
     */
    public static void deleteAll() {
        deleteDeviceListSave();
        deleteVariableListSave();
        deletePredictionModelListSave();
    }
    
    /**
     * Save {@link DeviceList} to local persistent storage.
     */
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

    /**
     * Try to read {@link DeviceList} save file.
     * 
     * @return {@link ReadSaveFileResult} with the save file contents (if reading was successful).
     */
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

    /**
     * Delete {@link DeviceList} save file and create a backup file for instead.
     */
    public static void deleteDeviceListSave() {
        if (Files.exists(ApplicationConfig.getDeviceSavePath())) {
            try {
                Files.copy(ApplicationConfig.getDeviceSavePath(), ApplicationConfig.getDeleteDevicePath(), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(ApplicationConfig.getDeviceSavePath());
            } catch (IOException e) {
                throw new CouldNotDeleteCollectionException();
            }
        }
    }

    /**
     * Save {@link VariableList} to local persistent storage.
     */
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

    /**
     * Try to read {@link VariableList} save file.
     * 
     * @return {@link ReadSaveFileResult} with the save file contents (if reading was successful).
     */
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

    /**
     * Delete {@link VariableList} save file and create a backup file for instead.
     */
    public static void deleteVariableListSave() {
        if (Files.exists(ApplicationConfig.getDeleteVariablePath())) {
            try {
                Files.copy(ApplicationConfig.getVariableSavePath(), ApplicationConfig.getDeleteVariablePath(), StandardCopyOption.REPLACE_EXISTING); //todo
                Files.delete(ApplicationConfig.getVariableSavePath());
            } catch (IOException e) {
                throw new CouldNotDeleteCollectionException();
            }
        }
    }

    /**
     * Save {@link PredictionModelList} to local persistent storage.
     */
    public static void savePredictionModelList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getPredictionModelSavePath().toFile()))) {
            oos.writeObject(ListService.getPredictionModelList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new SaveFileNotFoundException("predictionModels");
        } catch (IOException e) {
            throw new SavingToFileIOException("predictionModels");
        }
    } 

    /**
     * Try to read {@link PredictionModelList} save file.
     * 
     * @return {@link ReadSaveFileResult} with the save file contents (if reading was successful).
     */
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

    /**
     * Delete {@link PredictionModelList} save file and create a backup file for instead.
     */
    public static void deletePredictionModelListSave() {
        if (Files.exists(ApplicationConfig.getPredictionModelSavePath())) {
            try {
                Files.copy(ApplicationConfig.getPredictionModelSavePath(), ApplicationConfig.getDeletePredictionModelPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(ApplicationConfig.getPredictionModelSavePath());
            } catch (IOException e) {
                throw new CouldNotDeleteCollectionException();
            }
        }
    }

}