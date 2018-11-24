/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.impl;

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
import com.douwe.notes.entities.Genre;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.TypeCours;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;
import javax.inject.Inject;
import com.douwe.notes.service.util.RestaurationError;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.douwe.notes.service.IDatabaseBackupService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author guidona
 */
@Named
@Service
@Transactional
public class DatabaseBackupServiceImpl implements IDatabaseBackupService{
    
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
            evaluationDetail.setAttribute("pourcentage", replaceIfNull(evalDetails.getPourcentage().toString()));
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
            Element lieuNaissance = document.createElement("LieuNaissance");
            dateNaissance.appendChild(document.createTextNode(replaceIfNull(etdnt.getLieuDeNaissance())));
            etudiant.appendChild(lieuNaissance);
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
        crdts.stream().map((crdt) -> {
            Element credit = document.createElement("Credit");
            Element valeur = document.createElement("Valeur");
            valeur.appendChild(document.createTextNode(Double.toString(crdt.getValeur())));
            credit.appendChild(valeur);
            credit.setAttribute("cour", crdt.getCours().getIntitule());                    
            credit.setAttribute("niveau", crdt.getParcours().getNiveau().getCode());
            credit.setAttribute("option", crdt.getParcours().getOption().getCode());
            return credit;
        }).forEachOrdered((credit) -> {
            credits.appendChild(credit);
        });
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
            ens.getEnseignants().stream().map((ensgnt) -> {
                Element enseignant = document.createElement("Enseignant");
                enseignant.appendChild(document.createTextNode(replaceIfNull(ensgnt.getNom())));
                return enseignant;
            }).forEachOrdered((enseignant) -> {
                enseignement.appendChild(enseignant);
            });
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
    
    private String buildFile(Document document, OutputStream stream) throws TransformerConfigurationException, TransformerException{
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer t = tFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(stream);
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        t.transform(source, result);
        Date date = new Date();
        String filename = "sauvegarde_du_"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/");
        return filename;
    }

    @Override
    public String produireSauvegardeDocument(OutputStream stream) throws ServiceException {
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
            
            return buildFile(document, stream);
            
        } catch (DataAccessException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DatabaseBackupServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String produireSauvegardeDocument(OutputStream stream, long anneeId) throws ServiceException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("Root");
            document.appendChild(root);
            
            build(document, root);
            
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            
            buildYear(annee, document, root);
            
            return buildFile(document, stream);
        } catch (DataAccessException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DatabaseBackupServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public RestaurationError restauration(InputStream stream) throws SecurityException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(stream);
            Element root = document.getDocumentElement();
            RestaurationError erreur = new RestaurationError();
            int nombresErreurs = 0;
            Element departements = (Element) root.getElementsByTagName("Departements").item(0);
            NodeList departement = departements.getChildNodes();
            for(int i = 0; i < departement.getLength(); i++){
                if(departement.item(i).getNodeType() == Node.ELEMENT_NODE){
                    String code = departement.item(i).getAttributes().getNamedItem("code").getNodeValue();
                    Element dprtmnt = (Element) departement.item(i);
                    String frenchDescription = dprtmnt.getElementsByTagName("FrenchDescription").item(0).getTextContent();
                    String englishDescription = dprtmnt.getElementsByTagName("EnglishDescription").item(0).getTextContent();
                    
                    if(departementDao.findByCode(code) == null){
                        Departement dept = new Departement();
                        dept.setCode(code);
                        dept.setEnglishDescription(englishDescription);
                        dept.setFrenchDescription(frenchDescription);
                        departementDao.create(dept);
                    }
                }

            }
            Element cycles = (Element) root.getElementsByTagName("Cycles").item(0);
            NodeList cycle = cycles.getChildNodes();
            for(int i = 0; i < cycle.getLength(); i++){
                if(cycle.item(i).getNodeType() == Node.ELEMENT_NODE){
                    String nom = cycle.item(i).getAttributes().getNamedItem("nom").getNodeValue();
                    Element cycl = (Element) cycle.item(i);
                    String diplomeFr = cycl.getElementsByTagName("DiplomeFr").item(0).getTextContent();
                    String diplomeEn = cycl.getElementsByTagName("DiplomeEn").item(0).getTextContent();
                    if (cycleDao.findByNom(nom) == null) {
                        Cycle cyc = new Cycle();
                        cyc.setDiplomeEn(diplomeEn);
                        cyc.setDiplomeFr(diplomeFr);
                        cyc.setNom(nom);
                        cycleDao.create(cyc);
                    }
                }

            }
            Element niveaux = (Element) root.getElementsByTagName("Niveaux").item(0);
            NodeList niveau = niveaux.getChildNodes();
            for (int i = 0; i < niveau.getLength(); i++) {
                if (niveau.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element nv = (Element) niveau.item(i);
                    String code = nv.getAttribute("code");
                    boolean terminal = nv.getElementsByTagName("Terminal").item(0).getTextContent().equals(1);
                    String cycl = nv.getElementsByTagName("Cycle").item(0).getTextContent();
                    if (niveauDao.findByCode(code) == null) {
                        Niveau niv = new Niveau();
                        niv.setCode(code);
                        niv.setCycle(cycleDao.findByNom(cycl));
                        niv.setTerminal(terminal);
                        niveauDao.create(niv);
                    }
                }
            }
            Element options = (Element) root.getElementsByTagName("Options").item(0);
            NodeList option = options.getChildNodes();
            for (int i = 0; i < option.getLength(); i++) {
                if (option.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String code = option.item(i).getAttributes().getNamedItem("code").getNodeValue();
                    Element opt = (Element) option.item(i);
                    String frenchDescription = opt.getElementsByTagName("FrenchDescription").item(0).getTextContent();
                    String englishDescription = opt.getElementsByTagName("EnglishDescription").item(0).getTextContent();
                    String dprtmnt = opt.getElementsByTagName("Departement").item(0).getTextContent();
                    if (optionDao.findByCode(code) == null) {
                        Option optn = new Option();
                        optn.setCode(code);
                        optn.setDepartement(departementDao.findByCode(dprtmnt));
                        optn.setDescription(frenchDescription);
                        optn.setDescriptionEnglish(englishDescription);
                        optionDao.create(optn);
                    }
                }
            }
            Element parcours = (Element) root.getElementsByTagName("Parcours").item(0);
            NodeList parcour = parcours.getChildNodes();
            for (int i = 0; i < parcour.getLength(); i++) {
                if (parcour.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nv = parcour.item(i).getAttributes().getNamedItem("niveau").getNodeValue();
                    String opt = parcour.item(i).getAttributes().getNamedItem("option").getNodeValue();
                    if (parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)) == null) {
                        Parcours prcrs = new Parcours();
                        prcrs.setNiveau(niveauDao.findByCode(nv));
                        prcrs.setOption(optionDao.findByCode(opt));
                        parcoursDao.create(prcrs);
                    }
                }
            }
            Element enseignants = (Element) root.getElementsByTagName("Enseignants").item(0);
            NodeList enseignant = enseignants.getChildNodes();
            for (int i = 0; i < enseignant.getLength(); i++) {
                if (enseignant.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nom = enseignant.item(i).getAttributes().getNamedItem("nom").getNodeValue();
                    if (enseignantDao.findByName(nom) == null) {
                        Enseignant ens = new Enseignant();
                        ens.setNom(nom);
                        enseignantDao.create(ens);
                    }
                }
            }
            Element evaluations = (Element) root.getElementsByTagName("Evaluations").item(0);
            NodeList evaluation = evaluations.getChildNodes();
            for (int i = 0; i < evaluation.getLength(); i++) {
                if (evaluation.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String code = evaluation.item(i).getAttributes().getNamedItem("code").getNodeValue();
                    Element eval = (Element) evaluation.item(i);
                    boolean isExam = new Boolean(eval.getElementsByTagName("IsExam").item(0).getTextContent().equals("1"));
                    String description = eval.getElementsByTagName("Description").item(0).getTextContent();
                    if (evaluationDao.findByCode(code) == null) {
                        Evaluation evalu = new Evaluation();
                        evalu.setCode(code);
                        evalu.setDescription(description);
                        evalu.setExam(isExam);
                        evaluationDao.create(evalu);
                    }
                }
            }
            Element typeCours = (Element) root.getElementsByTagName("TypeCours").item(0);
            NodeList typeCour = typeCours.getChildNodes();
            for (int i = 0; i < typeCour.getLength(); i++) {
                if (typeCour.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nom = typeCour.item(i).getAttributes().getNamedItem("nom").getNodeValue();
                    if (typeCoursDao.findByName(nom) == null) {
                        TypeCours typCr = new TypeCours();
                        typCr.setNom(nom);
                        typeCoursDao.create(typCr);
                    }
                }
            }
            
            Element semestres = (Element) root.getElementsByTagName("Semestres").item(0);
            NodeList semestre = semestres.getChildNodes();
            for (int i = 0; i < semestre.getLength(); i++) {
                if (semestre.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element smstr = (Element) semestre.item(i);
                    String nv = new String(smstr.getElementsByTagName("Niveau").item(0).getTextContent());
                    String in = new String(smstr.getAttribute("intitule"));
                    if (semestreDao.findByIntutle(in) == null) {
                        Semestre sem = new Semestre();
                        sem.setIntitule(in);
                        sem.setNiveau(niveauDao.findByCode(nv));
                        semestreDao.create(sem);
                    }
                }
            }
            
            Element uniteEnseignements = (Element) root.getElementsByTagName("UniteEnseignements").item(0);
            NodeList uniteEnseignement = uniteEnseignements.getChildNodes();
            for (int i = 0; i < uniteEnseignement.getLength(); i++) {
                if (uniteEnseignement.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element uniteEns = (Element) uniteEnseignement.item(i);
                    String intitule = uniteEns.getElementsByTagName("Intitule").item(0).getTextContent();
                    boolean hasOptionalChoices = uniteEns.getElementsByTagName("HasOptionalChoices").item(0).getTextContent().equals("1");
                    String code = uniteEns.getAttribute("code");
                    String nv = uniteEns.getAttribute("niveau");
                    String opt = uniteEns.getAttribute("option");
                    if (uniteEnseignementDao.findByCode(code) == null) {
                        UniteEnseignement ue = new UniteEnseignement();
                        ue.setCode(code);
                        ue.setIntitule(intitule);
                        ue.setParcours(parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)));
                        ue.setHasOptionalChoices(hasOptionalChoices);
                        uniteEnseignementDao.create(ue);
                    }
                }
            }
            
            Element cours = (Element) root.getElementsByTagName("Cours").item(0);
            NodeList cour = cours.getChildNodes();
            for (int i = 0; i < cour.getLength(); i++) {
                if (cour.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element cr = (Element) cour.item(i);
                    String intitule = cr.getAttribute("intitule");
                    String dprtmnt = cr.getAttribute("departement");
                    String typCr = cr.getElementsByTagName("TypeCours").item(0).getTextContent();
                    List<UniteEnseignement> uniteEns = new ArrayList();
                    NodeList uniteEnsgnmnt = root.getElementsByTagName("UniteEnseignement");
                    for (int j = 0; j < uniteEnsgnmnt.getLength(); j++) {
                        if (uniteEnsgnmnt.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            uniteEns.add(uniteEnseignementDao.findByCode(uniteEnsgnmnt.item(j).getTextContent()));
                        }
                    }
                    if (coursDao.findByIntituleAndDepartement(intitule, departementDao.findByCode(dprtmnt)) == null) {
                        Cours c = new Cours();
                        c.setIntitule(intitule);
                        c.setDepartement(departementDao.findByCode(dprtmnt));
                        c.setTypeCours(typeCoursDao.findByName(typCr));
                        c.setUniteEnseignements(uniteEns);
                        coursDao.create(c);
                    }
                }
            }
            Element evalDetails = (Element) root.getElementsByTagName("EvaluationDetails").item(0);
            NodeList evalDetail = evalDetails.getChildNodes();
            for (int i = 0; i < evalDetail.getLength(); i++) {
                if (evalDetail.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element evlDtl = (Element) evalDetail.item(i);
                    String eval = evlDtl.getAttribute("evaluation");
                    String typCour = evlDtl.getAttribute("typeCour");
                    int pcent = Integer.parseInt(evlDtl.getAttribute("pourcentage"));
                    if (evaluationDetailsDao.findByTypeAndEvaluation(typeCoursDao.findByName(typCour), evaluationDao.findByCode(eval)) == null) {
                        EvaluationDetails evalDet = new EvaluationDetails();
                        evalDet.setEvaluation(evaluationDao.findByCode(eval));
                        evalDet.setPourcentage(pcent);
                        evalDet.setTypeCours(typeCoursDao.findByName(typCour));
                        evaluationDetailsDao.create(evalDet);
                    }
                }
            }
            Element etudiants = (Element) root.getElementsByTagName("Etudiants").item(0);
            NodeList etudiant = etudiants.getChildNodes();
            for (int i = 0; i < etudiant.getLength(); i++) {
                if (etudiant.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element etdnt = (Element) etudiant.item(i);
                    String nom = etdnt.getElementsByTagName("Nom").item(0).getTextContent();
                    String dateNaissance = etdnt.getElementsByTagName("DateNaissance").item(0).getTextContent();
                    String lieuNaissance = etdnt.getElementsByTagName("LieuNaissance").item(0).getTextContent();
                    String numeroTelephone = etdnt.getElementsByTagName("NumeroTelephone").item(0).getTextContent();
                    String email = etdnt.getElementsByTagName("Email").item(0).getTextContent();
                    boolean valideDate = etdnt.getElementsByTagName("ValideDate").item(0).getTextContent().equals(1);
                    String genre = etdnt.getElementsByTagName("Genre").item(0).getTextContent();
                    String matricule = etdnt.getAttribute("matricule");
                    if (etudiantDao.findByMatricule(matricule) == null) {
                        Etudiant et = new Etudiant();
                        et.setNom(nom);
                        et.setDateDeNaissance(java.sql.Date.valueOf(dateNaissance));
                        et.setLieuDeNaissance(lieuNaissance);
                        et.setNumeroTelephone(numeroTelephone);
                        et.setMatricule(matricule);
                        et.setEmail(email);
                        et.setGenre(Genre.valueOf(genre));
                        et.setValidDate(valideDate);
                        etudiantDao.create(et);
                    }
                }
            }
            NodeList anneeAcademiques = root.getElementsByTagName("AnneeAcademique");
            for (int i = 0; i < anneeAcademiques.getLength(); i++) {
                if (anneeAcademiques.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element annee = (Element) anneeAcademiques.item(i);
                    String an = annee.getAttribute("annee");
                    String db = annee.getAttribute("debut");
                    String fn = annee.getAttribute("fin");
                    AnneeAcademique aa = new AnneeAcademique();
                    if (academiqueDao.findByNumeroDebut(Integer.parseInt(an)) == null) {
                        aa.setNumeroDebut(Integer.parseInt(an));
                        aa.setDebut(java.sql.Date.valueOf(db));
                        aa.setFin(java.sql.Date.valueOf(fn));
                        academiqueDao.create(aa);
                    } else {
                        aa = academiqueDao.findByNumeroDebut(Integer.parseInt(an));
                    }
                    int j = 0;
                    NodeList notes = annee.getElementsByTagName("Note");
                    //TODO find a better means to deal with this
                    for (Note n : noteDao.getNoteByAnnee(aa)) {
                        noteDao.delete(n);
                    }
                    while(j < notes.getLength()){
                        if (notes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element note = (Element) notes.item(j++);
                            String cr = note.getAttribute("cour");
                            String etdnt = note.getAttribute("etudiant");
                            String eval = note.getAttribute("evaluation");
                            String session = note.getAttribute("session");
                            String val = note.getElementsByTagName("Valeur").item(0).getTextContent();
                            Note n = new Note();
                            n.setAnneeAcademique(aa);
                            n.setCours(coursDao.findByIntitule(cr));
                            n.setEtudiant(etudiantDao.findByMatricule(etdnt));
                            n.setEvaluation(evaluationDao.findByCode(eval));
                            if(evaluationDao.findByCode(eval).isExam())
                                n.setSession(Session.valueOf(session));
                            n.setValeur(Double.parseDouble(val));
                            noteDao.create(n);
                        }
                               
                    }
                    j = 0;
                    NodeList credits = annee.getElementsByTagName("Credit");
                    //TODO find a better means to deal with this
                    for (Credit credit : creditDao.findCreditByAnnee(aa)) {
                        creditDao.delete(credit);
                    }
                    while(j < credits.getLength()){
                        if (credits.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element credit = (Element) credits.item(j++);
                            String cr = credit.getAttribute("cour");
                            String nv = credit.getAttribute("niveau");
                            String opt = credit.getAttribute("option");
                            String val = credit.getElementsByTagName("Valeur").item(0).getTextContent();
                            
                            Credit cred = new Credit();
                            cred.setCours(coursDao.findByIntitule(cr));
                            cred.setParcours(parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)));
                            cred.setValeur(Integer.parseInt(val));
                            creditDao.create(cred);
                        }
                               
                    }
                    j = 0;
                    NodeList inscriptions = annee.getElementsByTagName("Inscription");
                    //TODO find a better means to deal with this
                    for (Inscription inscription : inscriptionDao.findByAnnee(aa)) {
                        inscriptionDao.delete(inscription);
                    }
                    while(j < inscriptions.getLength()){
                        if (inscriptions.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element inscription = (Element) inscriptions.item(j++);
                            String etdnt = inscription.getAttribute("etudiant");
                            String nv = inscription.getAttribute("niveau");
                            String opt = inscription.getAttribute("option");
                            Inscription insc = new Inscription();
                            insc.setEtudiant(etudiantDao.findByMatricule(etdnt));
                            insc.setParcours(parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)));
                            inscriptionDao.create(insc);
                        }
                               
                    }
                    j = 0;
                    NodeList enseignements = annee.getElementsByTagName("Enseignement");
                    //TODO find a better means to deal with this
                    for (Enseignement enseignement : enseignementDao.findByAnnee(aa)) {
                        enseignementDao.delete(enseignement);
                    }
                    while(j < enseignements.getLength()){
                        if (enseignements.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element enseignement = (Element) enseignements.item(j++);
                            List<Enseignant> ens = new ArrayList();
                            NodeList ensgnt = root.getElementsByTagName("Enseignant");
                            for (int k = 0; k < ensgnt.getLength(); k++) {
                                if (ensgnt.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    ens.add(enseignantDao.findByName(ensgnt.item(k).getTextContent()));
                                }
                            }
                            String cr = enseignement.getAttribute("cour");
                            String nv = enseignement.getAttribute("niveau");
                            String opt = enseignement.getAttribute("option");
                            Enseignement ensg = new Enseignement();
                            ensg.setCours(coursDao.findByIntitule(cr));
                            ensg.setEnseignants(ens);
                            ensg.setParcours(parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)));
                            enseignementDao.create(ensg);
                        }
                               
                    }
                    j = 0;
                    NodeList programme = annee.getElementsByTagName("Programme");
                    //TODO find a better means to deal with this
                    for (Programme programme1 : programmeDao.findByAnnee(aa)) {
                        programmeDao.delete(programme1);
                    }
                    while(j < inscriptions.getLength()){
                        if (inscriptions.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element inscription = (Element) inscriptions.item(j++);
                            String smstr = inscription.getAttribute("semstre");
                            String uni = inscription.getAttribute("uniteEnseignement");
                            String nv = inscription.getAttribute("niveau");
                            String opt = inscription.getAttribute("option");
                            Programme pgrmm = new Programme();
                            pgrmm.setSemestre(semestreDao.findByIntutle(smstr));
                            pgrmm.setUniteEnseignement(uniteEnseignementDao.findByCode(uni));
                            pgrmm.setParcours(parcoursDao.findByNiveauOption(niveauDao.findByCode(nv), optionDao.findByCode(opt)));
                            programmeDao.create(pgrmm);
                        }
                               
                    }
                }
            }
            erreur.setNombresErreurs(nombresErreurs);
            return erreur;
        } catch (ParserConfigurationException | SAXException | IOException | DataAccessException ex) {
            Logger.getLogger(DatabaseBackupServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
