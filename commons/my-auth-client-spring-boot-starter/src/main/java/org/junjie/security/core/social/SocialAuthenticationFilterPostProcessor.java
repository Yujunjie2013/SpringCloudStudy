package org.junjie.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

public interface SocialAuthenticationFilterPostProcessor {
    void process(SocialAuthenticationFilter filter);
}
