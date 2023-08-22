package com.vs.foosh.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.predictionModel.PredictionModelList;
import com.vs.foosh.api.model.variable.VariableList;
import com.vs.foosh.api.model.web.SmartHomeCredentials;
import com.vs.foosh.api.services.LinkBuilderService;
import com.vs.foosh.api.services.ListService;
import com.vs.foosh.api.services.PersistentDataService;
import com.vs.foosh.custom.MyPredictionModel;

@Configuration
public class ApplicationConfig {
    private static final String HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static int port;
    private static Path SAVE_DIR_PATH = Paths.get(System.getProperty("user.home") + File.separator + "foosh").toAbsolutePath();

    private static SmartHomeCredentials smartHomeCredentials;
    
    @Bean
    private static void setup() {
        readInApplicationProperties();
        PersistentDataService.setup();
        tryToLoadSaveFiles();
    }

    public static void setSmartHomeCredentials(SmartHomeCredentials credentials) {
        smartHomeCredentials = credentials;

        if (smartHomeCredentials.getUri() == null || smartHomeCredentials.getUri().equals("")) {
            System.err.println(
                    "[ERROR] Field 'uri' of the provided SmartHomeCredentials is either empty or non-existent! "
                            + "This might become a problem when trying to communicate with the SmartHome API.\n"
                            + "[ADVICE] Please set a correct path and restart the server!");
        } else {
            System.out.println("[INFO] Successfully set smart home credentials.");
        }
    }

    private static void readInApplicationProperties() {
        Properties config = new Properties();
        try (InputStream is = new FileInputStream(new File("src/main/resources/application.properties"))) {
            config.load(is);

            String serverPort = config.getProperty("server.port");
            setupServerPort(serverPort);
        } catch (IOException e) {
            port = DEFAULT_PORT;
            System.out.println(
                    "[INFO] Encountered a problem reading application.properties!\n"
                            + "[INFO] Using default port: "
                            + DEFAULT_PORT);
        } finally {
            LinkBuilderService.setServerVariables(HOST, port);
        }
    }

    private static void setupServerPort(String serverPort) {
        if (serverPort == null || serverPort.equals("")) {
            port = DEFAULT_PORT;
            System.out.println(
                    "[INFO] Field 'server.port' (" + serverPort
                            + ") in application.properties is either empty or non-existent!\n"
                            + "[INFO] Using default port: "
                            + DEFAULT_PORT);
            return;
        }

        try {
            port = Integer.parseInt(serverPort);
            System.out.println(
                    "[INFO] Successfully read application.properties!\n"
                            + "[INFO] Using port: " + port);
        } catch (NumberFormatException e) {
            port = DEFAULT_PORT;
            System.out.println(
                    "[INFO] Field 'server.port' (" + serverPort
                            + ") in application.properties cannot be converted to an Integer!\n"
                            + "[INFO] Using default port: "
                            + DEFAULT_PORT);
        } finally {
            LinkBuilderService.setServerVariables(HOST, port);
        }
    }

    public static void tryToLoadSaveFiles() {
        ReadSaveFileResult<DeviceList> devicesResult = PersistentDataService.hasSavedDeviceList();
        if (devicesResult.getSuccess()) {
            ListService.setDeviceList(devicesResult.getData());
            System.out.println("[INFO] Found and loaded devices save file.");
        }

        ReadSaveFileResult<VariableList> variablesResult = PersistentDataService.hasSavedVariableList();
        if (variablesResult.getSuccess()) {
            ListService.setVariableList(variablesResult.getData());
            System.out.println("[INFO] Found and loaded variables save file.");
        }

        ReadSaveFileResult<PredictionModelList> modelResult = PersistentDataService.hasSavedPredictionModelList();
        if (modelResult.getSuccess()) {
            ListService.setPredictionModelList(modelResult.getData());
            System.out.println("[INFO] Found and loaded predictionModel save file.");
        } else {
            ListService.getPredictionModelList().addThing(new MyPredictionModel());
            PersistentDataService.savePredictionModelList();
            System.out.println("[INFO] Did not found predictionModel save file. Created predictionModels from source.");
        }
    }

    public static SmartHomeCredentials getSmartHomeCredentials() {
        return smartHomeCredentials;
    }

    public static Path getSaveDirPath() {
        return SAVE_DIR_PATH;
    }

    public static Path getDeviceSavePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "devices.json");
    }

    public static Path getDeleteDevicePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "devices.old");
    }

    public static Path getVariableSavePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "variables.json");
    }

    public static Path getDeleteVariablePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "variables.old");
    }

    public static Path getPredictionModelSavePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "models.json");
    }

    public static Path getDeletePredictionModelPath() {
        return Paths.get(SAVE_DIR_PATH.toString() + File.separator + "models.old");
    }
}
