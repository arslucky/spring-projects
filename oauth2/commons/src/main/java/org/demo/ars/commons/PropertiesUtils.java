package org.demo.ars.commons;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

/**
 * @author arsen.ibragimov
 *         copied from https://stackoverflow.com/questions/57246626/how-to-read-config-file-yaml-properties-without-spring-framework
 */
public class PropertiesUtils {

    public static Properties loadPropertiesFromYaml() {
        return loadPropertiesFromYaml( getenv( "ENV"));
    }

    public static Properties loadPropertiesFromYaml( String profile) {
        Yaml yaml = new Yaml();
        Properties properties = new Properties();
        properties.putAll( getFlattenedMap( yaml.load( PropertiesUtils.class.getClassLoader().getResourceAsStream( "application.yml"))));
        if( profile != null) {
            properties.putAll( getFlattenedMap( yaml.load( PropertiesUtils.class.getClassLoader().getResourceAsStream( format( "application-$s.yml", profile)))));
        }
        return properties;
    }

    public static Properties loadProperties() throws IOException {
        return loadProperties( getenv( "ENV"));
    }

    public static Properties loadProperties( String profile) throws IOException {
        Properties properties = new Properties();
        properties.load( PropertiesUtils.class.getClassLoader().getResourceAsStream( "application.properties"));
        if( profile != null) {
            properties.load( PropertiesUtils.class.getClassLoader().getResourceAsStream( format( "application-$s.properties", profile)));
        }
        return properties;
    }

    private static final Map<String, Object> getFlattenedMap( Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap( result, source, null);
        return result;
    }

    @SuppressWarnings( "unchecked")
    private static void buildFlattenedMap( Map<String, Object> result, Map<String, Object> source, String path) {
        source.forEach( ( key, value) -> {
            if( !isBlank( path))
                key = path + (key.startsWith( "[") ? key : '.' + key);
            if( value instanceof String) {
                result.put( key, value);
            } else if( value instanceof Map) {
                buildFlattenedMap( result, (Map<String, Object>) value, key);
            } else if( value instanceof Collection) {
                int count = 0;
                for( Object object : (Collection<?>) value)
                    buildFlattenedMap( result, singletonMap( "[" + (count++) + "]", object), key);
            } else {
                result.put( key, value != null ? value : "");
            }
        });
    }
}
