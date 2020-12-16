package com.sdchomework.geohash;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The class to test class {@link GeoHashSDC}
 *
 * <p>The testing scenario covers following points:</p>
 * <ol>
 *   <li>Valid input for any parameters</li>
 *   <li>The string length equals 4 by default</li>
 *   <li>Extremum cases for longitude and latitude: (MAX, MAX), (MIN, MIN), (MAX, MIN), (MIN, MAX)</li>
 *   <li>Out of limit values for longitude and latitude parameters</li>
 *   <li>Out of limit values for character precision parameter</li>
 *   <li>Non-numeric strings as input longitude and latitude</li>
 * </ol>
 *
 * <p>The test is performed for both methods:
 * {@link GeoHashSDC#getGeoHash(String, String)}
 * and {@link GeoHashSDC#getGeoHash(String, String, int)}</p>
 *
 * @author alex
 * @version 1.0
 * @see GeoHashSDC
 * @see ch.hsr.geohash.GeoHash
 */
public class GeoHashSDCTest {

    private GeoHashSDC geoHashSDC = new GeoHashSDC();

    private static final int MAX_CHARACTER_PRECISION = 12;
    private static final int CHARACTER_PRECISION_BY_DEFAULT = 4;
    private static final double MAX_LATITUDE_VALUE = 90.0D ;
    private static final double MIN_LATITUDE_VALUE = -90.0D;
    private static final double MAX_LONGITUDE_VALUE = 180.0D;
    private static final double MIN_LONGITUDE_VALUE = -180.0D;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Testing normal case where all passed parameters are valid.
     * Final assert checks that returned values of both methods are equals.
     * Because {@link GeoHashSDC#getGeoHash(String, String)} uses CHARACTER_PRECISION_BY_DEFAULT inside
     */
    @Test
    public void geoHashSDC_getGeoHash_CommonCaseTest_OK() {

        String resultWithoutDefaultNumberOfPrec = geoHashSDC.getGeoHash("35.451305", "96.751393");
        assertEquals(resultWithoutDefaultNumberOfPrec, "wnkc");

        String resultWithinDefaultNumberOfPrec = geoHashSDC.getGeoHash("35.451305", "96.751393", CHARACTER_PRECISION_BY_DEFAULT);
        assertEquals(resultWithinDefaultNumberOfPrec, "wnkc");

        assertEquals(resultWithoutDefaultNumberOfPrec, resultWithinDefaultNumberOfPrec);

    }

    /**
     * Testing default geo hash length. By default this value is 4.
     * @see GeoHashSDC#getGeoHash(String, String)
     */
    @Test
    public void geoHashSDC_getGeoHash_GeoHashLengthIs4() {

        String result;

        result = geoHashSDC.getGeoHash("35.451305", "96.751393");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash("1", "2");
        assertTrue(result.length()==4);

        result = geoHashSDC.getGeoHash("4", "3");
        assertTrue(result.length()==4);

    }

    /**
     * Testing extremum cases where all passed parameters are on limits of valid values.
     */
    @Test
    public void geoHashSDC_getGeoHash_ExtremumCaseTest_OK() {

        String result;

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        assertEquals(result, "zzzz");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        assertEquals(result, "0000");

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        assertEquals(result, "bpbp");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        assertEquals(result, "pbpb");

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "", 0);
        assertEquals(result, "");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "", 0);
        assertEquals(result, "");

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "", 0);
        assertEquals(result, "");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "", 0);
        assertEquals(result, "");

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "", MAX_CHARACTER_PRECISION);
        assertEquals(result, "zzzzzzzzzzzz");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "", MAX_CHARACTER_PRECISION);
        assertEquals(result, "000000000000");

        result = geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "", MAX_CHARACTER_PRECISION);
        assertEquals(result, "bpbpbpbpbpbp");

        result = geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "", MAX_CHARACTER_PRECISION);
        assertEquals(result, "pbpbpbpbpbpb");

    }

    /**
     * Testing out of limits values for numberOfCharacters parameter.
     * Expecting IllegalArgumentException with message <q>A geohash can only be 12 character long.</q>
     * @see ch.hsr.geohash.GeoHash#geoHashStringWithCharacterPrecision(double, double, int)
     * @see org.junit.Rule
     * @see ExpectedException#expect(Class)
     * @see ExpectedException#expectMessage(String)
     */
    @Test
    public void geoHashSDC_getGeoHash_OutOfValidCharPrecValuesLimitsCaseTest_IllegalArgumentException() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("A geohash can only be 12 character long.");

        geoHashSDC.getGeoHash("35.451305", "35.451305", -1);
        geoHashSDC.getGeoHash("35.451305", "35.451305", MAX_CHARACTER_PRECISION + 1);

    }

    /**
     * Testing out of range values for longitude and latitude parameters.
     * Expecting IllegalArgumentException with the message <q>The supplied coordinates (lon,lat) are out of range.</q>
     * @see GeoHashSDC#getGeoHash(String, String)
     * @see org.junit.Rule
     * @see ExpectedException#expect(Class)
     * @see ExpectedException#expectMessage(String)
     */
    @Test
    public void geoHashSDC_getGeoHash_OutOfValidLonLatValuesLimitsCaseTest_IllegalArgumentException() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The supplied coordinates");
        thrown.expectMessage("are out of range");

        geoHashSDC.getGeoHash(2*MAX_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(2*MIN_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(2*MAX_LATITUDE_VALUE + "", MIN_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(2*MIN_LATITUDE_VALUE + "", MAX_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", 2*MAX_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", 2*MIN_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(MAX_LATITUDE_VALUE + "", 2*MIN_LONGITUDE_VALUE + "");
        geoHashSDC.getGeoHash(MIN_LATITUDE_VALUE + "", 2*MAX_LONGITUDE_VALUE + "");

        geoHashSDC.getGeoHash(Double.POSITIVE_INFINITY + "", Double.POSITIVE_INFINITY + "");
        geoHashSDC.getGeoHash(Double.NEGATIVE_INFINITY + "", Double.POSITIVE_INFINITY + "");
        geoHashSDC.getGeoHash(Double.POSITIVE_INFINITY + "", Double.NEGATIVE_INFINITY + "");
        geoHashSDC.getGeoHash(Double.NEGATIVE_INFINITY + "", Double.NEGATIVE_INFINITY + "");

    }

    /**
     * Testing non-numeric values for longitude and latitude parameters.
     * Expecting get <q>error</q> as a geo hash for further handling
     * @see GeoHashSDC#getGeoHash(String, String)
     * @see GeoHashSDC#getGeoHash(String, String, int)
     */
    @Test
    public void geoHashSDC_getGeoHash_NonNumericStringCaseTest_SetByDefaultErrorValue() {

        String result;

        result = geoHashSDC.getGeoHash("N/A", "35.451305");
        assertEquals(result, "error");

        result = geoHashSDC.getGeoHash("35.451305", "N/A");
        assertEquals(result, "error");

        result = geoHashSDC.getGeoHash("N/A", "N/A");
        assertEquals(result, "error");

        result = geoHashSDC.getGeoHash("N/A", "35.451305", CHARACTER_PRECISION_BY_DEFAULT);
        assertEquals(result, "error");

        result = geoHashSDC.getGeoHash("35.451305", "N/A", CHARACTER_PRECISION_BY_DEFAULT);
        assertEquals(result, "error");

        result = geoHashSDC.getGeoHash("N/A", "N/A", CHARACTER_PRECISION_BY_DEFAULT);
        assertEquals(result, "error");

    }

}
