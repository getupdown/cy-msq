package cn.cy.core.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;

public class BasePersistenceTest {
    protected Path path;

    @Before
    public void before() throws IOException, URISyntaxException {
        createFile();
    }

    @After
    public void after() throws IOException {
        Files.delete(path);
    }

    protected void createFile() throws URISyntaxException, IOException {
        path = Files.createTempFile(Paths.get(getClass().getClassLoader().getResource("testdata/persistence")
                .toURI()), "test", "mappedFile_1.log");
    }
}
