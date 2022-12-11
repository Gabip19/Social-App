package config;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class Config {

    private static final String CONFIG_LOCATION = "";
//            Objects.requireNonNull(
//                    Config.class.getClassLoader().getResource("config.properties")
//            ).getFile();

    public static Properties getProperties() {
        Properties properties = new Properties();
        URI CONFIG_LOCATION_AS_URI;

        try {
            CONFIG_LOCATION_AS_URI = new URI(CONFIG_LOCATION);
            properties.load(new FileReader(CONFIG_LOCATION_AS_URI.getPath()));
            return properties;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException("COULD NOT LOAD CONFIG FILE.\n");
        }
    }
}
