package com.kafkahomework.udf;

import com.sdchomework.geohash.GeoHashSDC;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;

/**
 * This is UDF function to call in Hive
 * for 006_Kafka homework.
 * <p>It is uses GeoHashSDC and extends GenericUDF</p>
 *
 * @author alex
 * @version 1.0
 * @see ch.hsr.geohash.GeoHash
 * @see GenericUDF
 */
public class GeoHashUDF extends GenericUDF {

    GeoHashSDC geoHashSDC = new GeoHashSDC();
    StringObjectInspector longitudeOI;
    StringObjectInspector latitudeOI;


    @Override
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        if (objectInspectors.length != 2)
        {
            throw new UDFArgumentLengthException("arrayContainsExample only takes 2 arguments: List<T>, T");
        }

        ObjectInspector a = objectInspectors[0];
        ObjectInspector b = objectInspectors[1];
        if (!(a instanceof StringObjectInspector) || !(b instanceof StringObjectInspector))
        {
            throw new UDFArgumentException("arguments must be a string");
        }

        this.longitudeOI = (StringObjectInspector) a;
        this.latitudeOI = (StringObjectInspector) b;

        return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        String latitude = latitudeOI.getPrimitiveJavaObject(deferredObjects[0].get());
        String longitude = longitudeOI.getPrimitiveJavaObject(deferredObjects[1].get());
        if(latitude == null || longitude == null)
            return null;
        return geoHashSDC.getGeoHash(latitude, longitude);
    }

    @Override
    public String getDisplayString(String[] strings) {
        return null;
    }
}
