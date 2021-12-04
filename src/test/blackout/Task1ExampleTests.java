package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.files.File;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.satellites.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task1ExampleTests {
    @Test
    public void test_file() {
        //Task 1
        //Check have quantum
        File f1 = new File("f1", "test assert", true);
        File f2 = new File("f2", "test assert quantum", true);
        assertFalse(f1.has_quantum());
        assertTrue(f2.has_quantum());

        //Task 2
        //Store f1 to the satellite1, test get_file
        var standard_satellite = new StandardSatellite("Satellite1", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));

        standard_satellite.store_file(f1);
        File get_f1 = standard_satellite.get_file("f1");
        File get_f2 = standard_satellite.get_file("f2");
        assertNotNull(get_f1);
        assertNull(get_f2);
        assertEquals("test assert", get_f1.getContent());

        //test for other types of satellites
        var relaysatellite = new StandardSatellite("Satellite2", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        var shrinkingsatellite = new StandardSatellite("Satellite3", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        get_f1 = relaysatellite.get_file("f1");
        get_f2 = shrinkingsatellite.get_file("f2");
        assertNull(get_f1);
        assertNull(get_f2);

        relaysatellite.store_file(f1);
        shrinkingsatellite.store_file(f2);
        get_f1 = relaysatellite.get_file("f1");
        get_f2 = shrinkingsatellite.get_file("f2");
        assertNotNull(get_f1);
        assertNotNull(get_f2);
        assertEquals("test assert", get_f1.getContent());
        assertEquals("test assert quantum", get_f2.getContent());
    }

    @Test
    public void testExample() {
        // Task 1
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices
        // 2 devices are in view of the satellite
        // 1 device is out of view of the satellite
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));

        assertEquals(new EntityInfoResponse("DeviceA", Angle.fromDegrees(30), RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("DeviceA"));
        assertEquals(new EntityInfoResponse("DeviceB", Angle.fromDegrees(180), RADIUS_OF_JUPITER, "LaptopDevice"), controller.getInfo("DeviceB"));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));
    }

    @Test
    public void testDelete() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices and deletes them
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        controller.removeDevice("DeviceA");
        controller.removeDevice("DeviceB");
        controller.removeDevice("DeviceC");
        controller.removeSatellite("Satellite1");
    }

    @Test
    public void basicFileSupport() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add some files to it
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));

        //test for the second file
        controller.addFileToDevice("DeviceC", "Hello World!", "My second file!");
        expected.put("Hello World!", new FileInfoResponse("Hello World!", "My second file!", "My second file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));

        //test HandheldDevice
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC","DeviceD"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceD", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("DeviceD"));

        controller.addFileToDevice("DeviceD", "Hello World", "My first file!");
        expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World", "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceD", Angle.fromDegrees(330), RADIUS_OF_JUPITER, "HandheldDevice", expected), controller.getInfo("DeviceD"));

    }

}
