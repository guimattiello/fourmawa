package com.general.mbts4ma.view.framework.graph;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import com.general.mbts4ma.view.MainView;
import com.general.mbts4ma.view.dialog.EventPropertiesDialog;
import com.general.mbts4ma.view.framework.util.FileUtil;
import com.general.mbts4ma.view.framework.util.MapUtil;
import com.general.mbts4ma.view.framework.util.StringUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class CustomGraphActions {

	static final Action deleteAction = new DeleteAction("delete");

	public static Action getDeleteAction() {
		return deleteAction;
	}

	static final Action editAction = new EditAction("edit");

	public static Action getEditAction() {
		return editAction;
	}

	static final Action displayIdAction = new DisplayIdAction("displayid");

	public static Action getDisplayIdAction() {
		return displayIdAction;
	}
	
	//static final Action displayMethodAndParams = new DisplayIdAction("displaymethodandparams", graphProject);

	public static Action getDisplayMethodAndParamsAction(GraphProjectVO graphProject) {
		return new DisplayMethodAndParamsAction("displaymethodandparams", graphProject);
	}

	static final Action selectAllEdgesAction = new SelectAllEdgesAction("selectalledges");

	public static Action getSelectAllEdgesAction() {
		return selectAllEdgesAction;
	}

	static final Action selectAllVerticesAction = new SelectAllVerticesAction("selectallvertices");

	public static Action getSelectAllVerticesAction() {
		return selectAllVerticesAction;
	}

	public static Action getDefineMethodTemplateAction(GraphProjectVO graphProject, String label) {
		return new DefineMethodTemplateAction("definemethodtemplate", graphProject, label);
	}

	public static Action getDefineEdgeTemplateAction(GraphProjectVO graphProject, String label) {
		return new DefineEdgeTemplateAction("defineedgetemplate", graphProject, label);
	}

	public static Action getClearMethodTemplateAction(GraphProjectVO graphProject) {
		return new ClearMethodTemplateAction("clearmethodtemplate", graphProject);
	}

	public static Action getClearEdgeTemplateAction(GraphProjectVO graphProject) {
		return new ClearEdgeTemplateAction("clearedgetemplate", graphProject);
	}

	static final Action selectAllAction = new SelectAllAction("selectall");

	public static Action getSelectAllAction() {
		return selectAllAction;
	}

	public static class DeleteAction extends AbstractAction {

		public DeleteAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

						if (result == JOptionPane.YES_OPTION) {
							graph.removeCells();
						}
					}
				}
			}
		}

	}

	public static class EditAction extends AbstractAction {

		public EditAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graphComponent != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						int result = JOptionPane.showOptionDialog(null, "Are you sure you want to edit?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

						if (result == JOptionPane.YES_OPTION) {
							graphComponent.startEditing();
						}
					}
				}
			}
		}

	}

	public static class DisplayIdAction extends AbstractAction {

		public DisplayIdAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					for (Object selectedCell : selectedCells) {
						mxCell cell = (mxCell) selectedCell;
						String message = (isVertex(cell) ? "Vertex" : "Edge") + " ID";

						JOptionPane.showMessageDialog(null, message + " : " + cell.getId(), message, JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}

	}
	
	public static class DisplayMethodAndParamsAction extends AbstractAction {

		private GraphProjectVO graphProject = null;
		
		public DisplayMethodAndParamsAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					for (Object selectedCell : selectedCells) {
						mxCell cell = (mxCell) selectedCell;

						String methodName = this.graphProject.getMethodTemplatesByVertices().get(cell.getId());
						Map<String, String> properties = graphProject.getMethodTemplatesPropertiesByVertices().get(cell.getId());
						
						String prop = "";
						if (properties != null && !properties.isEmpty()) {
							Iterator<String> iProperties = properties.keySet().iterator();

							while (iProperties.hasNext()) {
								String key = iProperties.next();
								String value = properties.get(key);
								
								prop += "=> " + key + " = " + value + "\n";
							}
							
						}
						
						JOptionPane.showMessageDialog(null, methodName + "\n\n" + prop, "Display Method Name", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}

	}

	public static class SelectAllEdgesAction extends AbstractAction {

		public SelectAllEdgesAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectEdges();
			}
		}

	}

	public static class SelectAllVerticesAction extends AbstractAction {

		public SelectAllVerticesAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectVertices();
			}
		}

	}

	public static class DefineMethodTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;
		private String label = null;

		public DefineMethodTemplateAction(String name, GraphProjectVO graphProject, String label) {
			super(name);
			this.graphProject = graphProject;
			this.label = label;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell vertice = (mxCell) selectedCell;

						if (isVertex(vertice) && !isStartVertex(vertice) && !isEndVertex(vertice) && !isGeneratedEventVertex(vertice)) {
							vertice.setStyle(MainView.EVENT_VERTEX);

							this.graphProject.updateMethodTemplateByVertice(vertice.getId(), this.label);

							if (!this.graphProject.getIsWebProject()) {
								
								String methodTemplateContent = FileUtil.readFile(new File("templates" + File.separator + "robotium-methods" + File.separator + this.label.replace(" ", "") + ".java"));

								List<String> values = StringUtil.getValuesWithRegEx(methodTemplateContent, "\\{\\{([a-z]+)\\}\\}");

								if (values != null && !values.isEmpty()) {
									EventPropertiesDialog dialog = new EventPropertiesDialog(this.graphProject, MapUtil.fromList(values));

									dialog.setVisible(true);

									this.graphProject.updateMethodTemplatePropertiesByVertice(vertice.getId(), dialog.getValues());
								}
								
							} else {
								//Se for projeto Web ele deve pegar os parametros
								String parametros = this.label.substring(this.label.indexOf("(")+1, this.label.indexOf(")"));								
								
								String[] arrParametros = parametros.split(",");
								
								List<String> values = Arrays.asList(arrParametros);
								
								if (!values.get(0).equals("")) {
									EventPropertiesDialog dialog = new EventPropertiesDialog(this.graphProject, MapUtil.fromList(values));

									dialog.setVisible(true);

									this.graphProject.updateMethodTemplatePropertiesByVertice(vertice.getId(), dialog.getValues());
								}								
							}							

						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class DefineEdgeTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;
		private String label = null;

		public DefineEdgeTemplateAction(String name, GraphProjectVO graphProject, String label) {
			super(name);
			this.graphProject = graphProject;
			this.label = label;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell edge = (mxCell) selectedCell;

						if (!isVertex(edge) && !isGeneratedEdge(edge)) {
							edge.setValue(this.label);

							edge.setStyle(MainView.MARKED_EDGE);

							this.graphProject.updateEdgeTemplateByVertice(edge.getId(), this.label);
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class ClearMethodTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;

		public ClearMethodTemplateAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell vertice = (mxCell) selectedCell;

						if (isVertex(vertice) && !isStartVertex(vertice) && !isEndVertex(vertice) && !isGeneratedEventVertex(vertice)) {
							vertice.setStyle(MainView.NORMAL_VERTEX);

							this.graphProject.removeMethodTemplateByVertice(vertice.getId());
							this.graphProject.removeMethodTemplatePropertiesByVertice(vertice.getId());
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class ClearEdgeTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;

		public ClearEdgeTemplateAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell edge = (mxCell) selectedCell;

						if (!isVertex(edge) && !isGeneratedEdge(edge)) {
							edge.setValue("");
							edge.setStyle("");

							this.graphProject.removeEdgeTemplateByVertice(edge.getId());
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class SelectAllAction extends AbstractAction {

		public SelectAllAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectAll();
			}
		}

	}

	public static final mxGraphComponent getGraphComponent(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof mxGraphComponent) {
			return (mxGraphComponent) source;
		}

		return null;
	}

	public static final mxGraph getGraph(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) source;

			return graphComponent.getGraph();
		}

		return null;
	}

	private static synchronized boolean hasStartOrEndEdges(Object[] selectedCells) {
		for (Object o : selectedCells) {
			mxCell cell = (mxCell) o;

			if (cell.getId().equalsIgnoreCase(MainView.ID_START_VERTEX) || cell.getId().equalsIgnoreCase(MainView.ID_END_VERTEX)) {
				return true;
			}
		}

		return false;
	}

	private static synchronized boolean isVertex(mxCell cell) {
		return cell.isVertex();
	}

	private static synchronized boolean isStartVertex(mxCell cell) {
		return cell.getId().equalsIgnoreCase(MainView.ID_START_VERTEX);
	}

	private static synchronized boolean isEndVertex(mxCell cell) {
		return cell.getId().equalsIgnoreCase(MainView.ID_END_VERTEX);
	}

	private static synchronized boolean isGeneratedEventVertex(mxCell cell) {
		return cell.getStyle().equalsIgnoreCase(MainView.GENERATED_EVENT_VERTEX);
	}

	private static synchronized boolean isGeneratedEdge(mxCell cell) {
		return cell.getStyle().equalsIgnoreCase(MainView.GENERATED_EDGE);
	}

}
