package fr.pick_eat.socketspring.config;

import fr.pick_eat.socketspring.SocketSpringApplication;
import fr.pick_eat.socketspring.service.JwtService;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public class RsaKeyProperties {
    @Getter
    private RSAPublicKey publicKey;
    private String publicKeyPath;
    public void setPublicKey(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
        loadPublicKey();
    }

    private void loadPublicKey() {
        // Load the public key from the classpath
        try {
            String publicKeyString = publicKeyPath;
            if (publicKeyPath.startsWith("classpath:")) {
                URL publicKeyUrl = SocketSpringApplication.class.getClassLoader().getResource(publicKeyPath.replace("classpath:", ""));
                Assert.notNull(publicKeyUrl, "Public key not found at " + publicKeyPath);
                publicKeyString = new String(publicKeyUrl.openStream().readAllBytes());
            }
            byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKeyString
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", ""));
            java.security.spec.X509EncodedKeySpec spec = new java.security.spec.X509EncodedKeySpec(keyBytes);
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load public key", e);
        }
    }

}