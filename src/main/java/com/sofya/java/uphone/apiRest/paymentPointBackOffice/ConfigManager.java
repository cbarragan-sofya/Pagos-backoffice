/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConfigManager {

    // Ruta absoluta al archivo de configuración externo
    private static final Path CONFIG_PATH = Paths.get(System.getProperties().getProperty("jboss.server.config.dir") + "/wsdl_paymentBackOffice.properties");

    private static final Properties properties = new Properties();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static long lastModified = 0;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        lock.writeLock().lock();
        try {
            if (!Files.exists(CONFIG_PATH)) {
                throw new RuntimeException("Archivo de configuración no encontrado: " + CONFIG_PATH);
            }

            long currentModified = Files.getLastModifiedTime(CONFIG_PATH).toMillis();
            if (currentModified > lastModified) {
                try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                    properties.clear();
                    properties.load(input);
                    lastModified = currentModified;
                    System.out.println("Propiedades recargadas desde: " + CONFIG_PATH);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static String get(String key) {
        lock.readLock().lock();
        try {
            return properties.getProperty(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Recarga forzada (desde un endpoint o cron job si quieres)
    public static void reload() {
        loadProperties();
    }
}