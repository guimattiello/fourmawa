package com.general.mbts4ma.view.framework.vo;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.general.mbts4ma.view.framework.util.DatabaseRegression;
import com.general.mbts4ma.view.framework.util.PageObject;

public class GraphProjectVO extends AbstractVO implements Serializable {

	private transient String fileSavingPath;

	private String name;
	private String description;
	private String androidProjectPath;
	private String webProjectURL;
	private String webProjectPageObject;
	private boolean isWebProject;
	private List<PageObject> pageObjects;
	private String webProjectDirTestPath;
	private DatabaseRegression databaseRegression;
	
	private String dbhost;
	private String dbname;
	private String dbuser;
	private String dbpassword;

	private String graphXML;

	private String applicationPackage;
	private String mainTestingActivity;

	private Map<String, String> methodTemplatesByVertices;
	private Map<String, Map<String, String>> methodTemplatesPropertiesByVertices;
	private Map<String, String> edgeTemplates;
	
	private String user;
	private Date lastDate;

	public GraphProjectVO() {
		super();
		this.id = generateUUID();
		this.isWebProject = false;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAndroidProjectPath() {
		return this.androidProjectPath;
	}

	public void setAndroidProjectPath(String androidProjectPath) {
		this.androidProjectPath = androidProjectPath;
	}

	public String getWebProjectURL() {
		return this.webProjectURL;
	}
	
	public void setWebProjectURL(String webProjectURL) {
		this.webProjectURL = webProjectURL;
	}
	
	public String getWebProjectPageObject() {
		return this.webProjectPageObject;
	}
	
	public void setWebProjectPageObject(String webProjectPageObject) {
		this.webProjectPageObject = webProjectPageObject;
	}
	
	public String getWebProjectDirTestPath() {
		return this.webProjectDirTestPath;
	}

	public void setWebProjectDirTestPath(String webProjectDirTestPath) {
		this.webProjectDirTestPath = webProjectDirTestPath;
	}	
	
	public DatabaseRegression getDatabaseRegression() {
		return this.databaseRegression;
	}
	
	public void setDatabaseRegression(DatabaseRegression databaseRegression) {
		this.databaseRegression = databaseRegression;
	}
	
	public String getGraphXML() {
		return this.graphXML;
	}

	public void setGraphXML(String graphXML) {
		this.graphXML = graphXML;
	}

	public String getApplicationPackage() {
		return this.applicationPackage;
	}

	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}

	public String getMainTestingActivity() {
		return this.mainTestingActivity;
	}

	public void setMainTestingActivity(String mainTestingActivity) {
		this.mainTestingActivity = mainTestingActivity;
	}
	
	public boolean getIsWebProject(){
		return this.isWebProject;
	}
	
	public void setIsWebProject(boolean isWebProject){
		this.isWebProject = isWebProject;
	}
	
	public void setPageObjects(List<PageObject> pageObjects){
		this.pageObjects = pageObjects;
	}

	public List<PageObject> getPageObjects(){
		return this.pageObjects;
	}
	
	public Map<String, String> getMethodTemplatesByVertices() {
		if (this.methodTemplatesByVertices == null) {
			this.methodTemplatesByVertices = new LinkedHashMap<String, String>();
		}

		return this.methodTemplatesByVertices;
	}

	public void updateMethodTemplateByVertice(String verticeId, String methodTemplate) {
		this.getMethodTemplatesByVertices().put(verticeId, methodTemplate);
	}

	public void removeMethodTemplateByVertice(String verticeId) {
		this.getMethodTemplatesByVertices().remove(verticeId);
	}

	public void setMethodTemplatesByVertices(Map<String, String> methodTemplatesByVertices) {
		this.methodTemplatesByVertices = methodTemplatesByVertices;
	}

	public Map<String, Map<String, String>> getMethodTemplatesPropertiesByVertices() {
		if (this.methodTemplatesPropertiesByVertices == null) {
			this.methodTemplatesPropertiesByVertices = new LinkedHashMap<String, Map<String, String>>();
		}

		return this.methodTemplatesPropertiesByVertices;
	}

	public void updateMethodTemplatePropertiesByVertice(String verticeId, Map<String, String> properties) {
		this.getMethodTemplatesPropertiesByVertices().put(verticeId, properties);
	}

	public void removeMethodTemplatePropertiesByVertice(String verticeId) {
		this.getMethodTemplatesPropertiesByVertices().remove(verticeId);
	}

	public void setMethodTemplatesPropertiesByVertices(Map<String, Map<String, String>> methodTemplatesPropertiesByVertices) {
		this.methodTemplatesPropertiesByVertices = methodTemplatesPropertiesByVertices;
	}

	public Map<String, String> getEdgeTemplates() {
		if (this.edgeTemplates == null) {
			this.edgeTemplates = new LinkedHashMap<String, String>();
		}

		return this.edgeTemplates;
	}

	public void updateEdgeTemplateByVertice(String verticeId, String edgeTemplate) {
		this.getEdgeTemplates().put(verticeId, edgeTemplate);
	}

	public void removeEdgeTemplateByVertice(String verticeId) {
		this.getEdgeTemplates().remove(verticeId);
	}

	public void setEdgeTemplates(Map<String, String> edgeTemplates) {
		this.edgeTemplates = edgeTemplates;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLastDate() {
		return this.lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getFileSavingPath() {
		return this.fileSavingPath;
	}

	public String getFileSavingDirectory() {
		if (this.hasFileSavingPath()) {
			return this.fileSavingPath.substring(0, this.fileSavingPath.lastIndexOf(File.separator));
		}

		return System.getProperty("user.home");
	}

	public boolean hasFileSavingPath() {
		return this.fileSavingPath != null && !"".equalsIgnoreCase(this.fileSavingPath);
	}

	public void setFileSavingPath(String fileSavingPath) {
		this.fileSavingPath = fileSavingPath;
	}

}
