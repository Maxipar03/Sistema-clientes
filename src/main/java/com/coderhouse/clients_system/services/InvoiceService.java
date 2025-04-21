package com.coderhouse.clients_system.services;

import com.coderhouse.clients_system.entities.Client;
import com.coderhouse.clients_system.entities.Invoice;
import com.coderhouse.clients_system.entities.InvoiceDetail;
import com.coderhouse.clients_system.entities.Product;
import com.coderhouse.clients_system.repositories.ClientRepository;
import com.coderhouse.clients_system.repositories.InvoiceDetailRepository;
import com.coderhouse.clients_system.repositories.InvoiceRepository;
import com.coderhouse.clients_system.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    public String createInvoice(Invoice invoiceRequest){
        log.info("Starting invoice creation...");

        if (invoiceRequest == null || invoiceRequest.getClient() == null) {
            log.error("Error: invoiceRequest o client es NULL");
            return "Internal error when creating the invoice: incomplete data.";
        }

        Optional<Client> clientOpt = clientRepository.findById(invoiceRequest.getClient().getId().longValue());

        if (clientOpt.isEmpty()) {
            log.error("Error: Client with ID not found.");
            return "Error: Client with ID " + invoiceRequest.getClient().getId() + " not found.";
        }
        invoiceRequest.setClient(clientOpt.get());

        double total = 0;
        List<InvoiceDetail> updatedDetails = new ArrayList<>();

        for (InvoiceDetail detail : invoiceRequest.getDetails()) {
            Optional<Product> productOpt = productRepository.findById(detail.getProduct().getId());

            if (productOpt.isEmpty()) {
                log.error("Error: Product with ID {} not found.");
                return "Error: Product with ID " + detail.getProduct().getId() + " not found.";
            }

            Product product = productOpt.get();

            if (detail.getAmount() > product.getStock()) {
                log.error("Error: Insufficient stock for the product {}.");
                return "Error: Insufficient stock for the product " + product.getDescription() + ".";
            }

            product.setStock(product.getStock() - detail.getAmount());
            productRepository.save(product);

            InvoiceDetail newDetail = new InvoiceDetail();
            newDetail.setProduct(product);
            newDetail.setAmount(detail.getAmount());
            newDetail.setPrice(product.getPrice());
            newDetail.setInvoice(invoiceRequest);

            updatedDetails.add(newDetail);

            total += newDetail.getPrice() * newDetail.getAmount();
        }

        invoiceRequest.setDetails(updatedDetails);
        invoiceRequest.setCreatedAt(new Date());
        invoiceRequest.setTotal(total);

        try {
            invoiceRequest = invoiceRepository.save(invoiceRequest);

            for (InvoiceDetail detail : updatedDetails) {
                detail.setInvoice(invoiceRequest);
                invoiceDetailRepository.save(detail);
            }

            log.info("Invoice created successfully. ID: {}");
            return "Invoice created successfully. ID: " + invoiceRequest.getId() + ", Total: $" + total;
        } catch (Exception e) {
            log.error("Error saving invoice to database");
            return "Internal error in the database.";
        }
    }
}
