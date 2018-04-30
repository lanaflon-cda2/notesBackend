package com.douwe.notes.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Configuration
public class SecurityInitializer  extends AbstractSecurityWebApplicationInitializer{

    public SecurityInitializer() {
        super(SecurityConfigurator.class);
    }
}
