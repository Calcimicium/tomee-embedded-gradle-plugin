package fr.laboiteadodo.gradle;

import java.util.Properties;

import javax.inject.Inject;

import org.apache.catalina.Realm;
import org.apache.catalina.realm.JAASRealm;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomee.embedded.*;
import org.apache.xbean.finder.filter.Filter;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.*;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.*;
import org.gradle.api.tasks.*;

public class StartTask extends DefaultTask {
	/**
	 * HTTP port
	 */
	@Input
	@Optional
	private final Property<Integer> httpPort;

	/**
	 * Shutdown port
	 */
	@Input
	@Optional
	private final Property<Integer> stopPort;

	/**
	 * Host
	 */
	@Input
	private final Property<String> host;

	/**
	 * Where to create a file hierarchy for tomee (conf, temp, …)
	 */
	@InputDirectory
	@Optional
	private final DirectoryProperty dir;

	/**
	 * Which server.xml to use
	 */
	@InputFile
	@Optional
	private final RegularFileProperty serverXml;

	/**
	 * Don’t adjust ports/host from the configuration and keep the ones in server.xml
	 */
	@Input
	@Optional
	private final Property<Boolean> keepServerXmlAsThis;

	/**
	 * Container properties
	 */
	@Input
	@Optional
	private final Property<Properties> properties;

	/**
	 * Use Random instead of SecureRandom (for dev)
	 */
	@Input
	@Optional
	private final Property<Boolean> quickSession;

	/**
	 * Don’t use the HTTP connector
	 */
	@Input
	@Optional
	private final Property<Boolean> skipHttp;

	/**
	 * HTTPS port
	 */
	@Input
	@Optional
	private final Property<Integer> httpsPort;

	/**
	 * Activate HTTPS
	 */
	@Input
	@Optional
	private final Property<Boolean> ssl;

	/**
	 * Use EJBd
	 */
	@Input
	@Optional
	private final Property<Boolean> withEjbRemote;

	/**
	 * HTTPS keystore location
	 */
	@InputFile
	@Optional
	private final RegularFileProperty keystoreFile;

	/**
	 * HTTPS keystore password
	 */
	@Input
	@Optional
	private final Property<String> keystorePass;

	/**
	 * HTTPS keystore type
	 */
	@Input
	@Optional
	private final Property<String> keystoreType;

	/**
	 * HTTPS client auth
	 */
	@Input
	@Optional
	private final Property<String> clientAuth;

	/**
	 * HTTPS alias
	 */
	@Input
	@Optional
	private final Property<String> keyAlias;

	/**
	 * SSL protocol for https connector
	 */
	@Input
	@Optional
	private final Property<String> sslProtocol;

	/**
	 * Default web.xml to use
	 */
	@InputFile
	@Optional
	private final RegularFileProperty webXml;

	/**
	 * Which {@link LoginConfig} to use, relies on {@link LoginConfigBuilder} to create it
	 */
	@Input
	@Optional
	private final Property<LoginConfigBuilder> loginConfig;

	/**
	 * Add some security constraints, use {@link SecurityConstaintBuilder} to build them
	 */
	@Input
	@Optional
	private final ListProperty<SecurityConstaintBuilder> securityConstraints;

	/**
	 * Which realm to use (useful to switch to {@link JAASRealm} for instance) without modifying the application
	 */
	@Input
	@Optional
	private final Property<Realm> realm;

	/**
	 * Should internal openejb application be deployed
	 */
	@Input
	@Optional
	private final Property<Boolean> deployOpenEjbApp;

	/**
	 * A map of user/password
	 */
	@Input
	@Optional
	private final MapProperty<String, String> users;

	/**
	 * A map of role/users
	 */
	@Input
	@Optional
	private final MapProperty<String, String> roles;

	/**
	 * Tomcat needs a docBase, in case you don’t provide one, one will be created there
	 */
	@InputDirectory
	@Optional
	private final DirectoryProperty tempDir;

	/**
	 * Should web resources be cached by tomcat (set false in frontend dev)
	 */
	@Input
	@Optional
	private final Property<Boolean> webResourceCached;

	/**
	 * Location (classpath or file) to a .properties to configure the server
	 */
	@InputFile
	@Optional
	private final RegularFileProperty conf;

	/**
	 * Implementation of a custom xbean {@link Filter} to ignore not desired classes during scanning
	 */
	@Input
	@Optional
	private final Property<Filter> classesFilter;

	@Inject
	public StartTask(ObjectFactory objectFactory) {
		this.httpPort = objectFactory.property(Integer.class);
		this.stopPort = objectFactory.property(Integer.class);
		this.host = objectFactory.property(String.class);
		this.dir = objectFactory.directoryProperty();
		this.serverXml = objectFactory.fileProperty();
		this.keepServerXmlAsThis = objectFactory.property(Boolean.class);
		this.properties = objectFactory.property(Properties.class);
		this.quickSession = objectFactory.property(Boolean.class);
		this.skipHttp = objectFactory.property(Boolean.class);
		this.httpsPort = objectFactory.property(Integer.class);
		this.ssl = objectFactory.property(Boolean.class);
		this.withEjbRemote = objectFactory.property(Boolean.class);
		this.keystoreFile = objectFactory.fileProperty();
		this.keystorePass = objectFactory.property(String.class);
		this.keystoreType = objectFactory.property(String.class);
		this.clientAuth = objectFactory.property(String.class);
		this.keyAlias = objectFactory.property(String.class);
		this.sslProtocol = objectFactory.property(String.class);
		this.webXml = objectFactory.fileProperty();
		this.loginConfig = objectFactory.property(LoginConfigBuilder.class);
		this.securityConstraints = objectFactory.listProperty(SecurityConstaintBuilder.class);
		this.realm = objectFactory.property(Realm.class);
		this.deployOpenEjbApp = objectFactory.property(Boolean.class);
		this.users = objectFactory.mapProperty(String.class, String.class);
		this.roles = objectFactory.mapProperty(String.class, String.class);
		this.tempDir = objectFactory.directoryProperty();
		this.webResourceCached = objectFactory.property(Boolean.class);
		this.conf = objectFactory.fileProperty();
		this.classesFilter = objectFactory.property(Filter.class);
	}

	public Property<Integer> getHttpPort() {
		return httpPort;
	}

	public Property<Integer> getStopPort() {
		return stopPort;
	}

	public Property<String> getHost() {
		return host;
	}

	public DirectoryProperty getDir() {
		return dir;
	}

	public RegularFileProperty getServerXml() {
		return serverXml;
	}

	public Property<Boolean> getKeepServerXmlAsThis() {
		return keepServerXmlAsThis;
	}

	public Property<Properties> getProperties() {
		return properties;
	}

	public Property<Boolean> getQuickSession() {
		return quickSession;
	}

	public Property<Boolean> getSkipHttp() {
		return skipHttp;
	}

	public Property<Integer> getHttpsPort() {
		return httpsPort;
	}

	public Property<Boolean> getSsl() {
		return ssl;
	}

	public Property<Boolean> getWithEjbRemote() {
		return withEjbRemote;
	}

	public RegularFileProperty getKeystoreFile() {
		return keystoreFile;
	}

	public Property<String> getKeystorePass() {
		return keystorePass;
	}

	public Property<String> getKeystoreType() {
		return keystoreType;
	}

	public Property<String> getClientAuth() {
		return clientAuth;
	}

	public Property<String> getKeyAlias() {
		return keyAlias;
	}

	public Property<String> getSslProtocol() {
		return sslProtocol;
	}

	public RegularFileProperty getWebXml() {
		return webXml;
	}

	public Property<LoginConfigBuilder> getLoginConfig() {
		return loginConfig;
	}

	public ListProperty<SecurityConstaintBuilder> getSecurityConstraints() {
		return securityConstraints;
	}

	public Property<Realm> getRealm() {
		return realm;
	}

	public Property<Boolean> getDeployOpenEjbApp() {
		return deployOpenEjbApp;
	}

	public MapProperty<String, String> getUsers() {
		return users;
	}

	public MapProperty<String, String> getRoles() {
		return roles;
	}

	public DirectoryProperty getTempDir() {
		return tempDir;
	}

	public Property<Boolean> getWebResourceCached() {
		return webResourceCached;
	}

	public RegularFileProperty getConf() {
		return conf;
	}

	public Property<Filter> getClassesFilter() {
		return classesFilter;
	}

	@TaskAction
	public void run() {
		var configuration = new Configuration();
		configuration.addCustomizer(new ConfigurationCustomizer(this));

		try (var container = new Container(configuration).deployClasspathAsWebApp()) {
			System.out.println("Started on http://localhost:" + container.getConfiguration().getHttpPort());

			// do something or wait until the end of the application
		}
	}

}
