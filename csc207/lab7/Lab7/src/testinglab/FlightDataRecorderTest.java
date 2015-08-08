package testinglab;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FlightDataRecorderTest {

	protected FlightDataRecorder flightData1, flightData2;
	protected List<Double> flightDataCollection1, recordCollection;
	protected double theSum1 = 0, theSum2 = 0;
	protected int size1 = 0, size2 = 0;
	protected double average1, average2;
    /**
     * Set up mock record collections.
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        flightData1 = new FlightDataRecorder();
        flightDataCollection1 = new ArrayList<Double>();
        for (int i = 0; i < FlightDataRecorder.CAPACITY; i++) {
        	flightData1.record(i + 1.0);
        	theSum1 += i + 1.0;
        	size1 += 1;
        	flightDataCollection1.add(i + 1.0);
        }

        flightData2 = new FlightDataRecorder();
        flightData2.record(20.0);
        theSum2 += 20.0;
        size2 = 1;
        
        recordCollection = new ArrayList<Double>();
        for (int i = FlightDataRecorder.CAPACITY; i > FlightDataRecorder.CAPACITY - 4; i--) {
        	recordCollection.add((double) i);
        }
        
        average1 = theSum1 / size1;
        average2 = theSum2 / size2;

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Test method for FlightDataRecorder.record().
     */

	@Test
	public void testRecordDataMoreThanCapacity() {
		try {
			flightData1.record(20.0);
		} catch (IndexOutOfBoundsException e) {
			fail("Cannot add flight record. Need iteration.");
		}
	}

	/**
	 * Test method for FlighDataRecorder.getLastDataPoint().
	 */
	@Test
	public void testIndexValue() {
		assertEquals("Index value may be messed up.",
				recordCollection, flightData1.getLastDataPoints(4));
		/** 
		 * Assume getLastDataPoints() messed up index value.
		 * Calling assertEquals() again to see if it passes the test.
		 */
		assertEquals("Index value may be messed up.",
				recordCollection, flightData1.getLastDataPoints(4));
	}
	
	/**
	 * Test method for FlighDataRecorder.average()
	 */
	@Test
	public void testAverageValue1() {
		assertEquals("Average calculation method may be wrong!",
				String.valueOf(average1), String.valueOf(flightData1.average()));
	}
	@Test
	public void testAverageValue2() {
		assertEquals("Average calculation method may be wrong!",
				String.valueOf(average2), String.valueOf(flightData2.average()));
	}
	
	/**
	 * Test method for FlighDataRecorder.getRecordedData().
	 */
	@Test
	public void testAllFlightData() {
		assertEquals("Constructor or record() method may be wrong!!",
				flightDataCollection1, flightData1.getRecordedData());

	}
}
