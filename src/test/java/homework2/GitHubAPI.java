package homework2;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GitHubAPI {

    @BeforeAll
    public static void beforeAll() {
        baseURI = "https://api.github.com";
    }

    @Test
    @DisplayName("Verify organization information")
    public void organizationInfo(){
        Response response =
                given().
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}").prettyPeek();

        response.then().
                assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("login",is("cucumber")).
                body("name",equalToIgnoringCase("cucumber")).
                body("id",is(320565));
    }
    @Test
    @DisplayName("Verify error message")
    public void errorMessage(){
        Response response =
                given().
                        header("Accept", "application/xml").
                        when().
                        get("/orgs/{org}","cucumber").prettyPeek();

        response.then().
                assertThat().
                statusCode(415).
                contentType("application/json; charset=utf-8").
                statusLine(containsString("Unsupported Media Type"));
    }
    @Test
    @DisplayName("Number of repositories")
    public void numOfRepo() {
        Response response =
                given().
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}").prettyPeek();

        int numberOfRepositories = response.jsonPath().getInt("public_repos");

        Response response2 =
                given().
                        queryParams("per_page",100).
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        response2.then().
                assertThat().
                body("size()",is(numberOfRepositories));
    }

    @Test
    @DisplayName("Repository id information")
    public void idInfo() {
        Response response =
                given().
                        queryParams("per_page",100).
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        List<Integer> idList = response.jsonPath().getList("id");
        List<String> nodeIDList = response.jsonPath().getList("node_id");

        Set<Integer>idSet = new HashSet<>(idList);
        Set<String>nodeIDSet = new HashSet<>(nodeIDList);

        assertEquals(idList.size(),idSet.size());
        assertEquals(nodeIDList.size(),nodeIDSet.size());


    }
    @Test
    @DisplayName("Repository owner information")
    public void repoOwnerInfo() {
        Response response =
                given().
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}").prettyPeek();

        int id = response.jsonPath().getInt("id");

        Response response2 =
                given().
                        queryParams("per_page",100).
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        response2.then().
                assertThat().
                body("owner.id",everyItem(is(id)));

    }

    @Test
    @DisplayName("Ascending order by full_name sort")
    public void ascOrderByFullName() {
        Response response =
                given().
                        pathParam("org","cucumber").
                        queryParams("sort","full_name").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        List<String>fullNames=response.jsonPath().getList("full_name");
        List<String>sortedFullNames = new ArrayList<>(fullNames);
        Collections.sort(sortedFullNames);

        assertEquals(fullNames,sortedFullNames);
    }

    @Test
    @DisplayName("Descending order by full_name sort")
    public void descOrderByFullName() {
        Response response =
                given().
                        pathParam("org","cucumber").
                        queryParams("sort","full_name").
                        queryParams("direction","desc").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        List<String>fullNames = response.jsonPath().getList("full_name");
        List<String>sortedFullNames = new ArrayList<>(fullNames);
        Collections.sort(sortedFullNames, Collections.reverseOrder());

        assertEquals(sortedFullNames,fullNames);
    }
    @Test
    @DisplayName("Default sort")
    public void defaultSort() {
        Response response =
                given().
                        pathParam("org","cucumber").
                        when().
                        get("/orgs/{org}/repos").prettyPeek();

        List<String>dates = response.jsonPath().getList("created_at");
        List<String>sortedDates = new ArrayList<>(dates);
        Collections.sort(sortedDates,Collections.reverseOrder());

        assertEquals(sortedDates,dates);
    }
}