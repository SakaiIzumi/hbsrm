package net.bncloud.uaa.security.core;

import javax.security.auth.Subject;
import java.security.Principal;

public class BasePrincipal implements Principal {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
