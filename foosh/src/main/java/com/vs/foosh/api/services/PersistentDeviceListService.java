package com.vs.foosh.api.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.vs.foosh.api.exceptions.SaveFileNotFoundException;
import com.vs.foosh.api.exceptions.SavingToFileIOException;
import com.vs.foosh.api.model.AbstractDevice;
import com.vs.foosh.api.model.DeviceList;
import com.vs.foosh.api.model.ReadSaveFileResult;

public class PersistentDeviceListService {
    public static void saveDeviceList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ApplicationConfig.getSaveFilePath().toFile()))) {
            oos.writeObject(DeviceList.getInstance());
        } catch (FileNotFoundException e) {
            throw new SaveFileNotFoundException();
        } catch (IOException e) {
            System.err.println(e);
            throw new SavingToFileIOException();
        }
    } 

    public static ReadSaveFileResult hasSavedDeviceList() {
        ReadSaveFileResult result = new ReadSaveFileResult();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ApplicationConfig.getSaveFilePath().toFile()))) {
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
}