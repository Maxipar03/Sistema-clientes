package com.coderhouse.clients_system.services;

import com.coderhouse.clients_system.entities.InvoiceDetail;
import com.coderhouse.clients_system.repositories.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(Integer invoiceId) {
        return invoiceDetailRepository.findByInvoiceId(invoiceId);
    }


}
