package com.coderhouse.clients_system.controllers;

import com.coderhouse.clients_system.entities.InvoiceDetail;
import com.coderhouse.clients_system.services.InvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("./invoice_details")
public class InvoiceDetailController {

    @Autowired
    @Lazy
    private InvoiceDetailService invoiceDetailService;

    @GetMapping("/{invoiceId}")
    public List<InvoiceDetail> getInvoiceDetails(@PathVariable Integer invoiceId) {
        return invoiceDetailService.getInvoiceDetailsByInvoiceId(invoiceId);
    }

}
