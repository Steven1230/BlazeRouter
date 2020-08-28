package com.fico.blaze.model.com.fico.blaze.model.project;

import com.fico.blaze.service.BlazeService;
import rma.XMLJSONConverter;

public class ProjectExecutor {

    private Project project;

    private BlazeService blazeService = null;

    public ProjectExecutor(Project project){
        this.project = project;
        try {
            blazeService = new BlazeService(project.getBlazeServerPath(), project.getBlazeServerName(), project.getBlazeEntryPoint(), project.getXsdPath());

            blazeService.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProjectName(){
        return this.project.getProjectName();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public BlazeService getBlazeService() {
        return blazeService;
    }

    public void setBlazeService(BlazeService blazeService) {
        this.blazeService = blazeService;
    }
}
