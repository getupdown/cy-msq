package cn.cy.core.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DiskPersistenceProcessorTest {

    private DiskPersistenceProcessor diskPersistenceProcessor
            = new DiskPersistenceProcessor
            (Paths.get(this.getClass().getClassLoader().getResource("testdata/persistence/input_1.log").toURI()));

    public DiskPersistenceProcessorTest() throws URISyntaxException {

    }

    @Before
    public void createReadOnlyFile() {

    }

    @After
    public void deleteReadOnlyFile() {

    }

    @Test
    public void test() {
        try {
            diskPersistenceProcessor.getFileChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            diskPersistenceProcessor.read(0, 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}