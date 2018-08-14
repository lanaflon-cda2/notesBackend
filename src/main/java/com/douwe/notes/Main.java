package com.douwe.notes;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.entities.Role;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.service.IUtilisateurService;
import java.util.Collections;
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

    public static void main(String[] args) throws DataAccessException {

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

    }
}
