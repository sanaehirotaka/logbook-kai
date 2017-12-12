package logbook.plugin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class JarBasedPluginTest {

    /**
     * {@link logbook.plugin.JarBasedPlugin} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
        new JarBasedPlugin(p);
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void test2() throws IOException {
        new JarBasedPlugin(Paths.get("--missing file--"));
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin#getURL()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetURL() throws IOException {
        Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
        URL expected = p.toUri().toURL();
        URL actual = new JarBasedPlugin(p).getURL();
        assertEquals(expected, actual);
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin#getName()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetName() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Name)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Title)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Title)";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getName();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin#getVendor()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetVendor() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Vendor)";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getVendor();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin#getVersion()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetVersion() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case2.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Implementation-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case3.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Specification-Version)";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getVersion();
            assertEquals(expected, actual);
        }
    }

    /**
     * {@link logbook.plugin.JarBasedPlugin#getLicense()} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testGetLicense() throws IOException {
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case1.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "test-plugin(Bundle-License)";
            String actual = plugin.getLicense();
            assertEquals(expected, actual);
        }
        {
            Path p = Paths.get("./src/test/resources/logbook/plugin/plugin-test-case4.jar.bin");
            JarBasedPlugin plugin = new JarBasedPlugin(p);
            String expected = "";
            String actual = plugin.getLicense();
            assertEquals(expected, actual);
        }
    }

}
