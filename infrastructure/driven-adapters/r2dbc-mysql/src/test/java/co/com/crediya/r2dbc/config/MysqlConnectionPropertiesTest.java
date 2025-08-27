package co.com.crediya.r2dbc.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MysqlConnectionPropertiesTest {

    @Test
    void shouldBindPropertiesCorrectly() {
        Map<String, Object> properties = Map.of(
                "adapters.r2dbc.host", "localhost",
                "adapters.r2dbc.port", 3306,
                "adapters.r2dbc.database", "testdb",
                "adapters.r2dbc.schema", "public",
                "adapters.r2dbc.username", "testuser",
                "adapters.r2dbc.password", "testpass"
        );

        Binder binder = new Binder(new MapConfigurationPropertySource(properties));
        MysqlConnectionProperties config = binder.bind("adapters.r2dbc", MysqlConnectionProperties.class).get();

        assertEquals("localhost", config.host());
        assertEquals(3306, config.port());
        assertEquals("testdb", config.database());
        assertEquals("public", config.schema());
        assertEquals("testuser", config.username());
        assertEquals("testpass", config.password());
    }
}