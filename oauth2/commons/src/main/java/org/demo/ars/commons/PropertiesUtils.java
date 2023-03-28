package org.demo.ars.commons;

import static java.lang.System.getenv;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

/**
 * @author arsen.ibragimov
 *
 */
public class PropertiesUtils {

    public static Properties loadPropertiesFromYaml( String file) {
        return loadPropertiesFromYaml( file, getenv( "ENV"));
    }

    public static Properties loadPropertiesFromYaml( String file, String profile) {
        Yaml yaml = new Yaml();
        Properties properties = new Properties();

        String fn = null;

        if( profile == null) {
            fn = String.format( "%s.yml", file);
        } else {
            fn = String.format( "%s-%s.yml", file, profile);
        }

        InputStream res = PropertiesUtils.class.getClassLoader().getResourceAsStream( fn);

        if( res == null) {
            return properties;
        }

        properties.putAll( getFlattenedMap( yaml.load( res)));

        return properties;
    }

    public static Properties loadProperties( String file) throws IOException {
        return loadProperties( file, getenv( "ENV"));
    }

    public static Properties loadProperties( String file, String profile) throws IOException {

        String fn = null;

        if( profile == null) {
            fn = String.format( "%s.properties", file);
        } else {
            fn = String.format( "%s-%s.properties", file, profile);
        }
        Properties properties = new Properties();

        InputStream res = PropertiesUtils.class.getClassLoader().getResourceAsStream( fn);

        if( res == null) {
            return properties;
        }
        properties.load( res);

        return properties;
    }

    private static final Map<String, Object> getFlattenedMap( Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap( result, source, null);
        return result;
    }

    /**
     * copied from https://stackoverflow.com/questions/57246626/how-to-read-config-file-yaml-properties-without-spring-framework
     *
     * @param result
     * @param source
     * @param path
     */
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
