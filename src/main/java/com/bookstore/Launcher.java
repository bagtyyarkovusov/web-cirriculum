package com.bookstore;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.nio.file.Files;

/**
 * Embedded Tomcat 10.1 launcher — one-command run for local dev and the live demo.
 * The same web application also packages to a deployable WAR via {@code mvn package}.
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        int port = Integer.getInteger("port", 8080);

        File webappDir = new File("src/main/webapp");
        if (!webappDir.isDirectory()) {
            throw new IllegalStateException("webapp dir not found: " + webappDir.getAbsolutePath());
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("bookstore-tomcat").toString());
        tomcat.setPort(port);
        tomcat.getConnector(); // force creation of the default HTTP connector

        Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
        ctx.setParentClassLoader(Launcher.class.getClassLoader());

        // Dev mode: expose compiled classes (target/classes) to the web app class loader
        // so JSP/JSTL and the servlets resolve when running via `mvn exec:java`.
        File classes = new File("target/classes");
        if (classes.isDirectory()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(
                    resources, "/WEB-INF/classes", classes.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        tomcat.start();
        System.out.println("Bookstore running: http://localhost:" + port + "/app/books");
        tomcat.getServer().await();
    }
}
