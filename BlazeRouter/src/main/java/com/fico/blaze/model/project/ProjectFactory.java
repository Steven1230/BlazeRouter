package com.fico.blaze.model.project;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix="blaze.project")
public class ProjectFactory {

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	private List<Project> projects = null;

	private List<ProjectExecutor> projectExecutorList = new ArrayList<>();

	private void initProjectExecutorList(){
		for(Project project : projects){
			ProjectExecutor projectExecutor = new ProjectExecutor(project);
			projectExecutorList.add(projectExecutor);
		}
	}

	public ProjectExecutor getProjectExecutor(String projectName) throws Exception {
		if(projectExecutorList.size() == 0){
			initProjectExecutorList();
		}

		for(ProjectExecutor projectExecutor : projectExecutorList){
			if(projectName!=null && projectName.equals(projectExecutor.getProjectName())){
				return projectExecutor;
			}
		}
		throw new Exception("Project " + projectName + " not Found");
	}

}
