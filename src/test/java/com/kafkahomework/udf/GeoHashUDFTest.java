package com.kafkahomework.udf;

import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The class to test class {@link GeoHashUDF}
 *
 * <p>The testing scenario covers only normal case of working UDF function</p>
 * <p>that calculates geohash </p>
 *
 * @author alex
 * @version 1.0
 * @see GeoHashUDF
 * @see ch.hsr.geohash.GeoHash
 * @see StandaloneHiveRunner
 */
@RunWith(StandaloneHiveRunner.class)
public class GeoHashUDFTest {

    @HiveSQL(files = {})
    private HiveShell shell;

    /**
    * Init testing database
    * Create a table weather with fields
    * <ol>
    *   <li>lng, DOUBLE</li>
    *   <li>lat, DOUBLE</li>
    *   <li>avg_tmpr_f, DOUBLE</li>
    *   <li>avg_tmpr_c, DOUBLE</li>
    *   <li>wthr_date, STRING</li>
    * </ol>
    * Create UDF function geohash
    */
    @Before
    public void setupDatabase() {
        shell.execute("CREATE DATABASE test_db");
        shell.execute(new StringBuilder()
                .append("CREATE TABLE test_db.weather (")
                .append("lng DOUBLE, lat DOUBLE, avg_tmpr_f DOUBLE, avg_tmpr_c DOUBLE, wthr_date STRING")
                .append(")")
                .toString());
        shell.insertInto("test_db", "weather")
                .withColumns("lng", "lat", "avg_tmpr_f", "avg_tmpr_c", "wthr_date")
                .addRow(35.451305, 96.751393, 22.0, 63.4, "2016-10-03")
                .commit();
        shell.execute("ADD JAR resources/geohash-1.0-SNAPSHOT.jar");
        shell.execute("CREATE TEMPORARY FUNCTION geohash as 'com.kafkahomework.udf.GeoHashUDF'");
    }

    /**
     * Call UDF function geohash to compare calculated result with expected
     */
    @Test
    public void useHiveConfValues() {
        final List<Object[]> result = shell.executeStatement("select geohash(lng, lat) from weather");
        assertEquals(result.get(0)[0].toString(), "wnkc");
    }

    @After
    public void tearDown() {
        shell.execute("DROP TABLE IF EXISTS test_db");
    }
}
