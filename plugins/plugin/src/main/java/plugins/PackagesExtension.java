package plugins;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackagesExtension {
    private final Project project;

    public List<PluginsPlugin.Package> packages=new ArrayList<>();

    public PackagesExtension(Project project) {
        this.project = project;
    }

    public void add(PluginsPlugin.Package pkg){
        packages.add(pkg);
    }

    public void add(String pkg) {

    if(pkg.equals("bmesh")){
        packages.add(PluginsPlugin.Package.bmesh);
    }
    }

    void addDependency(Project project, PluginsPlugin.Package pkg, String configurationName){
        project.getDependencies().add(configurationName, pkg.toString());
    }


    public void addUtils(){
        packages.add(PluginsPlugin.Package.utils);
    }

    public void addBmesh(){
        packages.add(PluginsPlugin.Package.bmesh);
    }

    public void addGizmos(){
        packages.add(PluginsPlugin.Package.gizmos);
    }

    public void addMeshPlus(){
        packages.add(PluginsPlugin.Package.mesh_plus);
    }

    public void addPicking(){
        packages.add(PluginsPlugin.Package.picking);
    }

    public void addScenegraph(){
        packages.add(PluginsPlugin.Package.scenegraph);
    }

}
