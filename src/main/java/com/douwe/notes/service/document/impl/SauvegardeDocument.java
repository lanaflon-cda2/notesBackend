/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.document.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.dao.ICreditDao;
import com.douwe.notes.dao.ICycleDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.dao.IEnseignantDao;
import com.douwe.notes.dao.IEnseignementDao;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.dao.IEvaluationDao;
import com.douwe.notes.dao.IEvaluationDetailsDao;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.dao.INoteDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.dao.IParcoursDao;
import com.douwe.notes.dao.IProgrammeDao;
import com.douwe.notes.dao.ISemestreDao;
import com.douwe.notes.dao.ITypeCoursDao;
import com.douwe.notes.dao.IUniteEnseignementDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Credit;
import com.douwe.notes.entities.Cycle;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Enseignant;
import com.douwe.notes.entities.Enseignement;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.EvaluationDetails;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.TypeCours;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.ISauvegardeDocument;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author guidona
 */
@Named
@Service
@Transactional
public class SauvegardeDocument implements ISauvegardeDocument{
    
    @Inject
    private IAnneeAcademiqueDao academiqueDao;
    
    @Inject
    private ICoursDao coursDao;
    
    @Inject
    private IUniteEnseignementDao uniteEnseignementDao;
    
    @Inject
    private ICreditDao creditDao;

    @Inject
    private IDepartementDao departementDao;
    
    @Inject
    private ICycleDao cycleDao;
    
    @Inject
    private INiveauDao niveauDao;
    
    @Inject
    private IOptionDao optionDao;
    
    @Inject
    private IParcoursDao parcoursDao;
    
    @Inject
    private IEnseignantDao enseignantDao;
    
    @Inject
    private IEvaluationDao evaluationDao;
    
    @Inject
    private ITypeCoursDao typeCoursDao;
    
    @Inject
    private ISemestreDao semestreDao;
    
    @Inject
    private IEvaluationDetailsDao evaluationDetailsDao;
    
    @Inject
    private IEtudiantDao etudiantDao;
    
    @Inject
    private INoteDao noteDao;
    
    @Inject
    private IInscriptionDao inscriptionDao;
    
    @Inject
    private IEnseignementDao enseignementDao;
    
    @Inject
    private IProgrammeDao programmeDao;
            
    private String replaceIfNull(String string){
        return (string == null)? "": string;
    }
    
    private void build(Document document, Element root) throws DataAccessException {
        List<Departement> dpt = departementDao.findAll();
        Element departements = document.createElement("Departements");
        for (Departement departement : dpt) {
            Element dprtmnt = document.createElement("Departement");
            Element french = document.createElement("FrenchDescription");
            french.appendChild(document.createTextNode(departement.getFrenchDescription()));
            dprtmnt.appendChild(french);
            Element english = document.createElement("EnglishDescription");
            english.appendChild(document.createTextNode(departement.getEnglishDescription()));
            dprtmnt.appendChild(english);
            dprtmnt.setAttribute("code", departement.getCode());
            departements.appendChild(dprtmnt);
        }
        root.appendChild(departements);
        List<Cycle> cyc = cycleDao.findAll();
        Element cycles = document.createElement("Cycles");
        for(Cycle cycl : cyc){
            Element cycle = document.createElement("Cycle");
            Element diplomeFr = document.createElement("DiplomeFr");
            diplomeFr.appendChild(document.createTextNode(cycl.getDiplomeFr()));
            cycle.appendChild(diplomeFr);
            Element diplomeEn = document.createElement("DiplomeEn");
            diplomeEn.appendChild(document.createTextNode(cycl.getDiplomeEn()));
            cycle.appendChild(diplomeEn);
            cycle.setAttribute("nom", cycl.getNom());
            cycles.appendChild(cycle);
        }
        root.appendChild(cycles);
        List<Niveau> niv = niveauDao.findAll();
        Element niveaux = document.createElement("Niveaux");
        for(Niveau n: niv){
            Element niveau = document.createElement("Niveau");
            Element terminal = document.createElement("Terminal");
            terminal.appendChild(document.createTextNode(n.isTerminal()? "1":"0"));
            niveau.appendChild(terminal);
            Element nivCycle = document.createElement("Cycle");
            nivCycle.appendChild(document.createTextNode(n.getCycle().getNom()));
            niveau.appendChild(nivCycle);
            niveau.setAttribute("code", n.getCode());
            niveaux.appendChild(niveau);
        }
        root.appendChild(niveaux);
        List<Option> opts = optionDao.findAll();
        Element options = document.createElement("Options");
        for(Option opt: opts){
            Element option = document.createElement("Option");
            Element frenchDescription = document.createElement("FrenchDescription");
            frenchDescription.appendChild(document.createTextNode(opt.getDescription()));
            option.appendChild(frenchDescription);
            Element englishDescription = document.createElement("EnglishDescription");
            englishDescription.appendChild(document.createTextNode(replaceIfNull(opt.getDescriptionEnglish())));
            option.appendChild(englishDescription);
            Element departement = document.createElement("Departement");
            departement.appendChild(document.createTextNode(opt.getDepartement().getCode()));
            option.appendChild(departement);
            option.setAttribute("code", opt.getCode());
            options.appendChild(option);
        }
        root.appendChild(options);
        List<Parcours> prcrs = parcoursDao.findAll();
        Element parcours = document.createElement("Parcours");
        for(Parcours prcr: prcrs){
            Element parcour = document.createElement("Parcour");
            parcour.setAttribute("niveau", prcr.getNiveau().getCode());
            parcour.setAttribute("option", prcr.getOption().getCode());
            parcours.appendChild(parcour);
        }
        root.appendChild(parcours);
        List<Enseignant> ensgnts = enseignantDao.findAll();
        Element enseignants = document.createElement("Enseignants");
        for(Enseignant ensgnt: ensgnts){
            Element enseignant = document.createElement("Enseignant");
            enseignant.setAttribute("nom", replaceIfNull(ensgnt.getNom()));
            enseignants.appendChild(enseignant);
        }
        root.appendChild(enseignants);
        List<Evaluation> evals = evaluationDao.findAll();
        Element evaluations = document.createElement("Evaluations");
        for(Evaluation eval: evals){
            Element evaluation = document.createElement("Evaluation");
            Element is_exam = document.createElement("IsExam");
            is_exam.appendChild(document.createTextNode(replaceIfNull(eval.isExam()? "1":"0")));
            evaluation.appendChild(is_exam);
            Element description = document.createElement("Description");
            description.appendChild(document.createTextNode(replaceIfNull(eval.getDescription())));
            evaluation.appendChild(description);
            evaluation.setAttribute("code", replaceIfNull(eval.getCode()));
            evaluations.appendChild(evaluation);
        }
        root.appendChild(evaluations);
        List<TypeCours> typCrs = typeCoursDao.findAll();
        Element typeCours = document.createElement("TypeCours");
        for(TypeCours typCr: typCrs){
            Element typeCour = document.createElement("TypeCour");
            typeCour.setAttribute("nom", replaceIfNull(typCr.getNom()));
            typeCours.appendChild(typeCour);
        }
        root.appendChild(typeCours);
        Element semestres = document.createElement("Semestres");
        for(Semestre smstr: semestreDao.findAll()){
            Element semestre = document.createElement("Semestre");
            Element niveau = document.createElement("Niveau");
            niveau.appendChild(document.createTextNode(replaceIfNull(smstr.getNiveau().getCode())));
            semestre.appendChild(niveau);
            semestre.setAttribute("intitule", replaceIfNull(smstr.getIntitule()));
            semestres.appendChild(semestre);
        }
        root.appendChild(semestres);
        Element uniteEnsignements = document.createElement("UniteEnseignements");
        for(UniteEnseignement uniteEns: uniteEnseignementDao.findAll()){
            Element uniteEnseignement = document.createElement("UniteEnseignement");
            Element intitule = document.createElement("Intitule");
            intitule.appendChild(document.createTextNode(replaceIfNull(uniteEns.getIntitule())));
            uniteEnseignement.appendChild(intitule);
            Element hasOptionalChoices = document.createElement("HasOptionalChoices");
            hasOptionalChoices.appendChild(document.createTextNode(replaceIfNull(uniteEns.isHasOptionalChoices()? "1": "0")));
            uniteEnseignement.appendChild(hasOptionalChoices);
            uniteEnseignement.setAttribute("code", replaceIfNull(uniteEns.getCode()));
            uniteEnseignement.setAttribute("niveau", replaceIfNull(uniteEns.getParcours().getNiveau().getCode()));
            uniteEnseignement.setAttribute("option", replaceIfNull(uniteEns.getParcours().getOption().getCode()));
            uniteEnsignements.appendChild(uniteEnseignement);
        }
        root.appendChild(uniteEnsignements);
        Element cours = document.createElement("Cours");
        for(Cours cr : coursDao.findAll()){
            Element cour = document.createElement("Cour");
            cour.setAttribute("intitule", replaceIfNull(cr.getIntitule()));
            cour.setAttribute("departement", replaceIfNull(cr.getDepartement().getCode()));
            Element typC = document.createElement("TypeCours");
            typC.appendChild(document.createTextNode(replaceIfNull(cr.getTypeCours().getNom())));
            cour.appendChild(typC);
            for(UniteEnseignement uniteEn: cr.getUniteEnseignements()){
                Element uniteEns = document.createElement("UniteEnseignement");
                uniteEns.appendChild(document.createTextNode(replaceIfNull(uniteEn.getCode())));
                cour.appendChild(uniteEns);
            }
            cours.appendChild(cour);
        }
        root.appendChild(cours);
        Element evaluationDetails = document.createElement("EvaluationDetails");
        for(EvaluationDetails evalDetails : evaluationDetailsDao.findAll()){
            Element evaluationDetail = document.createElement("EaluationDetail");
            evaluationDetail.setAttribute("evaluation", replaceIfNull(evalDetails.getEvaluation().getCode()));
            evaluationDetail.setAttribute("typeCour", replaceIfNull(evalDetails.getTypeCours().getNom()));
            Element pourcentage = document.createElement("Pourcentage");
            pourcentage.appendChild(document.createTextNode(replaceIfNull(evalDetails.getPourcentage().toString())));
            evaluationDetails.appendChild(evaluationDetail);
        }
        root.appendChild(evaluationDetails);
        Element etudiants = document.createElement("Etudiants");
        for(Etudiant etdnt : etudiantDao.findAll()){
            Element etudiant = document.createElement("Etudiant");
            Element nom = document.createElement("Nom");
            nom.appendChild(document.createTextNode(replaceIfNull(etdnt.getNom())));
            etudiant.appendChild(nom);
            Element dateNaissance = document.createElement("DateNaissance");
            dateNaissance.appendChild(document.createTextNode(replaceIfNull(etdnt.getDateDeNaissance().toString())));
            etudiant.appendChild(dateNaissance);
            Element numeroTel = document.createElement("NumeroTelephone");
            numeroTel.appendChild(document.createTextNode(replaceIfNull(etdnt.getNumeroTelephone())));
            etudiant.appendChild(numeroTel);
            Element email = document.createElement("Email");
            email.appendChild(document.createTextNode(replaceIfNull(etdnt.getEmail())));
            etudiant.appendChild(email);
            Element valideDate = document.createElement("ValideDate");
            valideDate.appendChild(document.createTextNode(replaceIfNull(etdnt.isValidDate()? "1": "0")));
            etudiant.appendChild(valideDate);
            Element genre = document.createElement("Genre");
            genre.appendChild(document.createTextNode(replaceIfNull(etdnt.getGenre().name())));
            etudiant.appendChild(genre);
            etudiant.setAttribute("matricule", replaceIfNull(etdnt.getMatricule()));
            etudiants.appendChild(etudiant);
        }
        root.appendChild(etudiants);
    }
    
    public void buildYear(AnneeAcademique annee, Document document, Element root) throws DataAccessException{
        Element anneeAcademique = document.createElement("AnneeAcademique");
        root.appendChild(anneeAcademique);
        Element notes = document.createElement("Notes");
        List<Note> nts = noteDao.getNoteByAnnee(annee);
        for(Note nt: nts){
            Element note = document.createElement("Note");
            Element valeur = document.createElement("Valeur");
            valeur.appendChild(document.createTextNode(Double.toString(nt.getValeur())));
            note.appendChild(valeur);
            note.setAttribute("cour", nt.getCours().getIntitule());
            note.setAttribute("etudiant", nt.getEtudiant().getMatricule());
            note.setAttribute("evaluation", nt.getEvaluation().getCode());
            if(nt.getEvaluation().isExam())
                note.setAttribute("session", nt.getSession().name());
            notes.appendChild(note);
        }
        Element credits = document.createElement("Credits");
        List<Credit> crdts = creditDao.findCreditByAnnee(annee);
        for(Credit crdt: crdts){
            Element credit = document.createElement("Credit");
            Element valeur = document.createElement("Valeur");
            valeur.appendChild(document.createTextNode(Double.toString(crdt.getValeur())));
            credit.appendChild(valeur);
            credit.setAttribute("cour", crdt.getCours().getIntitule());                    
            credit.setAttribute("niveau", crdt.getParcours().getNiveau().getCode());
            credit.setAttribute("option", crdt.getParcours().getOption().getCode());
            credits.appendChild(credit);
        }
        Element inscriptions = document.createElement("Inscriptions");
        List<Inscription> inscrptns = inscriptionDao.findByAnnee(annee);
        for(Inscription inscrptn: inscrptns){
            Element inscription = document.createElement("Inscription");
            inscriptions.appendChild(inscription);
            inscription.setAttribute("etudiant", inscrptn.getEtudiant().getMatricule());
            inscription.setAttribute("niveau", inscrptn.getParcours().getNiveau().getCode());
            inscription.setAttribute("option", inscrptn.getParcours().getOption().getCode());
        }
        Element enseignements = document.createElement("Enseignements");
        for(Enseignement ens : enseignementDao.findByAnnee(annee)){
            Element enseignement = document.createElement("Enseignement");
            enseignements.appendChild(enseignement);
            for(Enseignant ensgnt: ens.getEnseignants()){
                Element enseignant = document.createElement("Enseignant");
                enseignant.appendChild(document.createTextNode(replaceIfNull(ensgnt.getNom())));
                enseignement.appendChild(enseignant);
            }
            enseignement.setAttribute("cour", ens.getCours().getIntitule());
            enseignement.setAttribute("niveau", ens.getParcours().getNiveau().getCode());
            enseignement.setAttribute("option", ens.getParcours().getNiveau().getCode());

        }
        Element programmes = document.createElement("Programmes");
        for(Programme prg: programmeDao.findByAnnee(annee)){
            Element programme = document.createElement("Programme");
            programmes.appendChild(programme);
            programme.setAttribute("semestre", prg.getSemestre().getIntitule());                    
            programme.setAttribute("uniteEnseignement", prg.getUniteEnseignement().getCode());
            programme.setAttribute("niveau", prg.getParcours().getNiveau().getCode());
            programme.setAttribute("option", prg.getParcours().getOption().getCode());
        }
        anneeAcademique.appendChild(notes);                
        anneeAcademique.appendChild(credits);
        anneeAcademique.appendChild(inscriptions);
        anneeAcademique.appendChild(enseignements);                
        anneeAcademique.appendChild(programmes);

        anneeAcademique.setAttribute("annee",Long.toString(annee.getNumeroDebut()));
        anneeAcademique.setAttribute("debut", annee.getDebut().toString());
        anneeAcademique.setAttribute("fin", annee.getFin().toString());
    }
    
    private void buildFile(Document document, OutputStream stream) throws TransformerConfigurationException, TransformerException{
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer t = tFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(stream);
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        t.transform(source, result);
    }

    @Override
    public void sauvegardeBD(OutputStream stream, long anneeId) throws ServiceException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("Root");
            document.appendChild(root);
            
            build(document, root);
            
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            
            buildYear(annee, document, root);
            
            buildFile(document, stream);
        } catch (DataAccessException | ParserConfigurationException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sauvegardeBD(OutputStream stream) throws ServiceException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("Root");
            document.appendChild(root);
            build(document, root);
            
            List<AnneeAcademique> annees = academiqueDao.findAll();
            
            for(AnneeAcademique annee: annees){
                buildYear(annee, document, root);
            }
            
            buildFile(document, stream);
        } catch (DataAccessException | ParserConfigurationException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
