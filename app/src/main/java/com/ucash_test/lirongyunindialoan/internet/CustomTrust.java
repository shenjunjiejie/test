package com.ucash_test.lirongyunindialoan.internet;

import android.content.Context;

import com.ucash_test.lirongyunindialoan.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public final class CustomTrust {
    public static final String tag = "CustomTrust";
    private static final String CLIENT_KET_PASSWORD = "";

    public OkHttpClient getClient() {
        return client;
    }


    private OkHttpClient client;
    public static Context context;

    public static CustomTrust getInstance(){
        return new CustomTrust();
    }

    private CustomTrust()  {
//        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory=null;
        try {
            //  trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext =  trustManagerForCertificates(trustedCertificatesInputStream()); //SSLContext.getInstance("TLS");
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Headers headers2 = chain.request().headers();
                        if(chain.request().isHttps()){
//                            Log.e("test22","https");
                        }
//                        Log.e("请求报文",chain.request().toString());
                        for(int i=0;i < headers2.size();i++){
//                            Log.e("test_request1", headers2.name(i));
//                            Log.e("test_request2", headers2.get(headers2.name(i)));
                        }
                        Response response = chain.proceed(chain.request());
                        Headers headers = response.headers();
                        for(int i=0;i < headers.size();i++){
                            //Log.e("test_request3", headers.name(i));
                            //Log.e("test_request4", headers.get(headers.name(i)));
                        }
                        //存入Session
                        if (response.header("Set-Cookie") != null) {

                        }
                        //刷新API调用时间

                        return response;
                    }

                })
                .sslSocketFactory(sslSocketFactory).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

    }


    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.

        return context.getResources().openRawResource(R.raw.test);

    /*return new Buffer()
        .writeUtf8(comodoRsaCertificationAuthority)
        .writeUtf8(entrustRootCertificateAuthority)
        .inputStream();*/
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private SSLContext trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = CLIENT_KET_PASSWORD.toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        //  keyStore.load(in,CLIENT_KET_PASSWORD.toCharArray());
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }

        SSLContext ssContext = SSLContext.getInstance("SSL");
        ssContext.init(keyManagerFactory.getKeyManagers(),trustManagers,null);
        //return (X509TrustManager) trustManagers[0];
        return  ssContext;
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

//  public static void main(String... args) throws Exception {
//    new CustomTrust().run();
//  }
}
