package com.raphaeldelio.elasticsearch.config

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@Configuration
class RestClientConfig : AbstractElasticsearchConfiguration() {

    @Bean
    override fun elasticsearchClient(): RestHighLevelClient {
        val host = "localhost"
        val port = 9200
        val user = "elastic"
        val password = "password"
        val caCrtPath = "/path/to/ca.crt"

        val credentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(AuthScope.ANY, UsernamePasswordCredentials(user, password))

        val ks = KeyStore.getInstance("pkcs12")
        ks.load(null, null)

        val fis = FileInputStream(caCrtPath)
        val bis = BufferedInputStream(fis)

        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val cert = cf.generateCertificate(bis)
        ks.setCertificateEntry("ca", cert)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(ks)

        val context = SSLContext.getInstance("TLS")
        context.init(null, tmf.trustManagers, null)

        val restClientBuilder = RestClient.builder(
            HttpHost(host, port, "https")
        ).setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            httpClientBuilder.setSSLContext(context)
        }
        return RestHighLevelClient(restClientBuilder)
    }
}
