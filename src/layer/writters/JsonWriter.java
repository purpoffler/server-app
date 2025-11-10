package layer.writters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import layer.dto.JsonModel;
import layer.dto.UserPackage;
import config.ServerConfig;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter extends Writer {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type listType = new TypeToken<List<JsonModel>>() {
    }.getType();

    @Override
    public void doWrite(UserPackage userPackage) throws IOException {
        ArrayList<JsonModel> jsonModels = new ArrayList<>();
        File file = getFile(serverConfig.getJsonFileName());

        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                ArrayList<JsonModel> existingObject = gson.fromJson(fr, listType);
                if (existingObject != null) {
                    jsonModels.addAll(existingObject);
                }
            }
        }

        jsonModels.add(new JsonModel(userPackage.date(), userPackage.ip(), userPackage.data()));
        log.debug("Записываем в json: " + jsonModels.toString());
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(jsonModels, fw);
        }
    }
}
