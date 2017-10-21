/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.document;

import java.io.OutputStream;
import javax.ejb.Local;

/**
 *
 * @author cellule
 */
@Local
public interface ISyntheseDiplomationDocument {
    public void produireSyntheseDiplomation(long cycleId, long departementId, long anneeId, OutputStream output);
}
