package com.fico.blaze.model.com.fico.blaze.model.project;

public class Project {

    private String projectName;

    private String xsdPath;

    private String blazeServerPath;

    private String blazeServerName;

    private String blazeEntryPoint;

    private String blazeInvokeURL;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getXsdPath() {
        return xsdPath;
    }

    public void setXsdPath(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public String getBlazeServerPath() {
        return blazeServerPath;
    }

    public void setBlazeServerPath(String blazeServerPath) {
        this.blazeServerPath = blazeServerPath;
    }

    public String getBlazeServerName() {
        return blazeServerName;
    }

    public void setBlazeServerName(String blazeServerName) {
        this.blazeServerName = blazeServerName;
    }

    public String getBlazeEntryPoint() {
        return blazeEntryPoint;
    }

    public void setBlazeEntryPoint(String blazeEntryPoint) {
        this.blazeEntryPoint = blazeEntryPoint;
    }

    public String getBlazeInvokeURL() {
        return blazeInvokeURL;
    }

    public void setBlazeInvokeURL(String blazeInvokeURL) {
        this.blazeInvokeURL = blazeInvokeURL;
    }
}
