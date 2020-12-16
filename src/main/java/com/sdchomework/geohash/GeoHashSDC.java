package com.sdchomework.geohash;

import ch.hsr.geohash.GeoHash;

/**
 * This actually decorator for {@link GeoHash} which was introduced
 * for 005_DataFlow_Pipelining homework.
 * <p>It is used as external library in StreamSets for geo hash calculation</p>
 * <p>Converts String parameters to Double type and uses 
 * {@link GeoHash#geoHashStringWithCharacterPrecision} inside</p>
 * 
 * @author alex
 * @version 1.0
 * @see ch.hsr.geohash.GeoHash#geoHashStringWithCharacterPrecision(double, double, int) 
 */
public class GeoHashSDC {

    private static final int NUMBER_OF_CHARACTERS_BY_DEFAULT = 4;
    private static final String ERROR_HASH_VALUE = "error";

    /**
     *<p>Calculates geo hash according input parameters</p>
     * @param latitude geographic coordinate that specifies the north–south position of a point on the Earth's surface
     * @param longitude geographic coordinate that specifies the east–west position of a point on the Earth's surface
     * @param numberOfCharacters length of the calculating geo hash
     * @return String representation of the geo hash or {@link this#ERROR_HASH_VALUE} in case of non-numeric input
     * @see GeoHash#geoHashStringWithCharacterPrecision(double, double, int)
     */
    public String getGeoHash(String latitude, String longitude, int numberOfCharacters) {
        Double lat;
        Double lon;
        String geoHash;
        try {
            lat = Double.parseDouble(latitude);
            lon = Double.parseDouble(longitude);
            geoHash = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, numberOfCharacters);
        }
        catch(NumberFormatException e) {
            geoHash = ERROR_HASH_VALUE;
        }
        return geoHash;
    }

    /**
     *<p>Calculates geo hash according input parameters with default value of number of characters</p>
     *<p>{@link this#NUMBER_OF_CHARACTERS_BY_DEFAULT}</p>
     * @param latitude geographic coordinate that specifies the north–south position of a point on the Earth's surface
     * @param longitude geographic coordinate that specifies the east–west position of a point on the Earth's surface
     * @return String representation of the geo hash
     * @see GeoHashSDC#getGeoHash(String, String, int)
     */
    public String getGeoHash(String latitude, String longitude) {
        return getGeoHash(latitude, longitude, NUMBER_OF_CHARACTERS_BY_DEFAULT);
    }

}
