package api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) {

        //given - all input details
        //when - submit the api - resourse, http method
        //then - validate the response

        RestAssured.baseURI = "https://rahulshettyacademy.com/";
        given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}")
                .when().post("maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200);

    }

    @Test
    public void e2eTest() {
        //given - all input details
        //when - submit the api - resourse, http method
        //then - validate the response

        //Add place -> Update Place with new Adress -> Get place to validate if new adress is presented

        RestAssured.baseURI = "https://rahulshettyacademy.com/";

        String addPlaceResponse =  given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type","application/json")
                .body(Payload.addPlace())
                .when().post("maps/api/place/add/json")
                .then().assertThat()
                    .statusCode(200)
                    .body("scope", equalTo("APP"))
                    .header("server", "Apache/2.4.41 (Ubuntu)")
                .extract().response().asString();

        String placeID = ReUsableMethod.rawToJson(addPlaceResponse).getString("place_id"); //extract placeID

        String newAdress = "Summer walk adress, Africa";

        //edit place
        given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type","application/json")
                .body(Payload.editPlace(placeID, newAdress))
                .when().put("maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));

        //get place
        String getPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeID)
                .when().get("maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200)
                .extract().asString();

        String actualAdress = ReUsableMethod.rawToJson(getPlaceResponse).getString("address");

        Assertions.assertEquals(newAdress, actualAdress);
    }

    //Movies
    @Test
    public void getNewToken() {
        RestAssured.baseURI = "https://api.themoviedb.org/";
        given().log().all()
                .queryParam("api_key", "b245628f3ad4775009c2a28e1b7829a5")
                .header("Content-Type","application/json")
                .when().get("3/authentication/token/new")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

    }

    //BookStore
    @Test
    public void creaateNewUser() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com/";
        given().log().all()
                .header("Content-Type","application/json")
                .body("{\n" +
                        "  \"userName\": \"UserDAR12395\",\n" +
                        "  \"password\": \"Test123!\"\n" +
                        "}")
                .when().post("account/v1/user")
                .then().log().all().assertThat()
                .statusCode(201);
    }

    @Test
    public void test3() {
        RestAssured.baseURI = "https://api.themoviedb.org/";

        String response = given().log().all()
                .queryParam("api_key", "b245628f3ad4775009c2a28e1b7829a5")
                .queryParam("query", "avatar")
                .header("Content-Type","application/json")
                .when().get("3/search/movie")
                .then().log().all().assertThat()
                .statusCode(200)
                .extract().asString();

        //Count a number of films in response
        int countFilms = ReUsableMethod.rawToJson(response).getInt("results.size()");
        System.out.println(countFilms);

        //Get title of the first movie
        var getFirstFilmTitle = ReUsableMethod.rawToJson(response).getString("results[0].title");
        System.out.println(getFirstFilmTitle);

        //get each title from reults array
        for (int i=0; i<countFilms; i++) {
            var film = ReUsableMethod.rawToJson(response).get("results["+i+"].title");
            System.out.println(film);
        }

        //print popularity for  Avatar Ages: Memories
        for (int i=0; i<countFilms; i++) {
            var filmTitle = ReUsableMethod.rawToJson(response).get("results["+i+"].title");
            System.out.println(filmTitle);
            if(filmTitle.equals("Avatar Ages: Memories")) {
                var popularity = ReUsableMethod.rawToJson(response).get("results["+i+"].popularity");
                System.out.println("Popularity "+ popularity);
            }
        }
    }
}
