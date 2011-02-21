package br.gov.frameworkdemoiselle.internal.configuration;

import br.gov.frameworkdemoiselle.configuration.Configuration;

@Configuration
public class InternalConfig {
	private String databaseName;
	private String databaseVersion;
	private String domainPackage;
	private String domainClasses;

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseVersion() {
		return databaseVersion;
	}

	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	public String getDomainPackage() {
		return domainPackage;
	}

	public void setDomainPackage(String domainPackage) {
		this.domainPackage = domainPackage;
	}

	public String getDomainClasses() {
		return domainClasses;
	}

	public void setDomainClasses(String domainClasses) {
		this.domainClasses = domainClasses;
	}

}