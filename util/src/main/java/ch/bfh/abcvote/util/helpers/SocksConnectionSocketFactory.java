/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.helpers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

/**
 *
 * ConnectionSocketFactory needed for Socks Proxy
 *  based on answer http://stackoverflow.com/a/22960881 
 *  from http://stackoverflow.com/questions/22937983/how-to-use-socks-5-proxy-with-apache-http-client-4 
 * 
 * @author Sebastian Nellen <sebastian at nellen.it>
 */
class SocksConnectionSocketFactory extends SSLConnectionSocketFactory {

    public SocksConnectionSocketFactory(final SSLContext sslContext) {
        super(sslContext);
    }

    @Override
    public Socket createSocket(final HttpContext context) throws IOException {
        InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        return new Socket(proxy);
    }

}
