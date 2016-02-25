package logbook.bean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Test;

public class SortieBattleTest {

    /**
     * {@link logbook.bean.SortieBattle#toBattle(javax.json.JsonObject)} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testToBattle() throws IOException {
        Path p = Paths.get("./src/test/resources/logbook/bean/req_sortie_battle.json");
        try (Reader reader = Files.newBufferedReader(p)) {
            try (JsonReader jsonReader = Json.createReader(reader)) {
                JsonObject json = jsonReader.readObject()
                        .getJsonObject("api_data");
                SortieBattle.toBattle(json);
            }
        }
    }

}
