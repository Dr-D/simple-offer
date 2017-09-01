package com.drd.integration.rest.webservice;

import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Author: drd 2017-08-30
 */
public class OfferRestIntegrationIT implements RestAPIConstants {

    private final String base_url = "http://localhost:8080/restv1/rest/offer";
    private List<Long> ids = new ArrayList<>();

    @BeforeMethod
    public void setup() {
        ids.clear();
    }


    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        ids.forEach(this::deleteByID);
    }

    @Test
    public void shouldCreateOffer() throws Exception {
        //given
        OfferJSON offerJSON = createOfferJSON(0L, "create1", "Create Description1", 1.0f);

        //when
        Response response = given()
                .contentType("application/json")
                .body(offerJSON.json)
                .when()
                .post(base_url);

        //then
        Assert.assertEquals(response.getStatusCode(), CREATED, "Incorrect response status code");
        String location = response.getHeader("location");
        Assert.assertEquals(location, base_url + "/name/" + offerJSON.name, "Incorrect location header");
        long id = Long.parseLong(response.getBody().print());
        Assert.assertTrue(id > 0, "Incorrect ID: '" + id + "'");

        given()
                .get(base_url + "/name/" + offerJSON.name)
                .then()
                .statusCode(200)
                .body("name", equalTo(offerJSON.name))
                .body("description", equalTo(offerJSON.description))
                .body("price", equalTo(offerJSON.price));

        ids.add(id);
    }

    @Test
    public void shouldFailToCreateDuplicateName() {
        //given
        OfferJSON offerJSON = createOfferJSON(0L, "create2", "Create Description2", 1.0f);
        long id = createOffer(offerJSON);

        //when
        Response r = given()
                .contentType("application/json")
                .body(offerJSON.json)
                .when()
                .post(base_url);

        //then
        Assert.assertEquals(r.getStatusCode(), DUPLICATE_NAME);

        ids.add(id);
    }

    @Test
    public void shouldFailToCreateIfIDNotZero() {
        //given
        OfferJSON offerJSON = createOfferJSON(1L, "create2", "Create Description2", 1.0f);

        //when
        Response response = given()
                .contentType("application/json")
                .body(offerJSON.json)
                .when()
                .post(base_url);

        //then
        Assert.assertEquals(400, response.getStatusCode(), "Incorrect response status code");
    }

    @Test
    public void testList() throws Exception {
        //given
        OfferJSON offerJSON = createOfferJSON(0L, "list1", "List Desc1", 1.0f);
        OfferJSON offerJSON2 = createOfferJSON(0L, "list2", "List Desc2", 2.0f);
        Long id = createOffer(offerJSON);
        Long id2 = createOffer(offerJSON2);

        //when/then
        get(base_url)
                .then()
                .statusCode(200)
                .body("name", hasItems(offerJSON.name, offerJSON2.name));

        //cleanup
        ids.add(id);
        ids.add(id2);
    }

    @Test
    public void shouldGetByID() {
        //given
        OfferJSON offerJSON = createOfferJSON(0L, "Get1", "Get Desc1", 1.0f);
        Long id = createOffer(offerJSON);

        //when/then
        given()
                .get(base_url + "/id/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(offerJSON.name));

        ids.add(id);
    }

    @Test
    public void shouldFailGetUnknownID() {
        //given
        long unknownID = 9999999L;

        //when/then
        given()
                .get(base_url + "/id/" + unknownID)
                .then()
                .statusCode(UNKNOWN_ID)
                .body("message", equalTo("Get could not find offer with id: '9999999'"))
                .body("status", equalTo(UNKNOWN_ID));
    }


    @Test
    public void shouldGetByName() {
        //given
        OfferJSON offerJSON = createOfferJSON(0L, "Get2", "Get Desc2", 2.0f);
        Long id = createOffer(offerJSON);

        //when/then
        given()
                .get(base_url + "/name/" + offerJSON.name)
                .then()
                .statusCode(200)
                .body("name", equalTo(offerJSON.name));

        ids.add(id);
    }

    @Test
    public void shouldFailToGetUnknownName() {
        //when/then
        given()
                .get(base_url + "/name/UNKNOWN")
                .then()
                .statusCode(UNKNOWN_NAME)
                .body("message", equalTo("Get could not find offer with name: 'UNKNOWN'"))
                .body("status", equalTo(UNKNOWN_NAME));
    }

    @Test
    public void shouldUpdateOffer() {
        //given
        OfferJSON offerJSON = createOfferJSON(0, "update1", "Update Desc1", 1.0f);
        Long id = createOffer(offerJSON);

        OfferJSON updatedOffer = createOfferJSON(0, "Updated2", "Updated Desc2", 2.0f);

        //when
        Response response = given()
                .contentType("application/json")
                .body(updatedOffer.json)
                .when()
                .put(base_url + "/" + offerJSON.name);

        String location = response.getHeader("location");
        Assert.assertEquals(location, base_url + "/name/" + updatedOffer.name, "Incorrect location header");

        //then - check we can get with new name and that all fields have been updated
        given()
                .get(base_url + "/name/" + updatedOffer.name)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedOffer.name))
                .body("description", equalTo(updatedOffer.description))
                .body("price", equalTo(updatedOffer.price));

        ids.add(id);
    }


    @Test
    public void shouldFailUpdateUnknownName() {
        //given
        OfferJSON offerJSON = createOfferJSON(0, "update1", "Update Desc1", 1.0f);

        //when//then
        given()
                .contentType("application/json")
                .body(offerJSON.json)
                .when()
                .put(base_url + "/UNKNOWN")
                .then()
                .statusCode(UNKNOWN_NAME);

    }

    @Test
    public void shouldFailUpdateDuplicateName() {
        //given
        OfferJSON offerJSON = createOfferJSON(0, "update1", "Update Desc1", 1.0f);
        Long id = createOffer(offerJSON);

        OfferJSON offerJSON2 = createOfferJSON(0, "update2", "Update Desc2", 1.0f);
        Long id2 = createOffer(offerJSON2);

        String duplicatedName = "{\"id\":0, \"name\":\"" + offerJSON.name + "\",\"price\": 1.0,\"description\":\"\"}";

        //when//then
        given()
                .contentType("application/json")
                .body(duplicatedName)
                .when()
                .put(base_url + "/" + offerJSON2.name)
                .then()
                .statusCode(DUPLICATE_NAME);

        ids.add(id);
        ids.add(id2);
    }

    @Test
    public void shouldDeleteByID() {
        //given
        OfferJSON offerJSON = createOfferJSON(0, "delete1", "Delete Desc1", 1.0f);
        Long id = createOffer(offerJSON);

        //when
        given()
                .delete(base_url + "/id/" + id)
                .then()
                .statusCode(200);

        //then - check it no longer exists
        given()
                .get(base_url + "/name/" + offerJSON.name)
                .then()
                .statusCode(UNKNOWN_NAME);
    }

    @Test
    public void shouldFailDeleteByUnknownID() {
        //given
        long unknownID = 9999999L;

        //when/then
        given()
                .delete(base_url + "/id/" + unknownID)
                .then()
                .statusCode(UNKNOWN_ID);
    }

    @Test
    public void shouldDeleteByName() {
        //given
        OfferJSON offerJSON = createOfferJSON(0, "delete2", "Delete Desc2", 2.0f);
        createOffer(offerJSON);

        //when
        given()
                .delete(base_url + "/name/" + offerJSON.name)
                .then()
                .statusCode(200);

        //then - check it no longer exists
        given()
                .get(base_url + "/name/" + offerJSON.name)
                .then()
                .statusCode(UNKNOWN_NAME);
    }

    @Test
    public void shouldFailDeleteByUnknownName() {
        //when/then
        given()
                .delete(base_url + "/name/UNKNOWN")
                .then()
                .statusCode(UNKNOWN_NAME);
    }


    class OfferJSON {
        String json;
        long id;
        String name;
        String description;
        float price;

        OfferJSON(String json, long id, String name, String description, float price) {
            this.json = json;
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }

    private OfferJSON createOfferJSON(long id, String name, String description, float price) {
        String uniqueName = addUUID(name);
        return new OfferJSON("{\"id\":" + id + ",\"name\":\"" + uniqueName + "\",\"price\":" + price + ",\"description\":\"" + description + "\"}", id, uniqueName, description, price);
    }

    private Long createOffer(OfferJSON offerJSON) {
        //when
        Response r = given()
                .contentType("application/json")
                .body(offerJSON.json)
                .when()
                .post(base_url);

        Assert.assertEquals(r.getStatusCode(), CREATED, "Create offer returned invalid status code");
        return Long.parseLong(r.getBody().print());
    }

    private void deleteByID(long id) {
        given()
                .delete(base_url + "/id/" + id)
                .then()
                .statusCode(200);
    }

    private String addUUID(String text) {
        return text + "-" + UUID.randomUUID().toString();
    }
}