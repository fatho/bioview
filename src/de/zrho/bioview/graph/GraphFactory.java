package de.zrho.bioview.graph;

import de.zrho.bioview.model.Complex;
import de.zrho.bioview.model.Network;
import de.zrho.bioview.model.Reaction;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class GraphFactory {

	public static <S, R> Graph<Complex<S>, Reaction<S, R>> createGraph(Network<S, R> network) {
		Graph<Complex<S>, Reaction<S, R>> graph = new SparseMultigraph<>();
		
		for (Complex<S> c : network.getComplexes()) {
			graph.addVertex(c);
		}
		
		for (Reaction<S, R> r : network.getReactions()) {
			graph.addEdge(r, r.getReactant(), r.getProduct(), EdgeType.DIRECTED);
		}
		
		return graph;
	}
	
}
