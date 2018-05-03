package com.tax.fcr.library.network;

import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by zhy on 15/12/14.
 */
public class HttpsUtils {

    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    public static SSLParams getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        SSLParams sslParams = new SSLParams();
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);

            X509TrustManager trustManager = null;
            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }

            sslParams.sSLSocketFactory = new SSL(keyManagers, trustManager);
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    public static HostnameVerifier getHostnameVerifier() {
        return new UnSafeHostnameVerifier();
    }

    private static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)

                {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }


    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class SSL extends SSLSocketFactory {
        private SSLSocketFactory defaultFactory;
        static String protocols[] = null, cipherSuites[] = null;

        static {
            try {
                SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
                if (socket != null) {
                /* set reasonable protocol versions */
                    // - enable all supported protocols (enables TLSv1.1 and TLSv1.2 on Android <5.0)
                    // - remove all SSL versions (especially SSLv3) because they‘re insecure now
                    List<String> protocols = new LinkedList<>();
                    for (String protocol : socket.getSupportedProtocols())
                        if (!protocol.toUpperCase().contains("SSL"))
                            protocols.add(protocol);
                    SSL.protocols = protocols.toArray(new String[protocols.size()]);
                /* set up reasonable cipher suites */
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        // choose known secure cipher suites
                        List<String> allowedCiphers = Arrays.asList(
                                // TLS 1.2
                                "TLS_RSA_WITH_AES_256_GCM_SHA384",
                                "TLS_RSA_WITH_AES_128_GCM_SHA256",
                                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                                "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                                "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                                "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
                                // maximum interoperability
                                "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                                "TLS_RSA_WITH_AES_128_CBC_SHA",
                                // additionally
                                "TLS_RSA_WITH_AES_256_CBC_SHA",
                                "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                                "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
                        List<String> availableCiphers = Arrays.asList(socket.getSupportedCipherSuites());
                        // take all allowed ciphers that are available and put them into preferredCiphers
                        HashSet<String> preferredCiphers = new HashSet<>(allowedCiphers);
                        preferredCiphers.retainAll(availableCiphers);
                    /* For maximum security, preferredCiphers should *replace* enabled ciphers (thus disabling
                     * ciphers which are enabled by default, but have become unsecure), but I guess for
                     * the security level of DAVdroid and maximum compatibility, disabling of insecure
                     * ciphers should be a server-side task */
                        // add preferred ciphers to enabled ciphers
                        HashSet<String> enabledCiphers = preferredCiphers;
                        enabledCiphers.addAll(new HashSet<>(Arrays.asList(socket.getEnabledCipherSuites())));
                        SSL.cipherSuites = enabledCiphers.toArray(new String[enabledCiphers.size()]);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public SSL(KeyManager[] keyManagers, X509TrustManager tm) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, (tm != null) ? new X509TrustManager[]{tm} : null, new SecureRandom());
                defaultFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new AssertionError(); // The system has no TLS. Just give up.
            }
        }

        private void upgradeTLS(SSLSocket ssl) {
            // Android 5.0+ (API level21) provides reasonable default settings
            // but it still allows SSLv3
            // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
            if (protocols != null) {
                ssl.setEnabledProtocols(protocols);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && cipherSuites != null) {
                ssl.setEnabledCipherSuites(cipherSuites);
            }
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return cipherSuites;
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return cipherSuites;
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            Socket ssl = defaultFactory.createSocket(s, host, port, autoClose);
            if (ssl instanceof SSLSocket)
                upgradeTLS((SSLSocket) ssl);
            return ssl;
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            Socket ssl = defaultFactory.createSocket(host, port);
            if (ssl instanceof SSLSocket)
                upgradeTLS((SSLSocket) ssl);
            return ssl;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            Socket ssl = defaultFactory.createSocket(host, port, localHost, localPort);
            if (ssl instanceof SSLSocket)
                upgradeTLS((SSLSocket) ssl);
            return ssl;
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            Socket ssl = defaultFactory.createSocket(host, port);
            if (ssl instanceof SSLSocket)
                upgradeTLS((SSLSocket) ssl);
            return ssl;
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            Socket ssl = defaultFactory.createSocket(address, port, localAddress, localPort);
            if (ssl instanceof SSLSocket)
                upgradeTLS((SSLSocket) ssl);
            return ssl;
        }
    }
}

