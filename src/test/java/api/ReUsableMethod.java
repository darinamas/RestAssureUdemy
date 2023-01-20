package api;

import io.restassured.path.json.JsonPath;

public class ReUsableMethod {
    public static JsonPath rawToJson(String response){
        JsonPath json = new JsonPath(response);
        return json;
    }
}
