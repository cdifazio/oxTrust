/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.service.external;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.gluu.oxtrust.model.GluuCustomPerson;
import org.xdi.model.SimpleCustomProperty;
import org.xdi.model.custom.script.CustomScriptType;
import org.xdi.model.custom.script.conf.CustomScriptConfiguration;
import org.xdi.model.custom.script.type.user.UpdateUserType;
import org.xdi.service.custom.script.ExternalScriptService;

/**
 * Provides factory methods needed to create external dynamic client registration extension
 *
 * @author Yuriy Movchan Date: 01/08/2015
 */
@ApplicationScoped
@Named
public class ExternalUpdateUserService extends ExternalScriptService {

	private static final long serialVersionUID = 1416361273036208685L;

	public ExternalUpdateUserService() {
		super(CustomScriptType.UPDATE_USER);
	}

	public boolean executeExternalUpdateUserMethod(CustomScriptConfiguration customScriptConfiguration, GluuCustomPerson user, boolean persisted) {
		try {
			log.debug("Executing python 'updateUser' method");
			UpdateUserType externalType = (UpdateUserType) customScriptConfiguration.getExternalType();
			Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
			return externalType.updateUser(user, persisted, configurationAttributes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return false;
	}
	
	
	public boolean executeExternalAddUserMethod(CustomScriptConfiguration customScriptConfiguration, GluuCustomPerson user, boolean persisted) {
		try {
			log.debug("Executing python 'addUser' method");
			UpdateUserType externalType = (UpdateUserType) customScriptConfiguration.getExternalType();
			Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
			return externalType.addUser(user, persisted, configurationAttributes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return false;
	}

	public boolean executeExternalAddUserMethods(GluuCustomPerson user) {
		boolean result = true;
		for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
			result &= executeExternalAddUserMethod(customScriptConfiguration, user, true);
			if (!result) {
				return result;
			}
		}

		return result;
	}

	public boolean executeExternalUpdateUserMethods(GluuCustomPerson user) {
		boolean result = true;
		for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
			result &= executeExternalUpdateUserMethod(customScriptConfiguration, user, true);
			if (!result) {
				return result;
			}
		}

		return result;
	}
	
	public boolean executeExternalNewUserMethod(CustomScriptConfiguration customScriptConfiguration, GluuCustomPerson user) {
		try {
			log.debug("Executing python 'newUser' method");
			UpdateUserType externalType = (UpdateUserType) customScriptConfiguration.getExternalType();
			Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
			return externalType.newUser(user, configurationAttributes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return false;
	}

	public boolean executeExternalNewUserMethods(GluuCustomPerson user) {
		boolean result = true;
		for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
			result &= executeExternalNewUserMethod(customScriptConfiguration, user);
			if (!result) {
				return result;
			}
		}

		return result;
	}
	
	public boolean executeExternalDeleteUserMethod(CustomScriptConfiguration customScriptConfiguration, GluuCustomPerson user, boolean persisted) {
		try {
			log.debug("Executing python 'addUser' method");
			UpdateUserType externalType = (UpdateUserType) customScriptConfiguration.getExternalType();
			Map<String, SimpleCustomProperty> configurationAttributes = customScriptConfiguration.getConfigurationAttributes();
			return externalType.deleteUser(user, persisted, configurationAttributes);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return false;
	}

	public boolean executeExternalDeleteUserMethods(GluuCustomPerson user) {
		boolean result = true;
		for (CustomScriptConfiguration customScriptConfiguration : this.customScriptConfigurations) {
			result &= executeExternalDeleteUserMethod(customScriptConfiguration, user, true);
			if (!result) {
				return result;
			}
		}

		return result;
	}

}
