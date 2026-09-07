package org.example.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    static {
        try {

            InputStream input =
                    Config.class.getClassLoader()
                            .getResourceAsStream("application.properties");

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações.");
        }
    }

    public static String get(String chave) {
        return properties.getProperty(chave);
    }
}