import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Map<String, String> replacementMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        String inputFilePath = "src/main/java/input.json";
        String outputFilePath = "src/main/java/output.json";
        String mapFilePath = "src/main/java/map.json";

        Map<String, Object> inputData = readFromFile(inputFilePath);
        assert inputData != null;
        Map<String, Object> obfuscated = obfuscate(inputData);

        writeToFile(outputFilePath, obfuscated);
        writeToFile(mapFilePath, replacementMap);
    }


    private static Map<String, Object> readFromFile(String filePath){
        try(FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void writeToFile(String filePath, Object data){
        try(FileWriter writer = new FileWriter(filePath)){
            Gson gson = gsonBeauty();
            writer.write(gson.toJson(data));
        } catch (IOException e ){
            e.printStackTrace();
        }
    }

    private static Gson gsonBeauty(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    public static Map<String, Object> obfuscate(Map<String, Object> data) {
        Map<String, Object> obfuscated = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            obfuscated.put(unicodeEsc(entry.getKey()), obfuscateValue(entry.getValue()));
        }
        return obfuscated;
    }

    private static Object obfuscateValue(Object value) {
        switch (value) {
            case Map map -> {
                return obfuscate((Map<String, Object>) value);
            }
            case List list -> {
                return obfuscateList(list);
            }
            case String s -> {
                String obfuscatedValue = unicodeEsc(s);
                replacementMap.put(s, obfuscatedValue);
                return obfuscatedValue;
            }
            case null, default -> {
                return value;
            }
        }
    }

    private static List<Object> obfuscateList(List<?> list) {
        List<Object> obfuscatedList = new ArrayList<>();
        for (Object item : list) {
            obfuscatedList.add(obfuscateValue(item));
        }
        return obfuscatedList;
    }

    public static String unicodeEsc(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append("\\u").append(String.format("%04x", (int) c));
        }
        return sb.toString();
    }
}