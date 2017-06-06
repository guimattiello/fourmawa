package com.general.mbts4ma.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.general.mbts4ma.view.dialog.ExtractCESsDialog;
import com.general.mbts4ma.view.dialog.ExtractEventFlowDialog;
import com.general.mbts4ma.view.dialog.ProjectPropertiesDialog;
import com.general.mbts4ma.view.dialog.WebProjectPropertiesDialog;
import com.general.mbts4ma.view.framework.bo.GraphConverter;
import com.general.mbts4ma.view.framework.bo.GraphProjectBO;
import com.general.mbts4ma.view.framework.bo.GraphSolver;
import com.general.mbts4ma.view.framework.graph.CustomGraphActions;
import com.general.mbts4ma.view.framework.util.HardwareUtil;
import com.general.mbts4ma.view.framework.util.JavaParserUtil;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.github.eta.esg.Vertex;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import jdk.internal.org.objectweb.asm.MethodVisitor;

public class MainView extends JFrame {

	public static final String MARKED_EDGE = "MARKED_EDGE";
	public static final String GENERATED_EDGE = "GENERATED_EDGE";
	public static final String NORMAL_VERTEX = "NORMAL_VERTEX";
	public static final String EVENT_VERTEX = "EVENT_VERTEX";
	public static final String GENERATED_EVENT_VERTEX = "GENERATED_EVENT_VERTEX";
	public static final String START_VERTEX = "START_VERTEX";
	public static final String END_VERTEX = "END_VERTEX";

	public static final String ID_START_VERTEX = "1000";
	public static final String ID_END_VERTEX = "2000";
	
	private static final long serialVersionUID = 8273385277816531639L;
	
	public static ArrayList<String> metodos;
	public static ArrayList<ArrayList<String>> metodosWeb;
	public static ArrayList<String> pageObjectsPath;
	public static ArrayList<PageObject> pageObjects;
	
	private boolean isWebProject;
	
	private JPanel contentPane;

	private mxGraph graph = null;
	private mxGraphComponent graphComponent = null;

	public static final String MY_CUSTOM_VERTEX_STYLE = "MY_CUSTOM_VERTEX_STYLE";
	public static final String MY_CUSTOM_EDGE_STYLE = "MY_CUSTOM_EDGE_STYLE";

	private GraphProjectVO graphProject = null;

	private JButton btnNew;
	
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnPreferences;
	private JButton btnClose;
	private JButton btnExecuteGraph;
	private JButton btnExit;

	private JMenu mnFile;
	private JMenu mnProject;
	private JMenu mnSettings;

	private JMenuItem mnItemNew;
	private JMenuItem mnItemNewWebApplicationProject;
	private JMenuItem mnItemOpen;
	private JMenuItem mnItemSave;
	private JMenuItem mnItemClose;
	private JMenuItem mnItemExit;

	private JMenuItem mnItemProperties;
	private JMenuItem mnItemExtractEventFlow;
	private JMenu mnItemExportGraph;
	private JMenu mnItemImportGraph;

	private JMenuItem mnItemToPngImage;
	private JMenuItem mnItemToXml;
	private JMenuItem mnItemFromXml;

	private JMenuItem mnItemExtractCESs;
	private JMenuItem mnItemPreferences;
	private JButton btnProperties;
	private JButton btnExtractEventFlow;
	private JButton btnGenerateReusedEsg;
	private JMenuItem mnItemGenerateReusedESG;

	
	public MainView() {
				
		MainView.metodos = new ArrayList<String>();
		MainView.pageObjects = new ArrayList<PageObject>();
        
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 600, 450);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.setJMenuBar(menuBar);

		this.mnFile = new JMenu("File");
		this.mnFile.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnFile);

		this.mnItemNew = new JMenuItem("New");
		this.mnItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNew);
		
		this.mnItemNewWebApplicationProject = new JMenuItem("New Web App Project");
		this.mnItemNewWebApplicationProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newWebAppProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNewWebApplicationProject);

		this.mnItemOpen = new JMenuItem("Open");
		this.mnItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.mnItemOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemOpen);

		this.mnItemSave = new JMenuItem("Save");
		this.mnItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.mnItemSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemSave);

		this.mnItemClose = new JMenuItem("Close");
		this.mnItemClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.mnItemClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemClose);

		JSeparator separator = new JSeparator();
		this.mnFile.add(separator);

		this.mnItemExit = new JMenuItem("Exit");
		this.mnItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.mnItemExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemExit);

		this.mnProject = new JMenu("Project");
		this.mnProject.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnProject);

		this.mnItemProperties = new JMenuItem("Properties");
		this.mnItemProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.mnItemProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemProperties);

		this.mnItemExtractEventFlow = new JMenuItem("Extract event flow");
		this.mnItemExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.extractEventFlow();
			}
		});
		this.mnItemExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExtractEventFlow);

		this.mnItemExtractCESs = new JMenuItem("Extract CESs");
		this.mnItemExtractCESs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.extractCESs();
			}
		});
		this.mnProject.add(this.mnItemExtractCESs);
		this.mnItemExtractCESs.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.mnItemGenerateReusedESG = new JMenuItem("Generate reused ESG");
		this.mnItemGenerateReusedESG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.mnItemGenerateReusedESG.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemGenerateReusedESG);

		JSeparator separator_1 = new JSeparator();
		this.mnProject.add(separator_1);

		this.mnItemExportGraph = new JMenu("Export graph");
		this.mnItemExportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExportGraph);

		this.mnItemToPngImage = new JMenuItem("to PNG (Image)");
		this.mnItemToPngImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToPng();
			}
		});
		this.mnItemToPngImage.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToPngImage);

		JSeparator separator_3 = new JSeparator();
		this.mnItemExportGraph.add(separator_3);

		this.mnItemToXml = new JMenuItem("to XML");
		this.mnItemToXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToXml();
			}
		});
		this.mnItemToXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToXml);

		this.mnItemImportGraph = new JMenu("Import graph");
		this.mnItemImportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemImportGraph);

		this.mnItemFromXml = new JMenuItem("from XML");
		this.mnItemFromXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.importFromXml();
			}
		});
		this.mnItemFromXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemImportGraph.add(this.mnItemFromXml);

		this.mnSettings = new JMenu("Settings");
		this.mnSettings.setEnabled(false);
		this.mnSettings.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnSettings);

		this.mnItemPreferences = new JMenuItem("Preferences");
		this.mnItemPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnSettings.add(this.mnItemPreferences);

		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));

		this.setContentPane(this.contentPane);

		this.init();

		this.updateControllers();
	}

	private void updateControllers() {
		boolean isProjectOpened = this.graphProject != null;

		this.btnNew.setEnabled(!isProjectOpened);
		this.btnOpen.setEnabled(!isProjectOpened);
		this.btnSave.setEnabled(isProjectOpened);
		this.btnClose.setEnabled(isProjectOpened);

		this.btnProperties.setEnabled(isProjectOpened);
		this.btnExtractEventFlow.setEnabled(isProjectOpened);
		this.btnExecuteGraph.setEnabled(isProjectOpened);
		this.btnGenerateReusedEsg.setEnabled(isProjectOpened);

		this.btnPreferences.setEnabled(false);
		this.btnExit.setEnabled(true);

		this.mnItemNew.setEnabled(!isProjectOpened);
		this.mnItemOpen.setEnabled(!isProjectOpened);
		this.mnItemSave.setEnabled(isProjectOpened);
		this.mnItemClose.setEnabled(isProjectOpened);

		this.mnProject.setEnabled(isProjectOpened);

		this.mnItemPreferences.setEnabled(true);
		this.mnItemExit.setEnabled(true);
	}

	private void initGraph() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.graph = new mxGraph();

		this.graph.setCellsEditable(true);

		this.graphComponent = new mxGraphComponent(this.graph);

		this.graphComponent.getViewport().setOpaque(true);
		this.graphComponent.getViewport().setBackground(Color.WHITE);
		// this.graphComponent.setGridVisible(true);
		// this.graphComponent.setGridColor(Color.GRAY);

		this.graphComponent.setToolTips(true);

		this.setCustomStylesheet();

		this.configureKeyboardEvents();

		/* this.graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
		
			@Override
			public void invoke(Object sender, mxEventObject event) {
				try {
					System.out.println(((mxCell) ((mxGraphSelectionModel) sender).getCell()).getValue());
				} catch (Exception e) {
				}
			}
		}); */

		this.graph.addListener(mxEvent.CELLS_REMOVED, new mxIEventListener() {

			@Override
			public void invoke(Object o, mxEventObject eo) {
				Object[] cells = (Object[]) eo.getProperty("cells");

				for (Object oCell : cells) {
					MainView.this.graphProject.removeMethodTemplateByVertice(((mxCell) oCell).getId());
					MainView.this.graphProject.removeMethodTemplatePropertiesByVertice(((mxCell) oCell).getId());
					MainView.this.graphProject.removeEdgeTemplateByVertice(((mxCell) oCell).getId());
				}
			}
		});

		this.graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					String nodeValue = JOptionPane.showInputDialog(null, "Enter the value of the node", "Attention", JOptionPane.INFORMATION_MESSAGE);

					if (nodeValue != null && !"".equalsIgnoreCase(nodeValue)) {
						MainView.this.graph.getModel().beginUpdate();

						MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), UUID.randomUUID().toString(), nodeValue, e.getX() - 50, e.getY() - 25, 100, 50, NORMAL_VERTEX);

						MainView.this.graph.getModel().endUpdate();
					}
				} else {
					if (SwingUtilities.isMiddleMouseButton(e)) {
						MainView.this.graphComponent.zoomActual();
					} else if (SwingUtilities.isRightMouseButton(e)) {
						final JPopupMenu popup = new JPopupMenu();
						popup.add(MainView.this.bind("Delete", CustomGraphActions.getDeleteAction()));
						popup.add(MainView.this.bind("Rename", CustomGraphActions.getEditAction()));
						popup.add(MainView.this.bind("Display ID", CustomGraphActions.getDisplayIdAction()));
						popup.add(MainView.this.bind("Display Method", CustomGraphActions.getDisplayMethodAndParamsAction(MainView.this.graphProject)));

						popup.addSeparator();

						if (!graphProject.getIsWebProject()){
						
							popup.add(MainView.this.bind("Select All Edges", CustomGraphActions.getSelectAllEdgesAction()));
							popup.add(MainView.this.bind("Select All Vertices", CustomGraphActions.getSelectAllVerticesAction()));
	
							popup.addSeparator();
	
							final JMenu methodTemplatesMenu = new JMenu("Method Templates");
	
							methodTemplatesMenu.add(MainView.this.bind("Clear Method Template", CustomGraphActions.getClearMethodTemplateAction(MainView.this.graphProject)));
	
							methodTemplatesMenu.addSeparator();
	
							Map<String, String> methodTemplates = GraphProjectBO.getMethodTemplates();
	
							Iterator<String> iMethodTemplates = methodTemplates.keySet().iterator();
	
							while (iMethodTemplates.hasNext()) {
								String key = iMethodTemplates.next();
	
								methodTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(methodTemplatesMenu);
	
							final JMenu edgeTemplatesMenu = new JMenu("Edge Templates");
	
							edgeTemplatesMenu.add(MainView.this.bind("Clear Edge Template", CustomGraphActions.getClearEdgeTemplateAction(MainView.this.graphProject)));
	
							edgeTemplatesMenu.addSeparator();
	
							Map<String, String> edgeTemplates = GraphProjectBO.getEdgeTemplates();
	
							Iterator<String> iEdgeTemplates = edgeTemplates.keySet().iterator();
	
							while (iEdgeTemplates.hasNext()) {
								String key = iEdgeTemplates.next();
	
								edgeTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineEdgeTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(edgeTemplatesMenu);
							
						} else { //Senão é web project
							ArrayList<String> pageObjectsPath = new ArrayList(Arrays.asList(graphProject.getWebProjectPageObject().split(",")));
							
							JMenu pageObjectsTemplatesMenu;
							
							Map<String, String> metodosWeb;
							
							//for (Iterator<String> i = pageObjectsPath.iterator(); i.hasNext(); ){
							for (Iterator<PageObject> i = graphProject.getPageObjects().iterator(); i.hasNext(); ) {
								
								PageObject pageObjectNext = i.next();
								
								String fileContentPageObject = pageObjectNext.getContent();
								String fileNamePageObject = pageObjectNext.getClassName();
								
								pageObjectsTemplatesMenu = new JMenu(fileNamePageObject);
								
								metodosWeb = new LinkedHashMap<String, String>();
								
								JavaParserUtil.getMethodNames(fileContentPageObject);
								
								for (String nome : MainView.metodos){
			
									String key = nome;
									
									pageObjectsTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, key)));
			
								}
								
								popup.add(pageObjectsTemplatesMenu);
								
							}
						}
						
						popup.show(MainView.this.graphComponent, e.getX(), e.getY());
					}
				}
			}

		});

		this.graphComponent.getGraphControl().addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					MainView.this.graphComponent.zoomIn();
				} else {
					MainView.this.graphComponent.zoomOut();
				}
			}
		});

		this.getContentPane().add(this.graphComponent);
		this.refreshContentPane();
	}

	@SuppressWarnings("serial")
	private Action bind(String name, final Action action) {

		return new AbstractAction(name, null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(MainView.this.graphComponent, e.getID(), e.getActionCommand()));
			}
		};

	}

	private void refreshContentPane() {
		this.contentPane.revalidate();
		this.contentPane.repaint();
	}

	private void createBasicGraph() {
		MainView.this.graph.getModel().beginUpdate();

		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_START_VERTEX, "[", 100, 50, 50, 50, START_VERTEX);
		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_END_VERTEX, "]", 400, 50, 50, 50, END_VERTEX);

		MainView.this.graph.getModel().endUpdate();
	}

	private void newProject() {
		this.graphProject = null;

		ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			this.initGraph();

			this.createBasicGraph();
		}

		this.updateControllers();
	}

	private void newWebAppProject() {
		this.graphProject = null;

		WebProjectPropertiesDialog dialog = new WebProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			graphProject.setIsWebProject(true);
			
			this.initGraph();
			
			this.createBasicGraph();
		}

		this.updateControllers();
	}
	
	private void openProject() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
		fileChooser.setDialogTitle("Specify a file to open");

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			this.load(fileChooser.getSelectedFile().getAbsolutePath());
		}

		this.updateControllers();
	}

	private void load(String path) {
		this.graphProject = GraphProjectBO.open(path);

		if (this.graphProject != null) {
			this.initGraph();

			try {
				GraphProjectBO.loadGraphFromXML(this.graph, this.graphProject.getGraphXML());

				JOptionPane.showMessageDialog(null, "Project successfully opened.", "Attention", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.updateControllers();
	}

	private void saveProject() {
		String fileSavingPath = null;

		if (this.graphProject.hasFileSavingPath()) {
			fileSavingPath = this.graphProject.getFileSavingPath();
		} else {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

			fileChooser.setSelectedFile(new File(this.graphProject.getName()));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
			fileChooser.setDialogTitle("Specify a file to save");

			int result = fileChooser.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				fileSavingPath = fileChooser.getSelectedFile().getAbsolutePath();
			}
		}

		if (fileSavingPath != null) {
			GraphProjectBO.updateGraph(this.graphProject, this.graph);

			this.graphProject.setLastDate(new Date());
			this.graphProject.setUser(HardwareUtil.getComputerName());

			if (GraphProjectBO.save(fileSavingPath, this.graphProject)) {
				JOptionPane.showMessageDialog(null, "Project successfully saved.", "Attention", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		this.updateControllers();
	}

	private void closeProject() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.refreshContentPane();

		this.graphProject = null;

		this.updateControllers();
	}

	private void displayProjectProperties() {
		if (this.graphProject != null) {
			if (this.graphProject.getIsWebProject()){
				WebProjectPropertiesDialog dialog = new WebProjectPropertiesDialog(this.graphProject);
				
				dialog.setVisible(true);
			}else{
				ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);
	
				dialog.setVisible(true);	
			}
		}
	}

	private void extractEventFlow() {
		if (this.graphProject != null) {
			try {
				GraphConverter.convertToESG(this.graph);

				String eventFlow = GraphConverter.getEventFlow();

				ExtractEventFlowDialog dialog = new ExtractEventFlowDialog(this.graphProject, eventFlow);

				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void extractCESs() {
		if (this.graphProject != null) {
			try {
				GraphSolver.solve(this.graph);

				List<List<Vertex>> cess = GraphSolver.getCess();

				String cessAsString = GraphSolver.getCESsAsString();

				ExtractCESsDialog dialog = new ExtractCESsDialog(this.graph, this.graphProject, cess, cessAsString);

				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void generateReusedESG() {
		GraphProjectBO.generateReusedESG(this.graph, this.graphProject);
	}

	private void exportToPng() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
		fileChooser.setDialogTitle("Specify a file to export PNG");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToPNG(this.graph, this.graphComponent, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void exportToXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to export XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void importFromXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to import XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.importFromXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void displayPreferences() {

	}

	private void exitApplication() {
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

		if (result == JOptionPane.YES_OPTION) {
			this.dispose();
		}
	}

	private void init() {
		JToolBar toolBarHeader = new JToolBar();
		toolBarHeader.setFloatable(false);
		toolBarHeader.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.contentPane.add(toolBarHeader, BorderLayout.NORTH);

		this.btnNew = new JButton("");
		this.btnNew.setToolTipText("New");
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.btnNew.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/new.png")));
		this.btnNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnNew);

		this.btnOpen = new JButton("");
		this.btnOpen.setToolTipText("Open");
		this.btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.btnOpen.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/open.png")));
		this.btnOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnOpen);

		this.btnSave = new JButton("");
		this.btnSave.setToolTipText("Save");
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.btnSave.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/save.png")));
		this.btnSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnSave);

		this.btnPreferences = new JButton("");
		this.btnPreferences.setToolTipText("Preferences");
		this.btnPreferences.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/preferences.png")));
		this.btnPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayPreferences();
			}
		});

		this.btnClose = new JButton("");
		this.btnClose.setToolTipText("Close");
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.btnClose.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/close.png")));
		this.btnClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnClose);

		toolBarHeader.addSeparator();

		this.btnExecuteGraph = new JButton("");
		this.btnExecuteGraph.setToolTipText("Extract CESs");
		this.btnExecuteGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.executeGraph();
			}
		});

		this.btnProperties = new JButton("");
		this.btnProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.btnProperties.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/properties.png")));
		this.btnProperties.setToolTipText("Properties");
		this.btnProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnProperties.setEnabled(false);
		toolBarHeader.add(this.btnProperties);

		this.btnExtractEventFlow = new JButton("");
		this.btnExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.extractEventFlow();
			}
		});
		this.btnExtractEventFlow.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/eventflow.png")));
		this.btnExtractEventFlow.setToolTipText("Extract event flow");
		this.btnExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnExtractEventFlow.setEnabled(false);
		toolBarHeader.add(this.btnExtractEventFlow);
		this.btnExecuteGraph.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/executegraph.png")));
		this.btnExecuteGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExecuteGraph);

		this.btnGenerateReusedEsg = new JButton("");
		this.btnGenerateReusedEsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.btnGenerateReusedEsg.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/generate.png")));
		this.btnGenerateReusedEsg.setToolTipText("Generate reused ESG");
		this.btnGenerateReusedEsg.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnGenerateReusedEsg.setEnabled(false);
		toolBarHeader.add(this.btnGenerateReusedEsg);

		toolBarHeader.addSeparator();

		this.btnPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnPreferences);

		toolBarHeader.addSeparator();

		this.btnExit = new JButton("");
		this.btnExit.setToolTipText("Exit");
		this.btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.btnExit.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/exit.png")));
		this.btnExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExit);

		JToolBar toolBarFooter = new JToolBar();
		toolBarFooter.setFloatable(false);
		this.contentPane.add(toolBarFooter, BorderLayout.SOUTH);

		JLabel lblModelbasedTesting = new JLabel("Model-Based Test Suite For Mobile Applications (MBTS4MA)");
		lblModelbasedTesting.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarFooter.add(lblModelbasedTesting);
	}

	private void executeGraph() {
		this.extractCESs();
	}

	private void configureKeyboardEvents() {
		new mxKeyboardHandler(this.graphComponent) {

			@Override
			protected InputMap getInputMap(int condition) {
				InputMap map = null;

				if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
					map = (InputMap) UIManager.get("ScrollPane.ancestorInputMap");
				} else if (condition == JComponent.WHEN_FOCUSED) {
					map = new InputMap();

					map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
					map.put(KeyStroke.getKeyStroke("F1"), "displayid");
					map.put(KeyStroke.getKeyStroke("F2"), "edit");
					map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "selectall");
				}

				return map;
			}

			@Override
			protected ActionMap createActionMap() {
				ActionMap map = (ActionMap) UIManager.get("ScrollPane.actionMap");

				map.put("delete", CustomGraphActions.getDeleteAction());
				map.put("displayid", CustomGraphActions.getDisplayIdAction());
				map.put("edit", CustomGraphActions.getEditAction());
				map.put("selectall", CustomGraphActions.getSelectAllAction());

				return map;
			}

		};
	}

	private void setCustomStylesheet() {
		mxStylesheet stylesheet = new mxStylesheet();

		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put(mxConstants.STYLE_ROUNDED, true);
		edge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// edge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edge.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		edge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		edge.put(mxConstants.STYLE_FONTSIZE, "10");
		edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> markedEdge = new HashMap<>(edge);
		markedEdge.put(mxConstants.STYLE_ROUNDED, true);
		markedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// markedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		markedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		markedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		markedEdge.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		markedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		markedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		markedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		markedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		markedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		markedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> generatedEdge = new HashMap<>(edge);
		generatedEdge.put(mxConstants.STYLE_ROUNDED, true);
		generatedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// generatedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		generatedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		generatedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		generatedEdge.put(mxConstants.STYLE_STROKECOLOR, "#339900");
		generatedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		generatedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> startVertex = new HashMap<String, Object>();
		startVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		startVertex.put(mxConstants.STYLE_FILLCOLOR, "#88FFAA");
		startVertex.put(mxConstants.STYLE_STROKECOLOR, "#009933");
		startVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		startVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		startVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		startVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		startVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> endVertex = new HashMap<String, Object>();
		endVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		endVertex.put(mxConstants.STYLE_FILLCOLOR, "#FF9090");
		endVertex.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		endVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		endVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		endVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		endVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		endVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> normalVertex = new HashMap<String, Object>();
		normalVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		normalVertex.put(mxConstants.STYLE_FILLCOLOR, "#9DE7FF");
		normalVertex.put(mxConstants.STYLE_STROKECOLOR, "#006688");
		normalVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		normalVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		normalVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		normalVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		normalVertex.put(mxConstants.STYLE_ROUNDED, true);
		normalVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> eventVertex = new HashMap<String, Object>();
		eventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		eventVertex.put(mxConstants.STYLE_FILLCOLOR, "#FFFCDD");
		eventVertex.put(mxConstants.STYLE_STROKECOLOR, "#9A9E40");
		eventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		eventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		eventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		eventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		eventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		eventVertex.put(mxConstants.STYLE_ROUNDED, true);
		eventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> generatedEventVertex = new HashMap<String, Object>();
		generatedEventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		generatedEventVertex.put(mxConstants.STYLE_FILLCOLOR, "#CFBBEE");
		generatedEventVertex.put(mxConstants.STYLE_STROKECOLOR, "#996699");
		generatedEventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		generatedEventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEventVertex.put(mxConstants.STYLE_ROUNDED, true);
		generatedEventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		stylesheet.setDefaultEdgeStyle(edge);

		stylesheet.putCellStyle(MARKED_EDGE, markedEdge);

		stylesheet.putCellStyle(GENERATED_EDGE, generatedEdge);

		stylesheet.putCellStyle(START_VERTEX, startVertex);

		stylesheet.putCellStyle(END_VERTEX, endVertex);

		stylesheet.putCellStyle(NORMAL_VERTEX, normalVertex);

		stylesheet.putCellStyle(EVENT_VERTEX, eventVertex);

		stylesheet.putCellStyle(GENERATED_EVENT_VERTEX, generatedEventVertex);

		this.graph.setStylesheet(stylesheet);

		this.graph.setAllowDanglingEdges(false);
		this.graph.setMultigraph(false);
		this.graph.setAllowLoops(true);
	}

}
