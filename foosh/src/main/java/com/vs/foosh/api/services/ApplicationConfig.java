package com.vs.foosh.api.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vs.foosh.api.model.device.DeviceList;
import com.vs.foosh.api.model.misc.ReadSaveFileResult;
import com.vs.foosh.api.model.web.SmartHomeCredentials;

@Configuration
public class ApplicationConfig {
    private static final String HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static int port;
    private static Path SAVE_DIR_PATH = Paths.get(System.getProperty("user.home") + "/foosh").toAbsolutePath();

    private static SmartHomeCredentials smartHomeCredentials;
    
    @Bean
    private static void setup() {
        readInApplicationProperties();
        tryToLoadSaveFile();
    }

    private static void readInApplicationProperties() {
        Properties config = new Properties();
        try (InputStream is = new FileInputStream(new File("src/main/resources/application.properties"))) {
            config.load(is);

            String serverPort = config.getProperty("server.port");
            setupServerPort(serverPort);

            String smartHomeCredentialPath = config.getProperty("smartHomeCredentialPath");
            setupSmartHomeCredentials(smartHomeCredentialPath);

            setupSaveDirectory();

        } catch (IOException e) {
            port = DEFAULT_PORT;
            System.out.println(
                    "[INFO] Encountered a problem reading application.properties!\n"
                            + "[INFO] Using default port: "
                            + DEFAULT_PORT);
        } finally {
            LinkBuilder.setServerVariables(HOST, port);
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
            LinkBuilder.setServerVariables(HOST, port);
        }
    }

    private static void setupSmartHomeCredentials(String path) {
        if (path == null || path.equals("")) {
            System.err.println(
                    "[ERROR] Field 'smartHomeCredentialsPath' (" + path
                            + ") in application.properties is either empty or non-existent! This might become a problem when trying to communicate with the SmartHome API.\n"
                            + "[ADVICE] Please set a correct path and restart the server!");
            return;
        }

        File secrets = new File(path);
        if (secrets.exists() && !secrets.isDirectory()) {
            try {
                byte[] jsonData = Files.readAllBytes(Paths.get("src/main/java/com/vs/foosh/custom/secrets.json"));
                ObjectMapper mapper = new ObjectMapper();

                smartHomeCredentials = mapper.readValue(jsonData, SmartHomeCredentials.class);
            } catch (IOException e) {
                System.err.println("[ERROR] Something went wrong while reading secrets.json:\n" + e);
            }
        }
    }
    
    private static void setupSaveDirectory()  {
        File saveDir = new File(Paths.get(System.getProperty("user.home")).toAbsolutePath().toString());
        saveDir.mkdirs();

        File saveFile = new File(Paths.get(System.getProperty("user.home") + "/save.json").toAbsolutePath().toString());
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            System.err.println("[ERROR] Something went wrong while creating empty device save file " + saveFile.getAbsolutePath());
        }
    }

    private static void tryToLoadSaveFile() {
        ReadSaveFileResult result = PersistentDataService.hasSavedDeviceList();
        if (result.getSuccess()) {
            DeviceList.setDevices(result.getData());
        }
    }

    public static SmartHomeCredentials getSmartHomeCredentials() {
        return smartHomeCredentials;
    }

    public static Path getDeviceSavePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + "/devices.json");
    }

    public static Path getDeleteDevicePath() {
        return Paths.get(SAVE_DIR_PATH.toString() + "/devices.old");
    }
}
