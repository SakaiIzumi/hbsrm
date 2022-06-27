package net.bncloud.common.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUser()
                .map(BncUserDetails::getId)
                .orElse(-1L));
    }
}
