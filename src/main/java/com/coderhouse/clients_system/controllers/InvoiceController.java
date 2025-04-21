package com.coderhouse.clients_system.controllers;

import com.coderhouse.clients_system.entities.Invoice;
import com.coderhouse.clients_system.entities.InvoiceDetail;
import com.coderhouse.clients_system.entities.Client;
import com.coderhouse.clients_system.entities.Product;
import com.coderhouse.clients_system.services.InvoiceService;
import com.coderhouse.clients_system.repositories.ClientRepository;
import com.coderhouse.clients_system.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    @Lazy
    private InvoiceService invoiceService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public String createInvoice(@Valid @RequestBody Invoice invoice) {

        Optional<Client> clientOpt = clientRepository.findById(invoice.getClient().getId().longValue());
        if (clientOpt.isEmpty()) {
            return "Error: Client with ID " + invoice.getClient().getId() + " not found.";
        }

        for (InvoiceDetail detail : invoice.getDetails()) {
            Optional<Product> productOpt = productRepository.findById(detail.getProduct().getId());
            if (productOpt.isEmpty()) {
                return "Error: Product with ID " + detail.getProduct().getId() + " not found.";
            }
            Product product = productOpt.get();
            detail.setProduct(product);
            detail.setPrice(product.getPrice());
        }

        invoice.setClient(clientOpt.get());

        return invoiceService.createInvoice(invoice);
    }
}
