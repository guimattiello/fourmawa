package com.general.mbts4ma.view.framework.bo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.github.eta.esg.EventSequenceGraph;
import com.github.eta.esg.GenericVertex;
import com.github.eta.esg.Vertex;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public abstract class GraphConverter {

	private static Map<String, GenericVertex> esgNodes = new LinkedHashMap<String, GenericVertex>();
	private static Map<GenericVertex, List<GenericVertex>> esgEventFlow = new LinkedHashMap<GenericVertex, List<GenericVertex>>();

	public static synchronized String getEventFlow() {
		StringBuilder sb = new StringBuilder("");

		if (esgEventFlow != null && !esgEventFlow.isEmpty()) {
			Iterator<GenericVertex> iEventFlow = esgEventFlow.keySet().iterator();

			while (iEventFlow.hasNext()) {
				GenericVertex source = iEventFlow.next();

				List<GenericVertex> targets = esgEventFlow.get(source);

				if (targets != null && !targets.isEmpty()) {
					for (GenericVertex target : targets) {
						if (sb.length() > 0) {
							sb.append("\r\n");
						}

						sb.append(source.getName()).append(" -> ").append(target.getName());
					}
				}

			}
		}

		return sb.toString();
	}

	public static synchronized void printEventFlow() {
		System.out.println("#########################");

		getEventFlow();

		System.out.println("#########################");
	}

	public static synchronized EventSequenceGraph convertToESG(mxGraph graph) throws Exception {
		esgNodes.clear();
		esgEventFlow.clear();

		EventSequenceGraph esg = createESG(getVertices(graph));

		return esg;
	}

	private static synchronized Map<Vertex, List<Vertex>> getVertices(mxGraph graph) {
		Map<Vertex, List<Vertex>> vertices = new LinkedHashMap<Vertex, List<Vertex>>();

		for (Object vertice : graph.getChildCells(graph.getDefaultParent(), true, false)) {
			mxCell cell = (mxCell) vertice;

			List<Vertex> verticesChildren = new LinkedList<Vertex>();
			List<String> verticesChildrenId = new LinkedList<String>();

			if (cell.getEdgeCount() > 0) {
				for (int i = 0; i < cell.getEdgeCount(); i++) {
					mxCell edge = (mxCell) cell.getEdgeAt(i);

					boolean isOutgoingEdge = !edge.getTarget().equals(cell);

					isOutgoingEdge = isOutgoingEdge || edge.getSource().equals(edge.getTarget());

					if (isOutgoingEdge && !verticesChildrenId.contains(edge.getTarget().getId())) {
						verticesChildrenId.add(edge.getTarget().getId());

						verticesChildren.add(new GenericVertex(edge.getTarget().getId(), (String) edge.getTarget().getValue()));
					}
				}
			}

			vertices.put(new GenericVertex(cell.getId(), (String) cell.getValue()), verticesChildren);
		}

		return vertices;
	}

	private static synchronized EventSequenceGraph createESG(Map<Vertex, List<Vertex>> vertices) throws Exception {
		EventSequenceGraph esg = new EventSequenceGraph();

		if (!vertices.isEmpty()) {
			Iterator<Vertex> iVertices = vertices.keySet().iterator();

			while (iVertices.hasNext()) {
				Vertex source = iVertices.next();
				List<Vertex> targets = vertices.get(source);

				registerEventFlow(source, targets);

				if (targets != null && !targets.isEmpty()) {
					for (Vertex target : targets) {
						esg.addEdge(verifyNode(esg, source), verifyNode(esg, target));
					}
				}
			}
		}

		return esg;
	}

	@SuppressWarnings("unchecked")
	private static synchronized void registerEventFlow(Vertex source, List<Vertex> targets) {
		List<GenericVertex> vertices = new LinkedList<GenericVertex>();

		if (esgEventFlow.containsKey(source)) {
			vertices = esgEventFlow.get(source);
		}

		vertices.addAll((List<GenericVertex>) (List<?>) targets);

		esgEventFlow.put((GenericVertex) source, vertices);
	}

	private static synchronized GenericVertex verifyNode(EventSequenceGraph esg, Vertex vertex) {
		GenericVertex genericVertex = (GenericVertex) vertex;

		boolean vertexExists = esgNodes.containsKey(genericVertex.getId());

		if (!vertexExists) {
			esg.addVertex(vertex);

			esgNodes.put(genericVertex.getId(), genericVertex);
		} else {
			genericVertex = esgNodes.get(genericVertex.getId());
		}

		return genericVertex;
	}

}
