package codexstester.abstractor;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public abstract class PropertiesLoader extends AssertionTests {

    protected final Properties externalProp = loadExternalPropsTests();

    private static Properties loadExternalPropsTests() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile("classpath:"+externalPropertiesFilepath);
            InputStream in = Files.newInputStream(file.toPath());
            properties.load(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return properties;
    }

    protected final Properties internalProp = loadInternalPropsTests();

    protected static Properties loadInternalPropsTests() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile("classpath:"+internalPropertiesFilepath);
            InputStream in = Files.newInputStream(file.toPath());
            properties.load(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return properties;
    }

    protected final Properties unitaryProp = loadUnitaryPropsTests();

    protected static Properties loadUnitaryPropsTests() {
        Properties properties = new Properties();

        try {
            File file = ResourceUtils.getFile("classpath:"+unitaryPropertiesFilepath);
            InputStream in = Files.newInputStream(file.toPath());
            properties.load(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return properties;
    }

}
