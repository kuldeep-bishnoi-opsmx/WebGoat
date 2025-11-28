/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides a real 302 redirect for experimentation separate from assignment scoring.
 */
@Controller
public class OpenRedirectRealRedirect {

  // Allow redirects only to trusted internal hosts
  private static final Set<String> TRUSTED_HOSTS = Set.of("webgoat.local", "localhost", "127.0.0.1");

  @GetMapping("/OpenRedirect/realRedirect")
  public ModelAndView real(@RequestParam("url") String url) {
    // Validate URL to prevent open redirect attacks
    if (!isValidRedirectUrl(url)) {
      // Default to safe internal page if validation fails
      return new ModelAndView("redirect:/welcome.mvc");
    }
    return new ModelAndView("redirect:" + url);
  }

  private boolean isValidRedirectUrl(String url) {
    if (url == null || url.isBlank()) {
      return false;
    }
    
    // Allow relative URLs starting with /
    if (url.startsWith("/")) {
      return true;
    }
    
    // For absolute URLs, validate the host
    try {
      URI uri = new URI(url);
      String host = uri.getHost();
      if (host == null) {
        return false;
      }
      return TRUSTED_HOSTS.contains(host.toLowerCase());
    } catch (URISyntaxException e) {
      return false;
    }
  }
}
