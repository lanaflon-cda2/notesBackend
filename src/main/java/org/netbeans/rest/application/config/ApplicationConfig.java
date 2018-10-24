package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@javax.ws.rs.ApplicationPath("/api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.douwe.notes.resource.JAXBMarshaller.class);
        resources.add(com.douwe.notes.resource.impl.AnneeResource.class);
        resources.add(com.douwe.notes.resource.impl.AuthSecurityInterceptor.class);
        resources.add(com.douwe.notes.resource.impl.CoursResource.class);
        resources.add(com.douwe.notes.resource.impl.CreditResource.class);
        resources.add(com.douwe.notes.resource.impl.CycleResource.class);
        resources.add(com.douwe.notes.resource.impl.DepartementResource.class);
        resources.add(com.douwe.notes.resource.impl.EnseignantResource.class);
        resources.add(com.douwe.notes.resource.impl.EnseignementResource.class);
        resources.add(com.douwe.notes.resource.impl.EtudiantResource.class);
        resources.add(com.douwe.notes.resource.impl.EvaluationDetailResource.class);
        resources.add(com.douwe.notes.resource.impl.EvaluationResource.class);
        resources.add(com.douwe.notes.resource.impl.FilesResource.class);
        resources.add(com.douwe.notes.resource.impl.InscriptionResource.class);
        resources.add(com.douwe.notes.resource.impl.NiveauResource.class);
        resources.add(com.douwe.notes.resource.impl.NoteResource.class);
        resources.add(com.douwe.notes.resource.impl.OptionResource.class);
        resources.add(com.douwe.notes.resource.impl.ParcoursResource.class);
        resources.add(com.douwe.notes.resource.impl.ProgrammeResource.class);
        resources.add(com.douwe.notes.resource.impl.RapportResource.class);
        resources.add(com.douwe.notes.resource.impl.SemestreResource.class);
        resources.add(com.douwe.notes.resource.impl.TypeCoursResource.class);
        resources.add(com.douwe.notes.resource.impl.UniteEnseignementResource.class);
        resources.add(com.douwe.notes.resource.impl.UtilisateurResource.class);
        resources.add(org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.class);
        resources.add(org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider.class);
        resources.add(org.glassfish.jersey.server.spring.SpringLifecycleListener.class);
        resources.add(org.glassfish.jersey.server.spring.scope.RequestContextFilter.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }
    
}
