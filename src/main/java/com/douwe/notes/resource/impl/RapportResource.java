package com.douwe.notes.resource.impl;

import com.douwe.notes.resource.IRapportResource;
import com.douwe.notes.service.IDocumentFacadeService;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Path("/rapport")
public class RapportResource implements IRapportResource {

    @Inject
    private IDocumentFacadeService documentService;

    String filename = new String();

    public IDocumentFacadeService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(IDocumentFacadeService documentService) {
        this.documentService = documentService;
    }

    @Override
    public Response produirePv(final long niveauid, final long optionid, final long coursid, final long anneeid, final int session) {

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    //buildDocument(output);
                    filename = documentService.produirePv(niveauid, optionid, coursid, anneeid, session, output);

                    //fil
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };

        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "pv.pdf").build();
    }

    @Override
    public Response produireSyntheseSemestrielle(final long niveauid, final long optionid, final long anneeid, final long semestreId) {
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    //buildDocument(output);
                    //    filename = documentService.produireSyntheseSemestrielle(niveauid, optionid, anneeid, semestreId, output);
                    filename = documentService.produireSynthese(niveauid, optionid, anneeid, semestreId, output);

                    //fil
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "synthese.pdf").build();
    }

    @Override
    public Response produire() {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    //buildDocument(output);
                    //     filename = documentService.produireSyntheseAnnuelle(output, null, null, null) ;

                    //fil
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };

        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "synthese.pdf").build();
    }

    @Override
    public Response produireSyntheseAnnuelle(final long niveauid, final long optionid, final long anneeid) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    filename = documentService.produireSynthese(niveauid, optionid, anneeid, null, output);

                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "synthese.pdf").build();
    }

    @Override
    public Response produireRelevetParcours(final long niveauid, final long optionid, final long anneeid) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    documentService.produireRelevet(niveauid, optionid, anneeid, output, null);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "relevet.pdf").build();
    }

//    @Override
//    public Response produireRelevetParcours(final long niveauid,final long optionid, final long anneeid) {
//        StreamingOutput stream = new StreamingOutput() {
//            @Override
//            public void write(OutputStream output) throws IOException, WebApplicationException {
//                try {
//                    //buildDocument(output);
//                //    filename = documentService.produireSyntheseSemestrielle(niveauid, optionid, anneeid, semestreId, output);
//                     documentService.produireRelevetParcours(niveauid, optionid, anneeid, output);
//            
//                    //fil
//                } catch (Exception e) {
//                    throw new WebApplicationException(e);
//                }
//            }
//        };      
//        return Response.ok(stream).header("Content-Disposition",
//                "attachment; filename="+filename+"relevet.pdf").build();
//    }

   
    @Override
    public Response produireRelevetEtudiant(final long niveauid, final long optionid, final long anneeid, final long etudiantid) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    documentService.produireRelevet(niveauid, optionid, anneeid, output, etudiantid);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "relevet.pdf").build();
    }

    @Override
    public Response produireSyntheseDiplomation(final long cycleId, final long departementId, final long anneeId) {
         StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    documentService.produireSyntheseDiplomation(cycleId, departementId, anneeId, output);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).header("Content-Disposition",
                "attachment; filename=" + filename + "diplomation.pdf").build();
    }

}
