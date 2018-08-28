package com.douwe.notes;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.dao.impl.EtudiantDaoImpl;
import com.douwe.notes.dao.impl.InscriptionDaoImpl;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Genre;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Role;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.service.IEtudiantService;
import com.douwe.notes.service.IInscriptionService;
import com.douwe.notes.service.IUtilisateurService;
import com.douwe.notes.service.ServiceException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@SpringBootApplication
public class Main extends JpaBaseConfiguration {

    /**
     * @param dataSource
     * @param properties
     * @param jtaTransactionManagerProvider
     * @param transactionManagerCustomizers
     */
    protected Main(DataSource dataSource, JpaProperties properties,
            ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider,
            ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, properties, jtaTransactionManagerProvider, transactionManagerCustomizers);
    }

    /*
	 * (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#createJpaVendorAdapter()
     */
    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    /*
	 * (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#getVendorProperties()
     */
    @Override
    protected Map<String, Object> getVendorProperties() {

        // Turn off dynamic weaving to disable LTW lookup in static weaving mode
        return Collections.singletonMap("eclipselink.weaving", "false");
    }

    public static void main(String[] args) throws DataAccessException, ServiceException {

        ApplicationContext cxt = SpringApplication.run(Main.class, args);
        IUtilisateurService utilisateurDao = cxt.getBean(IUtilisateurService.class);
        if (utilisateurDao.findAll().isEmpty()) {
            Utilisateur u = new Utilisateur();
            u.setLogin("admin");
            u.setNom("administrateur");
            u.setEmail("admin@admin.info");
            u.setRole(Role.ADMINISTRATEUR);
            u.setActive(1);
            u.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            utilisateurDao.create(u);

        }
        IEtudiantService etudiantDao = cxt.getBean(IEtudiantService.class);
        Etudiant e = new Etudiant();
        if(etudiantDao.getAllEtudiant().isEmpty()){
            e.setActive(1);
            e.setDateDeNaissance(Date.from(Instant.now()));
            e.setEmail("etudiantToto@enspm.cm");
            e.setGenre(Genre.masculin);
            e.setId(Long.MAX_VALUE);
            e.setLieuDeNaissance("Touloum");
            e.setMatricule("14A500S");
            e.setNom("Mankreo Philippe");
            e.setNumeroTelephone("+237658563102");
            e.setValidDate(true);
            e.setVersion(0);
            etudiantDao.saveOrUpdateEtudiant(e);
        }
    }
}
