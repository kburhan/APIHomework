package homework3;

import static io.restassured.RestAssured.*;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HarryPotterAPI {
    private final String APIKEY = "$2a$10$0uB507wkDyQzv0rIIjUE0uEVSsTrf1XHggJTl5lMvjoa.3p8xU5lO";

    @BeforeAll
    public static void beforeAll() {
        baseURI = "https://www.potterapi.com/v1";
        config = config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
    }

    @Test
    @DisplayName("Verify sorting hat")
    public void sortingHat() {
        List<String>houseList = Arrays.asList("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff");

        Response response = get("/sortingHat").prettyPeek();

        String houseFromResponse = response.as(String.class);

        response.then().
                assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8");

        assertTrue(houseList.contains(houseFromResponse));
    }

    @Test
    @DisplayName("Verify bad key")
    public void verifyBadKey() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key","invalidKey").
                        when().
                        get("/characters").prettyPeek();

        response.then().
                assertThat().
                statusCode(401).
                contentType("application/json; charset=utf-8").
                statusLine(containsString("Unauthorized")).
                body("error",is("API Key Not Found"));

    }

    @Test
    @DisplayName("Verify no key")
    public void noKey() {
        Response response =
                given().
                        header("Accept","application/json").
                        when().
                        get("/characters").prettyPeek();

        response.then().
                assertThat().
                statusCode(409).
                contentType("application/json; charset=utf-8").
                body("error",is("Must pass API key for request"));

    }

    @Test
    @DisplayName("Verify number of characters")
    public void numOfChar() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        when().
                        get("/characters").prettyPeek();

        response.then().
                assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("size()",is(195));

        List<Object>characterList = response.jsonPath().getList("");

        assertTrue(characterList.size() == 195);
    }

    @Test
    @DisplayName("Verify number of character id and house")
    public void numOfCharacterIDandHouser() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        get("/characters").prettyPeek();

        List<String>houseList = Arrays.asList("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff");

        response.then().
                assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("_id",everyItem(not(isEmptyString()))).
                body("dumbledoresArmy",everyItem(is(instanceOf(Boolean.class)))).
                body("house",everyItem(is(oneOf("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff",null))));

    }
    @Test
    @DisplayName("Verify all character information")
    public void allCharactersInfo() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        when().
                        get("/characters").prettyPeek();

        response.then().
                assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8");

        List<Map<String,String>>allCharacters = response.jsonPath().getList("");

        System.out.println("allCharacters = " + allCharacters);

        int randomCharater = new Random().nextInt(allCharacters.size());

        String anyName = allCharacters.get(randomCharater).get("name");

        System.out.println("anyName = " + anyName);

        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        queryParam("name",anyName).
                        when().
                        get("/characters").prettyPeek();

        response2.then().
                assertThat().
                body("[0].name",is(anyName));

    }

    @Test
    @DisplayName("Verify name search")
    public void nameSearch() {

        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        queryParams("name", "Harry Potter").
                        when().
                        get("/characters").prettyPeek();

        response.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("[0].name",is("Harry Potter"));

        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        queryParams("name", "Marry Potter").
                        when().
                        get("/characters").prettyPeek();

        response2.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("[0]",isEmptyOrNullString()).
                body("size()",is(0));


    }
    @Test
    @DisplayName("Verify house members")
    public void houseMember() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        when().
                        get("/houses").prettyPeek();

        response.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8");

        String gryffindorID = response.jsonPath().getString("find{it.name == 'Gryffindor'}_id");

        System.out.println("gryffindorID = " + gryffindorID);

        List<String>memberIDs = response.jsonPath().getList("find{it.name == 'Gryffindor'}.members");
        System.out.println("Gryffindor member IDs = " + memberIDs);


        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        pathParam("id",gryffindorID).
                        when().
                        get("/houses/{id}").prettyPeek();

        List<String>memberIDsFrom2ndResponse = response2.jsonPath().getList("[0].members._id");
        System.out.println("memberIDsFrom2ndResponse = " + memberIDsFrom2ndResponse);

        assertEquals(memberIDs,memberIDsFrom2ndResponse);


    }

    @Test
    @DisplayName("Verify house members again")
    public void houseMembers() {
        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        pathParam("id","5a05e2b252f721a3cf2ea33f").
                        when().
                        get("/houses/{id}").prettyPeek();

        List<String>memberIDs = response.jsonPath().getList("[0].members._id");

        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",APIKEY).
                        queryParams("house","Gryffindor").
                        when().
                        get("/characters").prettyPeek();

        List<String>characterIDs = response.jsonPath().getList("findAll{it.house == 'Gryffindor'}._id");

        assertEquals(memberIDs,characterIDs);
    }


}