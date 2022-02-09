package ie.nuig.i3market.semantic.engine.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServiceUtil {
  private static final Logger loggger = LoggerFactory.getLogger(ServiceUtil.class);

  private final String port;

  private final String databasePublicIp;

  private final String databasePort;


  private String serviceAddress = null;


  @Autowired
  public ServiceUtil(@Value("${server.port}") String port,
                     @Value("${databasePublicIp.ipVal}") String databasePublicIp,
                     @Value("${databasePublicIp.port}")  String databasePort) {

    this.port = port;
    this.databasePublicIp = databasePublicIp;
    this.databasePort = databasePort;
  }


  private String findMyHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "unknown host name";
    }
  }

  private String findMyIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return "unknown IP address";
    }
  }

  public String findExecludedDatabaseIpAddress() {
    try {
      String execludDatabaseIp = databasePublicIp + ":" + databasePort;
      loggger.info("This node database IP address is, so skip it : "+ execludDatabaseIp);
      return execludDatabaseIp;
    } catch (Exception ex) {
      return "unknown IP address";
    }
  }
}
