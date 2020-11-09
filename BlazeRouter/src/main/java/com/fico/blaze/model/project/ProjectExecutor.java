package com.fico.blaze.model.project;

import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;
import com.fico.blaze.service.BlazeService;

public class ProjectExecutor {

    private static final String PACKAGE_ADAPTOR_NAME = "com.fico.blaze.model.project.adaptor.impl";

    private Project project;

    private BlazeService blazeService = null;

    private IProjectDataAdaptor projectDataAdaptor;

    public ProjectExecutor(Project project){
        this.project = project;
        try {
            blazeService = new BlazeService(project.getBlazeServerPath(), project.getBlazeServerName(), project.getBlazeEntryPoint(), project.getXsdPath());
            blazeService.init();

            if(project.getDataAdaptor() != null && !"".equalsIgnoreCase(project.getDataAdaptor())){
                Class adapterClsName = Class.forName(PACKAGE_ADAPTOR_NAME + "." + project.getDataAdaptor());
                this.setProjectDataAdaptor( (IProjectDataAdaptor) adapterClsName.newInstance() );
            }
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

    public String getFirstCallingCreditData() {
        return this.project.getFirstCallingCreditData();
    }

    public IProjectDataAdaptor getProjectDataAdaptor() {
        return projectDataAdaptor;
    }

    public void setProjectDataAdaptor(IProjectDataAdaptor projectDataAdaptor) {
        this.projectDataAdaptor = projectDataAdaptor;
    }
}
