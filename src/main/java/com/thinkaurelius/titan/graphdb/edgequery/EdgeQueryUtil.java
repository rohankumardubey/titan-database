package com.thinkaurelius.titan.graphdb.edgequery;

import com.google.common.collect.Iterators;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanProperty;
import com.thinkaurelius.titan.graphdb.edgetypes.InternalTitanType;
import com.thinkaurelius.titan.graphdb.vertices.InternalTitanVertex;
import com.thinkaurelius.titan.util.interval.AtomicInterval;

import java.util.Map;

public class EdgeQueryUtil {

	public static final TitanProperty queryHiddenFunctionalProperty(InternalTitanVertex node, TitanKey propType) {
		assert propType.isHidden() : "Expected hidden property type";
		assert propType.isFunctional() : "Expected functional property  type";
		return Iterators.getOnlyElement(
				new AtomicTitanQuery(node).
					includeHidden().
                        type(propType).
                        propertyIterator(),null);
	}

    /**
     * Checks whether the query can be answered by disk indexes alone or whether further processing in memory is necessary
     * to determine the exact result set.
     *
     * @param query Query to check
     * @return
     */
    public static boolean queryCoveredByDiskIndexes(InternalTitanQuery query) {
        if (!query.hasConstraints()) return true;
        if (!query.hasEdgeTypeGroupCondition()) return false;
        String[] keysig = ((InternalTitanType)query.getTypeCondition()).getDefinition().getKeySignature();
        Map<String,Object> constraints = query.getConstraints();
        int num = 0;
        for (String key : keysig) {
            if (!constraints.containsKey(key)) break;
            Object o = constraints.get(key);
            num++;
            if (o!=null && (o instanceof AtomicInterval) && ((AtomicInterval) o).isRange()) {
                if (((AtomicInterval)o).hasHoles()) num--; //Intervals with holes have to be filtered in memory
                break;
            }
        }
        assert num<=constraints.size();
        return num==constraints.size();
    }
    
    public static boolean hasFirstKeyConstraint(InternalTitanQuery query) {
        if (!query.hasConstraints()) return false;
        if (!query.hasEdgeTypeGroupCondition()) return false;
        String[] keysig = ((InternalTitanType)query.getTypeCondition()).getDefinition().getKeySignature();
        return query.getConstraints().containsKey(keysig[0]);
    }
    
}
