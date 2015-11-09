package logbook.internal;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JarBasedPluginTest {

    /**
     * {@link logbook.internal.JarBasedPlugin} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
        new JarBasedPlugin(p);
    }

    /**
     * {@link logbook.internal.JarBasedPlugin} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void test2() throws IOException {
        new JarBasedPlugin(Paths.get("--missing file--"));
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getURL()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetURL() throws IOException {
        Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
        URL expected = p.toUri().toURL();
        URL actual = new JarBasedPlugin(p).getURL();
        assertEquals(expected, actual);
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getServices()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetServices() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            List<String> expected = Arrays.asList("logbook.test.TestSpi1", "logbook.test.TestSpi2");
            List<String> actual = new ArrayList<>(plugin.getServices());
            Collections.sort(actual);
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            List<String> expected = Collections.emptyList();
            List<String> actual = plugin.getServices();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getName()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetName() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Name)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Title)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Title)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getVendor()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetVendor() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getVersion()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetVersion() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.internal.JarBasedPlugin#getLicense()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetLicense() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-License)";
            String actual = plugin.getLicense();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/internal/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getLicense();
            assertEquals(expected, actual);
        }
    }

}
