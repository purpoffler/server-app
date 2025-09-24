package layer.jsonWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.JsonModel;
import utlis.ConsoleHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private volatile File file = new File("UserJson.json");
    private final Type listType = new TypeToken<List<JsonModel>>() {
    }.getType();

    public void writeJson(String date, String ip, String data) throws IOException {
        ArrayList<JsonModel> jsonModels = new ArrayList<>();

        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                ArrayList<JsonModel> existingObject = gson.fromJson(fr, listType);
                if (existingObject != null) {
                    jsonModels.addAll(existingObject);
                }
            }
        }

        jsonModels.add(new JsonModel(date, ip, data));
        ConsoleHelper.writeMessage(jsonModels.toString());
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(jsonModels, fw);
        }
    }
}
