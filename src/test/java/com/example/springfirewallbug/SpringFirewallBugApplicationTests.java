package com.example.springfirewallbug;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringFirewallBugApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    public void success() throws Exception {
        final HttpURLConnection conn = prepareConnection("/foo");
        conn.connect();
        assertEquals(200, conn.getResponseCode());
    }

    @Test
    public void invalidPath() throws Exception {
        final HttpURLConnection conn = prepareConnection("//foo");
        conn.connect();
        assertEquals(400, conn.getResponseCode());
    }

    // FIXME This results in 500 Internal Server Error – caused by RequestRejectedException
    @Test
    public void invalidHeader() throws Exception {
        final HttpURLConnection conn = prepareConnection("/foo");
        conn.setRequestProperty("X-Header", "Test \u0099");
        conn.connect();
        assertEquals(400, conn.getResponseCode());
    }

    // FIXME This results in 200 OK – even with the same illegal header
    @Test
    public void invalidHeader2() throws Exception {
        final HttpURLConnection conn = prepareConnection("/bar");
        conn.setRequestProperty("X-Header", "Test \u0099");
        conn.connect();
        assertEquals(400, conn.getResponseCode());
    }

    private HttpURLConnection prepareConnection(final String file) throws IOException {
        final URL url = new URL("http", "localhost", port, file);
        return (HttpURLConnection) url.openConnection();
    }

}
