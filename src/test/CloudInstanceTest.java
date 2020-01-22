package test;

import main.CloudInstance;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class CloudInstanceTest {

    CloudInstance cloudInstance = new CloudInstance();

    @Test
    public void get_costs() throws Throwable {
        assertNull(cloudInstance.get_costs(0, 0, 0.0f));
    }
}