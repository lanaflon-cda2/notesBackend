package com.douwe.notes.service.document;

import java.io.OutputStream;

/**
 *
 * @author cellule
 */
public interface ISyntheseDiplomationDocument {
    public void produireSyntheseDiplomation(long cycleId, long departementId, long anneeId, OutputStream output);
}
