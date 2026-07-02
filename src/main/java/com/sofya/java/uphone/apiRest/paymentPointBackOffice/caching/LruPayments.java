/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.caching;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author CBARRAGAN
 */
public class LruPayments {
    static ConcurrentHashMap<String, Boolean> enProceso = new ConcurrentHashMap<>();
    public Boolean validateCache(String voucher) {
        if (enProceso.putIfAbsent(voucher, Boolean.TRUE) != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void deleteElementCache(String voucher) {
        enProceso.remove(voucher);
    }
}
