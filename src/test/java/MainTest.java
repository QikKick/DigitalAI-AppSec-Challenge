
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {


    @Test
    public void testReadFromFile() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("name", "John");
        expected.put("someInt", 1.0);
        expected.put("someBool", true);

        List<Map<String, Object>> cars = new ArrayList<>();
        cars.add(Map.of("name", "Ford", "models", List.of("Fiesta", "Focus", "Mustang")));
        cars.add(Map.of("name", "BMW", "models", List.of("320", "X3", "X5")));
        cars.add(Map.of("name", "Fiat", "models", List.of("500", "Panda")));

        expected.put("cars", cars);

        assertEquals(expected, Main.readFromFile("src/test/resources/test.json"));
    }

    @Test
    public void testWriteToFile() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        Main.writeToFile("src/test/resources/testWrite.json", data);
        assertEquals(data, Main.readFromFile("src/test/resources/testWrite.json"));
    }

    @Test
    public void testObfuscate() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        Map<String, Object> obfuscated = Main.obfuscate(data);
        assertNotEquals(data, obfuscated);
    }

    @Test
    public void testObfuscateValue() {
        String original = "value";
        String obfuscated = (String) Main.obfuscateValue(original);
        assertNotEquals(original, obfuscated);
    }

    @Test
    public void testUnicodeEsc() {
        String original = "value";
        String escaped = Main.unicodeEsc(original);
        assertEquals("\\u0076\\u0061\\u006c\\u0075\\u0065", escaped);
    }
}