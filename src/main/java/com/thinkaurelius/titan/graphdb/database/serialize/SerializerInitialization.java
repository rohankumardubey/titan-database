package com.thinkaurelius.titan.graphdb.database.serialize;

import com.thinkaurelius.titan.graphdb.edgetypes.*;
import com.thinkaurelius.titan.graphdb.edgetypes.EdgeCategory;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.DoubleSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.FloatSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.IntegerSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.LongSerializer;
import com.thinkaurelius.titan.graphdb.edgetypes.group.StandardTypeGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SerializerInitialization {

	public static final void initialize(Serializer serializer) {
		serializer.registerClass(StandardPropertyType.class);
		serializer.registerClass(StandardRelationshipType.class);
		serializer.registerClass(EdgeTypeVisibility.class);
		serializer.registerClass(Directionality.class);
		serializer.registerClass(EdgeCategory.class);
		serializer.registerClass(StandardTypeGroup.class);
        serializer.registerClass(Object.class);
        serializer.registerClass(Date.class);
        serializer.registerClass(ArrayList.class);
        serializer.registerClass(HashMap.class);
        serializer.registerClass(int[].class);
        serializer.registerClass(double[].class);
        serializer.registerClass(long[].class);
        serializer.registerClass(byte[].class);
        serializer.registerClass(TitanTypeClass.class);
		serializer.registerClass(Integer.class,new IntegerSerializer());
		serializer.registerClass(Double.class,new DoubleSerializer());
        serializer.registerClass(Float.class,new FloatSerializer());
		serializer.registerClass(Long.class,new LongSerializer());
	}
	
}
