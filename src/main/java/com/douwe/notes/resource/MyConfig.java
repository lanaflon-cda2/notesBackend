package com.douwe.notes.resource;

import com.douwe.notes.resource.impl.AnneeResource;
import com.douwe.notes.resource.impl.CoursResource;
import com.douwe.notes.resource.impl.CreditResource;
import com.douwe.notes.resource.impl.CycleResource;
import com.douwe.notes.resource.impl.DepartementResource;
import com.douwe.notes.resource.impl.EnseignantResource;
import com.douwe.notes.resource.impl.EnseignementResource;
import com.douwe.notes.resource.impl.EtudiantResource;
import com.douwe.notes.resource.impl.EvaluationDetailResource;
import com.douwe.notes.resource.impl.EvaluationResource;
import com.douwe.notes.resource.impl.InscriptionResource;
import com.douwe.notes.resource.impl.NiveauResource;
import com.douwe.notes.resource.impl.NoteResource;
import com.douwe.notes.resource.impl.OptionResource;
import com.douwe.notes.resource.impl.ParcoursResource;
import com.douwe.notes.resource.impl.ProgrammeResource;
import com.douwe.notes.resource.impl.RapportResource;
import com.douwe.notes.resource.impl.DatabaseBackupResource;
import com.douwe.notes.resource.impl.SemestreResource;
import com.douwe.notes.resource.impl.TypeCoursResource;
import com.douwe.notes.resource.impl.UniteEnseignementResource;
import com.douwe.notes.resource.impl.UtilisateurResource;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Component
@ApplicationPath("/api")
public class MyConfig extends ResourceConfig {

    public MyConfig() {
        super();
        registerComponents();
    }

    private void registerComponents() {
        register(MultiPartFeature.class);
        register(RequestContextFilter.class);
        register(AnneeResource.class);
        register(CoursResource.class);
        register(CreditResource.class);
        register(CycleResource.class);
        register(DepartementResource.class);
        register(EnseignantResource.class);
        register(EnseignementResource.class);
        register(EtudiantResource.class);
        register(EvaluationDetailResource.class);
        register(EvaluationResource.class);
        register(InscriptionResource.class);
        register(NiveauResource.class);
        register(NoteResource.class);
        register(OptionResource.class);
        register(ParcoursResource.class);
        register(ProgrammeResource.class);
        register(RapportResource.class);
        register(SemestreResource.class);
        register(DatabaseBackupResource.class);
        register(TypeCoursResource.class);
        register(UniteEnseignementResource.class);
        register(UtilisateurResource.class);
    }
}
