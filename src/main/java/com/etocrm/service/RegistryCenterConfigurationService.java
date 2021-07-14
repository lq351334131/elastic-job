package com.etocrm.service;

import com.etocrm.pojo.RegistryCenterConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

public interface RegistryCenterConfigurationService {

    Collection<RegistryCenterConfiguration> getRegCenter(HttpServletRequest request);

}
