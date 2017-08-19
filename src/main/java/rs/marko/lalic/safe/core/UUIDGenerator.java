package rs.marko.lalic.safe.core;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * UUID Generator Util
 * 
 * @author Marko Lalic
 */
public class UUIDGenerator {

    private static volatile EthernetAddress NIC = EthernetAddress.fromInterface();

    private static volatile TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator(NIC);

    /**
     * Generates UUID
     * 
     * @return UUID UUID String
     */
    public static synchronized String generateUUID() {
	return UUID_GENERATOR.generate().toString();
    }

}
