/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.controller;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.ConfigManager;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.Resource;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.caching.LruPayments;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.caching.lru;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.AditionaDataPayment;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.AditionalDataConsult;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.ConsultInvoiceDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.DataConsult;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.DataPaymentResponse;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.InvoiceDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.InvoiceItemConsult;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Meta;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response400DTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Response401DTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.Error;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.InvoiceItemResponsePayment;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.MetaResponse;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.PaymentDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.dto.PaymentResponseDTO;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception.ConsultInvoiceException;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception.PaymentInvoiceException;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.exception.UphoneExceptionApiRest;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.jwt.JWebToken;
import com.sofya.java.uphone.apiRest.paymentPointBackOffice.util.AsyncExecutor;
import com.sofya.java.uphone.backend.constant.EstadoEnum;
import com.sofya.java.uphone.backend.dao.CuotaDao;
import com.sofya.java.uphone.backend.dao.DistribuidorDao;
import com.sofya.java.uphone.backend.dao.PagoCabeceraDao;
import com.sofya.java.uphone.backend.dto.CuotasVoucher;
import com.sofya.java.uphone.backend.dto.CuotasVoucherApi;
import com.sofya.java.uphone.backend.dto.PagoListDTO;
import com.sofya.java.uphone.backend.dto.ProcesarCuotaDTO;
import com.sofya.java.uphone.backend.dto.ProcesarPagoDTO;
import com.sofya.java.uphone.backend.dto.Voucher;
import com.sofya.java.uphone.backend.dto.VoucherApi;
import com.sofya.java.uphone.backend.model.Cliente;
import com.sofya.java.uphone.backend.model.Contrato;
import com.sofya.java.uphone.backend.model.Cuota;
import com.sofya.java.uphone.backend.model.Distribuidor;
import com.sofya.java.uphone.backend.model.DistribuidorCuenta;
import com.sofya.java.uphone.backend.model.Empleado;
import com.sofya.java.uphone.backend.model.HistorialCrediticio;
import com.sofya.java.uphone.backend.model.Pago;
import com.sofya.java.uphone.backend.model.PagoCabecera;
import com.sofya.java.uphone.backend.service.ContratoService;
import com.sofya.java.uphone.backend.service.CuotaService;
import com.sofya.java.uphone.backend.service.EmpleadoService;
import com.sofya.java.uphone.backend.service.HistorialCrediticioService;
import com.sofya.java.uphone.backend.service.PagoCabeceraService;
import com.sofya.java.uphone.backend.service.PagoService;
import com.sofya.java.uphone.backend.util.BackOfficeSecurity;
import com.sofya.java.uphone.backend.util.FormaterDate;
import com.sofya.java.uphone.contabilidad.accountBank.BankAccountController;
import com.sofya.java.uphone.contabilidad.billing.transmitInvoice.SalesCheckController;
import com.sofya.java.uphone.contabilidad.constant.UphoneSassEnum;
import com.sofya.java.uphone.contabilidad.contractDTO.DistributorAccountDTO;
import com.sofya.java.uphone.contabilidad.payment.Payment;
import com.sofya.java.uphone.contabilidad.payment.PaymentController;
import com.sofya.java.uphone.contabilidad.payment.PaymentException;
import com.sofya.java.uphone.contabilidad.utils.ConnectionSicBdd;
import com.sofya.java.uphone.contabilidad.utils.ConnectionSicBddException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sofya.com.java.loggin.LogginControl;
import sofya.com.java.loggin.LogginException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author CBARRAGAN
 */
@Path("api")
public class InvoiceController extends Resource {

    @EJB
    private CuotaDao cuotaDao;

    @EJB
    ContratoService contratoService;

    @EJB
    CuotaService cuotaService;

    @EJB
    EmpleadoService empleadoService;

    @EJB
    DistribuidorDao distribuidorDao;

    @EJB
    PagoCabeceraDao pagoCabeceraDao;

    @EJB
    PagoCabeceraService pagoCabeceraService;

    @EJB
    PagoService pagoService;

    @EJB
    BackOfficeSecurity backOfficeSecurity;

    @EJB
    HistorialCrediticioService historialService;

    @Inject
    private UserTransaction userTransaction;

    lru cache = new lru();

    LruPayments cacheVouchers = new LruPayments();
    
    @POST
    @Path("v1/invoice/consult")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/json"})
    public Response consult(@HeaderParam("Authorization") String token, ConsultInvoiceDTO consultInvoiceDTO) throws InterruptedException {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        System.out.println("Consulta contrato BACKOFFICE: " + consultInvoiceDTO.getCounterpart());
        try {
            Boolean flag = Boolean.FALSE;
            String tokenJwt = token.substring(7, token.length());
            JWebToken incomingToken = new JWebToken(tokenJwt);
            String contractId = "";

            //validate token
            if (!incomingToken.isValid()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
            }

            //validate token expiration date  
            if (incomingToken.isExpired()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "The bearer token has expired.", "033", "/v1/invoice/consult")).build();
            }

            //contractId= validateInvoiceData(consultInvoiceDTO);
            invoiceDTO = getInvoicePayment(consultInvoiceDTO.getCounterpart());

        } catch (ConsultInvoiceException e) {
            return Response.serverError().
                    status(e.getStatus()).
                    type(MediaType.APPLICATION_JSON).
                    entity(e.getResp400()).build();
        } catch (NoSuchAlgorithmException ex) {
            return Response.serverError().
                    status(Response.Status.UNAUTHORIZED).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
        } catch (Exception e) {
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/invoice/consult")).build();
        }
        System.out.println("Finalizando consulta contrato BACKOFFICE: " + consultInvoiceDTO.getCounterpart());
        return response(Response.Status.OK, invoiceDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @POST
    @Path("v1/invoice/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/json"})
    public Response payment(@HeaderParam("Authorization") String token, PaymentDTO paymentDTO) throws LogginException, UphoneExceptionApiRest, ParseException {
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();
        System.out.println("Pago contrato BACKOFFICE:" + paymentDTO.getData().getCounterpart());
        try {
            Boolean flag = Boolean.FALSE;
            String tokenJwt = token.substring(7, token.length());
            JWebToken incomingToken = new JWebToken(tokenJwt);

            //validate token 
            if (!incomingToken.isValid()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
            }

            //validate token expiration date 
            if (incomingToken.isExpired()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "The bearer token has expired.", "033", "/v1/invoice/consult")).build();
            }

            paymentResponse = registerPayment(paymentDTO, token);

        } catch (PaymentInvoiceException e) {
            System.out.println("Error pago BACKOFFICE contrato: " + paymentDTO.getData().getCounterpart() + " " + e.getMessage());
            return Response.serverError().
                    status(e.getStatus()).
                    type(MediaType.APPLICATION_JSON).
                    entity(e.getResp400()).build();

        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error pago BACKOFFICE contrato: " + paymentDTO.getData().getCounterpart() + ex.getMessage());
            return Response.serverError().
                    status(Response.Status.UNAUTHORIZED).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
        } catch (Exception e) {
            System.out.println("Error pago BACKOFFICE contrato: " + paymentDTO.getData().getCounterpart() + e.getMessage());
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/invoice/payment")).build();
        }
        System.out.println("Finalaizando Pago contrato BACKOFFICE:" + paymentDTO.getData().getCounterpart());
        return response(Response.Status.OK, paymentResponse);
    }

    @GET
    @Path("v1/invoice/validateVoucherBackofficeApi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/json"})
    public Response validateVoucherBackofficeApi(@HeaderParam("Authorization") String token,
            @QueryParam("voucherNumber") String voucherNumber) throws InterruptedException {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        try {
            Boolean flag = Boolean.FALSE;
            String tokenJwt = token.substring(7, token.length());
            JWebToken incomingToken = new JWebToken(tokenJwt);
            String contractId = "";
            //validate token
            if (!incomingToken.isValid()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
            }

            //validate token expiration date  
            if (incomingToken.isExpired()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "The bearer token has expired.", "033", "/v1/invoice/consult")).build();
            }

            return response(Response.Status.OK, this.pagoCabeceraDao.validateVoucherNumber(voucherNumber));

        } catch (NoSuchAlgorithmException ex) {
            return Response.serverError().
                    status(Response.Status.UNAUTHORIZED).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/consult")).build();
        } catch (Exception e) {
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/invoice/consult")).build();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @POST
    @Path("v1/invoice/conciliationVoucher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response conciliationVoucher(
            @HeaderParam("Authorization") String token,
            @QueryParam("contractId") Integer contractId,
            @QueryParam("voucherNumber") String voucherNumber
    ) throws UphoneExceptionApiRest, PaymentException {
        List<Error> errorList = new ArrayList<Error>();
        try {
            System.out.println(String.format("CONCILIANDO CONTRATO %s, voucher: %s", contractId, voucherNumber));
            Boolean flag = Boolean.FALSE;
            String tokenJwt = token.substring(7, token.length());
            JWebToken incomingToken = new JWebToken(tokenJwt);

            //validate token 
            if (!incomingToken.isValid()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/conciliationVoucher")).build();
            }

            //validate token expiration date 
            if (incomingToken.isExpired()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "The bearer token has expired.", "033", "/v1/invoice/conciliationVoucher")).build();
            }

            if (cacheVouchers.validateCache(voucherNumber)) {
                return Response.serverError().
                        status(Response.Status.NOT_FOUND).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response400DTO("Error Contrato", String.format("El contrato: %s con número de voucher: %s se encuentra registrándose", contractId, voucherNumber), errorList, "022", "/api/v1/invoice/conciliationVoucher")).build();
            }

            VoucherApi voucher = new VoucherApi();
            Integer entryId = null;
            Boolean isLastCuota = Boolean.FALSE;
            Integer accountingEntryId = null;
            voucher = getPaymentHeaderData(contractId, voucherNumber);

            //VALIDAR EXISTENCIA DE VOUCHER
            if (voucher == null) {
                cacheVouchers.deleteElementCache(voucherNumber);
                errorList.add(new Error("Error Contrato", String.format("No se pudo obtener el pago del contrato: %s con número de voucher: %s", contractId, voucherNumber)));
                return Response.serverError().
                        status(Response.Status.NOT_FOUND).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response400DTO("Error Contrato", "Error al obtener la información del pago", errorList, "022", "/api/v1/invoice/conciliationVoucher")).build();
            }

            //VALIDAR CONTRATO EN ESTADO PENDIENTE DE COMPROBACION
            if (voucher.getConciliationState().equals("C")) {
                cacheVouchers.deleteElementCache(voucherNumber);
                errorList.add(new Error("Error Contrato", String.format("El pago del contrato: %s con número de voucher: %s ya se encuentra conciliado", contractId, voucherNumber)));
                return Response.serverError().
                        status(Response.Status.NOT_FOUND).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response400DTO("Error Contrato", "Pago conciliado con anterioridad", errorList, "022", "/api/v1/invoice/conciliationVoucher")).build();
            }

            PaymentController paymentController = new PaymentController();
            Date dateNow = new Date();
            Contrato contrato = new Contrato();
            contrato = contratoService.findByPk(voucher.getContractId());

            try {
                if (cache.getElementFromCache("sessionId").equals("Not exist") || !checkSessionSIC(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"))) {
                    logginSic();
                }

                PagoCabecera pagoCabecera = this.pagoCabeceraService.findByPk(voucher.getPaymentHeaderId());
                pagoCabecera.setEstado_conciliacion("C");
                voucher.getPayment().setPagoCabeceraId(pagoCabecera.getId());
                userTransaction.begin();

                Payment paymentAccount = registerAccountPayment(dateNow, voucher.getCustomerId(), voucher.getContractId(), voucher.getTotalValue(), paymentController, voucher.getVoucher(), voucher.getPayment());
                List<ProcesarCuotaDTO> cuotaList = new ArrayList<ProcesarCuotaDTO>();
                for (CuotasVoucherApi cuota : voucher.getCuotas()) {
                    Cuota cuotaUpdate = new Cuota();
                    cuotaUpdate = this.cuotaService.findByPk(cuota.getCuotaId());
                    Boolean isMora = Boolean.FALSE;
                    String newStateCuota = EstadoEnum.PAGO_TIEMPO.getEstado();

                    if (cuotaUpdate.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                        newStateCuota = EstadoEnum.PAGO_ABONO.getEstado();
                    } else {
                        //Pago completo
                        if (cuotaUpdate.getCuotaMora() != null) {
                            newStateCuota = EstadoEnum.PAGO_MORA.getEstado();
                        } else {
                            newStateCuota = FormaterDate.endDay(cuotaUpdate.getFechaCorte()).after(voucher.getPaymentDate())
                                    ? EstadoEnum.PAGO_TIEMPO.getEstado()
                                    : EstadoEnum.PAGO_DESTIEMPO.getEstado();
                        }
                    }
                    cuotaUpdate.setEstado(newStateCuota);

                    this.cuotaService.update(cuotaUpdate);
                    // armar la lista con los pagos 
                    if (cuotaUpdate.getCuotaMora() == null) {
                        Pago payment = pagoService.findByPk(cuota.getPaymentId());
                        cuotaList.add(new ProcesarCuotaDTO(cuotaUpdate.getNumeroCuota(), cuotaUpdate.getValor().subtract(cuotaUpdate.getSaldo()), cuotaUpdate.getSaldo(),
                                cuotaUpdate.getValor().compareTo(cuotaUpdate.getSaldo()) == 0 ? "GENERADA" : cuotaUpdate.getSaldo().compareTo(BigDecimal.ZERO) == 0 ? "PAGADA" : "NOTIFICADA"));
                    }
                }
                contrato.setDispositivoBloqueado(
                        this.cuotaDao.findByDateLockContract(new Date(), contrato.getId()).isEmpty()
                        ? EstadoEnum.DISPOSITIVO_DESBLOQUEADO.getEstado()
                        : EstadoEnum.DISPOSITIVO_BLOQUEADO.getEstado()
                );
                this.contratoService.update(contrato);
                ProcesarPagoDTO procesarPagoDTO = new ProcesarPagoDTO(voucher.getContractId().toString(), voucher.getTotalValue(), new BigDecimal(cuotaDao.getBalanceContract(voucher.getContractId())), contrato.getTag(), "PAGO_REALIZADO", contrato.getDispositivoBloqueado(), cuotaList);
                backOfficeSecurity.procesarPago(procesarPagoDTO);
                // armar el pago general inluyendo la lista de los pagos
                isLastCuota = cuotaService.releaseContract(contrato.getId());

                if (isLastCuota) {
                    backOfficeSecurity.freeDevice(contrato);
                    contrato.setEstado(EstadoEnum.CONTRATO_TERMINADO.getEstado());
                    HistorialCrediticio historialCrediticio = new HistorialCrediticio();
                    historialCrediticio.setContrato(new Contrato(contrato.getId()));
                    historialCrediticio.setEstado(EstadoEnum.ACTIVO.getEstado());
                    historialCrediticio.setFecha(dateNow);
                    historialCrediticio.setEmpleado(new Empleado(Integer.valueOf(ConfigManager.get("employeeId"))));
                    historialCrediticio.setObservacion(EstadoEnum.CONTRATO_TERMINADO.getEstado());
                    this.contratoService.update(contrato);
                    this.historialService.create(historialCrediticio);
                }

                pagoCabecera.setEntryId(paymentAccount.getEntryId());
                pagoCabecera.setFechaConciliacion(dateNow);
                pagoCabecera.setEntryBankId(paymentAccount.getEntryBankId());
                this.pagoCabeceraService.update(pagoCabecera);
                userTransaction.commit();
                paymentController.verificarCierreConexion();
                voucher.setConciliationState(EstadoEnum.CONCILIACION_EXITOSA.getEstado());
            } catch (LogginException ex) {
                System.out.println("Error Loggin usuario: " + ConfigManager.get("loggin"));
                errorList.add(new Error("Error de conciliación", "No se pudo registrar la conciliación"));
                throw new PaymentInvoiceException(new Response400DTO("Error de pago", "No se pudo registrar el pago", errorList, "EC004", "/api/v1/invoice/conciliationVoucher"), Response.Status.BAD_REQUEST);
            } catch (Exception e) {
                System.out.println("Error conciliación: " + e.getMessage());
                System.out.println("Rolback contable");
                paymentController.verificarCierreConexion(Boolean.TRUE);
                rollbackPayment();
            }
            //ELIMINAR CONTRATO DE CACHE
            cacheVouchers.deleteElementCache(voucherNumber);
            System.out.println(String.format("FINALIZANDO CONCILIACION CONTRATO %s, voucher: %s", contractId, voucherNumber));
            return response(Response.Status.OK, voucher);
        } catch (PaymentInvoiceException e) {
            cacheVouchers.deleteElementCache(voucherNumber);
            System.out.println("Error conciliación BACKOFFICE contrato: " + contractId + " " + e.getMessage());
            e.getResp400().setDetail("Error al registrar la conciliación de voucher");
            return Response.serverError().
                    status(e.getStatus()).
                    type(MediaType.APPLICATION_JSON).
                    entity(e.getResp400()).build();

        } catch (NoSuchAlgorithmException ex) {
            cacheVouchers.deleteElementCache(voucherNumber);
            System.out.println("Error conciliación BACKOFFICE contrato: " + contractId + ex.getMessage());
            return Response.serverError().
                    status(Response.Status.UNAUTHORIZED).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/conciliationVoucher")).build();
        } catch (Exception e) {
            cacheVouchers.deleteElementCache(voucherNumber);
            System.out.println("Error conciliación BACKOFFICE contrato: " + contractId + e.getMessage());
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/invoice/conciliationVoucher")).build();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @POST
    @Path("v1/invoice/reversePaymentVoucher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reversePaymentVoucher(
            @HeaderParam("Authorization") String token,
            @QueryParam("contractId") Integer contractId,
            @QueryParam("voucherNumber") String voucherNumber
    ) throws UphoneExceptionApiRest, PaymentException {
        try {
            System.out.println(String.format("REVERSA CONTRATO %s, voucher: %s", contractId, voucherNumber));
            List<Error> errorList = new ArrayList<Error>();
            String tokenJwt = token.substring(7, token.length());
            JWebToken incomingToken = new JWebToken(tokenJwt);

            //validate token 
            if (!incomingToken.isValid()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/reversePaymentVoucher")).build();
            }

            //validate token expiration date 
            if (incomingToken.isExpired()) {
                return Response.serverError().
                        status(Response.Status.UNAUTHORIZED).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response401DTO("Authorization Error", "The bearer token has expired.", "033", "/v1/invoice/reversePaymentVoucher")).build();
            }

            VoucherApi voucher = new VoucherApi();
            voucher = getPaymentHeaderData(contractId, voucherNumber);

            //VALIDAR EXISTENCIA DE VOUCHER
            if (voucher == null) {
                errorList.add(new Error("Error Contrato", String.format("No se pudo obtener el pago del contrato: %s con número de voucher: %s", contractId, voucherNumber)));
                return Response.serverError().
                        status(Response.Status.NOT_FOUND).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response400DTO("Error Contrato", "Error al obtener la información del pago", errorList, "022", "/api/v1/invoice/reversePaymentVoucher")).build();
            }

            //VALIDAR CONTRATO EN ESTADO PENDIENTE DE COMPROBACION
            if (voucher.getConciliationState().equals("C")) {
                errorList.add(new Error("Error Contrato", String.format("El pago del contrato: %s con número de voucher: %s ya se encuentra conciliado", contractId, voucherNumber)));
                return Response.serverError().
                        status(Response.Status.NOT_FOUND).
                        type(MediaType.APPLICATION_JSON).
                        entity(new Response400DTO("Error Contrato", "Pago conciliado con anterioridad", errorList, "022", "/api/v1/invoice/reversePaymentVoucher")).build();
            }
            userTransaction.begin();
            StringBuilder hcReverse = new StringBuilder("REVERSO DE PAGO VOUCHER " + voucherNumber + ", CUOTAS: ");
            boolean primera = true;

            List<ProcesarCuotaDTO> procesarCuotaList = new ArrayList<ProcesarCuotaDTO>();
            for (CuotasVoucherApi cuotaUpdate : voucher.getCuotas()) {
                if (!primera) {
                    hcReverse.append(",");
                }
                Pago pago = new Pago();
                Cuota cuota = new Cuota();
                BigDecimal value = new BigDecimal(0);
                pago = this.pagoService.findByPk(cuotaUpdate.getPaymentId());
                cuota = pago.getCuota();
                value = cuota.getSaldo().add(cuotaUpdate.getPaymentValue());
                value = value.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                cuota.setSaldo(value);

                if (cuota.getValor().compareTo(value) == 0) {
                    cuota.setEstado(EstadoEnum.PENDIENTE_PAGO.getEstado());
                }
                if (cuota.getValor().compareTo(value) == 1) {
                    cuota.setEstado(EstadoEnum.PAGO_ABONO.getEstado());
                }

                this.cuotaService.update(cuota);
                this.pagoService.delete(cuotaUpdate.getPaymentId());

                if (cuota.getCuotaMora() != null) {
                    continue;
                }
                ProcesarCuotaDTO procesarCuotaDTO = new ProcesarCuotaDTO(cuotaUpdate.getCuotaNumber(), cuota.getValor().subtract(cuota.getSaldo()), cuota.getSaldo(),
                        cuota.getValor().compareTo(cuota.getSaldo()) == 0 ? "GENERADA" : cuota.getSaldo().compareTo(BigDecimal.ZERO) == 0 ? "PAGADA" : "NOTIFICADA");
                procesarCuotaList.add(procesarCuotaDTO);
                hcReverse.append(cuota.getNumeroCuota());
                primera = false;
            }
            this.pagoCabeceraService.delete(voucher.getPaymentHeaderId());
            Contrato contrato = new Contrato();
            contrato = this.contratoService.findByPk(voucher.getContractId());
            contrato.setDispositivoBloqueado(
                    this.cuotaDao.findByDateLockContract(new Date(), contrato.getId()).isEmpty()
                    ? EstadoEnum.DISPOSITIVO_DESBLOQUEADO.getEstado()
                    : EstadoEnum.DISPOSITIVO_BLOQUEADO.getEstado()
            );
            this.contratoService.update(contrato);
            ProcesarPagoDTO procesarPagoDTO = new ProcesarPagoDTO(voucher.getContractId().toString(), contrato.getPrecioFinal(), BigDecimal.valueOf(cuotaDao.getBalanceContract(voucher.getContractId())), contrato.getTag(), "PAGO_REALIZADO", contrato.getDispositivoBloqueado(), procesarCuotaList);
            if (!procesarPagoDTO.getCuotaList().isEmpty()) {
                this.backOfficeSecurity.procesarPago(procesarPagoDTO);
            }
            HistorialCrediticio historialCrediticio = new HistorialCrediticio();
            historialCrediticio.setContrato(new Contrato(contrato.getId()));
            historialCrediticio.setEstado(EstadoEnum.ACTIVO.getEstado());
            historialCrediticio.setFecha(new Date());
            historialCrediticio.setEmpleado(new Empleado(Integer.valueOf(ConfigManager.get("employeeId"))));
            historialCrediticio.setObservacion(hcReverse.toString());
            this.historialService.create(historialCrediticio);

            userTransaction.commit();
            voucher.setConciliationState(EstadoEnum.REVERSA_PAGO_EXITOSA.getEstado());
            System.out.println(String.format("FINALIZANDO REVERSA CONTRATO %s, voucher: %s", contractId, voucherNumber));
            return response(Response.Status.OK, voucher);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error reversa BACKOFFICE contrato: " + contractId + ex.getMessage());
            return Response.serverError().
                    status(Response.Status.UNAUTHORIZED).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Authorization Error", "Bearer Token is invalid", "033", "/v1/invoice/reversePaymentVoucher")).build();
        } catch (Exception e) {
            System.out.println("Error reversa BACKOFFICE contrato: " + contractId + e.getMessage());
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                System.out.println(rollbackEx);
            }
            return Response.serverError().
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    type(MediaType.APPLICATION_JSON).
                    entity(new Response401DTO("Internal Server Error", e.getMessage(), "500", "/v1/invoice/reversePaymentVoucher")).build();
        }

    }

    private String validateInvoiceData(ConsultInvoiceDTO consultInvoiceDTO) throws ConsultInvoiceException {
        Boolean flagContract = Boolean.FALSE;
        String contractId = "";
        if (consultInvoiceDTO.getCounterpart().isEmpty()) {
            List<Error> errorList = new ArrayList<Error>();
            errorList.add(new Error("The body field: 'counterpart' is a mandatory field.", "El campo 'counterpart' es mandatorio "));
            throw new ConsultInvoiceException(new Response400DTO("Missing mandatory fields", "Los campos de entrada no son correctos", errorList, "022", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        for (AditionalDataConsult aditionalData : consultInvoiceDTO.getAditionalData()) {
            if (aditionalData.getKey().equals("contrato")) {
                contractId = aditionalData.getValue();
                flagContract = Boolean.TRUE;
            }
        }

        if (!flagContract) {
            List<Error> errorList = new ArrayList<Error>();
            errorList.add(new Error("The body field: 'contrato' is a mandatory field.", "El campo 'contrato' es mandatorio "));
            throw new ConsultInvoiceException(new Response400DTO("Missing mandatory fields", "Los campos de entrada no son correctos", errorList, "022", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }
        return contractId;
    }

    private InvoiceDTO getInvoicePayment(String contractId) throws ConsultInvoiceException {
        List<Error> errorList = new ArrayList<Error>();
        List<InvoiceItemConsult> invoiceItemList = new ArrayList<InvoiceItemConsult>();

        //validar contrapartida
        if (contractId.isEmpty() || contractId == null) {
            errorList.add(new Error("Contrapartida no existe", "Contrapartida incorrecta"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC001", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        contractId = sanitizeInputOnlyNumber(contractId);
        BigDecimal amount = new BigDecimal(0);
        List<Contrato> contratoList = new ArrayList<>();

        Contrato contract = this.contratoService.findByPk(Integer.valueOf(contractId));

        if (contract == null) {
            errorList.add(new Error("Contrapartida no existe", "Contrapartida incorrecta"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC001", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        if (contract.getEstado().equals(EstadoEnum.CONTRATO_TERMINADO.getEstado())) {
            errorList.add(new Error("Contrapartida sin deuda", "Contrapartida sin deuda"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC002", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        if (!contract.getEstado().equals(EstadoEnum.CONTRATO_APROBADO.getEstado()) && !contract.getEstado().equals(EstadoEnum.CONTRATO_TRANSITO.getEstado())) {
            errorList.add(new Error("Contrapartida no existe", "Contrapartida incorrecta"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC001", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        List<Cuota> cuotaList = this.cuotaDao.findByContratoIdPendientePago(Integer.valueOf(contractId));

        //validar si existe el contrato y tiene cuotas por pagar
        if (cuotaList.isEmpty()) {
            errorList.add(new Error("Contrapartida no existe", "Contrapartida incorrecta"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC001", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        Cuota cuota = new Cuota();
        Cuota mora = new Cuota();
        cuota = cuotaList.get(0);
        //generar moras
        this.contratoService.findByCedulaIdContratosAprobados(cuota.getContrato().getCliente().getCedula());

        //SEND INFO CUOTA
        InvoiceItemConsult invoiceItemCuota = new InvoiceItemConsult();
        invoiceItemCuota.setKey("CUOTA ID");
        invoiceItemCuota.setValue(cuota.getId().toString());
        invoiceItemCuota.setFlag("NINGUNO");
        invoiceItemCuota.setRequired(Boolean.TRUE);
        invoiceItemList.add(invoiceItemCuota);

        mora = this.cuotaDao.findByContratoIdMoras(cuota.getContrato().getId(), cuota.getId());
        amount = amount.add(cuota.getSaldo());
        if (!Objects.isNull(mora)) {
            amount = amount.add(mora.getSaldo());
            InvoiceItemConsult invoiceItemMora = new InvoiceItemConsult();
            invoiceItemMora.setKey("MORA ID");
            invoiceItemMora.setValue(mora.getId().toString());
            invoiceItemMora.setFlag("NINGUNO");
            invoiceItemMora.setRequired(Boolean.TRUE);
            invoiceItemList.add(invoiceItemMora);
        }

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            errorList.add(new Error("Contrapartida no existe", "Contrapartida sin deuda"));
            throw new ConsultInvoiceException(new Response400DTO("Error en consulta", "Error en consulta", errorList, "EC001", "/api/v1/invoice/consult"), Response.Status.BAD_REQUEST);
        }

        InvoiceDTO invoice = new InvoiceDTO();
        //generate audit number
        Date dateNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minutes = calendar.get(Calendar.MINUTE);
        Integer seconds = calendar.get(Calendar.SECOND);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        String auditNumber = String.format("%s%s%s%s%s%s%s", cuota.getId(), year, month, day, hour, minutes, seconds);
        invoice.setMeta(new Meta(auditNumber));

        DataConsult data = new DataConsult();
        data.setCode(cuota.getId().toString());
        data.setAmount(amount.setScale(2, RoundingMode.DOWN));

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'23:59:59.000");
        LocalDateTime fechaLocalDateTime = cuota.getFechaCorte().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        data.setExpirationDate(fechaLocalDateTime.format(formatDate));
        InvoiceItemConsult invoiceItemCustomer = new InvoiceItemConsult();
        List<DataConsult> dataList = new ArrayList<DataConsult>();

        String customerName = String.format("%s %s", cuota.getContrato().getCliente().getNombres(), cuota.getContrato().getCliente().getApellidos());
        invoiceItemCustomer.setKey("NOMBRE COMPLETO");
        invoiceItemCustomer.setValue(customerName);
        invoiceItemCustomer.setFlag("DETALLE");
        invoiceItemCustomer.setRequired(Boolean.TRUE);
        invoiceItemList.add(invoiceItemCustomer);
        data.setInvoiceItems(invoiceItemList);
        dataList.add(data);
        invoice.setData(dataList);
        return invoice;

    }

    private PaymentResponseDTO registerPayment(PaymentDTO paymentDTO, String token) throws PaymentInvoiceException, ParseException, PaymentException, ConnectionSicBddException {
        List<Error> errorList = new ArrayList<Error>();
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();
        BigDecimal amount = new BigDecimal(0);
        PagoListDTO paymentList = new PagoListDTO();
        String journalId = "";
        Integer cuotaId = 0;
        Integer modarId = 0;
        PaymentController paymentController = new PaymentController();

        for (AditionaDataPayment additionalData : paymentDTO.getData().getAditionalData()) {
            if (additionalData.getKey().equals("JOURNAL ID")) {
                journalId = additionalData.getValue();
            }
        }

        //validate journal id
        if (journalId.isEmpty()) {
            errorList.add(new Error("The body field: 'journal id' is a mandatory field.", "El campo 'journal id' es mandatorio "));
            throw new PaymentInvoiceException(new Response400DTO("Missing mandatory fields", "The input fields are not correct", errorList, "022", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }

        //validate quotas in state PENDIENTE_COMPROBACION
        if (this.cuotaDao.verifyPendienteComprobacion(Integer.valueOf(paymentDTO.getData().getCounterpart()))) {
            errorList.add(new Error("Error pago", String.format("El contrato %s tiene cuotas en estado PENDIENTE COMPROBACION", paymentDTO.getData().getCounterpart())));
            throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }

        //validate voucher number local
        if (this.pagoCabeceraDao.validateVoucherNumber(journalId.toString()) > 0) {
            errorList.add(new Error("Error voucher", String.format("El número de voucher ingresado %s ya se encuentra registrado en el sistema", journalId)));
            throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }

        //validate voucher server1
        if (ConfigManager.get("SERVER1") != null && !ConfigManager.get("SERVER1").isEmpty()) {
            validateVoucherBackoffice(journalId, ConfigManager.get("SERVER1"), token);
        }

        //validate voucher server2
        if (ConfigManager.get("SERVER2") != null && !ConfigManager.get("SERVER2").isEmpty()) {
            validateVoucherBackoffice(journalId, ConfigManager.get("SERVER2"), token);
        }

        BigDecimal saldo = BigDecimal.ZERO;
        List<Cuota> cuotaList = this.cuotaDao.findByContratoIdPendientePago(Integer.valueOf(Integer.parseInt(paymentDTO.getData().getCounterpart())));
        Cuota cuota = new Cuota();
        cuota = cuota = cuotaList.get(0);

        saldo = this.cuotaDao.getContractBalance(Integer.parseInt(paymentDTO.getData().getCounterpart()));

        if (paymentDTO.getData().getAmount().compareTo(saldo) > 0) {
            errorList.add(new Error("Error en notificación pago", String.format("El valor ingresado supera el saldo de la contrapartida: %s", saldo)));
            throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }

        if (cuota.getSaldo().compareTo(BigDecimal.ZERO) == 0) {
            errorList.add(new Error("Error en notificación pago", "No se pudo registrar el pago"));
            throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }

        Pago pago = new Pago();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date paymentDate = dateFormat.parse(paymentDTO.getData().getPaymentDate());
        //PaymentController paymentController = new PaymentController();
        try {
            if (cache.getElementFromCache("sessionId").equals("Not exist") || !checkSessionSIC(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"))) {
                logginSic();
            }

            userTransaction.begin();

            //REGISTRO DE PAGO SISTEMA CONTABLE
            try {
                boolean flag = new BankAccountController().getBanks(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"))
                        .stream()
                        .anyMatch(bank -> bank.getId() == paymentDTO.getData().getBankId());

                if (!flag) {
                    System.out.println("Banco no valido.");
                    System.out.println(paymentDTO.getData().getBankId());
                    //paymentController.verificarCierreConexion(Boolean.TRUE);
                    errorList.add(new Error("Id de banco no válido", "Id de banco no válido"));
                    throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
                }

            } catch (Exception e) {
                System.out.println("Error al validar id del banco");
                // paymentController.verificarCierreConexion(Boolean.TRUE);
                errorList.add(new Error("Id de banco no válido", "Id de banco no válido"));
                throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
            }

            Payment payment = new Payment();
            payment.setTypeBankId(paymentDTO.getData().getBankId());
            if (!paymentDTO.getData().getProvisionalPayment() == Boolean.TRUE) {
                registerAccountPayment(paymentDate, cuota.getContrato().getCliente().getCedula(), cuota.getContrato().getId(), paymentDTO.getData().getAmount(), paymentController, journalId, payment);
            }

            //REGISTRO DE PAGO SISTEMA UPHONE Y BACKOFFICE
            paymentList = cuotaService.registerPaymentByContratoId(Integer.parseInt(paymentDTO.getData().getCounterpart()), paymentDTO.getData().getAmount(), Integer.valueOf(ConfigManager.get("employeeId")), journalId, paymentDate, "", Boolean.FALSE, Integer.valueOf(ConfigManager.get("distributorId")), paymentDTO.getData().getProvisionalPayment(), payment.getTypeBankId(), Boolean.TRUE, payment.getEntryId(), paymentDate, payment.getEntryBankId());

            if (paymentList.getPagoList() == null || paymentList.getPagoList().isEmpty()) {
                paymentController.verificarCierreConexion(Boolean.TRUE);
                rollbackPayment();
            }

            pago = paymentList.getPagoList().get(0);
            //pago = this.cuotaService.registerPayment(cuota, mora, journalId, paymentDate, paymentDTO.getData().getAmount(), Integer.valueOf(properties.getWsdl("employeeId")), Integer.valueOf(properties.getWsdl("distributorId")), properties.getWsdl("BankId"));
            if (Objects.isNull(pago)) {
                paymentController.verificarCierreConexion(Boolean.TRUE);
                rollbackPayment();
            }

            List<DataPaymentResponse> dataList = new ArrayList<DataPaymentResponse>();
            List<InvoiceItemResponsePayment> invoiceItemList = new ArrayList<InvoiceItemResponsePayment>();
            paymentResponse.setMeta(new MetaResponse(pago.getId().toString()));
            DataPaymentResponse data = new DataPaymentResponse();
            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'23:59:59.000");
            LocalDateTime fechaLocalDateTime = cuota.getFechaCorte().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // data.setPagoList(paymentList.getPagoList());
            data.setCode(pago.getId().toString());
            data.setAmount(paymentDTO.getData().getAmount());
            data.setExpirationDate(fechaLocalDateTime.format(formatDate));
            InvoiceItemResponsePayment invoiceItem = new InvoiceItemResponsePayment();
            invoiceItem.setKey("Nro Comprobante");
            invoiceItem.setValue(pago.getPagoCabecera().getId().toString());
            invoiceItem.setFlag("CABECERA");
            invoiceItemList.add(invoiceItem);
            data.setInvoiceItems(invoiceItemList);
            dataList.add(data);
            paymentResponse.setData(dataList);
            userTransaction.commit();
            paymentController.verificarCierreConexion();
        } catch (LogginException ex) {
            System.out.println("Error Loggin NEGOCIO EFECTIVO");
            errorList.add(new Error("Error de pago", "No se pudo registrar el pago"));
            throw new PaymentInvoiceException(new Response400DTO("Error de pago", "No se pudo registrar el pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Error Pago: " + e.getMessage());
            System.out.println("Rolback contable");
            paymentController.verificarCierreConexion(Boolean.TRUE);

            rollbackPayment();
        }
        //TERMINAR CONTRATO
        /*
        try {
            System.out.println("Verificando finalización de contrato");
            validateFinishContract(cuota.getContrato().getId(), paymentDate);
        } catch (PaymentException ex) {
            System.out.println("Error, no se pudo finalizar el contrato");
        } catch (Exception e) {
            System.out.println("Ocurrió un problema al terminar el contrato: " + cuota.getContrato().getId());
        }*/

        return paymentResponse;
    }

    public void rollbackPayment() throws PaymentInvoiceException {
        List<Error> errorList = new ArrayList<Error>();
        try {
            userTransaction.rollback();
        } catch (Exception rollbackEx) {
            System.out.println(rollbackEx);
        }
        errorList.add(new Error("Error de pago", "No se pudo registrar el pago"));
        throw new PaymentInvoiceException(new Response400DTO("Error de pago", "No se pudo registrar el pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
    }

    private Payment registerAccountPayment(Date dateNow, String customerId, Integer contractId, BigDecimal amount, PaymentController paymentController, String journalId, Payment payment) throws LogginException, UphoneExceptionApiRest, PaymentException, ConnectionSicBddException {
        payment.setAffectation("D");
        payment.setAmount(amount.doubleValue());
        payment.setAnticipate("C");
        payment.setDescription("ANTICIPO AL CLIENTE: ");
        payment.setCurrency("1");
        payment.setObservation(String.format("Anticipo de contrato: %s", contractId));
        payment.setPaymentType("T");
        payment.setType("T");
        //payment.setTypeBankId(Integer.valueOf(ConfigManager.get("BankId")));
        payment.setTypeTransaction("ANT");
        payment.setVoucherNumber(journalId);
        String userName = ConfigManager.get("loggin");
        String sessionId = cache.getElementFromCache("sessionId");
        Integer module = Integer.valueOf(ConfigManager.get("module"));
        Integer enterprise = Integer.valueOf(ConfigManager.get("enterprise"));
        paymentController.verificarConexion(userName, sessionId);

        //Distribuidor distribuidortest = new Distribuidor();
        //distribuidortest = empleadoService.findDistribuidorByEmpleadoId(Integer.valueOf(ConfigManager.get("employeeId")));
        payment.setMatrizId(contratoService.findByPk(contractId).getCatalogo().getDistribuidor().getMatriz() == null
                ? contratoService.findByPk(contractId).getCatalogo().getDistribuidor().getId()
                : contratoService.findByPk(contractId).getCatalogo().getDistribuidor().getMatriz().getId());

        List<DistribuidorCuenta> distributorAccountList = distributorAccountList = distribuidorDao.getDistributorAccountListById(
                payment.getMatrizId(),
                Arrays.asList(
                        UphoneSassEnum.CUENTAS_POR_PAGAR_DISTRIBUIDOR.getDescription(),
                        UphoneSassEnum.CUENTAS_POR_COBRAR_TERCEROS.getDescription()
                ));
        List<DistributorAccountDTO> distributorAccountDTOList = distributorAccountDTOList = distributorAccountList.stream()
                .map(e -> {
                    DistributorAccountDTO dto = new DistributorAccountDTO();
                    dto.setAccountId(e.getCuentaId());
                    dto.setDescriptionAccount(e.getDescripcion());
                    return dto;
                })
                .collect(Collectors.toList());

        Integer entryId = paymentController.registerPaymentUphoneSAS(
                userName,
                sessionId,
                contractId,
                distributorAccountDTOList,
                amount.doubleValue(), module, enterprise, dateNow);

        payment.setEntryId(entryId);

        if (payment.getType().equals("T")) {
            // crear asiento de banco 
            payment.setVoucherNumber(journalId);
            payment.setDistributorAccount((distribuidorDao.getDistributorAccountListById(
                    payment.getMatrizId(),
                    Arrays.asList(
                            UphoneSassEnum.CUENTAS_POR_PAGAR_DISTRIBUIDOR_BALANCE.getDescription()
                    )).stream().map(e -> {
                DistributorAccountDTO dto = new DistributorAccountDTO();
                dto.setAccountId(e.getCuentaId());
                dto.setDescriptionAccount(e.getDescripcion());
                return dto;
            }).collect(Collectors.toList())).get(0).getAccountId());
            //crea el asiento contable
            Integer accountingEntryId
                    = paymentController.ocBookEntryUphoneSassNew(userName, sessionId,
                            payment, module, enterprise, dateNow, contractId);
            payment.setEntryBankId(accountingEntryId);
            //crea la activity
            paymentController.createBankActivityObservation(userName, sessionId,
                    payment.getTypeBankId(),
                    accountingEntryId,
                    "TRANSFERENCIA",
                    "VOUCHER: " + payment.getVoucherNumber(),
                    "B",
                    "PAGO DE CUOTA, CONTRATO: " + contractId,
                    payment.getAffectation(),
                    new SimpleDateFormat("yyyy-MM-dd").format(dateNow),
                    payment.getCurrency(),
                    payment.getAmount());
            //crea el voucher
            payment.setBeneficiaryId(0);
            paymentController.createVoucherEntryObservation(userName, sessionId,
                    0, "TRANSFERENCIA",
                    payment.getBeneficiaryId(),
                    payment.getAnticipate(),
                    accountingEntryId,
                    new SimpleDateFormat("yyyy-MM-dd").format(dateNow),
                    payment.getAmount(),
                    payment.getTypeBankId(),
                    0, "I",
                    "PAGO DE CUOTA, CONTRATO: " + contractId,
                    payment.getVoucherNumber());
            //actualiza el saldo del banco
            paymentController.updateBankBalance(userName, sessionId, payment);
        }
        return payment;
    }

    public void finishContract(final Integer contractId, final Date dateNow) {
        AsyncExecutor.submit(() -> {
            try {
                Integer invoiceId = 0;
                invoiceId = new PaymentController().generateContractInvoice(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"), contractId, dateNow, Integer.valueOf(ConfigManager.get("module")), Integer.valueOf(ConfigManager.get("enterprise")));
                if (invoiceId != 0) {
                    try {
                        SalesCheckController salesCheck = new SalesCheckController();
                        //salesCheck.generateInvoice(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"), invoiceId, dateNow,"NE");
                    } catch (Exception e) {
                        System.out.println(String.format("Error al emitir la factura electrónica del contrato: %s, error: %s ", contractId.toString(), e.getMessage()));
                    }
                }
            } catch (PaymentException ex) {
                System.out.println(ex);
            }

        });
    }

    /* public void finishContract(final Integer contractId, final Date dateNow) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //validateFinishContract(contractId,dateNow);
                    Integer invoiceId = 0;
                    invoiceId = new PaymentController().generateContractInvoice(ConfigManager.get("loggin"), cache.getElementFromCache("sessionId"), contractId, dateNow, Integer.valueOf(ConfigManager.get("module")), Integer.valueOf(ConfigManager.get("enterprise")));
                    invoiceId = invoiceId == null ? 0 : invoiceId;
                    if (invoiceId != 0) {
                        try {
                            SalesCheckController salesCheck = new SalesCheckController();
                            salesCheck.generateInvoice(properties.getWsdl("loggin"), cache.getElementFromCache("sessionId"), invoiceId, dateNow);
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println(e.getMessage());
                            System.out.println("Error al emitir la factura electrónica");
                        }

                    }
                } catch (PaymentException ex) {
                    System.out.println(ex);
                }
            }
        });
    }*/
    public void validateFinishContract(Integer contractId, Date dateNow) throws PaymentException {
        Contrato contrato = new Contrato();
        contrato = this.contratoService.findByPk(contractId);
        //REGISTAR FACTURA CONTRATO TERMINADO CONTABLE
        if (contrato.getEstado().equals("CONTRATO_TERMINADO")) {
            finishContract(contractId, dateNow);
        }
    }

    private void logginSic() throws LogginException {
        LogginControl logginControl = new LogginControl();
        String sessionId = logginControl.loggUser(ConfigManager.get("loggin"), ConfigManager.get("password"), Integer.valueOf(ConfigManager.get("modulo")), Integer.valueOf(ConfigManager.get("perfil")));
        cache.putElementInCache("sessionId", sessionId.split("\\|")[1]);
    }

    public void validateVoucherBackoffice(String voucherNumber, String server, String token) throws PaymentInvoiceException {
        List<Error> errorList = new ArrayList<Error>();
        try {
            Integer resp = 0;
            HttpResponse<String> response = Unirest.get(server + "/validateVoucherBackofficeApi")
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .queryString("voucherNumber", voucherNumber)
                    .asString();

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                errorList.add(new Error("Error voucher", String.format("Error, no se pudo obtener información del voucher: %s en el sistema: %s ", voucherNumber, server)));
                throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
            }

            Gson gson = new Gson();
            resp = gson.fromJson(response.getBody(), Integer.class);
            if (resp > 0) {
                errorList.add(new Error("Error voucher", String.format("El número de voucher ingresado %s ya se encuentra registrado en el sistema: %s", voucherNumber, server)));
                throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
            }
        } catch (Exception ex) {
            errorList.add(new Error("Error voucher", String.format("Error al validar voucher")));
            throw new PaymentInvoiceException(new Response400DTO("Error en notificación pago", "Error en notificación pago", errorList, "EC004", "/api/v1/invoice/payment"), Response.Status.BAD_REQUEST);
        }
    }

    public VoucherApi getPaymentHeaderData(Integer contractId, String voucherNumber) {
        PagoCabecera pagoCabecera = new PagoCabecera();
        pagoCabecera = this.pagoCabeceraDao.getPendingApprovalContract(contractId, voucherNumber);
        if (pagoCabecera == null) {
            return null;
        }

        Contrato contrato = pagoCabecera.getPagoList().get(0).getCuota().getContrato();
        Cliente cliente = contrato.getCliente();
        List<CuotasVoucherApi> cuotas = new ArrayList<>();
        for (Pago i : pagoCabecera.getPagoList()) {
            CuotasVoucherApi cuota = new CuotasVoucherApi();
            cuota.setCuotaId(i.getCuota().getId());
            cuota.setPaymentValue(i.getValor());
            cuota.setPaymentId(i.getId());
            cuota.setCuotaNumber(i.getCuota().getNumeroCuota());
            cuotas.add(cuota);
        }

        VoucherApi voucher = new VoucherApi();
        voucher.setVoucher(pagoCabecera.getNumeroVoucher());
        voucher.setCustomerNames(cliente.getNombres() + " " + cliente.getApellidos());
        voucher.setCustomerId(cliente.getCedula());
        voucher.setContractId(contrato.getId());
        voucher.setBankId(pagoCabecera.getBancoId());
        voucher.setPaymentHeaderId(pagoCabecera.getId());
        voucher.setEmployee(pagoCabecera.getEmpleado().getNombres() + " " + pagoCabecera.getEmpleado().getApellidos());
        voucher.setPaymentDate(pagoCabecera.getFecha());
        voucher.setTotalValue(pagoCabecera.getMontoTotal());
        voucher.setCuotas(cuotas);
        voucher.setConciliationState(pagoCabecera.getEstado_conciliacion());

        Payment payment = new Payment();
        payment.setType("T");
        payment.setDescription("ANTICIPO AL CLIENTE: ");
        payment.setAffectation("D");
        payment.setCurrency("1");
        payment.setAmount(pagoCabecera.getMontoTotal().doubleValue());
        payment.setTypeBankId(pagoCabecera.getBancoId());
        payment.setTypeTransaction("ANT");
        payment.setAnticipate("C");
        payment.setBeneficiaryName("");
        payment.setPaymentType("T");
        payment.setSupplierAccountNumber("");
        payment.setObservation("Anticipo de contrato: " + contrato.getId());
        payment.setIsCobranza(false);
        payment.setMatrizId(null);
        voucher.setPayment(payment);
        return voucher;
    }
}
