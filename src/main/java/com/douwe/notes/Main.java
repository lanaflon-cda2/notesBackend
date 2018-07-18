package com.douwe.notes;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.service.IUtilisateurService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */


@SpringBootApplication
public class Main {

	public static void main(String[] args) throws DataAccessException {
           
            ApplicationContext cxt = SpringApplication.run(Main.class, args);
            IUtilisateurService utilisateurDao = cxt.getBean(IUtilisateurService.class);
            if(utilisateurDao.findAll().isEmpty()){
                Utilisateur u = new Utilisateur();
                u.setLogin("admin");
                u.setNom("administrateur");
                u.setEmail("admin@admin.info");
                u.setPassword(new BCryptPasswordEncoder().encode("admin123"));
                utilisateurDao.create(u);
                
            }
           
	}
}