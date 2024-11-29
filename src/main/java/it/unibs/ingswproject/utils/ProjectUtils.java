package it.unibs.ingswproject.utils;

import java.io.IOException;
import java.util.Properties;

public class ProjectUtils extends Properties {
    private static final String PROJECT_PROPERTIES = "project.properties";

    public ProjectUtils() {
        super();
        try {
            this.load(this.getClass().getClassLoader().getResourceAsStream(PROJECT_PROPERTIES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProjectVersion() {
        return this.getProperty("version");
    }

    public String getProjectArtifactId() {
        return this.getProperty("artifactId");
    }
}
