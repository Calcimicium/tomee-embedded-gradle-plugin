package fr.laboiteadodo.gradle;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

import org.apache.tomee.embedded.Configuration;
import org.gradle.api.file.*;
import org.gradle.api.provider.*;

public class ConfigurationCustomizer implements Configuration.ConfigurationCustomizer {
	private final StartTask startTask;

	public ConfigurationCustomizer(StartTask startTask) {
		this.startTask = startTask;
	}

	@Override
	public void customize(Configuration configuration) {
		update(startTask.getHttpPort(), configuration::setHttpPort);
		update(startTask.getStopPort(), configuration::setStopPort);
		update(startTask.getHost(), configuration::setHost);
		update(startTask.getHttpPort(), configuration::setHttpPort);
		update(startTask.getStopPort(), configuration::setStopPort);
		update(startTask.getHost(), configuration::setHost);
		update(startTask.getDir(), configuration::setDir);
		update(startTask.getServerXml(), configuration::setServerXml);
		update(startTask.getKeepServerXmlAsThis(), configuration::setKeepServerXmlAsThis);
		update(startTask.getProperties(), configuration::setProperties);
		update(startTask.getQuickSession(), configuration::setQuickSession);
		update(startTask.getSkipHttp(), configuration::setSkipHttp);
		update(startTask.getHttpsPort(), configuration::setHttpsPort);
		update(startTask.getSsl(), configuration::setSsl);
		update(startTask.getWithEjbRemote(), configuration::setWithEjbRemote);
		update(startTask.getKeystoreFile(), configuration::setKeystoreFile);
		update(startTask.getKeystorePass(), configuration::setKeystorePass);
		update(startTask.getKeystoreType(), configuration::setKeystoreType);
		update(startTask.getClientAuth(), configuration::setClientAuth);
		update(startTask.getKeyAlias(), configuration::setKeyAlias);
		update(startTask.getSslProtocol(), configuration::setSslProtocol);
		update(startTask.getWebXml(), configuration::setWebXml);
		update(startTask.getLoginConfig(), configuration::loginConfig);
		update(startTask.getSecurityConstraints(), configuration::securityConstaint);
		update(startTask.getRealm(), configuration::setRealm);
		update(startTask.getDeployOpenEjbApp(), configuration::setDeployOpenEjbApp);
		update(startTask.getUsers(), configuration::setUsers);
		update(startTask.getRoles(), configuration::setRoles);
		update(startTask.getTempDir(), configuration::setTempDir);
		update(startTask.getWebResourceCached(), configuration::setWebResourceCached);
		update(startTask.getConf(), configuration::setConf);
		update(startTask.getClassesFilter(), configuration::setClassesFilter);
	}

	private static <T> void update(Property<T> property, Consumer<T> setter) {
		Optional.of(property).map(Provider::get).ifPresent(setter);
	}

	private static <T> void update(ListProperty<T> property, Consumer<T> adder) {
		Optional.of(property).map(Provider::get).stream().flatMap(Collection::stream).forEach(adder);
	}

	private static <K, V> void update(MapProperty<K, V> property, Consumer<Map<K, V>> setter) {
		Optional.of(property).map(Provider::get).ifPresent(setter);
	}

	private static void update(RegularFileProperty property, Consumer<String> setter) {
		Optional.of(property).map(Provider::get).map(RegularFile::getAsFile).map(File::getAbsolutePath).ifPresent(setter);
	}

	private static void update(DirectoryProperty property, Consumer<String> setter) {
		Optional.of(property).map(Provider::get).map(Directory::getAsFile).map(File::getAbsolutePath).ifPresent(setter);
	}
}
