/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gluu.oxtrust.config.OxTrustConfiguration;
import org.gluu.oxtrust.ldap.service.ApplianceService;
import org.gluu.oxtrust.ldap.service.OrganizationService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;
import org.xdi.model.ApplicationType;
import org.xdi.model.metric.MetricType;
import org.xdi.model.metric.ldap.MetricEntry;

/**
 * Store and retrieve metric
 *
 * @author Rahat Ali Date: 07/30/2015
 */
@Scope(ScopeType.APPLICATION)
@Name(MetricService.METRIC_SERVICE_COMPONENT_NAME)
@Startup
public class MetricService extends org.xdi.service.metric.MetricService {

	public static final String METRIC_SERVICE_COMPONENT_NAME = "metricService";

	private static final long serialVersionUID = 7875838160379126796L;

	@Logger
	private Log log;

	@In
	private ApplianceService applianceService;
	
	@In
	private OrganizationService organizationService;

	@In
	private OxTrustConfiguration oxTrustConfiguration;

	Map<MetricType, List<MetricEntry>> entries;
	Map<String,Integer> successStats;
	Map<String,Integer> failureStats;
	
	@Create
	public void create() {
//		init(3000);
		List<MetricType> metricTypes = new ArrayList<MetricType>();
		metricTypes.add(MetricType.OXAUTH_USER_AUTHENTICATION_FAILURES);
		metricTypes.add(MetricType.OXAUTH_USER_AUTHENTICATION_SUCCESS);
		metricTypes.add(MetricType.OXAUTH_USER_AUTHENTICATION_RATE);

		Date endDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.MONTH ,  -1 );

		Date startDate = calendar.getTime();
		try {
			entries = findMetricEntry(ApplicationType.OX_AUTH,
					oxTrustConfiguration.getApplicationConfiguration()
							.getApplianceInum(), metricTypes, startDate,
					endDate, null);
			System.out.println(entries);
			successStats = calculateMonthlyStatistics(entries.get(MetricType.OXAUTH_USER_AUTHENTICATION_SUCCESS));
			failureStats = calculateMonthlyStatistics(entries.get(MetricType.OXAUTH_USER_AUTHENTICATION_FAILURES));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private Map<String, Integer> calculateMonthlyStatistics(
			List<MetricEntry> success) {
		Map<String, Integer> stats = new HashMap<String, Integer>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (MetricEntry metricEntry : success) {
			Date date = metricEntry.getCreationDate();
			String dateString =df.format(date);
			Integer count = stats.get(dateString);
			if(count!=null)
				stats.put(dateString,count++);
			else
				stats.put(dateString,1);
		}
		return stats;
	}

	@Override
	public String baseDn() {
		String orgDn = OrganizationService.instance().getDnForOrganization();
		String baseDn = String.format("ou=metric,%s", orgDn);
		return baseDn;
	}

	@Override
	public String applianceInum() {
		return applianceService.getApplianceInum();
	}

	@Override
	public String getComponentName() {
		return METRIC_SERVICE_COMPONENT_NAME;
	}

	public Map<MetricType, List<MetricEntry>> getEntries() {
		return entries;
	}

	public void setEntries(Map<MetricType, List<MetricEntry>> entries) {
		this.entries = entries;
	}

	public static MetricService instance() {
		return (MetricService) Component.getInstance(MetricService.class);
	}

	public Map<String, Integer> getSuccessStats() {
		return successStats;
	}

	public void setSuccessStats(Map<String, Integer> successStats) {
		this.successStats = successStats;
	}

	public Map<String, Integer> getFailureStats() {
		return failureStats;
	}

	public void setFailureStats(Map<String, Integer> failureStats) {
		this.failureStats = failureStats;
	}

}