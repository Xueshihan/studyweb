package cn.iatc.mqtt.utils;

import cn.hutool.core.util.StrUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SslUtil {
    /**
     * 获取 tls 安全套接字工厂   (单向认证，服务器端认证)
     *
     * @param caCrtFile null:使用系统默认的 ca 证书来验证。 非 null:指定使用的 ca 证书来验证服务器的证书。
     * @return tls 套接字工厂
     * @throws Exception
     */
    public static SSLSocketFactory getSocketFactory(final String caCrtFile) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, KeyManagementException {
        Security.addProvider(new BouncyCastleProvider());
        //===========加载 ca 证书==================================
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        if (StrUtil.isNotBlank(caCrtFile)) {
            // 加载本地指定的 ca 证书
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate) fact.generateCertificate(Files.newInputStream(Paths.get(caCrtFile)));

            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", caCert);
            // 把ca作为信任的 ca 列表,来验证服务器证书
            tmf.init(caKs);
        } else {
            //使用系统默认的安全证书
            tmf.init((KeyStore) null);
        }

        // ============finally, create SSL socket factory==============
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

//    public static SSLSocketFactory getSSLSocktet(String caCrtFile) throws Exception {
//        // CA certificate is used to authenticate server
//        CertificateFactory cAf = CertificateFactory.getInstance("X.509");
//        FileInputStream caIn = new FileInputStream(caCrtFile);
//        X509Certificate ca = (X509Certificate) cAf.generateCertificate(caIn);
//        KeyStore caKs = KeyStore.getInstance("JKS");
//        caKs.load(null, null);
//        caKs.setCertificateEntry("ca-certificate", ca);
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
//        tmf.init(caKs);
//
//        // finally, create SSL socket factory
//        SSLContext context = SSLContext.getInstance("TLSv1.2");
//        context.init(null, tmf.getTrustManagers(), new SecureRandom());
//
//        return context.getSocketFactory();
//    }

    /**
     * 双向认证
     */
    public static SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile,
                                                    final String password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        X509Certificate caCert = null;

        FileInputStream fis = new FileInputStream(caCrtFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {
            caCert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client certificate
        bis = new BufferedInputStream(new FileInputStream(crtFile));
        X509Certificate cert = null;
        while (bis.available() > 0) {
            cert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client private key
        PEMParser pemParser = new PEMParser(new FileReader(keyFile));
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair key = converter.getKeyPair((PEMKeyPair) object);
        pemParser.close();


        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
                new java.security.cert.Certificate[]{cert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}
