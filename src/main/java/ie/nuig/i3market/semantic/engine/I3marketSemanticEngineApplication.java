/**
 Copyright (c) 2020-2022 in alphabetical order:

 ATHENS UNIVERSITY OF ECONOMICS AND BUSINESS - RESEARCH CENTER
 ATOS SPAIN SA
 EUROPEAN DIGITAL SME ALLIANCE
 GFT ITALIA SRL
 GUARDTIME OU
 HOP UBIQUITOUS SL
 IBM RESEARCH GMBH
 IDEMIA FRANCE
 NATIONAL UNIVERSITY OF IRELAND GALWAY (NUI Galway)
 SIEMENS AKTIENGESELLSCHAFT
 SIEMENS SRL
 TELESTO TECHNOLOGIES PLIROFORIKIS KAI EPIKOINONION EPE
 UNIVERSITAT POLITECNICA DE CATALUNYA
 UNPARALLEL INNOVATION LDA

 This program and the accompanying materials are made
 available under the terms of the MIT License, which is available at https://opensource.org/licenses/MIT

 License-Identifier: EUPL-2.0

 Contributors:

 Achille Zappa (NUI Galway)
 Chi-Hung Le (NUI Galway)
 Qaiser Medmood (NUI Galway)
 */

package ie.nuig.i3market.semantic.engine;

import ie.nuig.i3market.semantic.engine.config.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class I3marketSemanticEngineApplication {

	private static final Logger log = LoggerFactory.getLogger(I3marketSemanticEngineApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(I3marketSemanticEngineApplication.class);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		String serverPort = env.getProperty("server.port");
		String contextPath = env.getProperty("server.servlet.context-path");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\t{}://localhost:{}{}swagger-ui.html\n\t" +
						"External: \t{}://{}:{}{}swagger-ui.html\n\t" +
						"Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				protocol,
				serverPort,
				contextPath,
				protocol,
				hostAddress, // This will return host address as 172.26.144.1
				serverPort,
				contextPath,
				env.getActiveProfiles());
	}

}
