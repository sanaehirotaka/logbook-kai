package logbook.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingGenerator {
    /** Source URL, thanks to KC3 */
    private static final String SOURCE_URL = "https://raw.githubusercontent.com/KC3Kai/KC3Kai/master/src/data/edges.json";
    /** Prefix of each key representing map area */
    private static final String KEY_PREFIX = "World ";
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, List<String>>> json = mapper.readValue(new URL(SOURCE_URL), Map.class);
        try (FileOutputStream fos = new FileOutputStream(new File("src/main/resources/logbook/map/mapping.json"));
                PrintWriter pw = new PrintWriter(fos)) {
            // generate the JSON on our own to keep the order in the original JSON file
            pw.println("{");
            json.keySet().stream()
                .filter(key -> key.startsWith(KEY_PREFIX))
                .flatMap(key -> {
                    String area = key.substring(KEY_PREFIX.length());
                    Map<String, List<String>> cells = json.get(key);
                    return cells.entrySet().stream()
                            .filter(cell -> cell.getValue().size() == 2 && !"Start".equals(cell.getValue().get(1)))
                            .map(cell -> ("    \"" + area + "-" + cell.getKey() + "\": \"" + cell.getValue().get(1) + "\""));
                })
                .reduce((acc, cur) -> acc + ",\n" + cur)
                .ifPresent(pw::println);
            pw.println("}");
        }
    }
}
