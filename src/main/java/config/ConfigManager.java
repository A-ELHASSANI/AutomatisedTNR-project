package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads config.properties so credentials, URLs and timeouts
 * are never hardcoded in tests or page objects.
 */
public class ConfigManager {

    private static final Properties props = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found on classpath");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public String get(String key) {
        String value = props.getProperty(key);
        if (value == null) throw new RuntimeException("Missing config key: " + key);
        return value;
    }

    public int getInt(String key) { return Integer.parseInt(get(key)); }
    public boolean getBool(String key) { return Boolean.parseBoolean(get(key)); }

    // ── Convenience shortcuts ──────────────────────────────────────────────────
    public String getBaseUrl()   { return get("app.url"); }
    public String getUsername()  { return get("app.username"); }
    public String getPassword()  { return get("app.password"); }
    public int    getExplicitTimeout() { return getInt("timeout.explicit"); }
    public boolean isHeadless()  { return getBool("headless"); }
}
