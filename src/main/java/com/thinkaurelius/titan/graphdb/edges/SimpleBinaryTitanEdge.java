package com.thinkaurelius.titan.graphdb.edges;

import com.google.common.collect.ImmutableList;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.exceptions.InvalidNodeException;
import com.thinkaurelius.titan.graphdb.transaction.InternalTitanTransaction;
import com.thinkaurelius.titan.graphdb.vertices.InternalTitanVertex;
import com.tinkerpop.blueprints.Direction;
import static com.tinkerpop.blueprints.Direction.*;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;

public class SimpleBinaryTitanEdge extends AbstractTypedRelation implements TitanEdge {

	private final InternalTitanVertex start;
	private final InternalTitanVertex end;
	
	public SimpleBinaryTitanEdge(TitanLabel type, InternalTitanVertex start, InternalTitanVertex end) {
		super(type);
		this.start= start;
		this.end = end;
	}
	
	@Override
	public InternalTitanTransaction getTransaction() {
		return start.getTransaction();
	}
	
	@Override
	public int hashCode() {
		if (isUndirected()) {
			return new HashCodeBuilder().append(start.hashCode()+end.hashCode()).append(type).toHashCode();
		} else {
			assert isDirected() || isUnidirected();
			return new HashCodeBuilder().append(start).append(end).append(type).toHashCode();
		}
	}

	@Override
	public boolean equals(Object oth) {
		if (oth==this) return true;
		else if (!(oth instanceof TitanEdge)) return false;
		TitanEdge other = (TitanEdge)oth;
		if (!getType().equals(other.getType())) return false;
		if (isUndirected()) {
			return (start.equals(other.getVertex(OUT)) && end.equals(other.getVertex(IN)))
			|| (start.equals(other.getVertex(IN)) && end.equals(other.getVertex(OUT)));
		} else {
			assert isDirected() || isUnidirected();
			return start.equals(other.getVertex(OUT)) && end.equals(other.getVertex(IN));
		}
	}

    @Override
    public String getLabel() {
        return type.getName();
    }
    
    
	@Override
	public InternalTitanVertex getVertex(int pos) {
		switch(pos) {
		case 0: return start;
		case 1: return end;
		default: throw new ArrayIndexOutOfBoundsException("Exceeded number of vertices of 2 with given position: " + pos);
		}
	}

	@Override
	public boolean isSelfLoop(TitanVertex vertex) {
		if (!start.equals(end)) return false;
		else return vertex ==null || vertex.equals(start);
	}

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public Direction getDirection(TitanVertex vertex) {
        if (start.equals(vertex)) {
            if (end.equals(vertex)) return BOTH;
            else return OUT;
        } else if (end.equals(vertex)) return IN;
        else throw new InvalidNodeException("TitanRelation is not incident on given node.");
	}

	@Override
	public boolean isIncidentOn(TitanVertex vertex) {
		return start.equals(vertex) || end.equals(vertex);
	}

	@Override
	public void forceDelete() {
		start.removeRelation(this);
		if (!isUnidirected())
			end.removeRelation(this);
		super.forceDelete();
	}

	@Override
	public TitanVertex getVertex(Direction dir) {
        switch(dir) {
            case OUT: return start;
            case IN : return end;
            default: throw new IllegalArgumentException("Illegal direction: " + dir);
        }
	}

	@Override
	public TitanVertex getOtherVertex(TitanVertex vertex) {
		if (start.equals(vertex)) return end;
		else if (end.equals(vertex)) return start;
		else throw new InvalidNodeException("TitanRelation is not incident on given node.");
	}

	@Override
	public TitanLabel getTitanLabel() {
		return (TitanLabel)type;
	}

	@Override
	public final boolean isProperty() {
		return false;
	}

	@Override
	public final boolean isEdge() {
		return true;
	}

	@Override
	public String toString() {
		return start.toString() + " - " + getTitanLabel().getName() + " -> " + end.toString();
	}



}
